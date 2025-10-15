package kr.co.greengarden.dto.admin;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CouponDTO {

    private String couponNo; // 쿠폰번호
    private String type; // 쿠폰종류
    private String name; // 쿠폰명
    private String benefit; // 혜택

    private LocalDateTime startDate; // 사용시작일
    private LocalDateTime endDate; // 사용종료일

    private String issuer; // 발급자
    private int issueCount; // 발급수
    private int usedCount; // 사용수
    private String staus; // 상태
    private LocalDateTime issuedAt; // 발급일
    private String note; // 기타(비고)
}
