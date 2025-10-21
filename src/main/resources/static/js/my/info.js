(function () {
    const ZIP_SELECTOR = '#zipCode';
    const ADDRESS_SELECTOR = '#addressBasic';
    const DETAIL_SELECTOR = '#addressDetail';
    const BUTTON_SELECTOR = '#findAddressBtn';

    const composeRoadExtra = (data) => {
        const extras = [];
        if (data.bname && /[동|로|가]$/g.test(data.bname)) {
            extras.push(data.bname);
        }
        if (data.buildingName && data.apartment === 'Y') {
            extras.push(data.buildingName);
        }
        return extras.length ? ` (${extras.join(', ')})` : '';
    };

    const resolveAddress = (data) => {
        if (!data) {
            return '';
        }
        let address = data.userSelectedType === 'R' ? data.roadAddress : data.jibunAddress;
        if (!address) {
            address = data.autoRoadAddress || data.autoJibunAddress || '';
        }
        if (data.userSelectedType === 'R' && address) {
            address += composeRoadExtra(data);
        }
        return address.trim();
    };

    const fillAddressFields = (data) => {
        const zipField = document.querySelector(ZIP_SELECTOR);
        const addressField = document.querySelector(ADDRESS_SELECTOR);
        const detailField = document.querySelector(DETAIL_SELECTOR);

        if (!zipField || !addressField) {
            return;
        }

        zipField.value = data.zonecode || '';
        addressField.value = resolveAddress(data);

        if (detailField) {
            detailField.focus();
        }
    };

    const openPostcode = () => {
        if (typeof window === 'undefined' || !window.daum || typeof window.daum.Postcode !== 'function') {
            window.alert('주소 검색 서비스를 불러오지 못했습니다.\n잠시 후 다시 시도해주세요.');
            return;
        }

        new window.daum.Postcode({
            oncomplete: (data) => {
                fillAddressFields(data);
            }
        }).open({
            popupTitle: '그린가든 주소 검색'
        });
    };

    const toggleModalVisibility = (modal, visible) => {
        if (!modal) {
            return;
        }
        modal.style.display = visible ? 'flex' : 'none';
        modal.setAttribute('aria-hidden', visible ? 'false' : 'true');
        if (!visible) {
            const password = modal.querySelector('input[type="password"]');
            if (password) {
                password.value = '';
            }
        }
    };

    const bindWithdrawModal = () => {
        const trigger = document.getElementById('withdrawBtn');
        const modal = document.getElementById('withdrawModal');
        if (!trigger || !modal) {
            return;
        }

        const closeButtons = modal.querySelectorAll('[data-close="withdrawModal"]');
        const closeModal = () => toggleModalVisibility(modal, false);
        const openModal = () => {
            toggleModalVisibility(modal, true);
            const field = modal.querySelector('#withdrawPassword');
            if (field) {
                field.focus();
            }
        };

        trigger.addEventListener('click', openModal);
        closeButtons.forEach(btn => {
            btn.addEventListener('click', closeModal);
            btn.addEventListener('keydown', (event) => {
                if (event.key === 'Enter' || event.key === ' ') {
                    event.preventDefault();
                    closeModal();
                }
            });
        });

        modal.addEventListener('click', (event) => {
            if (event.target === modal) {
                closeModal();
            }
        });

        modal.addEventListener('keydown', (event) => {
            if (event.key === 'Escape') {
                closeModal();
            }
        });
    };

    document.addEventListener('DOMContentLoaded', () => {
        const trigger = document.querySelector(BUTTON_SELECTOR);
        if (trigger) {
            trigger.addEventListener('click', openPostcode);
        }
        bindWithdrawModal();
    });
})();
