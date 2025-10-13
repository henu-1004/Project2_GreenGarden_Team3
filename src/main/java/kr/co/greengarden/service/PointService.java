package kr.co.greengarden.service;

import kr.co.greengarden.entity.MemberGeneral;
import kr.co.greengarden.entity.Point;
import kr.co.greengarden.repository.CartRepository;
import kr.co.greengarden.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Optional<Point> getPoint(int pointId){
        return pointRepository.findById(pointId);
    }


}
