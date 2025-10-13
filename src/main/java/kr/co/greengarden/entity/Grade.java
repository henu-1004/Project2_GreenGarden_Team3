package kr.co.greengarden.entity;


import jakarta.persistence.*;
import kr.co.greengarden.dto.GradeDTO;
import kr.co.greengarden.dto.MemberSellerDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/*
 * 날짜 : 2025/09/25
 * 이름 : 한탁원
 * 내용 : Grade(등급) 초안 설정
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_GRADE")
public class Grade {

    @Id
    private String memId;

    @Column
    private String grade;

    @Column
    private int discountRate;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name="memId")
    private Member member;

    public GradeDTO toDTO() {
        return GradeDTO.builder()
                       .memId(memId)
                       .grade(grade)
                       .discountRate(discountRate)
                       .build();
    }

}
