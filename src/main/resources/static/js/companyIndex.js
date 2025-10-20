/**
 * 갤러리 슬라이더 기능 구현 (company/index.js)
 * * 주요 원리:
 * 1. gallery-track 요소를 찾는다.
 * 2. 현재 보여주는 이미지의 인덱스를 저장하는 변수(currentIndex)를 둔다.
 * 3. 좌우 버튼 클릭 시 currentIndex를 변경한다.
 * 4. CSS transform: translateX()를 이용해 gallery-track을 새로운 인덱스 위치로 이동시킨다.
 */

// 1. 필요한 DOM 요소 가져오기
const galleryTrack = document.getElementById('galleryTrack');
const galleryItems = document.querySelectorAll('.gallery-item');
const totalItems = galleryItems.length; // 전체 슬라이드 아이템 개수

// 2. 현재 인덱스 및 이동 단위 설정
let currentIndex = 0; // 현재 보고 있는 슬라이드 아이템의 인덱스
// 각 아이템 너비의 100%만큼 이동 (CSS에서 갤러리 아이템 너비가 중요함)
const itemWidth = 33.333; // % 단위로 가정. 실제 CSS 설정에 따라 픽셀(px)이나 뷰포트 너비(vw)로 설정할 수도 있어.

// 3. 슬라이더를 움직이는 핵심 함수
function updateGalleryPosition() {
    // 이동할 거리 계산: (현재 인덱스) * (아이템 너비)
    // 팁: -100%는 트랙을 왼쪽으로 한 칸 움직인다는 뜻이야.
    const moveDistance = -currentIndex * itemWidth;

    // CSS의 transform 속성을 이용해 트랙을 이동시키는 핵심! (원리)
    // - GPU 가속을 사용해서 부드럽고 빠르게 움직여!
    galleryTrack.style.transform = `translateX(${moveDistance}%)`;

    // 개발 초보자 팁: `transform: translateX()`를 쓰는 이유?
    // - `left` 속성을 변경하는 것보다 성능이 훨씬 좋아.
    // - 웹 브라우저가 요소를 다시 배치(reflow)하거나 다시 그리는(repaint) 과정을 건너뛰고
    //   요소의 위치만 빠르게 이동(composite)시키기 때문이야. 👍
}

// 4. 네비게이션 버튼 클릭 시 호출되는 함수 (HTML에 이미 onclick으로 연결되어 있음)
function scrollGallery(direction) {
    // direction: 1은 다음, -1은 이전

    // 다음 인덱스 계산
    let newIndex = currentIndex + direction;

    // **무한 루프(Loop) 구현 (선택 사항 - 개발 초보에게 유용한 기능)**
    // - 마지막 슬라이드에서 다음 버튼을 누르면 처음으로, 처음에서 이전 버튼을 누르면 마지막으로 가도록!
    if (newIndex >= totalItems) {
        newIndex = 0; // 마지막에서 다음으로 가면 처음(0)으로
    } else if (newIndex < 0) {
        newIndex = totalItems - 1; // 처음(0)에서 이전으로 가면 마지막으로
    }

    // 인덱스 업데이트 및 이동 함수 호출
    currentIndex = newIndex;
    updateGalleryPosition();
}

// 5. 초기 로드 시 슬라이더 위치 설정
// window.onload 또는 DOMContentLoaded 이벤트가 더 안전하지만,
// 이 코드가 HTML 끝 <script>에 있다면 바로 실행해도 괜찮아.
updateGalleryPosition();

// 함수를 외부에서 사용할 수 있도록 window 객체에 등록 (onclick="scrollGallery()" 때문에 필요)
window.scrollGallery = scrollGallery;