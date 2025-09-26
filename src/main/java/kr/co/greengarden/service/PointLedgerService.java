package kr.co.greengarden.service;

import kr.co.greengarden.repository.PointLedgerRepository;
import kr.co.greengarden.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/*
 * 날짜 : 2025/09/25
 * 이름 : 한탁원
 * 내용 : PointLedgerService 작성
 */
@RequiredArgsConstructor
@Service
public class PointLedgerService {

    private final PointLedgerRepository pointLegderRepository;

}