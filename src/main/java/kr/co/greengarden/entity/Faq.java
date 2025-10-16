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
@Table(name = "TB_Faq")
public class Faq {

    /**
     * 이름 : 박효빈
     * 날짜 : 2025/10/13
     * 내용 : 고객센터 - Faq Entity  / DTO 생성
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FAQ_ID")
    private int faqId; // FAQID

    @Column(name = "CATEGORY1", length = 100)
    private String category1; // 1차 분류

    @Column(name = "CATEGORY2", length = 100)
    private String category2; // 2차 분류

    @Column(name = "TITLE", length = 255)
    private String title; // 제목

    @Builder.Default
    @Column(name = "VIEWS")
    private int views = 0; // 조회수 (int로 변경)

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt; // 작성일

    @Column(name = "CONTENT", length = 2000)
    private String content; // 내용

    // ✅ 수정용 메서드
    public void update(String category1, String category2, String title, String content) {
        this.category1 = category1;
        this.category2 = category2;
        this.title = title;
        this.content = content;
    }

}
