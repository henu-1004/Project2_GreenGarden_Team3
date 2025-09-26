package kr.co.greengarden.dto;

import kr.co.greengarden.entity.Grade;
import kr.co.greengarden.entity.Member;
import kr.co.greengarden.entity.Point;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 날짜 : 2025/09/25
 * 이름 : 한탁원
 * 내용 : PointDTO 초안 설정
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PointDTO {
    private int pointId;
    private int totalPoint;
    private String memId;

    public Point toEntity(Member member) {
        return Point.builder()
                .member(member)
                .pointId(pointId)
                .totalPoint(totalPoint)
                .build();
    }
}
