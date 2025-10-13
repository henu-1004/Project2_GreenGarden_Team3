package kr.co.greengarden.service;

import kr.co.greengarden.repository.OrderItemRepository;
import kr.co.greengarden.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/*
 * 날짜 : 2025/09/25
 * 이름 : 한탁원
 * 내용 : OrderItemService 작성
 */
@RequiredArgsConstructor
@Service
public class OrderItemService {
    
    private final OrderItemRepository orderItemRepository;

}
