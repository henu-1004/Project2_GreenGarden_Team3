package kr.co.greengarden.entity;

import jakarta.persistence.*;
import kr.co.greengarden.dto.ProductDTO;
import lombok.*;

/*
 * 날짜 : 2025/09/24
 * 이름 : 한탁원
 * 내용 : Product 초안 설정
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_PRODUCT")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int proId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memId", referencedColumnName = "memId")
    private MemberSeller seller;

    @Column
    private String proNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_SLUG", referencedColumnName = "SLUG") // 마이그레이션 끝난 뒤 NOT NULL 권장
    private Category category;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String manufacturer;

    @Column
    private int price;

    @Column
    private int discountRate;

    @Column
    private int point;

    @Column
    private int stock;

    @Column
    private int deliveryFee;

    @Column
    private String img1;

    @Column
    private String img2;

    @Column
    private String img3;

    @Column
    private String imgDetail;

    @Column
    private int views;

    public ProductDTO toDTO() {
        return ProductDTO.builder()
                .proId(this.proId)
                .memId(seller.getMemId())
                .proNo(this.proNo)
                .categorySlug(category.getSlug())
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
                .build();
    }


}
