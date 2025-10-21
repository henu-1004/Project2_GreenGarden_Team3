package kr.co.greengarden.dto.my;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 마이페이지 반품 신청을 TB_RETURN 테이블에 기록하기 위한 DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnRequestDTO {
    private String orderNo;
    private Long orderItemId;
    private String type;
    private String detail;
    private String imgPath;
}
