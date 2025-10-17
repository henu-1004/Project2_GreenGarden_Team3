package kr.co.greengarden.service;

import kr.co.greengarden.dto.my.PointLedgerDTO;
import kr.co.greengarden.entity.MemberGeneral;
import kr.co.greengarden.entity.Point;
import kr.co.greengarden.mapper.my.PointMapper;
import kr.co.greengarden.repository.CartRepository;
import kr.co.greengarden.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}
