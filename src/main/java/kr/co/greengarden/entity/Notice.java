package kr.co.greengarden.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_Notice")
public class Notice {

    /**
     * 이름 : 박효빈
     * 날짜 : 2025/10/02
     * 내용 : 고객센터 - 공지사항 Entity  / DTO 생성
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTICE_ID")
    private int noticeId;

    @Column(name = "TYPE")
    private String type; // 유형

    @Column(name = "TITLE")
    private String title; // 제목

    @Column(name = "CONTENT")
    private String content; // 내용

    @Column(name = "VIEWS")
    private int views; // 조회수 (기본값 0)

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt; // 작성일

}
