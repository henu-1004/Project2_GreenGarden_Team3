package kr.co.greengarden.dto.my;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyInquirySummaryDTO {

    private long totalCount;
    private long waitingCount;
    private long completedCount;

    @Builder.Default
    private Map<String, Long> typeCounts = new HashMap<>();

    /** ✅ Thymeleaf EL이 확실히 인식하도록 public + Long 타입으로 */
    public Long getTypeCount(String type) {
        return typeCounts.getOrDefault(type, 0L);
    }
}
