package kr.co.greengarden.dto.my;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PointLedgerPageDTO {
    private List<PointLedgerDTO> items;
    private long totalCount;
    private int currentPage;
    private int totalPages;
    private int pageSize;
    private int startPage;
    private int endPage;
    private boolean hasPrev;
    private boolean hasNext;
    private int prevPage;
    private int nextPage;
}
