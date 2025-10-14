// 모달 스크립트

document.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById("myModal");
    const openBtn = document.querySelector(".btn a[href='#']"); // 등록 버튼
    const closeX = document.getElementById("closeModal");
    const closeBtn = document.getElementById("closeModalBtn");

    // 등록 버튼 클릭 시 모달 열기
    openBtn.addEventListener("click", function (e) {
        e.preventDefault();
        modal.style.display = "block";
    });

    // X 버튼 및 닫기 버튼 클릭 시 닫기
    closeX.addEventListener("click", () => modal.style.display = "none");
    closeBtn.addEventListener("click", (e) => {
        e.preventDefault();
        modal.style.display = "none";
    });

    // 배경 클릭 시 닫기
    window.addEventListener("click", (e) => {
        if (e.target === modal) {
            modal.style.display = "none";
        }
    });
});
