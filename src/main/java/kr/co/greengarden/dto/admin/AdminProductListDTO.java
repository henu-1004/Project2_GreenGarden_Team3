package kr.co.greengarden.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminProductListDTO {
    private int proId;
    private String img1;
    private String proNo;
    private String name;
    private int price;
    private int discountRate;
    private int point;
    private int stock;
    private String company;
    private int views;
}

