document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.coupon-table .btn-stop').forEach(btn => {
        btn.addEventListener('click', async () => {
            const url = btn.dataset.url;
            if (!url) return;
            if (!confirm('정말 이 발급을 중단하시겠습니까?')) return;

            try {
                const res = await fetch(url, {
                    method: 'POST',
                    headers: { 'X-Requested-With': 'XMLHttpRequest' }
                });

                if (res.ok) {
                    alert('쿠폰 발급이 중단되었습니다.');
                    location.reload();
                } else {
                    const txt = await res.text().catch(() => '');
                    alert(`중단 실패: ${res.status} ${txt}`);
                }
            } catch (e) {
                console.error(e);
                alert('요청 중 오류가 발생했습니다.');
            }
        });
    });
});
