  // 모든 input[type=text]와 textarea 선택
  const fields = document.querySelectorAll('input[type="text"], textarea');

  fields.forEach(field => {
    // 각 요소에 자체적으로 기본값 보관
    field.dataset.default = field.value;

    // focus 시 (클릭 시)
    field.addEventListener('focus', function() {
      if (field.value === field.dataset.default) {
        field.value = '';
      }
    });

    // blur 시 (포커스 해제 시)
    field.addEventListener('blur', function() {
      if (field.value.trim() === '') {
        field.value = field.dataset.default;
      }
    });
  });