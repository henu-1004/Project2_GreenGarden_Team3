package kr.co.greengarden.dto.my;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryCriteria {

    private String memId;                // 로그인 회원 ID
    private LocalDate startDate;         // 검색 시작일 (YYYY-MM-DD)
    private LocalDate endDate;           // 검색 종료일 (YYYY-MM-DD)
    private LocalDateTime startDateTime; // 검색 시작일 00:00:00
    private LocalDateTime endDateTime;   // 검색 종료일 23:59:59
    private int page;                    // 요청 페이지 (1부터 시작)
    private int size;                    // 페이지당 출력 건수
    private int startRow;                // Oracle 페이징 시작 ROWNUM
    private int endRow;                  // Oracle 페이징 종료 ROWNUM
}
