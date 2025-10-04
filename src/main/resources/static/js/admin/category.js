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