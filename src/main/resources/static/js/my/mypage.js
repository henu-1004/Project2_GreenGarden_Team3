(function () {
    'use strict';

    const CONTEXT_PATH = '/greengarden';
    const currencyFormatter = new Intl.NumberFormat('ko-KR');
    let selectedRating = 0;

    function formatCurrency(value) {
        if (value === null || value === undefined || isNaN(Number(value))) {
            return '0';
        }
        return currencyFormatter.format(Number(value));
    }

    function formatDiscount(value) {
        if (value === null || value === undefined) {
            return '0';
        }
        const numeric = Number(value);
        if (Number.isNaN(numeric) || numeric <= 0) {
            return '0';
        }
        return '-' + formatCurrency(numeric);
    }

    function parseDate(value) {
        if (!value) return null;
        const date = new Date(value);
        if (!Number.isNaN(date.getTime())) {
            return date;
        }
        return null;
    }

    function formatDateTime(value) {
        const date = parseDate(value);
        if (!date) {
            return value ? String(value).replace('T', ' ') : '-';
        }
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        return `${year}-${month}-${day} ${hours}:${minutes}`;
    }

    function setOrderProductListMessage(message) {
        const list = document.getElementById('orderDetailProductList');
        if (list) {
            list.innerHTML = `<p class="empty">${message}</p>`;
        }
    }

    function resetOrderModal() {
        const defaults = {
            orderModalOrderNo: '-',
            orderModalDate: '-',
            orderDetailOrderNo: '-',
            orderDetailOrderedAt: '-',
            orderDetailStatus: '-',
            orderDetailPayMethod: '-',
            orderDetailItemsTotal: '0',
            orderDetailDiscount: '0',
            orderDetailDeliveryTotal: '0',
            orderDetailPayment: '0',
            orderDetailRecName: '-',
            orderDetailRecPhone: '-',
            orderDetailRecAddress: '-',
            orderDetailMessage: '-'
        };
        Object.entries(defaults).forEach(([id, value]) => {
            const el = document.getElementById(id);
            if (el) el.textContent = value;
        });
        setOrderProductListMessage('주문 상품 정보가 없습니다.');
        const errorEl = document.getElementById('orderDetailError');
        if (errorEl) {
            errorEl.style.display = 'none';
            errorEl.textContent = '';
        }
    }

    function updateOrderModal(data) {
        if (!data) return;

        const fields = [
            ['orderModalOrderNo', data.orderNo ?? '-'],
            ['orderDetailOrderNo', data.orderNo ?? '-'],
            ['orderModalDate', formatDateTime(data.orderedAt)],
            ['orderDetailOrderedAt', formatDateTime(data.orderedAt)],
            ['orderDetailStatus', data.status ?? '-'],
            ['orderDetailPayMethod', data.payMethod ?? '-'],
            ['orderDetailItemsTotal', formatCurrency(data.itemsTotal ?? data.totalPrice ?? 0)],
            ['orderDetailDiscount', formatDiscount(data.discountTotal)],
            ['orderDetailDeliveryTotal', formatCurrency(data.deliveryTotal ?? 0)],
            ['orderDetailPayment', formatCurrency(data.paymentTotal ?? data.totalPrice ?? 0)],
            ['orderDetailRecName', data.recName ?? '-'],
            ['orderDetailRecPhone', data.recPhone ?? '-'],
            ['orderDetailRecAddress', data.fullAddress ?? '-'],
            ['orderDetailMessage', data.deliveryMessage ?? '-']
        ];

        fields.forEach(([id, value]) => {
            const el = document.getElementById(id);
            if (el) el.textContent = value;
        });

        const list = document.getElementById('orderDetailProductList');
        if (!list) return;

        list.innerHTML = '';
        if (Array.isArray(data.items) && data.items.length > 0) {
            data.items.forEach(item => {
                const line = document.createElement('div');
                line.className = 'product-line';

                const thumb = document.createElement('div');
                thumb.className = 'thumb';
                const img = document.createElement('img');
                const path = item.productImg || '';
                if (path.startsWith('/uploads/')) {
                    img.src = `${CONTEXT_PATH}${path}`;
                } else {
                    img.src = path || `${CONTEXT_PATH}/img/sample.png`;
                }
                img.alt = '상품';
                img.onerror = () => { img.src = `${CONTEXT_PATH}/img/sample.png`; };
                thumb.appendChild(img);

                const info = document.createElement('div');
                info.className = 'info';

                const name = document.createElement('strong');
                name.textContent = item.productName || '-';
                info.appendChild(name);

                const quantity = item.quantity ?? 0;
                const price = item.price ?? 0;

                const option = document.createElement('span');
                option.className = 'option';
                option.textContent = `수량 ${quantity}개 / 단가 ${formatCurrency(price)}원`;
                info.appendChild(option);

                if (item.discountRate && item.discountRate > 0) {
                    const discount = document.createElement('span');
                    discount.className = 'option';
                    const amount = item.discountAmount ?? 0;
                    discount.textContent = `할인율 ${item.discountRate}% (할인 ${formatCurrency(amount)}원)`;
                    info.appendChild(discount);
                }

                const lineTotal = document.createElement('div');
                lineTotal.className = 'price';
                lineTotal.textContent = `합계 ${formatCurrency(price * quantity)}원`;
                info.appendChild(lineTotal);

                line.appendChild(thumb);
                line.appendChild(info);
                list.appendChild(line);
            });
        } else {
            setOrderProductListMessage('주문 상품 정보가 없습니다.');
        }
    }

    async function loadOrderDetail(orderNo) {
        const errorEl = document.getElementById('orderDetailError');
        setOrderProductListMessage('주문 상품 정보를 불러오는 중입니다...');
        try {
            const response = await fetch(`${CONTEXT_PATH}/my/home/order/${orderNo}`);
            if (!response.ok) {
                let message = '주문 정보를 불러오는 중 오류가 발생했습니다.';
                if (response.status === 401) {
                    message = '로그인이 필요합니다.';
                } else if (response.status === 404) {
                    message = '주문 정보를 찾을 수 없습니다.';
                }
                throw new Error(message);
            }
            const data = await response.json();
            updateOrderModal(data);
        } catch (err) {
            resetOrderModal();
            if (errorEl) {
                errorEl.textContent = err.message;
                errorEl.style.display = 'block';
            }
        }
    }

    function resetSellerModal() {
        const defaults = {
            sellerInfoGrade: '-',
            sellerInfoCompany: '-',
            sellerInfoRepresentative: '-',
            sellerInfoTel: '-',
            sellerInfoFax: '-',
            sellerInfoEmail: '-',
            sellerInfoBusinessNumber: '-',
            sellerInfoAddress: '-'
        };
        Object.entries(defaults).forEach(([id, value]) => {
            const el = document.getElementById(id);
            if (el) el.textContent = value;
        });
        const errorEl = document.getElementById('sellerInfoError');
        if (errorEl) {
            errorEl.style.display = 'none';
            errorEl.textContent = '';
        }
    }

    function updateSellerModal(data) {
        if (!data) return;
        const mapping = {
            sellerInfoGrade: data.gradeName ?? '-',
            sellerInfoCompany: data.company ?? '-',
            sellerInfoRepresentative: data.representative ?? '-',
            sellerInfoTel: data.tel ?? '-',
            sellerInfoFax: data.fax ?? '-',
            sellerInfoEmail: data.email ?? '-',
            sellerInfoBusinessNumber: data.businessNumber ?? '-',
            sellerInfoAddress: data.fullAddress ?? '-'
        };
        Object.entries(mapping).forEach(([id, value]) => {
            const el = document.getElementById(id);
            if (el) el.textContent = value;
        });
    }

    async function loadSellerInfo(sellerId) {
        const errorEl = document.getElementById('sellerInfoError');
        try {
            const response = await fetch(`${CONTEXT_PATH}/my/home/seller/${sellerId}`);
            if (!response.ok) {
                let message = '판매자 정보를 불러오는 중 오류가 발생했습니다.';
                if (response.status === 401) {
                    message = '로그인이 필요합니다.';
                } else if (response.status === 404) {
                    message = '판매자 정보를 찾을 수 없습니다.';
                }
                throw new Error(message);
            }
            const data = await response.json();
            updateSellerModal(data);
        } catch (err) {
            resetSellerModal();
            if (errorEl) {
                errorEl.textContent = err.message;
                errorEl.style.display = 'block';
            }
        }
    }

    function updateReviewProductName(name) {
        const productNameEl = document.getElementById('reviewProductName');
        if (productNameEl) {
            productNameEl.textContent = name || '선택된 상품 정보가 없습니다.';
        }
    }

    function updateRequestProductSummary(modal) {
        if (!modal) {
            return;
        }

        const mapping = {
            returnModal: {
                name: 'returnProductName',
                order: 'returnOrderNo',
                code: 'returnProductCode'
            },
            exchangeModal: {
                name: 'exchangeProductName',
                order: 'exchangeOrderNo',
                code: 'exchangeProductCode'
            }
        };

        const config = mapping[modal.id];
        if (!config) {
            return;
        }

        const dataset = modal.dataset || {};
        const productName = dataset.productName && dataset.productName.trim()
            ? dataset.productName
            : '선택된 상품 정보가 없습니다.';

        const nameEl = document.getElementById(config.name);
        if (nameEl) {
            nameEl.textContent = productName;
        }

        const orderEl = document.getElementById(config.order);
        if (orderEl) {
            orderEl.textContent = dataset.orderNo && dataset.orderNo.trim() ? dataset.orderNo : '-';
        }

        const codeEl = document.getElementById(config.code);
        if (codeEl) {
            codeEl.textContent = dataset.proId && dataset.proId.trim() ? dataset.proId : '-';
        }
    }

    function resetStars() {
        const stars = document.querySelectorAll('#starRating span');
        const ratingInput = document.getElementById('ratingValue');
        selectedRating = 0;
        if (!stars.length || !ratingInput) return;
        ratingInput.value = 0;
        stars.forEach(s => {
            s.classList.remove('selected', 'hovered');
        });
    }

    function initStarRating() {
        const stars = document.querySelectorAll('#starRating span');
        const ratingInput = document.getElementById('ratingValue');
        const previewArea = document.getElementById('previewArea');
        const inputs = [
            document.getElementById('reviewFile1'),
            document.getElementById('reviewFile2'),
            document.getElementById('reviewFile3')
        ];

        if (stars.length && ratingInput) {
            stars.forEach((star, index) => {
                const value = index + 1;
                star.addEventListener('mouseenter', () => {
                    stars.forEach((s, i) => s.classList.toggle('hovered', i < value));
                });
                star.addEventListener('mouseleave', () => {
                    stars.forEach((s, i) => {
                        s.classList.remove('hovered');
                        s.classList.toggle('selected', i < selectedRating);
                    });
                });
                star.addEventListener('click', () => {
                    selectedRating = value;
                    ratingInput.value = selectedRating;
                    stars.forEach((s, i) => s.classList.toggle('selected', i < selectedRating));
                });
            });
        }

        if (previewArea) {
            inputs.forEach((input, idx) => {
                if (!input) return;
                input.addEventListener('change', (event) => {
                    const file = event.target.files[0];
                    if (!file) return;

                    const oldImg = previewArea.querySelector(`img[data-slot="${idx}"]`);
                    if (oldImg) oldImg.remove();

                    const reader = new FileReader();
                    reader.onload = ev => {
                        const img = document.createElement('img');
                        img.src = ev.target.result;
                        img.dataset.slot = idx;
                        img.style.width = '80px';
                        img.style.height = '80px';
                        img.style.objectFit = 'cover';
                        img.style.border = '1px solid #ccc';
                        img.style.borderRadius = '4px';
                        img.style.marginRight = '5px';
                        previewArea.appendChild(img);
                    };
                    reader.readAsDataURL(file);
                });
            });
        }
    }

    function initBannerCarousel() {
        document.querySelectorAll('.banner-carousel').forEach(carousel => {
            const slides = carousel.querySelectorAll('.banner-slide');
            if (!slides.length) {
                return;
            }
            const dots = carousel.querySelectorAll('.banner-dots button');
            const interval = Number(carousel.dataset.interval) || 5000;
            const autoplay = carousel.dataset.autoplay !== 'false';
            let currentIndex = 0;
            let timerId;

            const showSlide = (index) => {
                slides.forEach((slide, i) => {
                    slide.classList.toggle('active', i === index);
                });
                dots.forEach((dot, i) => {
                    dot.classList.toggle('active', i === index);
                    dot.setAttribute('aria-pressed', String(i === index));
                });
                currentIndex = index;
            };

            const nextSlide = () => {
                const next = (currentIndex + 1) % slides.length;
                showSlide(next);
            };

            const startAutoplay = () => {
                if (!autoplay || slides.length <= 1) {
                    return;
                }
                stopAutoplay();
                timerId = window.setInterval(nextSlide, interval);
            };

            const stopAutoplay = () => {
                if (timerId) {
                    window.clearInterval(timerId);
                    timerId = undefined;
                }
            };

            dots.forEach((dot, index) => {
                dot.addEventListener('click', () => {
                    showSlide(index);
                    stopAutoplay();
                    startAutoplay();
                });
            });

            carousel.addEventListener('mouseenter', stopAutoplay);
            carousel.addEventListener('mouseleave', startAutoplay);

            showSlide(0);
            startAutoplay();
        });
    }

    function openModal(id, orderNo, proId, sellerId, orderItemId, redirect, productName) {
        const modal = document.getElementById(id);
        if (!modal) return;

        modal.style.display = 'flex';

        if (orderNo !== undefined) {
            modal.dataset.orderNo = orderNo != null ? String(orderNo) : '';
        }

        if (proId !== undefined) {
            modal.dataset.proId = proId != null ? String(proId) : '';
        }

        if (sellerId !== undefined) {
            modal.dataset.sellerId = sellerId != null ? String(sellerId) : '';
        }

        if (orderItemId !== undefined) {
            modal.dataset.orderItemId = orderItemId != null ? String(orderItemId) : '';
        }

        if (redirect !== undefined) {
            modal.dataset.redirect = redirect != null ? String(redirect) : '';
        }

        if (productName !== undefined) {
            modal.dataset.productName = productName != null ? String(productName) : '';
        }

        const orderInput = modal.querySelector("input[name='orderNo']");
        if (orderInput) {
            orderInput.value = orderNo || '';
        }

        const proInput = modal.querySelector("input[name='proId']");
        if (proInput) {
            proInput.value = proId || '';
        }

        const orderItemInput = modal.querySelector("input[name='orderItemId']");
        if (orderItemInput) {
            orderItemInput.value = orderItemId || '';
        }

        const redirectInput = modal.querySelector("input[name='redirect']");
        if (redirectInput && redirect !== undefined) {
            redirectInput.value = redirect || '';
        }

        if (id === 'orderModal') {
            resetOrderModal();
            if (orderNo) {
                loadOrderDetail(orderNo);
            }
        }

        if (id === 'sellerModal') {
            resetSellerModal();
            if (sellerId) {
                loadSellerInfo(sellerId);
            }
        }

        if (id === 'reviewModal') {
            resetStars();
            updateReviewProductName(productName);
        }

        if (id === 'returnModal' || id === 'exchangeModal') {
            updateRequestProductSummary(modal);
        }
    }

    function closeModal(id) {
        const modal = document.getElementById(id);
        if (modal) {
            modal.style.display = 'none';
        }
    }

    function openInquiry(redirectTarget = 'home') {
        const sellerModal = document.getElementById('sellerModal');
        const sellerId = sellerModal && sellerModal.dataset ? sellerModal.dataset.sellerId : '';

        closeModal('sellerModal');
        openModal('inquiryModal', undefined, undefined, sellerId || undefined, undefined, redirectTarget, undefined);

        const inquiryModal = document.getElementById('inquiryModal');
        if (inquiryModal) {
            inquiryModal.dataset.sellerId = sellerId || '';
            inquiryModal.dataset.redirect = redirectTarget != null ? String(redirectTarget) : '';
        }
    }

    function cancelInquiry() {
        const inquiryModal = document.getElementById('inquiryModal');
        if (!inquiryModal) {
            return;
        }

        const sellerId = inquiryModal.dataset ? inquiryModal.dataset.sellerId : '';
        const redirectTarget = inquiryModal.dataset ? inquiryModal.dataset.redirect : '';

        closeModal('inquiryModal');

        if (sellerId) {
            openModal('sellerModal', undefined, undefined, sellerId, undefined, redirectTarget || undefined, undefined);
        }
    }

    window.openModal = openModal;
    window.closeModal = closeModal;
    window.openInquiry = openInquiry;
    window.cancelInquiry = cancelInquiry;

    document.addEventListener('DOMContentLoaded', () => {
        initStarRating();
        initBannerCarousel();
    });
})();
