package kr.co.greengarden.dto.my;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 최근 주문 내역에서 노출되는 주문 상세 모달의 상품 행 정보를 위한 DTO.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailItemDTO {

    private Long orderItemId;   // 주문 상세 일련번호
    private Long proId;         // 상품 ID
    private String productName; // 상품명
    private String productImg;  // 상품 이미지 경로
    private int price;          // 단가
    private int quantity;       // 수량
    private int discountRate;   // 할인율
    private int deliveryFee;    // 배송비

    /**
     * 할인 금액 계산 (할인율은 % 단위라고 가정한다).
     */
    public int getDiscountAmount() {
        if (discountRate <= 0) {
            return 0;
        }
        long original = (long) price * quantity;
        return (int) Math.round(original * (discountRate / 100.0));
    }

    /**
     * 상품 총액(할인 전) 계산.
     */
    public int getLineTotal() {
        return price * quantity;
    }
}

