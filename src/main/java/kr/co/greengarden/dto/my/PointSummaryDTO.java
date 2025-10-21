package kr.co.greengarden.dto.my;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PointSummaryDTO {
    private int totalPoint;
    private int earnedInPeriod;
    private int usedInPeriod;
    private int expiringSoonAmount;
    private int expiringSoonCount;
    private LocalDate expiringReferenceDate;
}
