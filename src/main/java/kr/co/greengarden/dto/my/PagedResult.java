package kr.co.greengarden.dto.my;

import java.util.List;

public class PagedResult<T> {
    private final List<T> items;
    private final PaginationDTO pagination;

    public PagedResult(List<T> items, PaginationDTO pagination) {
        this.items = items;
        this.pagination = pagination;
    }

    public List<T> getItems() {
        return items;
    }

    public PaginationDTO getPagination() {
        return pagination;
    }

    public static <T> PagedResult<T> empty(int pageSize) {
        return new PagedResult<>(List.of(), PaginationDTO.empty(pageSize));
    }
}
