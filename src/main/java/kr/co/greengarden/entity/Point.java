package kr.co.greengarden.entity;

import jakarta.persistence.*;
import lombok.*;

/*
 * 날짜 : 2025/09/25
 * 이름 : 한탁원
 * 내용 : Point(포인트) 초안 설정
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_POINT")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pointId;

    @Column
    private int totalPoint;

    @OneToOne
    @JoinColumn(name="memId")
    private Member member;
}
