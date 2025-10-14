package kr.co.greengarden.service;

import kr.co.greengarden.entity.Order;
import kr.co.greengarden.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyService {

    private final OrderRepository orderRepository;

    // 회원 아이디(memId)로 최근 5건 주문 조회
    public List<Order> getRecent5Orders(String memberId) {
        return orderRepository.findTop5ByMember_MemIdOrderByOrderedAtDesc(memberId);
    }

    // 회원의 모든 주문 조회 (나중에 /my/order 페이지용)
    public List<Order> findAllByMember_MemId(String memberId) {
        return orderRepository.findAllByMember_MemId(memberId);
    }

}
