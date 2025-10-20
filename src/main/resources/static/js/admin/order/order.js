/*
    날짜 : 2025/10/13
    이름 : 이수연
    내용 : admin 주문현황-주문번호 클릭시, 주문상세 모달화면 출력
 */
// 1. 모달 엘리먼트를 ID로 가져오기
const modalOrderDetail = document.getElementById('modalOrderDetail'); // 주문상세
const modalDeliveryInput = document.getElementById('modalDeliveryInput'); // 배송입력
const modalDeliveryDetail = document.getElementById('modalDeliveryDetail'); // 배송상세

// 2. 모달 열기
async function openModal(modalId, orderNo) {
    const modal = document.getElementById(modalId);
    if (!modal) return;

    // 모달 표시
    modal.classList.add('active');
    document.body.style.overflow = 'hidden';

    if (modalId === 'modalDeliveryInput') {
        await fillDeliveryModal(modal, orderNo);
    }
    else if (modalId === 'modalOrderDetail') {
        await fillOrderDetailModal(modal, orderNo);
    }
}

// 3-1. 모달 닫기 함수 (모달 안의 닫기 버튼)
function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if(modal) {
        modal.classList.remove('active');
        document.body.style.overflow = '';
    }
}

// 3-2. 모달 외부 클릭 시 닫기
// 원리: 클릭된 요소가 모달 배경(modalElement)과 정확히 같으면 닫는다.
window.addEventListener('click', (event) => {
    if(modalOrderDetail && modalOrderDetail.classList.contains('active') && event.target === modalOrderDetail) {
        closeModal('modalOrderDetail');
    }
    else if (modalDeliveryInput && modalDeliveryInput.classList.contains('active') && event.target === modalDeliveryInput) {
        closeModal('modalDeliveryInput');
    }
    else if (modalDeliveryDetail && modalDeliveryDetail.classList.contains('active') && event.target === modalDeliveryDetail) {
        closeModal('modalDeliveryDetail');
    }
});

// ✅ 배송입력 모달 초기화 및 데이터 주입 함수
async function fillDeliveryModal(modal, orderNo) {
    const $ = (sel) => modal.querySelector(sel);
    const setVal = (name, val = '') => {
        const el = $(`[name="${name}"]`);
        if (el) el.value = val ?? '';
    };

    // 1) 주문번호 표시 (헤더 + 인풋)
    setVal('orderNo', orderNo || '');
    const titleNo = modal.querySelector('.modal-title .order-number');
    if (titleNo) titleNo.textContent = orderNo || '';

    // 2) 필드 초기화
    setVal('recName', '');
    setVal('recZipCode', '');
    setVal('recAddressBasic', '');
    setVal('recAddressDetail', '');
    setVal('invoiceNo', '');
    setVal('note', '');
    const carrierSel = $('[name="company"]');
    if (carrierSel) carrierSel.value = '';

    // 3) 상세 데이터 불러오기
    if (orderNo) {
        try {
            const url = `${window.BASE_URL}admin/order/deliveryInput/${encodeURIComponent(orderNo)}`;
            const res = await fetch(url, { headers: { 'Accept': 'application/json' } });
            if (!res.ok) throw new Error('delivery info load failed');

            const d = await res.json();
            setVal('recName', d.recName);
            setVal('recZipCode', d.recZipCode);
            setVal('recAddressBasic', d.recAddressBasic);
            setVal('recAddressDetail', d.recAddressDetail);
            setVal('invoiceNo', d.invoiceNo);
            setVal('note', d.note);

            if (carrierSel) {
                const want = d.company ?? '';
                const exists = Array.from(carrierSel.options).some(o => o.value === want);
                carrierSel.value = exists ? want : '';
            }
        } catch (err) {
            console.error(err);
        }
    }
}


    // 금액 포맷
    const fmt = n => (Number(n ?? 0)).toLocaleString('ko-KR') + '원';
    const safe = v => (v ?? '').toString();

    // 단가(할인 적용)
    function discountedUnit(price, discountRate) {
        const p = Number(price ?? 0);
        const dr = Number(discountRate ?? 0);
        return Math.floor(p * (100 - dr) / 100);
    }

    async function fillOrderDetailModal(modal, orderNo) {
        // 1) 데이터 조회
        let items;
        try {
            const url = `${window.BASE_URL}admin/order/orderDetail/${encodeURIComponent(orderNo)}`;
            const res = await fetch(url, { headers: { 'Accept': 'application/json' } });
            if (!res.ok) throw new Error('주문상세 조회 실패');
            items = await res.json(); // List<AdminOrderDetailListDTO>
        } catch (e) {
            console.error(e);
            alert('주문 상세를 불러오지 못했습니다.');
            return;
        }
        if (!Array.isArray(items) || items.length === 0) {
            alert('해당 주문의 상세가 없습니다.');
            return;
        }

        // 2) 공통 헤더(주문번호)
        const orderNoEl = modal.querySelector('.order-number');
        if (orderNoEl) orderNoEl.textContent = items[0].orderNo ?? '';

        // 3) 상품 테이블
        const tbody = modal.querySelector('#cart-items');
        if (tbody) tbody.innerHTML = '';

        // 합계 계산용
        let productCount = 0;            // 총 수량
        let productPriceSum = 0;         // 상품금액(할인 전 합)
        let discountPriceSum = 0;        // 할인금액 합
        let shippingFeeSum = 0;          // 배송비 합
        let earnedPointsSum = 0;         // 적립 포인트 합

        items.forEach(it => {
            const unitPrice = Number(it.price ?? 0);
            const unitDiscounted = discountedUnit(it.price, it.discountRate);
            const discountPerUnit = Math.max(0, unitPrice - unitDiscounted);
            const qty = Number(it.quantity ?? 0);
            const deliveryFee = Number(it.deliveryFee ?? 0);

            // 라인 합계
            const lineProduct = unitDiscounted * qty; // 할인적용 단가 * 수량
            const lineTotal = lineProduct + deliveryFee;

            // 합계 누적
            productCount += qty;
            productPriceSum += unitPrice * qty;
            discountPriceSum += discountPerUnit * qty;
            shippingFeeSum += deliveryFee;
            earnedPointsSum += Number(it.point ?? 0) * qty;

            // 행 렌더
            const tr = document.createElement('tr');
            tr.innerHTML = `
        <td class="col-item-info">
          <div class="product-image">
            <img src="${safe(it.img1) || 'https://placehold.co/150x150/e0e0e0/000000?text=Product+Image'}"
                 alt="${safe(it.proName)}"
                 onerror="this.src='https://placehold.co/150x150/e0e0e0/000000?text=Product+Image';">
          </div>
        </td>
        <td><a href="#">${safe(it.proNo)}</a></td>
        <td>${safe(it.proName)}</td>
        <td>${safe(it.sellerCompany)}</td>
        <td>${fmt(unitPrice)}</td>
        <td>-${fmt(discountPerUnit)}</td>
        <td>${qty}</td>
        <td>${fmt(deliveryFee)}</td>
        <td>${fmt(lineTotal)}</td>
      `;
            tbody.appendChild(tr);
        });

        // 4) 요약 박스 채우기 (쿠폰/추가할인 없으면 0 처리)
        const couponDiscount = 0;
        const totalOrderPrice = productPriceSum - discountPriceSum + shippingFeeSum - couponDiscount;

        const setText = (sel, val) => { const el = modal.querySelector(sel); if (el) el.textContent = val; };
        setText('#product-count', productCount.toString());
        setText('#product-price', productPriceSum.toLocaleString('ko-KR'));
        setText('#discount-price', '-' + discountPriceSum.toLocaleString('ko-KR'));
        setText('#shipping-fee', shippingFeeSum.toLocaleString('ko-KR'));
        setText('#coupon-discount', '-' + couponDiscount.toLocaleString('ko-KR'));
        setText('#total-order-price', totalOrderPrice.toLocaleString('ko-KR'));
        setText('#earned-points', earnedPointsSum.toLocaleString('ko-KR'));

        // 5) 결제정보(주문자) / 배송정보(수취인)
        const first = items[0];

        // 결제정보
        setText('#pay-orderedAt', (first.orderedAt ?? '').toString().replace('T', ' ').replace('Z',''));
        setText('#pay-orderNo', first.orderNo ?? '');
        setText('#pay-buyerName', first.buyerName ?? '');
        setText('#pay-buyerAddress', `${safe(first.addressBasic)} ${safe(first.addressDetail)}`.trim());
        setText('#pay-buyerPhone', first.phone ?? '');

        // 배송정보
        setText('#ship-recName', first.recName ?? '');
        setText('#ship-recPhone', first.recPhone ?? '');
        setText('#ship-recAddress', `${safe(first.recAddressBasic)} ${safe(first.recAddressDetail)}`.trim());
    }

    // 기존 openModal에 연결
    async function openModal(modalId, orderNo) {
        const modal = document.getElementById(modalId);
        if (!modal) return;

        modal.classList.add('active');
        document.body.style.overflow = 'hidden';

        if (modalId === 'modalOrderDetail') {
            await fillOrderDetailModal(modal, orderNo);
        } else if (modalId === 'modalDeliveryInput') {
            await fillDeliveryModal(modal, orderNo);
        }
    }

    function closeModal(modalId) {
        const modal = document.getElementById(modalId);
        if (!modal) return;
        modal.classList.remove('active');
        document.body.style.overflow = '';
    }
