const MyOrder = (() => {
    const state = {
        orderNo: null,
        sellerId: null
    };

    const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;

    function openModal(id) {
        const modal = document.getElementById(id);
        if (!modal) return;
        modal.style.display = 'flex';
        document.body.style.overflow = 'hidden';
    }

    function closeModal(id) {
        const modal = document.getElementById(id);
        if (!modal) return;
        modal.style.display = 'none';
        if (!document.querySelector('.modal[style*="display: flex"]')) {
            document.body.style.overflow = '';
        }
    }

    function formatCurrency(value) {
        const amount = Number(value ?? 0);
        return `${amount.toLocaleString('ko-KR')}원`;
    }

    function formatDateTime(value) {
        if (!value) return '-';
        const date = new Date(value);
        if (Number.isNaN(date.getTime())) {
            return value;
        }
        return `${date.getFullYear()}.${String(date.getMonth() + 1).padStart(2, '0')}.${String(date.getDate()).padStart(2, '0')} ` +
            `${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
    }

    async function fetchJson(url, options = {}) {
        const headers = new Headers(options.headers || {});
        headers.set('Accept', 'application/json');
        if (options.body && !headers.has('Content-Type')) {
            headers.set('Content-Type', 'application/json');
        }
        if (csrfToken && csrfHeader) {
            headers.set(csrfHeader, csrfToken);
        }
        const response = await fetch(url, { ...options, headers });
        if (!response.ok) {
            let message = response.statusText;
            try {
                const data = await response.json();
                if (data?.message) {
                    message = data.message;
                }
            } catch (_) {
                const text = await response.text();
                if (text) {
                    message = text;
                }
            }
            throw new Error(message || '요청에 실패했습니다.');
        }
        if (response.status === 204) {
            return null;
        }
        return response.json();
    }

    function setField(container, field, value) {
        const target = container.querySelector(`[data-field="${field}"]`);
        if (!target) return;
        target.textContent = value ?? '-';
    }

    function resetOrderModal(message) {
        const modal = document.getElementById('orderModal');
        if (!modal) return;
        ['orderNo', 'orderedAt', 'payMethod', 'deliveryStatus', 'deliveryCompany', 'invoiceNo', 'productTotal',
            'discountTotal', 'shippingFee', 'pointUsed', 'finalAmount', 'receiverName', 'receiverPhone', 'receiverAddress',
            'deliveryNote', 'ordererName', 'ordererPhone', 'ordererEmail'].forEach(field => setField(modal, field, '-'));
        const tbody = modal.querySelector('[data-field="items"]');
        if (tbody) {
            tbody.innerHTML = `<tr class="loading-row"><td colspan="4">${message}</td></tr>`;
        }
    }

    async function openOrderDetail(button) {
        const orderNo = button?.dataset?.orderNo;
        if (!orderNo) return;
        state.orderNo = orderNo;
        openModal('orderModal');
        resetOrderModal('주문 정보를 불러오는 중입니다...');
        try {
            const detail = await fetchJson(`/my/api/orders/${orderNo}`);
            renderOrderDetail(detail);
        } catch (error) {
            resetOrderModal(error.message || '주문 정보를 불러오지 못했습니다. 잠시 후 다시 시도해주세요.');
        }
    }

    function renderOrderDetail(detail) {
        const modal = document.getElementById('orderModal');
        if (!modal || !detail) {
            resetOrderModal('주문 정보를 찾을 수 없습니다.');
            return;
        }

        setField(modal, 'orderNo', detail.orderNo);
        setField(modal, 'orderedAt', formatDateTime(detail.orderedAt));
        setField(modal, 'payMethod', detail.payMethod || '-');
        setField(modal, 'deliveryStatus', detail.deliveryStatus || '배송 정보 준비중');
        setField(modal, 'deliveryCompany', detail.deliveryCompany || '-');
        setField(modal, 'invoiceNo', detail.invoiceNo || '-');
        setField(modal, 'productTotal', formatCurrency(detail.productTotal));
        setField(modal, 'discountTotal', formatCurrency(-Math.abs(detail.discountTotal)));
        setField(modal, 'shippingFee', formatCurrency(detail.shippingFee));
        setField(modal, 'pointUsed', formatCurrency(-Math.abs(detail.pointUsed)));
        setField(modal, 'finalAmount', formatCurrency(detail.finalAmount));
        setField(modal, 'receiverName', detail.receiverName || '-');
        setField(modal, 'receiverPhone', detail.receiverPhone || '-');
        const address = buildAddress(detail.receiverZipCode, detail.receiverAddressBasic, detail.receiverAddressDetail);
        setField(modal, 'receiverAddress', address || '-');
        setField(modal, 'deliveryNote', detail.deliveryNote || '요청사항 없음');
        setField(modal, 'ordererName', detail.ordererName || '-');
        setField(modal, 'ordererPhone', detail.ordererPhone || '-');
        setField(modal, 'ordererEmail', detail.ordererEmail || '-');

        const tbody = modal.querySelector('[data-field="items"]');
        if (!tbody) {
            return;
        }
        tbody.innerHTML = '';

        const items = Array.isArray(detail.items) ? detail.items : [];
        if (!items.length) {
            tbody.innerHTML = '<tr class="loading-row"><td colspan="4">상품 정보가 없습니다.</td></tr>';
            return;
        }

        items.forEach(item => {
            const tr = document.createElement('tr');
            const productCell = document.createElement('td');
            productCell.innerHTML = `
                <div class="item-product">
                    <img src="${item.productImg || '/img/sample.png'}" alt="상품">
                    <div>
                        <p class="item-name">${item.productName || '-'}</p>
                        <p class="item-meta">단가 ${formatCurrency(item.price)} · 할인 ${formatCurrency(-Math.abs(item.discountAmount))}</p>
                    </div>
                </div>
            `;

            const qtyCell = document.createElement('td');
            qtyCell.textContent = `${item.quantity ?? 0}개`;

            const priceCell = document.createElement('td');
            priceCell.innerHTML = `${formatCurrency(item.lineTotal)}<br><small>배송비 포함</small>`;

            const sellerCell = document.createElement('td');
            if (item.sellerId) {
                const sellerButton = document.createElement('button');
                sellerButton.type = 'button';
                sellerButton.className = 'seller-link';
                sellerButton.dataset.sellerId = item.sellerId;
                sellerButton.dataset.orderNo = state.orderNo ?? '';
                sellerButton.textContent = item.sellerName || item.sellerId;
                sellerButton.addEventListener('click', () => openSellerInfo(sellerButton));
                sellerCell.appendChild(sellerButton);
            } else {
                sellerCell.textContent = item.sellerName || '-';
            }

            tr.append(productCell, qtyCell, priceCell, sellerCell);
            tbody.appendChild(tr);
        });
    }

    function buildAddress(zip, basic, detail) {
        const parts = [];
        if (zip) parts.push(`[${zip}]`);
        if (basic) parts.push(basic);
        if (detail) parts.push(detail);
        return parts.join(' ');
    }

    async function openSellerInfo(button) {
        const sellerId = button?.dataset?.sellerId;
        if (!sellerId) return;
        if (button.dataset.orderNo) {
            state.orderNo = button.dataset.orderNo;
        }
        state.sellerId = sellerId;
        openModal('sellerModal');
        resetSellerModal('판매자 정보를 불러오는 중입니다...');
        try {
            const detail = await fetchJson(`/my/api/seller/${sellerId}`);
            renderSellerDetail(detail);
        } catch (error) {
            resetSellerModal(error.message || '판매자 정보를 불러오지 못했습니다.');
        }
    }

    function resetSellerModal(message) {
        const modal = document.getElementById('sellerModal');
        if (!modal) return;
        ['company', 'grade', 'representative', 'tel', 'email', 'businessNumber', 'tin', 'address', 'status'].forEach(field => setField(modal, field, '-'));
        const table = modal.querySelector('.seller-table tbody');
        if (table && message) {
            table.querySelectorAll('tr').forEach(row => {
                const cell = row.querySelector('td');
                if (cell) {
                    cell.textContent = '-';
                }
            });
            setField(modal, 'company', message);
        }
    }

    function renderSellerDetail(detail) {
        const modal = document.getElementById('sellerModal');
        if (!modal || !detail) {
            resetSellerModal('판매자 정보를 찾을 수 없습니다.');
            return;
        }
        setField(modal, 'company', detail.company || detail.sellerId || '-');
        setField(modal, 'grade', detail.grade || '일반 판매자');
        setField(modal, 'representative', detail.representative || '-');
        setField(modal, 'tel', detail.tel || detail.phone || '-');
        setField(modal, 'email', detail.email || '-');
        setField(modal, 'businessNumber', detail.businessNumber || '-');
        setField(modal, 'tin', detail.tin || '-');
        const address = buildAddress(detail.zipCode, detail.addressBasic, detail.addressDetail);
        setField(modal, 'address', address || '-');
        setField(modal, 'status', detail.status || '정상');
    }

    function openInquiry() {
        closeModal('sellerModal');
        const form = document.getElementById('inquiryForm');
        if (!form) {
            openModal('inquiryModal');
            return;
        }
        form.reset();
        form.querySelector('input[name="sellerId"]').value = state.sellerId || '';
        form.querySelector('input[name="orderNo"]').value = state.orderNo || '';
        const feedback = form.querySelector('[data-role="inquiry-feedback"]');
        if (feedback) {
            feedback.textContent = '';
            feedback.classList.remove('success');
        }
        const firstRadio = form.querySelector('input[name="category"]');
        if (firstRadio) {
            firstRadio.checked = true;
        }
        openModal('inquiryModal');
    }

    function openConfirm(button) {
        const orderNo = button?.dataset?.orderNo;
        const proId = button?.dataset?.proId;
        setHiddenFields('confirmModal', orderNo, proId);
        openModal('confirmModal');
    }

    function openReview(button) {
        const orderNo = button?.dataset?.orderNo;
        const proId = button?.dataset?.proId;
        const productName = button?.dataset?.productName;
        const modal = document.getElementById('reviewModal');
        if (modal) {
            const productEl = modal.querySelector('#reviewProductName');
            if (productEl) {
                productEl.textContent = productName || '-';
            }
        }
        resetStars();
        setHiddenFields('reviewModal', orderNo, proId);
        openModal('reviewModal');
    }

    function openReturn(button) {
        const orderNo = button?.dataset?.orderNo;
        const proId = button?.dataset?.proId;
        setHiddenFields('returnModal', orderNo, proId);
        openModal('returnModal');
    }

    function openExchange(button) {
        const orderNo = button?.dataset?.orderNo;
        const proId = button?.dataset?.proId;
        setHiddenFields('exchangeModal', orderNo, proId);
        openModal('exchangeModal');
    }

    function setHiddenFields(modalId, orderNo, proId) {
        const modal = document.getElementById(modalId);
        if (!modal) return;
        const orderInput = modal.querySelector('input[name="orderNo"]');
        if (orderInput) {
            orderInput.value = orderNo ?? '';
        }
        const proInput = modal.querySelector('input[name="proId"]');
        if (proInput) {
            proInput.value = proId ?? '';
        }
    }

    function resetStars() {
        const stars = document.querySelectorAll('#starRating span');
        const ratingInput = document.getElementById('ratingValue');
        if (!stars.length || !ratingInput) return;
        ratingInput.value = '0';
        stars.forEach(star => {
            star.classList.remove('selected', 'hovered');
        });
    }

    function attachStarRating() {
        const stars = document.querySelectorAll('#starRating span');
        const ratingInput = document.getElementById('ratingValue');
        if (!stars.length || !ratingInput) return;
        let selected = Number(ratingInput.value || 0);
        stars.forEach((star, index) => {
            const value = index + 1;
            star.addEventListener('mouseenter', () => {
                stars.forEach((s, i) => {
                    s.classList.toggle('hovered', i < value);
                });
            });
            star.addEventListener('mouseleave', () => {
                stars.forEach((s, i) => {
                    s.classList.remove('hovered');
                    s.classList.toggle('selected', i < selected);
                });
            });
            star.addEventListener('click', () => {
                selected = value;
                ratingInput.value = String(selected);
                stars.forEach((s, i) => {
                    s.classList.toggle('selected', i < selected);
                });
            });
        });
    }

    function attachReviewPreview() {
        const previewArea = document.getElementById('previewArea');
        if (!previewArea) return;
        ['reviewFile1', 'reviewFile2', 'reviewFile3'].forEach((id, index) => {
            const input = document.getElementById(id);
            if (!input) return;
            input.addEventListener('change', event => {
                const file = event.target.files?.[0];
                if (!file) return;
                const reader = new FileReader();
                reader.onload = e => {
                    const existing = previewArea.querySelector(`img[data-slot="${index}"]`);
                    if (existing) existing.remove();
                    const img = document.createElement('img');
                    img.src = e.target?.result;
                    img.dataset.slot = String(index);
                    img.style.width = '80px';
                    img.style.height = '80px';
                    img.style.objectFit = 'cover';
                    img.style.border = '1px solid #ccc';
                    img.style.borderRadius = '8px';
                    img.style.marginRight = '6px';
                    previewArea.appendChild(img);
                };
                reader.readAsDataURL(file);
            });
        });
    }

    function attachInquiryForm() {
        const form = document.getElementById('inquiryForm');
        if (!form) return;
        form.addEventListener('submit', async event => {
            event.preventDefault();
            const data = new FormData(form);
            const payload = {
                category: data.get('category'),
                detailCategory: data.get('detailCategory'),
                title: data.get('title'),
                content: data.get('content'),
                sellerId: data.get('sellerId'),
                orderNo: data.get('orderNo')
            };
            const feedback = form.querySelector('[data-role="inquiry-feedback"]');
            const showFeedback = (message, success = false) => {
                if (!feedback) return;
                feedback.textContent = message;
                feedback.classList.toggle('success', success);
            };
            try {
                await fetchJson('/my/api/inquiry', {
                    method: 'POST',
                    body: JSON.stringify(payload)
                });
                showFeedback('문의가 등록되었습니다.', true);
                form.reset();
                form.querySelector('input[name="sellerId"]').value = state.sellerId || '';
                form.querySelector('input[name="orderNo"]').value = state.orderNo || '';
                const firstRadio = form.querySelector('input[name="category"]');
                if (firstRadio) firstRadio.checked = true;
            } catch (error) {
                showFeedback(error.message || '문의 등록에 실패했습니다. 다시 시도해주세요.');
            }
        });
    }

    function attachModalBackdrops() {
        document.querySelectorAll('.modal').forEach(modal => {
            modal.addEventListener('click', event => {
                if (event.target === modal) {
                    modal.style.display = 'none';
                    if (!document.querySelector('.modal[style*="display: flex"]')) {
                        document.body.style.overflow = '';
                    }
                }
            });
        });
    }

    document.addEventListener('DOMContentLoaded', () => {
        attachStarRating();
        attachReviewPreview();
        attachInquiryForm();
        attachModalBackdrops();
    });

    return {
        openOrderDetail,
        openSellerInfo,
        openInquiry,
        closeModal,
        openConfirm,
        openReview,
        openReturn,
        openExchange
    };
})();

window.MyOrder = MyOrder;
