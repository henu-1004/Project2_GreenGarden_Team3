package kr.co.greengarden.service;

import kr.co.greengarden.dto.my.PointLedgerCriteria;
import kr.co.greengarden.dto.my.PointLedgerDTO;
import kr.co.greengarden.dto.my.PointLedgerPageDTO;
import kr.co.greengarden.dto.my.PointSummaryDTO;
import kr.co.greengarden.entity.Point;
import kr.co.greengarden.mapper.my.PointMapper;
import kr.co.greengarden.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/*
 * 날짜 : 2025/09/25
 * 이름 : 한탁원
 * 내용 : PointService 작성
 */
@RequiredArgsConstructor
@Service
public class PointService {
    
    private final PointRepository pointRepository;
    private final PointMapper pointMapper;

    public Optional<Point> getPoint(int pointId){
        return pointRepository.findById(pointId);
    }
    /** ✅ 회원별 총 포인트 */
    public int getTotalPoint(String memId) {
        return pointMapper.selectTotalPoint(memId);
    }

    /** ✅ 회원별 포인트 내역 전체 */
    public List<PointLedgerDTO> getPointLedger(String memId) {
        return pointMapper.selectPointLedger(memId);
    }

    /** ✅ 최근 5건만 출력용 */
    public List<PointLedgerDTO> getRecentLedger(String memId) {
        return pointMapper.selectPointLedger(memId).stream().limit(5).toList();
    }

    public PointLedgerPageDTO getPointLedgerPage(PointLedgerCriteria criteria) {
        int page = Math.max(criteria.getPage(), 1);
        int size = criteria.getSize() > 0 ? criteria.getSize() : 10;
        int offset = (page - 1) * size;

        criteria.setPage(page);
        criteria.setSize(size);
        criteria.setOffset(offset);

        long totalCount = pointMapper.countPointLedger(criteria);
        if (totalCount == 0) {
            return PointLedgerPageDTO.builder()
                    .items(List.of())
                    .totalCount(0)
                    .currentPage(page)
                    .totalPages(0)
                    .pageSize(size)
                    .startPage(0)
                    .endPage(0)
                    .hasPrev(false)
                    .hasNext(false)
                    .prevPage(0)
                    .nextPage(0)
                    .build();
        }

        int totalPages = (int) Math.ceil(totalCount / (double) size);
        int currentPage = Math.min(page, totalPages);
        if (currentPage != page) {
            offset = (currentPage - 1) * size;
            criteria.setPage(currentPage);
            criteria.setOffset(offset);
        }

        List<PointLedgerDTO> items = pointMapper.selectPointLedgerPage(criteria);

        int totalPoint = getTotalPoint(criteria.getMemId());
        int priorAmount = safeInt(pointMapper.sumAmountBefore(criteria));
        int runningBalance = totalPoint - priorAmount;

        for (PointLedgerDTO item : items) {
            item.setBalanceAfter(runningBalance);
            runningBalance -= item.getAmount();
        }

        int blockSize = 5;
        int startPage = ((currentPage - 1) / blockSize) * blockSize + 1;
        int endPage = Math.min(startPage + blockSize - 1, totalPages);
        boolean hasPrev = currentPage > 1;
        boolean hasNext = currentPage < totalPages;
        int prevPage = hasPrev ? currentPage - 1 : 1;
        int nextPage = hasNext ? currentPage + 1 : totalPages;

        return PointLedgerPageDTO.builder()
                .items(items)
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

    public PointSummaryDTO getPointSummary(String memId, LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();
        LocalDate expiringUntil = today.plusDays(30);

        int totalPoint = getTotalPoint(memId);
        int earned = safeInt(pointMapper.sumEarned(memId, startDate, endDate));
        int used = Math.abs(safeInt(pointMapper.sumUsed(memId, startDate, endDate)));
        int expiringAmount = safeInt(pointMapper.sumExpiringSoon(memId, today, expiringUntil));
        int expiringCount = safeInt(pointMapper.countExpiringSoon(memId, today, expiringUntil));

        return PointSummaryDTO.builder()
                .totalPoint(totalPoint)
                .earnedInPeriod(earned)
                .usedInPeriod(used)
                .expiringSoonAmount(expiringAmount)
                .expiringSoonCount(expiringCount)
                .expiringReferenceDate(expiringUntil)
                .build();
    }

    private int safeInt(Integer value) {
        return value == null ? 0 : value;
    }

}
