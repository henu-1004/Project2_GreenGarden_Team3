package kr.co.greengarden.util;

import kr.co.greengarden.dto.my.PaginationDTO;

public final class PaginationUtils {

    private static final int BLOCK_SIZE = 5;

    private PaginationUtils() {
    }

    public static PaginationDTO buildPagination(int requestedPage, int pageSize, long totalCount) {
        int size = pageSize > 0 ? pageSize : 10;
        if (totalCount <= 0) {
            return PaginationDTO.empty(size);
        }

        int totalPages = (int) Math.ceil(totalCount / (double) size);
        int currentPage = requestedPage < 1 ? 1 : Math.min(requestedPage, totalPages);

        int startPage = ((currentPage - 1) / BLOCK_SIZE) * BLOCK_SIZE + 1;
        int endPage = Math.min(startPage + BLOCK_SIZE - 1, totalPages);
        boolean hasPrev = currentPage > 1;
        boolean hasNext = currentPage < totalPages;
        int prevPage = hasPrev ? currentPage - 1 : 1;
        int nextPage = hasNext ? currentPage + 1 : totalPages;

        return PaginationDTO.builder()
                .totalCount(totalCount)
                .currentPage(currentPage)
                .totalPages(totalPages)
                .pageSize(size)
                .startPage(startPage)
                .endPage(endPage)
                .hasPrev(hasPrev)
                .hasNext(hasNext)
                .prevPage(prevPage)
                .nextPage(nextPage)
                .build();
    }
}
