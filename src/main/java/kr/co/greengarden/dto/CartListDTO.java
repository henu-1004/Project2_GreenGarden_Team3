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
    private int cartId;
    private int proId;
    private int quantity;
    private String name;
    private String description;
    private String img1;
    private int price;
    private int discountRate;
    private int point;
    private int deliveryFee;

    public CartListDTO(int carId, int proId, int quantity, String name, String description, String img1, int price, int discountRate, int point, int deliveryFee) {
        this.cartId = carId;
        this.proId = proId;
        this.quantity = quantity;
        this.name = name;
        this.description = description;
        this.img1 = img1;
        this.price = price;
        this.discountRate = discountRate;
        this.point = point;
        this.deliveryFee = deliveryFee;
    }

    private int originalPrice;
}
