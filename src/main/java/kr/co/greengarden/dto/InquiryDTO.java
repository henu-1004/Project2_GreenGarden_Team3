package kr.co.greengarden.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * 이름 : 박효빈
 * 날짜 : 2025/10/13
 * 내용 : 고객센터 - Inquiry DTO 구현
 */
public class InquiryDTO {

    /** 문의 고유 번호 (PK) */
    private int inquiryId;

    /** 1차 분류*/
    private String category1;

    /** 2차 분류  */
    private String category2;

    /** 문의 제목 */
    private String title;

    /** 작성자 */
    private String writer;

    /** 문의 내용 */
    private String content;

    /** 관리자 답변 내용 */
    private String answer;

    /** 문의 처리 상태 */
    @Builder.Default
    private String status="답변대기";

    /** 문의 채널 (예: 이메일, 전화, 웹) */
    private String channel;

    /** 문의 작성일 */
    private LocalDateTime createdAt;

}