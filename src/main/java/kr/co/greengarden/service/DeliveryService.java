package kr.co.greengarden.service;

import jakarta.persistence.Column;
import kr.co.greengarden.dto.CartDTO;
import kr.co.greengarden.dto.CartListDTO;
import kr.co.greengarden.dto.admin.AdminIndexOrderInfoDTO;
import kr.co.greengarden.dto.admin.DeliveryDTO;
import kr.co.greengarden.entity.*;
import kr.co.greengarden.repository.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/*
 * 날짜 : 2025/10/21
 * 이름 : 한탁원
 * 내용 : DeliveryService 작성
 */
@RequiredArgsConstructor
@Service
public class DeliveryService {
    
    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public void register(DeliveryDTO deliveryDTO) {

        Order order = orderRepository.findById(deliveryDTO.getOrderNo()).get();
        Delivery delivery = deliveryRepository.findByOrder_OrderNo(order.getOrderNo());

        Delivery newDelivery = Delivery.builder()
                .deliveryId(delivery.getDeliveryId())  // 기존 ID 유지 (업데이트를 위해)
                .order(order)
                .invoiceNo(deliveryDTO.getInvoiceNo())
                .company(deliveryDTO.getCompany())
                .status("배송 중")
                .note(deliveryDTO.getNote())
                .createdAt(delivery.getCreatedAt())  // 기존 생성일시 유지
                .build();

        deliveryRepository.save(newDelivery);
    }

    public int getStatusCount(){
        List<Delivery> delivery = deliveryRepository.findAll();

        int statusCount = 0;

        for (Delivery del : delivery) {
            if(del.getStatus().equals("배송 대기")) {
                statusCount++;
            }
        }

        return statusCount;
    }

}
