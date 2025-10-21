package kr.co.greengarden.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="TB_PRODUCT_REVIEW")
public class ProductReview {
    @Id
    @Column(name="REVIEW_ID")
    private int reviewId;

    @ManyToOne
    @JoinColumn(name="proId")
    private Product product;

    @ManyToOne
    @JoinColumn(name="memId")
    private Member member;

    @Column
    private int rating;
}
