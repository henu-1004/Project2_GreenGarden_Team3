document.addEventListener('DOMContentLoaded', function() {
    const mainMenus = document.querySelectorAll('#aside > ul > li > a');

    mainMenus.forEach(menu => {
        menu.addEventListener('click', function(e) {
            e.preventDefault();

            // 모든 서브메뉴 숨기기
            document.querySelectorAll('#aside .submenu').forEach(submenu => {
                submenu.style.display = 'none';
            });

            // 클릭한 메뉴의 서브메뉴 보이기
            const subMenu = this.nextElementSibling;
            if (subMenu && subMenu.classList.contains('submenu')) {
                subMenu.style.display = 'block';
            }
        });
    });
});