package kr.co.greengarden.dto;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeDTO {
    /**
     * 이름 : 박효빈
     * 날짜 : 2025/10/02
     * 내용 : 고객센터 - 공지사항 Entity  / DTO 생성
     * */


    private int noticeId;


    private String type; // 유형


    private String title; // 제목


    private String content; // 내용

    @Builder.Default
    private int views =0; // 조회수 (기본값 0)


    private LocalDateTime createdAt; // 작성일

}
