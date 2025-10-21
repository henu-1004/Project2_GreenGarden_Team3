package kr.co.greengarden.dto.my;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointLedgerDTO {
    private Long legId;         // PK
    private Long pointId;       // FK (TB_POINT 참조)
    private int amount;         // 증감 금액
    private LocalDate earnedAt; // 발생일
    private LocalDate expiredAt;// 만료일
    private String type;        // 적립/사용
    private String note;        // 상세 내용
    private String orderNo;     // 관련 주문번호
    private Integer balanceAfter; // 내역 적용 후 잔여 포인트
}
