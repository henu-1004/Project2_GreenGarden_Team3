package kr.co.greengarden.dto.my;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailRowDTO {

    private String orderNo;
    private LocalDateTime orderedAt;
    private String orderStatus;
    private String payMethod;
    private Integer totalPrice;

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

    private Long proId;
    private String productName;
    private String productImg;
    private Integer itemPrice;
    private Integer quantity;
    private Integer discountRate;
    private Integer deliveryFee;
    private Integer pointUsed;
    private String sellerId;
    private String sellerName;
}
