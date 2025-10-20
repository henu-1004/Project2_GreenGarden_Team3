package kr.co.greengarden.entity;

import jakarta.persistence.*;
import lombok.*;

/*
 * 날짜 : 2025/09/25
 * 이름 : 한탁원
 * 내용 : OrderItem(주문 내역) 초안 설정
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString (exclude = {"order", "cart"})
@Entity
@Table(name = "TB_ORDER_ITEM")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="orderNo")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="proId")
    private Product product;

    @Column
    private int quantity;

    @Column
    private int price;

    @Column
    private int discountRate;

    @Column
    private int pointUsed;

    /* admin chart에 필요해서 추가 */
    @Column(name = "CANCEL_YN")
    private String cancelYN;
}
