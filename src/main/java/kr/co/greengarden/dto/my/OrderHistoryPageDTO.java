package kr.co.greengarden.dto.my;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryPageDTO {

    @Builder.Default
    private List<OrderSummaryDTO> orders = Collections.emptyList();

    private int currentPage;   // 현재 페이지
    private int pageSize;      // 페이지당 출력 건수
    private long totalCount;   // 전체 건수
    private int totalPages;    // 전체 페이지 수

    private int startPage;     // 페이징 바 시작 번호
    private int endPage;       // 페이징 바 종료 번호
    private boolean hasPrev;   // 이전 블록 존재 여부
    private boolean hasNext;   // 다음 블록 존재 여부
    private int prevPage;      // 이전 이동 페이지
    private int nextPage;      // 다음 이동 페이지
}
