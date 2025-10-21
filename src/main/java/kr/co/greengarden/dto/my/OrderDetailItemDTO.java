package kr.co.greengarden.dto.my;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailItemDTO {

    private Long proId;
    private String productName;
    private String productImg;
    private int price;
    private int quantity;
    private int discountRate;
    private int discountAmount;
    private int deliveryFee;
    private int lineTotal;
    private String sellerId;
    private String sellerName;
}
