package kr.co.greengarden.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageRequestDTO {

    @Builder.Default
    private int no = 1;

    @Builder.Default
    private int pg = 1;
    @Builder.Default
    private int size = 10;

    //공지사항 유형 필터
    private String type;

    // 검색 조건
    private String searchType;
    private String keyword;

    // [⭐ QNA 필터링을 위해 추가 (500 에러 해결) ⭐]
    /** 관리자 QNA 1차 분류 필터 조건 */
    private String category1;

    /** 관리자 QNA 2차 분류 필터 조건 */
    private String category2;

    public int getOffset() {
        return (pg -1) * size;
    }

}
