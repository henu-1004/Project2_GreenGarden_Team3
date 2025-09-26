package kr.co.greengarden.dto;

import kr.co.greengarden.entity.Grade;
import kr.co.greengarden.entity.Member;
import kr.co.greengarden.entity.MemberSeller;
import kr.co.greengarden.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 날짜 : 2025/09/25
 * 이름 : 한탁원
 * 내용 : GradeDTO 초안 설정
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GradeDTO {

    private String memId;
    private String grade;
    private int discountRate;

    public Grade toEntity(Member member) {
        return Grade.builder()
                    .member(member)
                    .grade(grade)
                    .discountRate(discountRate)
                    .build();
    }
}
