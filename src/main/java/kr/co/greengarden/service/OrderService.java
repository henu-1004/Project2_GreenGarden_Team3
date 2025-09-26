package kr.co.greengarden.service;

import kr.co.greengarden.repository.OrderRepository;
import kr.co.greengarden.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/*
 * 날짜 : 2025/09/25
 * 이름 : 한탁원
 * 내용 : OrderService 작성
 */
@RequiredArgsConstructor
@Service
public class OrderService {
    
    private final OrderRepository orderRepository;

}
