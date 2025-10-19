document.addEventListener('DOMContentLoaded', function () {

    //////////////////////////////////////////////////////////////////////
    // ✨ 날짜 범위 표시
    const dateRange = document.getElementById('dateRange');
    // dateLabels는 이미 전역 변수로 존재한다.
    if (dateRange && dateLabels.length > 0) {
        const startDate = dateLabels[0];
        const endDate = dateLabels[dateLabels.length - 1];
        dateRange.textContent = `${startDate} ~ ${endDate}`;
    }

    // 🌟 주차 이동 버튼 로직 (수정)
    const prevWeekBtn = document.getElementById('prevWeekBtn');
    const nextWeekBtn = document.getElementById('nextWeekBtn');

    // 이전 주차 버튼 이벤트
    if (prevWeekBtn) {
        prevWeekBtn.addEventListener('click', function() {
            // currentWeekOffset은 이미 전역 변수로 존재한다.
            const newOffset = currentWeekOffset - 1;
            // 쿼리 파라미터를 변경하여 페이지를 다시 로드한다.
            window.location.href = '/admin/?weekOffset=' + newOffset;
        });
    }

    // 다음 주차 버튼 이벤트
    if (nextWeekBtn) {
        nextWeekBtn.addEventListener('click', function() {
            const newOffset = currentWeekOffset + 1;
            window.location.href = '/admin/?weekOffset=' + newOffset;
        });
    }


    //////////////////////////////////////////////////////////////////////
    // 막대 그래프
    const barCtx = document.getElementById('barChart');
    if (barCtx) {
        new Chart(barCtx.getContext('2d'), {
            type: 'bar',
            data: {
                // 🌟 전역 변수 dateLabels 사용
                labels: dateLabels,
                datasets: [
                    {
                        label: '주문',
                        data: orderCounts, // 🌟 전역 변수 사용
                        backgroundColor: '#4e80bc',
                        borderWidth: 0
                    },
                    {
                        label: '결제',
                        // 🌟 전역 변수 paymentCounts 사용
                        data: paymentCounts,
                        backgroundColor: '#be4f4c',
                        borderWidth: 0
                    },
                    {
                        label: '취소',
                        // 🌟 전역 변수 cancelCounts 사용
                        data: cancelCounts,
                        backgroundColor: '#9aba58',
                        borderWidth: 0
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'top',
                        labels: {
                            font: {
                                size: 12
                            },
                            boxWidth: 15,
                            padding: 15
                        }
                    },
                    title: {
                        display: false
                    }
                },
                scales: {
                    x: {
                        grid: {
                            display: false
                        }
                    },
                    y: {
                        beginAtZero: true,
                        grid: {
                            color: '#e0e0e0'
                        }
                    }
                }
            }
        });
    }

    // 원 그래프 (기간별 카테고리 매출)
    const pieCtx = document.getElementById('pieChart');
    if (pieCtx) {
        new Chart(pieCtx.getContext('2d'), {
            type: 'pie',
            data: {
                // 🌟 전역 변수 categoryNames 사용
                labels: categoryNames,
                datasets: [{
                    // 🌟 전역 변수 categoryValues 사용
                    data: categoryValues,
                    backgroundColor: [
                        '#5b9bd5',
                        '#ed7d31',
                        '#a5a5a5',
                        '#70ad47',
                    ],
                    borderWidth: 2,
                    borderColor: '#fff'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'right',
                        labels: {
                            font: {
                                size: 12
                            },
                            boxWidth: 15,
                            padding: 10
                        }
                    },
                    title: {
                        display: true,
                        text: '주요매출',
                        font: {
                            size: 14,
                            weight: 'bold'
                        },
                        padding: {
                            bottom: 20
                        }
                    }
                }
            }
        });
    }
});
