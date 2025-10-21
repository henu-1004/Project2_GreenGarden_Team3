package kr.co.greengarden.dto.my;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 주문 상품의 단일 상태를 조회하기 위한 DTO.
 * 교환/반품/취소 요청 시 해당 주문이 실제 회원에게 속한 건인지 검증하고,
 * 배송 상태에 따라 가능 여부를 판단하는 데 사용한다.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemStatusDTO {
    private Long orderItemId;
    private String deliveryStatus;
    private String cancelYn;
    private String exchangeYn;
    private String returnYn;
}
