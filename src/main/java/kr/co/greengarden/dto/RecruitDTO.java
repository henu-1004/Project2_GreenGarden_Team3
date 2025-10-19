package kr.co.greengarden.dto;

import lombok.*;
import java.time.LocalDateTime;

/*
 * 이름 : 박효빈
 * 날짜 : 2025/10/19
 * 내용 : 채용공고 DTO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RecruitDTO {

    private int recruitId; // 채용 ID
    private String department; // 채용부서
    private String career;     // 경력사항
    private String type;       // 채용형태
    private String title;      // 제목
    private String writer;     // 작성자
    private String status;     // 상태
    private String period;     // 모집기간
    private LocalDateTime createdAt; // 작성일
    private String note;       // 비고
}
