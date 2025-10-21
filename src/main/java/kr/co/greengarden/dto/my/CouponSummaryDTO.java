package kr.co.greengarden.dto.my;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CouponSummaryDTO {
    private int availableCount;
    private int usedCount;
    private int expiredCount;
    private int expiringSoonCount;
    private int estimatedSavings;
    private LocalDate expiringReferenceDate;
}
