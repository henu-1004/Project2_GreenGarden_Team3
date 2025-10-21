package kr.co.greengarden.dto.my;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationDTO {
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

    public static PaginationDTO empty(int pageSize) {
        return PaginationDTO.builder()
                .totalCount(0)
                .currentPage(1)
                .totalPages(0)
                .pageSize(pageSize)
                .startPage(0)
                .endPage(0)
                .hasPrev(false)
                .hasNext(false)
                .prevPage(1)
                .nextPage(1)
                .build();
    }
}
