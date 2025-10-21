package kr.co.greengarden.dto.my;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MyInquiryDTO {
    private Integer inquiryId;
    private String type;
    private String title;
    private String status;
    private String answer;
    private LocalDateTime createdAt;

    public String getDisplayType() {
        return (type == null || type.isBlank()) ? "기타" : type;
    }

    public boolean isCompleted() {
        String normalizedStatus = status == null ? "" : status.trim();
        if (normalizedStatus.isEmpty()) {
            return answer != null && !answer.isBlank();
        }
        return normalizedStatus.contains("완료") || normalizedStatus.equalsIgnoreCase("answered");
    }

    public boolean isWaiting() {
        return !isCompleted();
    }

    public String getDisplayStatus() {
        return isCompleted() ? "답변완료" : "답변대기";
    }

    public String getStatusClass() {
        return isCompleted() ? "completed" : "waiting";
    }

    public String getNormalizedType() {
        return (type == null || type.isBlank()) ? "기타" : type;
    }
}
