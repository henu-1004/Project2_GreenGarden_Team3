package kr.co.greengarden.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_INQUIRY")
public class Inquiry {
    @Id // 기본 키 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB에서 ID 자동 생성 시
    @Column(name = "INQUIRY_ID")
    private int inquiryId;    // 문의 ID

    @Column(name = "CATEGORY1", length = 100)
    private String category1;  // 1차 분류

    @Column(name = "CATEGORY2", length = 100)
    private String category2;  // 2차 분류

    @Column(name = "TITLE", length = 255)
    private String title;      // 제목

    @Column(name = "WRITER", length = 100)
    private String writer;     // 작성자

    @Column(name = "CREATED_AT")
    // DB의 DATE 타입은 보통 Java의 java.util.Date 또는 java.time.LocalDateTime으로 매핑합니다.
    private LocalDateTime createdAt; // 작성일

    @Column(name = "STATUS", length = 50)
    private String status;     // 상태 (ex: 대기, 완료)

    @Column(name = "CONTENT", length = 2000)
    private String content;    // 내용

    @Column(name = "ANSWER", length = 2000)
    private String answer;     // 답변

    @Column(name = "CHANNEL", length = 100)
    private String channel;    // 문의채널
}
