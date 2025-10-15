/*
    날짜 : 2025/10/14
    이름 : 이수연
    내용 : 관리자 쿠폰관리 - 모달화면js
    1. 모달 화면 뜨우고 없애기
*/

// 1. 모달 엘리먼트를 ID로 가져오기
const modalIssueInfo = document.getElementById('modalIssueInfo'); //쿠폰상세정보
const modalCouponInsert = document.getElementById('modalCouponInsert'); //쿠폰등록
const modalCouponInfo = document.getElementById('modalCouponInfo'); //쿠폰정보

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
    if(modalIssueInfo && modalIssueInfo.classList.contains('active') && event.target === modalIssueInfo) {
        closeModal('modalIssueInfo');
    }
    else if (modalCouponInsert && modalCouponInsert.classList.contains('active') && event.target === modalCouponInsert) {
        closeModal('modalCouponInsert');
    }else if (modalCouponInfo && modalCouponInfo.classList.contains('active') && event.target === modalCouponInfo) {
        closeModal('modalCouponInfo');
    }
});

// ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
