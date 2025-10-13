package kr.co.greengarden.dto;

import jakarta.persistence.*;
import kr.co.greengarden.entity.Cart;
import kr.co.greengarden.entity.Member;
import kr.co.greengarden.entity.Product;
import lombok.*;

/*
 * 날짜 : 2025/10/01
 * 이름 : 한탁원
 * 내용 : Cart(장바구니) 초안 설정
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CartDTO {

    private int cartId;
    private String memId;
    private int proId;
    private int quantity;

    // Entity 변환 메서드
    public Cart toEntity(Member member, Product product) {
        return Cart.builder()
                .member(member)
                .product(product)
                .quantity(this.quantity)
                .build();
    }
}
