/**
 *
 */
//유효성 검사에 사용할 정규표현식
const reUid   = /^[a-z]+[a-z0-9]{4,19}$/g;
const rePass  = /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{5,16}$/;
const reName  = /^[가-힣]{2,10}$/;
const reEmail = /^[0-9a-zA-Z._%+\-]+@[0-9a-zA-Z.-]+\.[a-zA-Z]{2,}$/i;
const reHp    = /^01(?:0|1|[6-9])-(?:\d{4})-\d{4}$/;

// 유효성 검사 상태 변수
let isUidOk = false;
let isPassOk = false;
let isNameOk = false;
let isEmailOk = false;
let isHpOk = false;
let isBirthOk = false;
let isGenderOk = false;


document.addEventListener('DOMContentLoaded', function () {

    const isSeller = location.pathname.includes('registerSeller');
    if (isSeller) {
        initSellerValidation();
    } else {
        initGeneralValidation();
    }

});

//////////////////////////////////////////////////////////
// 일반 회원가입
//////////////////////////////////////////////////////////
function initGeneralValidation(){
    const btnCheckUid = document.getElementById('memIdBtn');
    const btnCheckPhone = document.getElementById('phoneBtn');
    const btnCheckEmail = document.getElementById('emailBtn');
    const btnEmailCode = document.getElementById('btnEmailCode');

    const uidResult = document.getElementsByClassName('uidResult')[0];
    const emailResult = document.getElementsByClassName('emailResult')[0];
    const hpResult = document.getElementsByClassName('hpResult')[0];
    const passResult = document.getElementsByClassName('passResult')[0];
    const nameResult = document.getElementsByClassName('nameResult')[0];

    const birthResult  = document.getElementsByClassName('birthResult')[0];
    const genderInputs = document.getElementsByName('gender');
    const genderResult  = document.getElementsByClassName('genderResult')[0];

    const auth = document.getElementsByClassName('auth')[0];

    const form = document.getElementsByTagName('form')[0];

    const authLabel = form.querySelector('label[for="auth"]');
    const authInput = form.auth;
    const authResult = form.getElementsByClassName('authResult')[0];


    //////////////////////////////////////////////////////////
    // 아이디 검사
    //////////////////////////////////////////////////////////
    btnCheckUid.addEventListener('click', function(e){

        const value = form.memId.value;
        console.log('value : ' + value);

        // 아이디 유효성 검사
        if(!value.match(reUid)){
            uidResult.innerText = '아이디가 유효하지 않습니다.';
            uidResult.style.color = 'red';
            isUidOk = false;
            return;
        }

        // 아이디 중복체크 요청
        fetch(`/greengarden/member/memId/${value}`)
            .then(res => res.json())
            .then(data => {
                console.log(data);
                if(data.count > 0){
                    uidResult.innerText = '이미 사용 중인 아이디 입니다.';
                    uidResult.style.color = 'red';
                    isUidOk = false;
                }else{
                    uidResult.innerText = '사용 가능한 아이디 입니다.';
                    uidResult.style.color = 'green';
                    isUidOk = true;
                }
            })
            .catch(err => {
                console.log(err);
            });
    });

    ////////////////////////////////////////
    // 비밀번호 검사
    ////////////////////////////////////////
    form.password2.addEventListener('focusout', function(e){

        const pw1 = form.password.value;
        const pw2 = form.password2.value;

        // 비밀번호 유효성 검사
        if(!pw1.match(rePass)){

            passResult.innerText = '비밀번호가 유효하지 않습니다. (영문, 숫자, 특수문자 8~12자)';
            passResult.style.color = 'red';
            isPassOk = false;
            return;

        }

        // 비밀번호 2회 일치 여부
        if(pw1 == pw2){
            passResult.innerText = '비밀번호가 일치합니다.';
            passResult.style.color = 'green';
            isPassOk = true;

        }else{
            passResult.innerText = '비밀번호가 일치하지 않습니다.';
            passResult.style.color = 'red';
            isPassOk = false;


        }

    });

    //////////////////////////////////////////
    // 이름 검사
    //////////////////////////////////////////
    form.name.addEventListener('focusout', function(e){

        const value = form.name.value;

        if(!value.match(reName)){
            nameResult.innerText = '이름은 한글 2~10자로 입력해주세요';
            nameResult.style.color = 'red';
            isNameOk = false;

        }else{
            nameResult.innerText = '올바른 이름 형식입니다.';
            nameResult.style.color = 'green';
            isNameOk = true;
        }
    });

    //////////////////////////////////////////////////////////
    // 생년월일
    //////////////////////////////////////////////////////////
    form.birth.addEventListener('focusout', function (){

        const value = form.birth.value;

        if(!value){
            birthResult.innerText = '생년월일을 입력하세요.';
            birthResult.style.color = 'red';
            isBirthOk = false;
        }else{
            birthResult.innerText = '생년월일이 확인되었습니다.';
            birthResult.style.color = 'green';
            isBirthOk = true;
        }
    });

    //////////////////////////////////////////////////////////
    // 성별
    //////////////////////////////////////////////////////////
    for(const radio of genderInputs){
        radio.addEventListener('change', function (){
            genderResult.innerText = '성별이 선택되었습니다.';
            genderResult.style.color = 'green';
            isGenderOk = true;
        });
    }


    //////////////////////////////////////////////////////////
    // 이메일 검사
    //////////////////////////////////////////////////////////

    function withCsrf(headers = {}) {
        const token  = document.querySelector('meta[name="_csrf"]')?.content;
        const header = document.querySelector('meta[name="_csrf_header"]')?.content;
        if (token && header) headers[header] = token;
        return headers;
    }


    let preventDblClick = false; // 이중 클릭 방지를 위한 상태 변수

    btnCheckEmail.addEventListener('click', function(e) {


        // 이중 클릭 방지
        if (preventDblClick) {
            return;
        }

        const value = form.email.value;
        console.log('value : ' + value);

        // 이메일 유효성 검사
        if (!value.match(reEmail)) {
            emailResult.innerText = '이메일이 유효하지 않습니다.';
            emailResult.style.color = 'red';
            isEmailOk = false;
            return;
        }

        // 이중 클릭 방지 실행
        preventDblClick = true;
        emailResult.innerText = '이메일로 인증코드 전송 중 입니다.';
        emailResult.style.color = 'green';

        fetch(`/greengarden/member/email/${encodeURIComponent(value)}`)
            .then(res => res.json())
            .then(data => {
                console.log(data);

                // 이중 클릭 방지 해제
                preventDblClick =false;

                if(data.count > 0){
                    emailResult.innerText = '이미 사용 중인 이메일 입니다.';
                    emailResult.style.color = 'red';
                    isEmailOk = false;
                }else{
                    emailResult.innerText = '이메일 인증번호를 입력하세요.';
                    emailResult.style.color = 'green';

                    // 인증번호 입력 필드 띄우기
                    //auth.style.display = 'block';
                    authLabel.style.display = 'block';
                    form.auth.style.display = 'block';
                    btnEmailCode.style.display = 'inline-block';
                    authResult.style.display = 'inline-block';


                }
            })
            .catch(err => {
                console.log(err);
            });
    });

    // 이메일 코드 전송 버튼 클릭
    btnEmailCode.addEventListener('click', async function (e) {
        // 중복 클릭 방지(원하면 동일 플래그 재사용)
        if (btnEmailCode.disabled) return;
        btnEmailCode.disabled = true;

        const email = form.email.value.trim();
        const code  = form.auth.value.trim(); // 문자열 그대로(선행 0 보존)
        if (!code) {
            authResult.innerText = '인증코드를 입력하세요.';
            authResult.style.color = 'red';
            btnEmailCode.disabled = false;
            return;
        }

        try {
            const response = await fetch('/greengarden/email/code', {
                method: 'POST',
                credentials: 'same-origin', // JSESSIONID/remember-me 포함
                headers: withCsrf({ 'Content-Type': 'application/json' }),
                body: JSON.stringify({ email, code }) // 서버에서 email+code 함께 검증 권장
            });

            // 200/400/410 등 상태별 처리
            if (response.status === 410) {
                authResult.innerText = '인증코드가 만료되었습니다. 다시 전송하세요.';
                authResult.style.color = 'red';
                isEmailOk = false;
                return;
            }
            if (!response.ok) {
                // 400 등: 불일치 혹은 기타 서버 에러
                authResult.innerText = '인증코드가 일치하지 않습니다.';
                authResult.style.color = 'red';
                isEmailOk = false;
                return;
            }

            const data = await response.json(); // { ok:true } 또는 { isMatched:true } 등
            const ok = data.ok === true || data.isMatched === true;

            if (ok) {
                emailResult.innerText = '이메일이 인증되었습니다.';
                emailResult.style.color = 'green';
                authResult.innerText = '';
                isEmailOk = true;
            } else {
                emailResult.innerText = '인증코드가 일치하지 않습니다.';
                emailResult.style.color = 'red';
                isEmailOk = false;
            }
        } catch (err) {
            console.error(err);
            emailResult.innerText = '인증 요청 중 오류가 발생했습니다.';
            emailResult.style.color = 'red';
            isEmailOk = false;
        } finally {
            btnEmailCode.disabled = false;
        }
    });



        //////////////////////////////////////////////////////////
        // 휴대폰 중복 체크
        //////////////////////////////////////////////////////////
        btnCheckPhone.addEventListener('click', function (e) {

            const value = form.phone.value;
            console.log('value : ' + value);

            if (!value.match(reHp)) {
                hpResult.innerText = '휴대폰 번호가 유효하지 않습니다.';
                hpResult.style.color = 'red';
                isHpOk = false;
                return;

            }

            fetch(`/greengarden/member/phone/${value}`)
                .then(res => res.json())
                .then(data => {
                    console.log(data);
                    if (data.count > 0) {
                        hpResult.innerText = '이미 사용 중인 휴대폰 입니다.';
                        hpResult.style.color = 'red';
                        isHpOk = false;

                    } else {
                        hpResult.innerText = '사용 가능한 휴대폰 입니다.';
                        hpResult.style.color = 'green';
                        isHpOk = true;

                    }
                })
                .catch(err => {
                    console.log(err);
                });
        });

        // 최종 폼 전송 처리
        form.addEventListener('submit', function (e) {
            e.preventDefault(); // 기본 홈전송 해제


            if (!isUidOk) {
                alert('아이디를 확인하세요.');
                return;
            }

            if (!isPassOk) {
                alert('비밀번호를 확인하세요.');
                return;
            }

            if (!isNameOk) {
                alert('이름을 확인하세요.');
                return;
            }

            if (!isBirthOk) {
                alert('생년월일을 확인하세요.');
                return;
            }

            if (!isGenderOk) {
                alert('성별을 선택하세요.');
                return;
            }

            if (!isEmailOk) {
                alert('이메일을 확인하세요.');
                return;
            }

            if (!isHpOk) {
                alert('휴대폰을 확인하세요.');
                return;
            }

            // 최종 폼 전송 실행
            form.submit();

        });
    }


//////////////////////////////////////////////////////////
// 판매자 회원가입
//////////////////////////////////////////////////////////

function initSellerValidation(){

    const form = document.getElementsByTagName('form')[0];
    const btnCheckUid = document.getElementById('memIdBtn');

    const uidResult = document.getElementsByClassName('uidResult')[0];
    const passResult = document.getElementsByClassName('passResult')[0];

    //////////////////////////////////////////////////////////
    // 아이디 검사
    //////////////////////////////////////////////////////////
    btnCheckUid.addEventListener('click', function(e){

        const value = form.memId.value;
        console.log('value : ' + value);

        // 아이디 유효성 검사
        if(!value.match(reUid)){
            uidResult.innerText = '아이디가 유효하지 않습니다.';
            uidResult.style.color = 'red';
            isUidOk = false;
            return;
        }

        // 아이디 중복체크 요청
        fetch(`/greengarden/member/memId/${value}`)
            .then(res => res.json())
            .then(data => {
                console.log(data);
                if(data.count > 0){
                    uidResult.innerText = '이미 사용 중인 아이디 입니다.';
                    uidResult.style.color = 'red';
                    isUidOk = false;
                }else{
                    uidResult.innerText = '사용 가능한 아이디 입니다.';
                    uidResult.style.color = 'green';
                    isUidOk = true;
                }
            })
            .catch(err => {
                console.log(err);
            });
    });

    ////////////////////////////////////////
    // 비밀번호 검사
    ////////////////////////////////////////
    form.password2.addEventListener('focusout', function(e){

        const pw1 = form.password.value;
        const pw2 = form.password2.value;

        // 비밀번호 유효성 검사
        if(!pw1.match(rePass)){

            passResult.innerText = '비밀번호가 유효하지 않습니다. (영문, 숫자, 특수문자 8~12자)';
            passResult.style.color = 'red';
            isPassOk = false;
            return;

        }

        // 비밀번호 2회 일치 여부
        if(pw1 == pw2){
            passResult.innerText = '비밀번호가 일치합니다.';
            passResult.style.color = 'green';
            isPassOk = true;

        }else{
            passResult.innerText = '비밀번호가 일치하지 않습니다.';
            passResult.style.color = 'red';
            isPassOk = false;

        }
    });

    // 최종 폼 전송 처리
    form.addEventListener('submit', function (e) {
        e.preventDefault(); // 기본 홈전송 해제


        if (!isUidOk) {
            alert('아이디를 확인하세요.');
            return;
        }

        if (!isPassOk) {
            alert('비밀번호를 확인하세요.');
            return;
        }


        // 최종 폼 전송 실행
        form.submit();

    });






}