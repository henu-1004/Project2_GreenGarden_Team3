package kr.co.greengarden.dto.my;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CouponPageDTO {
    private List<MyCouponDTO> availableCoupons;
    private List<MyCouponDTO> usedCoupons;
    private List<MyCouponDTO> expiredCoupons;
    private CouponSummaryDTO summary;
}
