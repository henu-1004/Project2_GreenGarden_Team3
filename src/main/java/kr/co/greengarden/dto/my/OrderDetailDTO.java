package kr.co.greengarden.dto.my;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 주문 상세 모달에서 사용하는 주문 전체 정보 DTO.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {

    private String orderNo;               // 주문번호
    private String orderedAt;      // 주문일시
    private String status;                // 주문상태
    private String payMethod;             // 결제수단
    private int totalPrice;               // 주문 총액(테이블 컬럼)

    private String recName;               // 수취인 이름
    private String recPhone;              // 수취인 연락처
    private String recZipCode;            // 수취인 우편번호
    private String recAddressBasic;       // 기본 주소
    private String recAddressDetail;      // 상세 주소
    private String deliveryMessage;       // 배송 메시지

    @Builder.Default
    private List<OrderDetailItemDTO> items = new ArrayList<>();

    // 화면에서 사용할 계산 값들
    private int itemsTotal;               // 상품 총액(합계)
    private int deliveryTotal;            // 배송비 합계
    private int discountTotal;            // 총 할인 금액
    private int paymentTotal;             // 최종 결제 금액

    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (recZipCode != null && !recZipCode.isBlank()) {
            sb.append('[').append(recZipCode).append("] ");
        }
        if (recAddressBasic != null) {
            sb.append(recAddressBasic);
        }
        if (recAddressDetail != null && !recAddressDetail.isBlank()) {
            sb.append(' ').append(recAddressDetail);
        }
        return sb.toString().trim();
    }
}

