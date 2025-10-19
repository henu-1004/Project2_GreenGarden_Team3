package kr.co.greengarden.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminIndexChartDTO {
    private List<String> dateLabels;      // 날짜 목록 (그래프 X축)
    private List<Integer> orderCounts;    // 날짜별 주문 건수
    private List<Integer> paymentCounts;  // 날짜별 결제 건수
    private List<Integer> cancelCounts;   // 날짜별 취소 건수
    private List<String> categoryNames;   // 카테고리명
    private List<Integer> categoryValues; // 카테고리별 매출
}
