package kr.co.greengarden.dto.my;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewSummaryDTO {
    private double averageRating;
    private long totalCount;
    private long photoReviewCount;
    private long answeredCount;

    public String getFormattedAverageRating() {
        if (Double.isNaN(averageRating) || Double.isInfinite(averageRating)) {
            return "0.0";
        }
        return String.format("%.1f", averageRating);
    }
}
