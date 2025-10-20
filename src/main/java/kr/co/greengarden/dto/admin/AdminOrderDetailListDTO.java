package kr.co.greengarden.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminOrderDetailListDTO {
    private String img1;
    private String proNo;
    private String proName;

    private String seller;

    private int price;
    private int discountRate;
    private int quantity;
    private int deliveryFee;
    private int point;

    private LocalDateTime orderedAt;
    private String orderNo;

    private String name;
    private String zipCode;
    private String addressBasic;
    private String addressDetail;
    private String phone;

    private String recName;
    private String recZipCode;
    private String recAddressBasic;
    private String recAddressDetail;
    private String recPhone;
}
