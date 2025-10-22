package kr.co.greengarden.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductListDTO {

    private int proId;
    private String img1;
    private String name;
    private String description;
    private int price;
    private int deliveryFee;
    private int discountRate;
    private String company;
    private LocalDateTime createdAt;
    private int rating;

    public ProductListDTO(int proId, String img1, String name, String description,
                          int price, int deliveryFee, int discountRate, String company) {
        this.proId = proId;
        this.img1 = img1;
        this.name = name;
        this.description = description;
        this.price = price;
        this.deliveryFee = deliveryFee;
        this.discountRate = discountRate;
        this.company = company;
    }

    public ProductListDTO(int proId, String img1, String name, String description,
                          int price, int deliveryFee, int discountRate, String company, LocalDateTime createdAt) {
        this.proId = proId;
        this.img1 = img1;
        this.name = name;
        this.description = description;
        this.price = price;
        this.deliveryFee = deliveryFee;
        this.discountRate = discountRate;
        this.company = company;
        this.createdAt = createdAt;
    }

    public ProductListDTO(int proId, String img1, String name, String description,
                          int price, int deliveryFee, int discountRate, String company, LocalDateTime createdAt, int rating) {
        this.proId = proId;
        this.img1 = img1;
        this.name = name;
        this.description = description;
        this.price = price;
        this.deliveryFee = deliveryFee;
        this.discountRate = discountRate;
        this.company = company;
        this.createdAt = createdAt;
        this.rating = rating;
    }

    private int originalPrice;
}
