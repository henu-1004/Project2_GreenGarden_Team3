package kr.co.greengarden.dto;

import kr.co.greengarden.entity.MemberSeller;
import kr.co.greengarden.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 날짜 : 2025/09/24
 * 이름 : 한탁원
 * 내용 : ProductDTO 초안 설정
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {

    private int proId;
    private String memId;
    private String proNo;
    private String classification1;
    private String classification2;
    private String name;
    private String description;
    private String manufacturer;
    private int price;
    private int discountRate;
    private int point;
    private int stock;
    private int deliveryFee;
    private String img1;
    private String img2;
    private String img3;
    private String imgDetail;
    private int views;

    public Product toEntity(MemberSeller seller) {
        return Product.builder()
                .proId(proId)
                .seller(seller)
                .proNo(proNo)
                .classification1(classification1)
                .classification2(classification2)
                .name(name)
                .description(description)
                .manufacturer(manufacturer)
                .price(price)
                .discountRate(discountRate)
                .point(point)
                .stock(stock)
                .deliveryFee(deliveryFee)
                .img1(img1)
                .img2(img2)
                .img3(img3)
                .imgDetail(imgDetail)
                .views(views)
                .build();
    }
}