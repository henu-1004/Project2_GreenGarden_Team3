document.addEventListener('DOMContentLoaded', () => {
  const $  = (sel, el=document) => el.querySelector(sel);
  const $$ = (sel, el=document) => Array.from(el.querySelectorAll(sel));

  const openModal  = (el) => el.classList.add('is-open');
  const closeModal = (el) => el.classList.remove('is-open');

  // 확인 모달
  $('#versionTable').addEventListener('click', e => {
    const a = e.target.closest('.js-open-view');
    if (!a) return;
    e.preventDefault();
    $('#viewVersion').textContent = a.dataset.version || '';
    $('#viewChanges').textContent = a.dataset.changes || '';
    openModal($('#modalView'));
  });

  // 등록 모달
  $('#btnCreate').addEventListener('click', e => {
    e.preventDefault();
    openModal($('#modalCreate'));
  });

  // 닫기
  $$('.js-close').forEach(btn => {
    btn.addEventListener('click', e => {
      e.preventDefault();
      closeModal(btn.closest('.modal'));
    });
  });

  // 오버레이 닫기
  [$('#modalView'), $('#modalCreate')].forEach(modal => {
    modal.addEventListener('click', e => {
      if (e.target === modal) closeModal(modal);
    });
  });
});
