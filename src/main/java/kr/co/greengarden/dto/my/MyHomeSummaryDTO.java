package kr.co.greengarden.dto.my;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyHomeSummaryDTO {

    private long orderCount;
    private int availableCouponCount;
    private int totalPoint;
    private long inquiryCount;

    public static MyHomeSummaryDTO empty() {
        return MyHomeSummaryDTO.builder()
                .orderCount(0)
                .availableCouponCount(0)
                .totalPoint(0)
                .inquiryCount(0)
                .build();
    }
}

