package kr.co.greengarden.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_COUPON_ISSUE")
public class CouponIssue {

    @Id
    @Column(name = "ISSUE_ID")
    private String issueId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COUPON_NO")
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private Member member;

    private String status;

    @Column(name = "USED_AT")
    private LocalDateTime usedAt;
}
