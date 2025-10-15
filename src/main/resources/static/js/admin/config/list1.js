  const modal = document.getElementById("myModal");
  const openBtn = document.getElementById("openModalBtn");
  const closeBtn = document.querySelector(".modal-close"); // class로 선택

  // 모달 열기
  openBtn.addEventListener("click", () => {
    const checked = document.querySelectorAll('tbody input[type="checkbox"]:checked');
    if (checked.length === 0) {
      alert("수정할 회원을 선택하세요.");
      return;
    }
    modal.style.display = "flex"; // 모달 열기
  });

  // X 버튼으로 닫기
  closeBtn.addEventListener("click", () => {
    modal.style.display = "none";
  });

  // 모달 바깥 클릭 시 닫기
  window.addEventListener("click", (e) => {
    if (e.target === modal) modal.style.display = "none";
  });