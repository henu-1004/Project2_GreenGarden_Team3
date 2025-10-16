const radios = document.querySelectorAll('input[name="discountValue"]');
const benefitInput = document.getElementById('benefitName');
const discountTypeInput = document.getElementById('discountType');

radios.forEach(radio => {
    radio.addEventListener('change', function () {
        const discountValue = this.value;
        const discountType = this.getAttribute('data-type');
        const benefitNameText = this.parentNode.textContent.trim();

        benefitInput.value = benefitNameText;
        discountTypeInput.value = discountType;

        console.log(`선택된 혜택: ${benefitNameText}, 유형: ${discountType}, 값: ${discountValue}`);
    });
});