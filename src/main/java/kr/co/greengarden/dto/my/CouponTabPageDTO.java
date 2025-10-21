package kr.co.greengarden.dto.my;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CouponTabPageDTO {
    private String activeTab;
    private PagedResult<MyCouponDTO> page;
    private CouponSummaryDTO summary;
}
