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

    document.addEventListener('DOMContentLoaded', () => {
        const trigger = document.querySelector(BUTTON_SELECTOR);
        if (!trigger) {
            return;
        }
        trigger.addEventListener('click', openPostcode);
    });
})();
