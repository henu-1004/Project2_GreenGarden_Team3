package kr.co.greengarden.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/*
 * 날짜 : 2025/09/25
 * 이름 : 한탁원
 * 내용 : PointLedger(포인트 내역) 초안 설정
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_POINT_LEDGER")
public class PointLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int legId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="pointId")
    private Point point;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="orderNo")
    private Order order;

    @Column
    private LocalDate earnedAt;

    @Column
    private String type;

    @Column
    private int amount;

    @Column
    private String note;

    @Column
    private LocalDate expiredAt;

    @PrePersist
    void onCreate() { if (earnedAt == null) earnedAt = LocalDate.now(); }

}
