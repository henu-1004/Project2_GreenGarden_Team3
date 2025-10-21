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
@Table(name = "TB_COUPON_ISSUE",
        indexes = {
                @Index(name = "IX_CI_COUPONNO_STATUS", columnList = "COUPON_NO, STATUS"),
                @Index(name = "IX_CI_USER", columnList = "USER_ID"),
                @Index(name = "IX_CI_USEDAT", columnList = "USED_AT")
        }
)
public class CouponIssue {

    @Id
    @Column(name = "ISSUE_ID")
    private String issueId;

    /** 
     * ✅ 1️⃣ 외래키 컬럼 직접 접근 (단순 조회용)
     */
    @Column(name = "COUPON_NO", nullable = false, length = 11, insertable = false, updatable = false)
    private String couponNo;

    @Column(name = "USER_ID", nullable = false, length = 100, insertable = false, updatable = false)
    private String userId;

    /** 
     * ✅ 2️⃣ 연관관계 매핑 (ORM 기반 접근용)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COUPON_NO", nullable = false)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private Member member;

    /** ✅ 공통 필드 */
    @Column(name = "STATUS", nullable = false, length = 20)
    private String status;

    @Column(name = "USED_AT")
    private LocalDateTime usedAt;
}
