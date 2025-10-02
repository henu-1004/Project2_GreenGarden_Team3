/**
 *
 */
//유효성 검사에 사용할 정규표현식
const reUid   = /^[a-z]+[a-z0-9]{4,19}$/g;
const rePass  = /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{5,16}$/;
const reName  = /^[가-힣]{2,10}$/
const reNick  = /^[a-zA-Zㄱ-힣0-9][a-zA-Zㄱ-힣0-9]*$/;
const reEmail = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
const reHp    = /^01(?:0|1|[6-9])-(?:\d{4})-\d{4}$/;

// 유효성 검사 상태 변수
let isUidOk = false;
let isPassOk = false;
let isNameOk = false;
let isNickOk = false;
let isEmailOk = false;
let isHpOk = false;


document.addEventListener('DOMContentLoaded', function(){

    const btnCheckUid = document.getElementById('memIdBtn');
    const btnCheckPhone = document.getElementById('phoneBtn');
    const btnCheckEmail = document.getElementById('emailBtn');
    const btnEmailCode = document.getElementById('btnEmailCode');

    const uidResult = document.getElementsByClassName('uidResult')[0];
    const emailResult = document.getElementsByClassName('emailResult')[0];
    const hpResult = document.getElementsByClassName('hpResult')[0];
    const passResult = document.getElementsByClassName('passResult')[0];
    const nameResult = document.getElementsByClassName('nameResult')[0];

    const auth = document.getElementsByClassName('auth')[0];

    const form = document.getElementsByTagName('form')[0];

    //////////////////////////////////////////////////////////
    // 아이디 검사
    //////////////////////////////////////////////////////////
    btnCheckUid.addEventListener('click', function(e){

        const value = form.usid.value;
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

        const pw1 = form.pass.value;
        const pw2 = form.pass2.value;

        // 비밀번호 유효성 검사
        if(!pw1.match(rePass)){

            passResult.innerText = '비밀번호가 유효하지 않습니다.';
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
    form.us_name.addEventListener('focusout', function(e){

        const value = form.us_name.value;

        if(!value.match(reName)){
            nameResult.innerText = '이름이 유효하지 않습니다.';
            nameResult.style.color = 'red';
            isNameOk = false;

        }else{
            nameResult.innerText = '';
            isNameOk = true;
        }
    });

    //////////////////////////////////
    // 별명 검사
    ////////////////////////////////////

    btnCheckNick.addEventListener('click', function(e){

        const value = form.nick.value;
        console.log('value : ' + value);

        // 별명 유효서 검사
        if(!value.match(reNick)){
            nickResult.innerText = '별명이 유효하지 않습니다.';
            nickResult.style.color = 'red';
            isNickOk = false;
            return;
        }

        // 별명 중복 요청
        fetch(`/sboard/user/nick/${value}`)
            .then(res => res.json())
            .then(data => {
                console.log(data);
                if(data.count > 0){
                    nickResult.innerText = '이미 사용 중인 별명 입니다.';
                    nickResult.style.color = 'red';
                    isNickOk = false;

                }else{
                    nickResult.innerText = '사용 가능한 별명 입니다.';
                    nickResult.style.color = 'green';
                    isNickOk = true;

                }
            })
            .catch(err => {
                console.log(err);
            });
    });

    //////////////////////////////////////////////////////////
    // 이메일 검사
    //////////////////////////////////////////////////////////


    let preventDblClick = false; // 이중 클릭 방지를 위한 상태 변수

    btnCheckEmail.addEventListener('click', function(e){


        // 이중 클릭 방지
        if(preventDblClick){
            return;
        }

        const value = form.email.value;
        console.log('value : ' + value);

        // 이메일 유효성 검사
        if(!value.match(reEmail)){
            emailResult.innerText = '이메일이 유효하지 않습니다.';
            emailResult.style.color = 'red';
            isEmailOk = false;
            return;
        }

        // 이중 클릭 방지 실행
        preventDblClick = true;
        emailResult.innerText = '이메일로 인증코드 전송 중 입니다.';
        emailResult.style.color = 'green';

        fetch(`/sboard/user/email/${value}`)
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
                    auth.style.display = 'block';

                }
            })
            .catch(err => {
                console.log(err);
            });
    });

    // 이메일 코드 전송 버튼 클릭
    btnEmailCode.addEventListener('click', async function(e){

        const code = form.auth.value;

        // JSON 생성
        const jsonData = {
            "code": code
        };


        const response = await fetch('/sboard/email/code', {
            method: 'POST',
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(jsonData)
        });

        const data = await response.json();
        console.log(data);

        if(data.isMatched){
            emailResult.innerText = '이메일이 인증되었습니다.';
            emailResult.style.color = 'green';
            isEmailOk = true;
        }else{
            emailResult.innerText = '인증코드가 일치 않습니다.';
            emailResult.style.color = 'red';
            isEmailOk = false;
        }

    });

    //////////////////////////////////////////////////////////
    // 휴대폰 중복 체크
    //////////////////////////////////////////////////////////
    form.hp.addEventListener('focusout', function(e){

        const value = form.hp.value;
        console.log('value : ' + value);

        if(!value.match(reHp)){
            hpResult.innerText = '휴대폰 번호가 유효하지 않습니다.';
            hpResult.style.color = 'red';
            isHpOk = false;
            return;

        }

        fetch(`/sboard/user/hp/${value}`)
            .then(res => res.json())
            .then(data => {
                console.log(data);
                if(data.count > 0){
                    hpResult.innerText = '이미 사용 중인 휴대폰 입니다.';
                    hpResult.style.color = 'red';
                    isHpOk = false;

                }else{
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
    form.addEventListener('submit', function(e){
        e.preventDefault(); // 기본 홈전송 해제


        if(!isUidOk){
            alert('아이디를 확인하세요.');
            return;
        }

        if(!isPassOk){
            alert('비밀번호를 확인하세요.');
            return;
        }

        if(!isNameOk){
            alert('이름을 확인하세요.');
            return;
        }

        if(!isNickOk){
            alert('별명을 확인하세요.');
            return;
        }

        if(!isEmailOk){
            alert('이메일을 확인하세요.');
            return;
        }

        if(!isHpOk){
            alert('휴대폰을 확인하세요.');
            return;
        }

        // 최종 폼 전송 실행
        form.submit();

    });


}); // DOMContentLoaded 끝