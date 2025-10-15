
    // 유틸
    const $  = (sel, el=document) => el.querySelector(sel);
    const $$ = (sel, el=document) => Array.from(el.querySelectorAll(sel));

    // 모달 열기/닫기
    const openModal  = (el) => el.classList.add('is-open');
    const closeModal = (el) => el.classList.remove('is-open');

    // placeholder 제어(해당 모달 내부 필드에만 바인딩)
    function wirePlaceholders(modal){
      $$('.version-box input[type="text"], .version-box textarea', modal).forEach(field=>{
        const ph = field.getAttribute('placeholder') || '';
        field.addEventListener('focus', ()=> field.setAttribute('placeholder',''));
        field.addEventListener('blur',  ()=> { if(field.value.trim()==='') field.setAttribute('placeholder', ph); });
      });
    }

    // 1) [확인] → 모달1
    $('#versionTable').addEventListener('click', (e)=>{
      const a = e.target.closest('.js-open-view');
      if(!a) return;
      e.preventDefault();

      // 데이터 주입
      $('#viewVersion').textContent = a.dataset.version || '';
      $('#viewChanges').textContent = a.dataset.changes || '';

      openModal($('#modalView'));
    });

    // 2) 등록 버튼 → 모달2
    $('#btnCreate').addEventListener('click', (e)=>{
      e.preventDefault();
      openModal($('#modalCreate'));
      wirePlaceholders($('#modalCreate'));
     // $('#createVersion').focus();
    });

    // 공통 닫기 (X, 닫기 버튼)
    $$('.js-close').forEach(btn=>{
      btn.addEventListener('click', (e)=>{
        e.preventDefault();
        closeModal(btn.closest('.modal'));
      });
    });

    // 오버레이 클릭 닫기
    [$('#modalView'), $('#modalCreate')].forEach(modal=>{
      modal.addEventListener('click', (e)=>{
        if(e.target === modal) closeModal(modal);
      });
    });

// 등록하기 버튼
$('#btnSubmit').addEventListener('click', e => {
  e.preventDefault();

  const version = $('#createVersion').value.trim();
  const changes = $('#createChanges').value.trim();

  if (!version || !changes) {
    alert('버전과 변경내역을 모두 입력하세요.');
    return;
  }

  // 현재 시각
  const now = new Date();
  const dateStr = `${now.getFullYear()}.${String(now.getMonth()+1).padStart(2,'0')}.${String(now.getDate()).padStart(2,'0')} ${String(now.getHours()).padStart(2,'0')}:${String(now.getMinutes()).padStart(2,'0')}:${String(now.getSeconds()).padStart(2,'0')}`;

  // 새 행 추가
  const tbody = $('#versionTable tbody');
  const newRow = document.createElement('tr');
  const rowCount = tbody.querySelectorAll('tr').length + 1;

  newRow.innerHTML = `
    <td><input type="checkbox"></td>
    <td>${rowCount}</td>
    <td>${version}</td>
    <td>yingbbang</td>
    <td>${dateStr}</td>
    <td><a href="#" class="js-open-view" data-version="${version}" data-changes="${changes}">[확인]</a></td>
  `;
  tbody.prepend(newRow);

// 등록 클릭 시 저장
localStorage.setItem('lastVersion', $('#createVersion').value);
localStorage.setItem('lastChanges', $('#createChanges').value);

// 모달 열 때 불러오기
$('#createVersion').value = localStorage.getItem('lastVersion') || '';
$('#createChanges').value = localStorage.getItem('lastChanges') || '';

  closeModal($('#modalCreate'));
});
