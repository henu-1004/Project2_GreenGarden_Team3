package kr.co.greengarden.dto.my;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 마이페이지 교환 신청을 TB_EXCHANGE 테이블에 기록하기 위한 DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRequestDTO {
    private String orderNo;
    private Long orderItemId;
    private String type;
    private String detail;
    private String imgPath;
}
