package kr.co.greengarden.dto.my;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointDTO {
    private Long pointId;     // PK
    private String memId;     // 회원 ID
    private int totalPoint;   // 총 보유 포인트
}
