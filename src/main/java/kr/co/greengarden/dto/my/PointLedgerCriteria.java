package kr.co.greengarden.dto.my;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PointLedgerCriteria {
    private String memId;
    private LocalDate startDate;
    private LocalDate endDate;
    /** "ALL", "EARN", "USE" */
    private String type;
    private int page;
    private int size;
    private int offset;
}
