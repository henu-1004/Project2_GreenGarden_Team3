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

    const bindPasswordValidation = () => {
        const form = document.querySelector('.info-form');
        if (!form) {
            return;
        }

        const newPassword = form.querySelector('#newPassword');
        const confirmPassword = form.querySelector('#confirmPassword');
        const message = document.getElementById('passwordMismatch');

        if (!newPassword || !confirmPassword || !message) {
            return;
        }

        const setErrorState = (visible) => {
            [newPassword, confirmPassword].forEach(input => {
                if (!input) {
                    return;
                }
                input.classList.toggle('input-error', visible);
            });
            message.classList.toggle('active', visible);
        };

        const validate = (shouldDisplayError = true) => {
            const newValue = newPassword.value.trim();
            const confirmValue = confirmPassword.value.trim();

            if (!newValue) {
                setErrorState(false);
                return true;
            }

            if (!confirmValue) {
                if (shouldDisplayError) {
                    setErrorState(true);
                } else {
                    setErrorState(false);
                }
                return false;
            }

            if (newValue === confirmValue) {
                setErrorState(false);
                return true;
            }

            if (shouldDisplayError || confirmValue.length > 0) {
                setErrorState(true);
            } else {
                setErrorState(false);
            }
            return false;
        };

        [newPassword, confirmPassword].forEach(input => {
            input.addEventListener('input', () => {
                if (!newPassword.value && !confirmPassword.value) {
                    setErrorState(false);
                    return;
                }
                validate(false);
            });
        });

        form.addEventListener('submit', (event) => {
            if (!validate(true)) {
                event.preventDefault();
                confirmPassword.focus();
            }
        });
    };

    document.addEventListener('DOMContentLoaded', () => {
        const trigger = document.querySelector(BUTTON_SELECTOR);
        if (trigger) {
            trigger.addEventListener('click', openPostcode);
        }
        bindWithdrawModal();
        bindPasswordValidation();
    });
})();
