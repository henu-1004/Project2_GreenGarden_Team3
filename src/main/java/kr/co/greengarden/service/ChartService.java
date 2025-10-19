package kr.co.greengarden.service;

import kr.co.greengarden.dto.admin.AdminIndexChartDTO;
import kr.co.greengarden.repository.ChartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/*
    날짜 : 2025/10/19
    이름 : 이수연
    내용 : admin chart
*/
@RequiredArgsConstructor
@Service
public class ChartService {

    private final ChartRepository chartRepository;


    // 관리자 차트 데이터용 (주차별 오프셋 적용)
    public AdminIndexChartDTO getAdminIndexChartData(int weekOffset) {

        List<String> dateLabels = new ArrayList<>();
        List<Integer> orderCounts = new ArrayList<>();
        List<Integer> paymentCounts = new ArrayList<>();
        List<Integer> cancelCounts = new ArrayList<>();

        // 기준일 계산 (weekOffset에 따라 이동)
        // weekOffset = 0: 이번주
        // weekOffset = -1: 지난주
        // weekOffset = 1: 다음주
        LocalDate baseDate = LocalDate.now().plusWeeks(weekOffset);

        // 월요일 기준으로 시작 (이번주 월요일 찾기)
        LocalDate startDate = baseDate.with(DayOfWeek.MONDAY);

        // 월~일 7일 데이터 수집
        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            dateLabels.add(date.format(DateTimeFormatter.ofPattern("MM-dd")));

            // DB에서 해당 날짜 데이터 조회
            orderCounts.add(chartRepository.countOrdersByDate(date));
            paymentCounts.add(chartRepository.countPaymentsByDate(date));
            cancelCounts.add(chartRepository.countCancelsByDate(date));
        }

        // 카테고리별 매출. 시작일과 6일 후(일요일)를 전달하여 총 7일 범위 설정
        chartRepository.findCategorySalesByDateRange(startDate, startDate.plusDays(6));

        // 카테고리별 매출 (기간 필터 적용 - 선택사항)
        List<Object[]> categorySales = chartRepository.findCategorySalesByDateRange(startDate, startDate.plusDays(4));
        List<String> categoryNames = new ArrayList<>();
        List<Integer> categoryValues = new ArrayList<>();

        for (Object[] row : categorySales) {
            categoryNames.add((String) row[0]);
            categoryValues.add(((Number) row[1]).intValue());
        }

        return new AdminIndexChartDTO(
                dateLabels, orderCounts, paymentCounts, cancelCounts,
                categoryNames, categoryValues
        );
    }

}
