/*
*   날짜 : 2025/10/16
*   이름 : 이수연
*   내용 : 쿠폰 등록
*/
document.getElementById('couponForm').addEventListener('submit', function(event) {
    event.preventDefault(); // 폼 기본 동작(새로고침) 방지

    const form = event.target;

    // 1. 선택된 radio 버튼을 찾는다. (혜택 정보 추출의 시작점)
    const selectedRadio = form.querySelector('input[name="discountValue"]:checked');

    // 2. [유효성 검사] 혜택 선택 여부 확인
    if (!selectedRadio) {
        alert('혜택을 반드시 선택해야 합니다.');
        return;
    }

    // 3. 세 가지 값 추출 및 Hidden 필드에 채우기
    const discountValue = selectedRadio.value;                           // DB의 DISCOUNT_VALUE에 저장될 값 ("1000", "10" 등)
    const discountType = selectedRadio.getAttribute('data-type'); // DB의 DISCOUNT_TYPE에 저장될 값 ("AMOUNT", "PERCENT" 등)
    const benefitNameText = selectedRadio.nextSibling.textContent.trim(); // DB의 BENEFIT에 저장될 혜택 이름 ("1,000원 할인")

    // Hidden 필드에 값을 넣어, DTO 필드명(name="benefit", name="discountType")에 맞게 준비
    form.benefit.value = benefitNameText;
    form.discountType.value = discountType;

    // 4. JSON 데이터 구성 (서버로 보낼 최종 DTO 형식)
    const couponData = {
        // [필수] DTO의 @NotNull, @NotBlank 검증 대상 필드
        type: form.type.value, // 쿠폰 종류 코드 (예: "1", "2")
        name: form.name.value,
        benefit: form.benefit.value,         // '1,000원 할인' (Hidden 필드에서 가져옴)
        discountValue: parseInt(discountValue), // 문자열을 숫자로 변환 (1000)
        discountType: form.discountType.value, // 'AMOUNT' (Hidden 필드에서 가져옴)

        // 날짜 형식 맞추기 (YYYY-MM-DDT00:00:00)
        startDate: form.startDate.value + 'T00:00:00',
        endDate: form.endDate.value + 'T23:59:59',
    };

    // 5. 서버 API 엔드포인트
    const apiUrl = '/admin/coupon/couponRegister';

    // 6. Fetch API (POST 요청)
    fetch(apiUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(couponData)
    })
        .then(response => { /* ... 성공/실패 처리 로직 ... */
            // 서버에서 던진 Validation 오류(400) 처리
            if (response.status === 400) {
                return response.text().then(text => { throw new Error(text); });
            }
            if (!response.ok) {
                throw new Error('등록 요청 실패! (상태 코드: ' + response.status + ')');
            }
            return response.json();
        })
        .then(data => {
            // 7. 성공 처리
            console.log('등록 성공:', data);
            alert(`✅ 쿠폰 등록 성공! 번호: ${data.couponNo}`);
            // 모달 닫기 함수 호출 (closeModal 함수가 정의되어 있다고 가정)
            // closeModal('modalCouponInsert');
        })
        .catch(error => { /* ... 오류 처리 로직 ... */
            // 8. 실패 처리
            console.error('등록 중 오류 발생:', error);
            alert('쿠폰 등록 중 오류가 발생했습니다. ' + error.message);
        });
});