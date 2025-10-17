package kr.co.greengarden.service;

import jakarta.transaction.Transactional;
import kr.co.greengarden.dto.OrderDTO;
import kr.co.greengarden.dto.OrderItemDTO;
import kr.co.greengarden.dto.OrderItemListWrapper;
import kr.co.greengarden.dto.admin.AdminOrderListDTO;
import kr.co.greengarden.dto.admin.CouponDTO;
import kr.co.greengarden.entity.Coupon;
import kr.co.greengarden.entity.Member;
import kr.co.greengarden.entity.Order;
import kr.co.greengarden.entity.OrderItem;
import kr.co.greengarden.repository.MemberRepository;
import kr.co.greengarden.repository.OrderItemRepository;
import kr.co.greengarden.repository.OrderRepository;
import kr.co.greengarden.repository.PointRepository;
import kr.co.greengarden.security.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 * 날짜 : 2025/09/25
 * 이름 : 한탁원
 * 내용 : OrderService 작성
 */
@RequiredArgsConstructor
@Service
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void orderRegister(OrderDTO orderDTO, OrderItemListWrapper orderItemList, MemberDetails memberDetails){

        // 0. orderNo 만드는 로직, 사용자
        String memId = memberDetails.getUsername();

        String generatedOrderNo = "A" + System.currentTimeMillis(); // 임시 생성 <- 로직 만들어 변경
        orderDTO.setOrderNo(generatedOrderNo);
        orderDTO.setStatus("결제 대기");

        Optional<Member> optMember = memberRepository.findById(memId);

        if(optMember.isPresent()){
            Member member = optMember.get();
            orderDTO.setMember(member);
        }
        Order order = modelMapper.map(orderDTO, Order.class);

        // 1. 주문 테이블에 삽입
        Order savedOrder = orderRepository.save(order);

        // 3. 위에서 추출한 아이디를 통해 OrderItem 테이블에 데이터를 삽입
        for (OrderItemDTO item : orderItemList.getItems()) {
            int proId = item.getProId();
            int quantity = item.getQuantity();
            int price = item.getPrice();
            int discountRate = item.getDiscountRate();        // 처리 로직

            OrderItem orderItem = OrderItem.builder()
                    .order(savedOrder)
                    .proId(proId)
                    .quantity(quantity)
                    .price(price)
                    .discountRate(discountRate)
                    .build();

            orderItemRepository.save(orderItem);

            // 장바구니 삭제
            
            // (물품 주문 수 DB 추가) 주문했으니까 주문 수 상승

        }
    }

    @Transactional
    public List<AdminOrderListDTO> getOrderList() {
        return orderRepository.findAllAdminOrderList();
    }

    public void getCompleteOrderList(String orderNo) {

        /*
            1. 주문 정보 조회
            2. 주문 상품 목록 조회
            3. 최종 결제 정보 계산/조회
            4. 주문자 정보 조회(member 서비스?)
        */



        // - 주문 정보 가져오기 (Order 테이블) findById
        Optional<Order> optOrder = orderRepository.findById(orderNo);

        if(optOrder.isPresent()){
            Order order = optOrder.get();
            // - OrderNo - OrderItem 테이블 정보 가져오기
            List<OrderItem> orderItemList = orderItemRepository.findAllByOrder_OrderNo(orderNo);

            System.out.println(order);
            System.out.println(orderItemList.toString());
        }

    }

}
