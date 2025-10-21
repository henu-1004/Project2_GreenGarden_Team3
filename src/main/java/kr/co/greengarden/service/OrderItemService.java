package kr.co.greengarden.service;

import kr.co.greengarden.entity.Delivery;
import kr.co.greengarden.entity.Order;
import kr.co.greengarden.entity.OrderItem;
import kr.co.greengarden.repository.OrderItemRepository;
import kr.co.greengarden.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * 날짜 : 2025/09/25
 * 이름 : 한탁원
 * 내용 : OrderItemService 작성
 */
@RequiredArgsConstructor
@Service
public class OrderItemService {
    
    private final OrderItemRepository orderItemRepository;

    public int getCancleCount(){
        List<OrderItem> orderItemList = orderItemRepository.findAll();

        int statusCount = 0;

        for (OrderItem oi : orderItemList) {
            if(oi.getCancelYN() != null && oi.getCancelYN().equals("Y")) {
                statusCount++;
            }
        }

        return statusCount;
    }

    public int getExchangeCount(){
        List<OrderItem> orderItemList = orderItemRepository.findAll();

        int statusCount = 0;

        for (OrderItem oi : orderItemList) {
            if(oi.getExchangeYN() != null && oi.getExchangeYN().equals("Y")) {
                statusCount++;
            }
        }

        return statusCount;
    }


    public int getReturnCount(){
        List<OrderItem> orderItemList = orderItemRepository.findAll();

        int statusCount = 0;

        for (OrderItem oi : orderItemList) {
            if(oi.getReturnYN() != null && oi.getReturnYN().equals("Y")) {
                statusCount++;
            }
        }

        return statusCount;
    }


}
