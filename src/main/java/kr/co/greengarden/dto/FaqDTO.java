package kr.co.greengarden.dto;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FaqDTO {

    private int faqId; // FAQ번호 (int로 변경)

    private String category1; // 1차 분류

    private String category2; // 2차 분류

    private String title; // 제목

    @Builder.Default
    private int views = 0; // 조회수 (int로 변경)

    private LocalDateTime createdAt; // 작성일

    private String content; // 내용
}