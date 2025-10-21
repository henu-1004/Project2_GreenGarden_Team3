package kr.co.greengarden.dto.my;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyCouponDTO {
    private String issueId;
    private String couponNo;
    private String name;
    private String benefit;
    private String issuer;
    private String status;
    private String discountType;
    private int discountValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime issuedAt;
    private LocalDateTime usedAt;
    private String note;
}
