package kr.co.greengarden.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Lazy;

import java.time.LocalDateTime;

/*
 * 날짜 : 2025/09/25
 * 이름 : 한탁원
 * 내용 : Order(주문) 초안 설정
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_ORDER")
public class Order {

    @Id
    private String orderNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="memId")
    private Member member;

    @Column
    private int totalPrice;

    @Column
    private String payMethod;

    @Column
    private String status;

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

    @PrePersist
    void onCreate() { if (orderedAt == null) orderedAt = LocalDateTime.now(); }



}
