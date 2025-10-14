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
function openModal(modalId) {
    const modal = document.getElementById(modalId);

    if (modal) {
        modal.classList.add('active'); // 모달 보이게.
        document.body.style.overflow = 'hidden'; // 메일 화면의 스크롤 잠금
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

