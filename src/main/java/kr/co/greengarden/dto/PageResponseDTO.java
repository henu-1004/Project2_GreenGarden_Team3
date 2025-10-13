package kr.co.greengarden.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class PageResponseDTO<T> { // ⭐ 제네릭으로 어떤 DTO든 사용 가능

    private List<T> dtoList; // 목록 데이터 (NoticeDTO, ArticleDTO, UserDTO 등)

    private String type;      // 유형 필터
    private String cate;      // 카테고리
    private int pg;           // 현재 페이지
    private int size;         // 페이지 크기
    private int total;        // 전체 데이터 개수
    private int startNo;      // 시작 번호 (목록에서 역순 번호 표시용)
    private int start, end;   // 페이지 그룹 시작, 끝
    private boolean prev, next; // 이전, 다음 버튼 표시 여부

    private String searchType; // 검색 조건
    private String keyword;    // 검색어

    @Builder
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<T> dtoList, int total) {

        // 기본 정보 저장
        this.type = pageRequestDTO.getType();
        this.pg = pageRequestDTO.getPg();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        // 시작 번호 계산 (게시글 번호 역순 표시)
        this.startNo = total - ((pg - 1) * size);

        // 페이지 그룹 계산 (1~10, 11~20, 21~30...)
        this.end = (int) Math.ceil(this.pg / 10.0) * 10;
        this.start = this.end - 9;

        // 마지막 페이지 계산
        int last = (int) Math.ceil(total / (double) size);

        // end가 last를 초과하지 않도록 보정
        this.end = Math.min(last, this.end);

        // 이전, 다음 버튼 표시 여부
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;

        // 검색 조건 유지
        this.searchType = pageRequestDTO.getSearchType();
        this.keyword = pageRequestDTO.getKeyword();
    }
}