package kr.co.greengarden.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDeliveryDetailListDTO {
    private String img1;
    private String proNo;
    private String proName;

    private String seller;

    private int price;
    private int discountRate;
    private int quantity;
    private int deliveryFee;

    private String orderNo;

    private String recName;
    private String recPhone;
    private String recZipCode;
    private String recAddressBasic;
    private String recAddressDetail;

    private String company;
    private String invoiceNo;
    private String note;
}
