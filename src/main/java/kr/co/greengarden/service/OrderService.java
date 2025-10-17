package kr.co.greengarden.service;

import jakarta.transaction.Transactional;
import kr.co.greengarden.dto.*;
import kr.co.greengarden.dto.admin.*;
import kr.co.greengarden.entity.*;
import kr.co.greengarden.repository.*;
import kr.co.greengarden.security.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

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
    private final MemberGeneralRepository memberGeneralRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void orderRegister(OrderDTO orderDTO, OrderItemListWrapper orderItemList, MemberDetails memberDetails){

        // 1. 필요한 정보 가져오기
        Cart cart = cartRepository.findAll(PageRequest.of(0, 1))
                .getContent()
                .stream()
                .findFirst()
                .orElse(null);
        
        orderDTO.setOrderNo(cart.getOrderNo());
        orderDTO.setMember(cart.getMember());
        orderDTO.setStatus("결제 완료");

        Order order = modelMapper.map(orderDTO, Order.class);

        // 2. 주문 테이블 업데이트
        Order updatedOrder = orderRepository.save(order);

        // 3. 위에서 추출한 아이디를 통해 OrderItem 테이블에 데이터를 삽입
        for (OrderItemDTO item : orderItemList.getItems()) {
            int proId = item.getProId();
            int quantity = item.getQuantity();
            int price = item.getPrice();
            int discountRate = item.getDiscountRate();

            OrderItem orderItem = OrderItem.builder()
                    .order(updatedOrder)
                    .proId(proId)
                    .quantity(quantity)
                    .price(price)
                    .discountRate(discountRate)
                    .build();

            // (물품 주문 수 DB 추가) 주문했으니까 주문 수 상승
            productRepository.updateViewByProductId(proId);

            // 장바구니 삭제
            cartRepository.deleteByProduct_ProId(proId);

            orderItemRepository.save(orderItem);
        }

    }

    @Transactional
    public List<AdminOrderListDTO> getOrderList() {
        return orderRepository.findAllAdminOrderList();
    }
    
    public void getCompleteOrderList(String orderNo) {

        /*
            1. 주문 정보 조회
             - 주문 정보 - optOrder
             - 주문자 정보 - 이름, 연락처(로그인한 회원 정보) memberGeneral
            2. 주문 상품 목록 조회 - orderItemList
            3. 최종 결제 정보 계산/조회
            4. 주문자 정보 조회(member 서비스?)
        */

        // - 주문 정보 가져오기 (Order 테이블) findById
        Optional<Order> optOrder = orderRepository.findById(orderNo);
        // - 주문자 정보 가져오기 (MemberGeneral 테이블)


        if(optOrder.isPresent()){
            Order order = optOrder.get();
            // - OrderNo - OrderItem 테이블 정보 가져오기
            List<OrderItem> orderItemList = orderItemRepository.findAllByOrder_OrderNo(orderNo);

            System.out.println(order);
            System.out.println(orderItemList.toString());

            String memId = order.getMember().getMemId();

            Optional<MemberGeneral> optGeneral = memberGeneralRepository.findById(memId);

            if(optGeneral.isPresent()){
                MemberGeneral general = optGeneral.get();
                general.getName();
                general.getPhone();
            }
            Order orderCompleteList = Order.builder()
                    .orderNo(order.getOrderNo())
                    .payMethod(order.getPayMethod())
                    .totalPrice(order.getTotalPrice())
                    .recName(order.getRecName())
                    .recPhone(order.getRecPhone())
                    .recZipCode(order.getRecZipCode())
                    .recAddressBasic(order.getRecAddressBasic())
                    .recAddressDetail(order.getRecAddressDetail())
                    .orderItems(orderItemList)
                    .build();
        }

    }

    // 주문 정보
    public OrderInfoDTO getOrderInfo(List<CartListDTO> cartList) {

        OrderInfoDTO orderInfoDTO = new OrderInfoDTO();

        int count = 0;
        int originalTotalPrice = 0;
        int totalPrice = 0;
        int discountPrice = 0;
        int deliveryFee = 0;
        int totalPoint = 0;

        for (CartListDTO c : cartList) {
            int original = (int) Math.ceil(c.getPrice() / (1 - (c.getDiscountRate() / 100.0)));
            c.setOriginalPrice(original);

            count += c.getQuantity();
            originalTotalPrice += original * count;
            discountPrice += (c.getPrice() - original) * count;
            if (deliveryFee < c.getDeliveryFee()) {
                deliveryFee = c.getDeliveryFee();
            }
            totalPoint += c.getPoint() * count;
            totalPrice += c.getPrice() * count;
        }

        totalPrice += deliveryFee;

        orderInfoDTO.setCount(count);
        orderInfoDTO.setOriginalTotalPrice(originalTotalPrice);
        orderInfoDTO.setTotalPrice(totalPrice);
        orderInfoDTO.setDiscountPrice(discountPrice);
        orderInfoDTO.setDeliveryFee(deliveryFee);
        orderInfoDTO.setTotalPoint(totalPoint);

        return orderInfoDTO;
    }

    public Page<AdminOrderListDTO> findAllOrderBySearch(String searchType, String keyword, int page, int size){
        String st = (searchType == null) ? "" : searchType.trim();
        String kw = (keyword == null) ? "" : keyword.trim();
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAllOrderBySearch(st, kw, pageable);
    }

    public Page<DeliveryDTO> findAllDeliveryBySearch(String searchType, String keyword, int page, int size){
        String st = (searchType == null) ? "" : searchType.trim();
        String kw = (keyword == null) ? "" : keyword.trim();
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAllDeliveryBySearch(st, kw, pageable);
    }

    // 관리자 인덱스용
    public AdminIndexOrderInfoWrapperDTO getAdminIndexOrderInfo() {
        List<AdminIndexOrderInfoDTO> orderInfo = orderRepository.findAdminIndexOrderInfo();
        
        // 상태, 주문 총액, 주문 수
        int statusCount = 0;
        int totalPrice = 0;
        int count = orderInfo.size();

        for (AdminIndexOrderInfoDTO orderInfoDTO : orderInfo) {
            if(orderInfoDTO.getStatus().equals("결제 대기")) {
                statusCount++;
            }
            totalPrice += orderInfoDTO.getTotalPrice();
        }
        
        return new AdminIndexOrderInfoWrapperDTO(statusCount, totalPrice, count);
    }

}
