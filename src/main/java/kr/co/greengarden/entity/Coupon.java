package kr.co.greengarden.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_COUPON")
public class Coupon {

    @Id
    @Column(name = "COUPON_NO", length = 11)
    private String couponNo; // 쿠폰번호

    private String type; // 쿠폰종류
    private String name; // 쿠폰명
    private String benefit; // 혜택

    @Column(name = "START_DATE")
    private LocalDateTime startDate; // 사용시작일

    @Column(name = "END_DATE")
    private LocalDateTime endDate; // 사용종료일

    private String issuer; // 발급자

    @Column(name = "ISSUE_COUNT")
    private int issueCount; // 발급수

    @Column(name = "USED_COUNT")
    private int usedCount; // 사용수

    private String status; // 상태

    @Column(name = "ISSUED_AT")
    private LocalDateTime issuedAt; // 발급일

    private String note; // 기타(비고)

}
