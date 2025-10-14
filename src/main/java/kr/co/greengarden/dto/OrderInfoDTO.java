package kr.co.greengarden.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInfoDTO {
    int count;
    int originalTotalPrice;
    int totalPrice;
    int discountPrice;
    int deliveryFee;
    int totalPoint;
}
