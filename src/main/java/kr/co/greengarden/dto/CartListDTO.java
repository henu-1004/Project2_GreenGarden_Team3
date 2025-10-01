package kr.co.greengarden.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartListDTO {
    private int quantity;
    private String name;
    private String description;
    private String img1;
    private int price;
    private int discountRate;
    private int point;
    private int deliveryFee;
}
