package kr.co.greengarden.service;

import kr.co.greengarden.dto.my.OrderSummaryDTO;
import kr.co.greengarden.entity.Order;
import kr.co.greengarden.mapper.my.MyMapper;
import kr.co.greengarden.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyService {

    private final OrderRepository orderRepository;
    private final MyMapper myMapper;

    // 🔹 [JPA] 단순 엔티티 기반 조회
    public List<Order> getRecent5Orders(String memberId) {
        return orderRepository.findTop5ByMember_MemIdOrderByOrderedAtDesc(memberId);
    }

    // 🔹 [MyBatis] 조인된 데이터(상품명, 이미지 등) 포함 조회
//    public List<OrderSummaryDTO> getRecentOrderSummary(String memId) {
//        return myMapper.selectRecentOrders(memId);
//
//    }
    public List<OrderSummaryDTO> getRecentOrderSummary(String memId) {
        List<OrderSummaryDTO> orders = myMapper.selectRecentOrders(memId);

        log.info("🧩 최근 주문 {}건 불러옴 (memId={})", orders.size(), memId);
        for (OrderSummaryDTO o : orders) {
            log.debug("→ orderNo={}, orderedAt={}, status={}",
                    o.getOrderNo(), o.getOrderedAt(), o.getStatus());
        }

        return orders;
    }

    // 🔹 [JPA] 전체 주문 내역 (나중에 상세 페이지용)
    public List<Order> findAllByMember_MemId(String memberId) {
        return orderRepository.findAllByMember_MemId(memberId);
    }

    public void updateConfirmYn(String orderNo, String yn) {
        myMapper.updateConfirmYn(orderNo, yn);
    }
    public void updateReviewYn(String orderNo, String yn) {
        myMapper.updateReviewYn(orderNo, yn);
    }
    public void updateExchangeYn(String orderNo, String yn) {
        myMapper.updateExchangeYn(orderNo, yn);
    }
    public void updateReturnYn(String orderNo, String yn) {
        myMapper.updateReturnYn(orderNo, yn);
    }


}
