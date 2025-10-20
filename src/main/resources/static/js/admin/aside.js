document.addEventListener('DOMContentLoaded', function() {
    // 현재 페이지 URL
    const currentPath = window.location.pathname;

    // 모든 2차 메뉴 링크 확인
    document.querySelectorAll('#aside > ul > li > ul > li > a').forEach(link => {
        if (link.getAttribute('href') === currentPath) {
            // 현재 페이지와 일치하면
            const li2 = link.parentElement; // 2차 메뉴 li
            const li1 = li2.closest('#aside > ul > li'); // 1차 메뉴 li

            li2.classList.add('active'); // 2차 메뉴 활성화
            li1.classList.add('active'); // 1차 메뉴 활성화
        }
    });

    // 1차 메뉴 클릭 시 (기존 코드 유지 가능)
    const mainMenus = document.querySelectorAll('#aside > ul > li > span');
    mainMenus.forEach(menu => {
        menu.addEventListener('click', function(e) {
            e.preventDefault();
            const subMenu = this.nextElementSibling;
            if (subMenu && subMenu.classList.contains('submenu')) {
                // 토글 기능
                if (subMenu.style.display === 'block') {
                    subMenu.style.display = 'none';
                } else {
                    subMenu.style.display = 'block';
                }
            }
        });
    });
});