package kr.co.greengarden.dto.my;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {

    private String orderNo;
    private LocalDateTime orderedAt;
    private String orderStatus;
    private String payMethod;

    private int productTotal;
    private int discountTotal;
    private int shippingFee;
    private int pointUsed;
    private int finalAmount;

    private String deliveryStatus;
    private String deliveryCompany;
    private String invoiceNo;
    private String deliveryNote;

    private String receiverName;
    private String receiverPhone;
    private String receiverZipCode;
    private String receiverAddressBasic;
    private String receiverAddressDetail;

    private String ordererName;
    private String ordererPhone;
    private String ordererEmail;

    @Builder.Default
    private List<OrderDetailItemDTO> items = Collections.emptyList();
}
