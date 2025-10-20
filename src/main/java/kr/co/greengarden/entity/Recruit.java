package kr.co.greengarden.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
 * 이름 : 박효빈
 * 날짜 : 2025/10/19
 * 내용 : 채용공고 Entity
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_RECRUIT")
public class Recruit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECRUIT_ID")
    private int recruitId; // 채용 ID

    @Column(name = "DEPARTMENT", length = 100)
    private String department; // 채용부서

    @Column(name = "CAREER", length = 100)
    private String career; // 경력사항

    @Column(name = "TYPE", length = 50)
    private String type; // 채용형태 (정규직, 계약직 등)

    @Column(name = "TITLE", length = 255)
    private String title; // 제목

    @Column(name = "WRITER", length = 100)
    private String writer; // 작성자

    @Column(name = "STATUS", length = 50)
    private String status; // 상태 (모집중, 종료)

    @Column(name = "PERIOD", length = 100)
    private String period; // 모집기간

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt; // 작성일

    @Column(name = "NOTE", length = 500)
    private String note; // 비고

    // 도메인 메서드
    public void closeRecruit() {
        this.status = "종료";
    }
}
