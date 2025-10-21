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
//@SequenceGenerator(
        //name = "COUPON_ISSUE_SEQ_GEN",
        //sequenceName = "COUPON_ISSUE_SEQ",
        //allocationSize = 1
// )
public class CouponIssue {

    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COUPON_ISSUE_SEQ_GEN")
    @Column(name = "ISSUE_ID")
    private String issueId;

    @Column(name = "COUPON_NO", nullable = false, length = 11)
    private String couponNo;

    @Column(name = "USER_ID",  nullable = false, length = 100)
    private String userId;

    @Column(name = "STATUS", nullable = false, length = 20)
    private String status;

    @Column(name = "USED_AT")
    private LocalDateTime usedAt;
}
