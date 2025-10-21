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

    public long getTypeCount(String type) {
        return typeCounts.getOrDefault(type, 0L);
    }
}
