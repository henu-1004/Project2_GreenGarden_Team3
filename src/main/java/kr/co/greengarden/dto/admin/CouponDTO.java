package kr.co.greengarden.dto.admin;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CouponDTO {

    // 서버가 채워줄 필드 (등록 요청 시에는 null)
    private String couponNo; // 쿠폰번호
    private String staus; // 상태
    private LocalDateTime issuedAt; // 발급일

    // 클라이언트가 입력할 필수 필드
    private String issuer; // 발급처
    private String type; // 쿠폰종류 : 개별상품할인-1, 주문상품할인-2, 배송비무료-3
    private String name; // 쿠폰명
    private String benefit; // 혜택명
    private int discountValue; // 할인값: 계산용 숫자
    private String discountType; // 할인 종류 구분자: AMOUNT(정액), PERCENT(정률), SHIPPING배송
    private LocalDateTime startDate; // 사용시작일
    private LocalDateTime endDate; // 사용종료일

    // 나머지 필드 (혜택, 발급자 등은 일단 optional로 간주)
    private int issueCount; // 발급수
    private int usedCount; // 사용수
    private String status; // 상태
    private String note; // 기타(비고)


}
