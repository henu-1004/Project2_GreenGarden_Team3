<<<<<<< HEAD
            // 아코디언 메뉴: 한 번에 하나만 열리도록
            document.querySelectorAll(".menu-title").forEach(title => {
                title.addEventListener("click", () => {
                    const parent = title.parentElement;
                    const isOpen = parent.classList.contains("open");

                    // 모든 메뉴 닫기
                    document.querySelectorAll(".menu").forEach(menu => {
                        menu.classList.remove("open");
                    });

                    // 클릭한 메뉴만 열기
                    if (!isOpen) {
                        parent.classList.add("open");
                    }
                });
            });
=======

document.addEventListener("DOMContentLoaded", () => {
  /* ======================================================
     공통: a[href="#"] 기본동작 막기 (원치 않는 스크롤/네비 방지)
  ====================================================== */
  document.querySelectorAll('a[href="#"]').forEach(a => {
    a.addEventListener('click', e => e.preventDefault());
  });

  /* ======================================================
     ① 아코디언 메뉴 (한 번에 하나만 열기)
     - 드래그로 끄는 중(click 방지)과 버튼 클릭은 토글 제외
  ====================================================== */
  let menuDragging = false; // 드래그-클릭 충돌 방지 플래그

  document.querySelectorAll(".menu-title").forEach(title => {
    title.addEventListener("click", (e) => {
      if (menuDragging) { // 방금 드래그했다면 클릭 무시
        menuDragging = false;
        return;
      }
      if (e.target.closest("button")) return; // 삭제버튼 클릭 시 토글 금지
      e.preventDefault();

      const parent = title.parentElement; // li.menu
      const isOpen = parent.classList.contains("open");

      document.querySelectorAll(".menu").forEach(menu => menu.classList.remove("open"));
      if (!isOpen) parent.classList.add("open");
    });
  });

  /* ======================================================
     ② 2차 카테고리 정렬 (각 .submenu 내부 li[draggable="true"])
     - 한 덩어리(li 전체)로만 이동
  ====================================================== */
  document.querySelectorAll(".submenu").forEach(list => {
    let draggingItem = null;

    // 위임: a/button을 잡아도 가장 가까운 li[draggable=true]만 드래그로 인정
    list.addEventListener('dragstart', e => {
      const li = e.target.closest('li[draggable="true"]');
      if (!li || !list.contains(li)) { e.preventDefault(); return; }
      draggingItem = li;
      li.classList.add('dragging');
      if (e.dataTransfer) { // FF 호환
        e.dataTransfer.effectAllowed = 'move';
        e.dataTransfer.setData('text/plain', '');
      }
    });

    list.addEventListener('dragend', e => {
      const li = e.target.closest('li[draggable="true"]');
      if (li) li.classList.remove('dragging');
      draggingItem = null;
    });

    list.addEventListener('dragover', e => {
      if (!draggingItem) return;
      e.preventDefault();
      const after = getAfterElement(list, e.clientY, 'li[draggable="true"]:not(.dragging)');
      if (!after) list.appendChild(draggingItem);
      else list.insertBefore(draggingItem, after);
    });
  });

  /* ======================================================
     ③ 1차 카테고리(메뉴) 정렬
     - 드래그 핸들: .menu-title[draggable="true"]
     - 이동 대상: .menu (li 전체 블록)
  ====================================================== */
  const rootList = document.querySelector('.sidebar > ul');
  let draggingMenuEl = null;

  document.querySelectorAll('.menu-title[draggable="true"]').forEach(handle => {
    handle.addEventListener('dragstart', e => {
      const menu = handle.closest('.menu');
      if (!menu) { e.preventDefault(); return; }
      draggingMenuEl = menu;
      menu.classList.add('dragging-menu');
      menuDragging = true; // 드래그 후 클릭 토글 방지
      if (e.dataTransfer) {
        e.dataTransfer.effectAllowed = 'move';
        e.dataTransfer.setData('text/plain', '');
      }
    });

    handle.addEventListener('dragend', () => {
      if (draggingMenuEl) draggingMenuEl.classList.remove('dragging-menu');
      draggingMenuEl = null;
      // 클릭 이벤트와 충돌하지 않게 다음 tick에 해제
      setTimeout(() => { menuDragging = false; }, 0);
    });
  });

// 고정 대상: data-fixed 있으면 그걸, 없으면 마지막 li를 고정으로 사용(폴백)
const fixedAdd = rootList.querySelector('[data-fixed="root-add"]') || rootList.lastElementChild;

rootList.addEventListener('dragover', e => {
  if (!draggingMenuEl) return;
  e.preventDefault();

  const after = getAfterElement(rootList, e.clientY, '.menu:not(.dragging-menu)');

  // 맨 끝에 둘 상황에도 append 하지 말고 "고정 줄" 바로 앞에만 넣기
  if (!after) {
    rootList.insertBefore(draggingMenuEl, fixedAdd);
  } else {
    rootList.insertBefore(draggingMenuEl, after);
  }
});
  /* ======================================================
     공통: 위치 계산 유틸
  ====================================================== */
  function getAfterElement(container, mouseY, selector) {
    const els = [...container.querySelectorAll(selector)];
    return els.reduce((closest, el) => {
      const box = el.getBoundingClientRect();
      const offset = mouseY - (box.top + box.height / 2);
      // 마우스가 해당 요소의 위쪽(음수)에 가장 가깝게 위치한 요소 선택
      if (offset < 0 && offset > closest.offset) {
        return { offset, element: el };
      }
      return closest;
    }, { offset: Number.NEGATIVE_INFINITY, element: null }).element;
  }
});
>>>>>>> 640797a (feat: admin > config/banner, config/version, shop/list 모달(open-modal) 추가 및 CSS 수정)
