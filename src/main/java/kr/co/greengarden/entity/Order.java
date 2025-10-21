package kr.co.greengarden.entity;

import jakarta.persistence.*;
import lombok.*;
import org.apache.logging.log4j.util.Lazy;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
 * 날짜 : 2025/09/25
 * 이름 : 한탁원
 * 내용 : Order(주문) 초안 설정
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"member", "orderItems"})
@Entity
@Table(name = "TB_ORDER")
public class Order {

    @Id
    private String orderNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memId")
    private Member member;

    @Column
    private int totalPrice;

    @Column
    private String payMethod;

    @Column
    private String status;

    @CreationTimestamp
    @Column
    private LocalDateTime orderedAt;

    @Column
    private String recName;

    @Column
    private String recPhone;

    @Column
    private String recZipCode;

    @Column
    private String recAddressBasic;

    @Column
    private String recAddressDetail;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();


    public void setMember(Member member) {
        this.member = member;
    }
}
