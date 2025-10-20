package kr.co.greengarden.dto;

import kr.co.greengarden.entity.Category;
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
    private String categorySlug;
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
    private int orderCount;

    // ✅ 새로 추가된 컬럼들
    private String status;      // 상품상태
    private String tax;         // 과세여부
    private String receipt;     // 영수증 발행여부
    private String sellerType;  // 사업자구분
    private String origin;      // 원산지

    public Product toEntity(MemberSeller seller, Category category) {
        return Product.builder()
                .proId(this.proId)
                .seller(seller)
                .proNo(this.proNo)
                .category(category)
                .name(this.name)
                .description(this.description)
                .manufacturer(this.manufacturer)
                .price(this.price)
                .discountRate(this.discountRate)
                .point(this.point)
                .stock(this.stock)
                .deliveryFee(this.deliveryFee)
                .img1(this.img1)
                .img2(this.img2)
                .img3(this.img3)
                .imgDetail(this.imgDetail)
                .views(this.views)
                .orderCount(this.orderCount)
                .status(this.status)
                .tax(this.tax)
                .receipt(this.receipt)
                .sellerType(this.sellerType)
                .origin(this.origin)
                .build();
    }
}