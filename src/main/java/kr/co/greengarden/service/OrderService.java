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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private final DeliveryRepository deliveryRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void orderRegister(OrderDTO orderDTO, OrderItemListWrapper orderItemList, MemberDetails memberDetails) {

        System.out.println("서비스주문 : " + orderDTO.toString());
        System.out.println("서비스주문상세 : " + orderItemList.toString());

        // ✅ 로그인한 회원 정보 가져오기
        if (memberDetails == null || memberDetails.getMember() == null) {
            throw new IllegalArgumentException("로그인 정보가 없습니다.");
        }

        MemberGeneral memberGeneral = memberGeneralRepository.findById(memberDetails.getMember().getMemId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 회원입니다."));

        Member member = memberGeneral.getMember();

        // ✅ 주문 번호 새로 생성
        String orderNo = UUID.randomUUID().toString().substring(0, 12);
        orderDTO.setOrderNo(orderNo);
        orderDTO.setStatus("결제 완료");

        // ✅ 주문 Entity 생성
        Order order = Order.builder()
                .orderNo(orderNo)
                .member(member)
                .totalPrice(orderDTO.getTotalPrice())
                .payMethod(orderDTO.getPayMethod())
                .status("결제 완료")
                .recName(orderDTO.getRecName())
                .recPhone(orderDTO.getRecPhone())
                .recZipCode(orderDTO.getRecZipCode())
                .recAddressBasic(orderDTO.getRecAddressBasic())
                .recAddressDetail(orderDTO.getRecAddressDetail())
                .orderedAt(LocalDateTime.now())
                .build();

        Order updatedOrder = orderRepository.save(order);

        // ✅ 상품 정보가 비어있는 경우 방지
        if (orderItemList == null || orderItemList.getItems() == null) {
            System.out.println("orderItemList가 비어있음");
            return;
        }

        // ✅ 상품 / 배송 처리
        for (OrderItemDTO item : orderItemList.getItems()) {
            Product product = productRepository.findById(item.getProId())
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

            OrderItem orderItem = OrderItem.builder()
                    .order(updatedOrder)
                    .product(product)
                    .quantity(item.getQuantity())
                    .price(item.getPrice())
                    .discountRate(item.getDiscountRate())
                    .build();

            orderItemRepository.save(orderItem);

            // ✅ 장바구니에 있을 경우만 삭제
            cartRepository.deleteByProduct_ProId(product.getProId());
        }

        // ✅ 배송 정보 생성
        Delivery delivery = Delivery.builder()
                .order(updatedOrder)
                .status("배송 대기")
                .createdAt(LocalDateTime.now())
                .build();

        deliveryRepository.save(delivery);
    }

    

    /*
    @Transactional
    public List<AdminOrderListDTO> getOrderList() {
        return orderRepository.findAllAdminOrderList();
    }
    */

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
            int price = (int) Math.ceil(c.getPrice() * (100 - c.getDiscountRate()) / 100.0);
            c.setOriginalPrice(price);

            count += c.getQuantity();
            originalTotalPrice += c.getPrice() * count;
            discountPrice += (c.getPrice() - price) * count;
            if (deliveryFee < c.getDeliveryFee()) {
                deliveryFee = c.getDeliveryFee();
            }
            totalPoint += c.getPoint() * count;
            totalPrice += price * count;
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


    public Page<DeliveryListDTO> findAllDeliveryBySearch(String searchType, String keyword, int page, int size){
        String st = (searchType == null) ? "" : searchType.trim();
        String kw = (keyword == null) ? "" : keyword.trim();
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAllDeliveryBySearch(st, kw, pageable);
    }

    // 배송창 입력
    public DeliveryInputDTO findDeliveryInfo(String orderNo) {
        return orderRepository.findDeliveryInfo(orderNo);
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

    public AdminIndexOrderInfoWrapperDTO getAdminIndexOrderInfoToday() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay(); // 오늘 00:00:00
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX); // 오늘 23:59:59
        List<AdminIndexOrderInfoDTO> orderInfo = orderRepository.findAdminIndexOrderInfo(startOfDay, endOfDay);

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

    public AdminIndexOrderInfoWrapperDTO getAdminIndexOrderInfoYesterday() {
        LocalDate yesterday = LocalDate.now().minusDays(1);  // 어제 날짜
        LocalDateTime startOfDay = yesterday.atStartOfDay(); // 어제 00:00:00
        LocalDateTime endOfDay = yesterday.atTime(LocalTime.MAX); // 어제 23:59:59.999999999
        List<AdminIndexOrderInfoDTO> orderInfo = orderRepository.findAdminIndexOrderInfo(startOfDay, endOfDay);

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

    public List<AdminOrderDetailListDTO> findOrderDetailList(String orderNo) {
        return orderRepository.findOrderDetailList(orderNo);
    }

    public List<AdminDeliveryDetailListDTO> findDeliveryDetailList(String invoiceNo) {
        return orderRepository.findDeliveryDetailList(invoiceNo);
    }
}
