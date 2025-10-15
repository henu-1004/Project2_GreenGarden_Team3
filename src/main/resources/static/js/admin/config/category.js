document.addEventListener('DOMContentLoaded', () => {
  const categoryList = document.getElementById('categoryList');
  const addMainBtn = document.getElementById('addMainCategoryBtn');

  // 1차 카테고리 추가
  addMainBtn.addEventListener('click', () => {
    const name = prompt('1차 카테고리명을 입력하세요.');
    if (!name) return;

    const li = document.createElement('li');
    li.className = 'menu open';
    li.draggable = true;
    li.innerHTML = `
      <div class="menu-title">
        <img src="arrow_1.png" alt="화살표" class="arrow">
        <span class="category-name">${name}</span>
        <button class="btn red delete-main">삭제</button>
      </div>
      <ul class="submenu">
        <li class="submenu-item add-sub">
          <button class="btn gray add-sub-btn"><span>&nbsp;+&nbsp;</span>2차 카테고리 추가</button>
        </li>
      </ul>
    `;
    categoryList.insertBefore(li, document.querySelector('.add-main'));
  });

  // 삭제 및 추가 이벤트
  categoryList.addEventListener('click', (e) => {
    if (e.target.classList.contains('delete-main')) {
      if (confirm('1차 카테고리를 삭제하시겠습니까?')) {
        e.target.closest('.menu').remove();
      }
    }
    if (e.target.classList.contains('delete-sub')) {
      if (confirm('2차 카테고리를 삭제하시겠습니까?')) {
        e.target.closest('.submenu-item').remove();
      }
    }
    if (e.target.classList.contains('add-sub-btn')) {
      const name = prompt('2차 카테고리명을 입력하세요.');
      if (!name) return;

      const newSub = document.createElement('li');
      newSub.className = 'submenu-item';
      newSub.draggable = true;
      newSub.innerHTML = `
        <span>${name}</span>
        <button class="btn red delete-sub">삭제</button>
      `;
      const submenu = e.target.closest('.submenu');
      submenu.insertBefore(newSub, e.target.closest('.add-sub'));
    }
  });

  // 메뉴 클릭 시 서브메뉴 토글
  categoryList.addEventListener('click', (e) => {
    const title = e.target.closest('.menu-title');
    if (title && !e.target.classList.contains('btn')) {
      const menu = title.parentElement;
      menu.classList.toggle('open');
    }
  });

  // 드래그 앤 드롭
  let dragged;
  categoryList.addEventListener('dragstart', (e) => {
    // add-main, add-sub은 드래그 금지
    if (e.target.classList.contains('add-main') || e.target.classList.contains('add-sub')) {
      e.preventDefault();
      return;
    }
    dragged = e.target;
    e.target.style.opacity = 0.5;
  });

  categoryList.addEventListener('dragend', (e) => {
    e.target.style.opacity = '';
  });

  categoryList.addEventListener('dragover', (e) => e.preventDefault());

  categoryList.addEventListener('drop', (e) => {
    e.preventDefault();
    const target = e.target.closest('li.menu, li.submenu-item');
    if (
      target &&
      target !== dragged &&
      !target.classList.contains('add-main') && // 1차 추가 버튼 예외
      !target.classList.contains('add-sub')    // 2차 추가 버튼 예외
    ) {
      const parent = target.parentNode;
      parent.insertBefore(dragged, target.nextSibling);
    }

    // 🔥 드롭 후 항상 add-sub을 맨 아래로 재배치
    document.querySelectorAll('.submenu').forEach(submenu => {
      const addSub = submenu.querySelector('.add-sub');
      if (addSub) submenu.appendChild(addSub);
    });
  });
});
