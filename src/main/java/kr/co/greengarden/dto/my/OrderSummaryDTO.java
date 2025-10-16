package kr.co.greengarden.dto.my;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderSummaryDTO {
    private String orderNo;         // 주문번호
    private String productName;     // 상품명
    private String productImg;      // 대표 이미지
    private int price;              // 상품 가격
    private int quantity;           // 수량
    private int totalPrice;         // ✅ 총금액 (price * quantity)
    private String status;          // 주문 상태
    private LocalDateTime orderedAt;// 주문일자
    private String sellerId;        // 판매자 ID
    private String sellerAddress;   // 판매자 주소
    private String sellerName;// TB_MEMBER_SELLER.COMPANY

    // ✅ 10.16 추가된 상태 관리 컬럼
    private String confirmYn;       // 구매확정 여부 ('Y' or 'N')
    private String reviewYn;        // 리뷰작성 여부 ('Y' or 'N')
    private String exchangeYn;      // 교환신청 여부 ('Y' or 'N')
    private String returnYn;        // 반품신청 여부 ('Y' or 'N')
}
