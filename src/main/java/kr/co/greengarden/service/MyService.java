package kr.co.greengarden.service;

import jakarta.transaction.Transactional;
import kr.co.greengarden.dto.my.OrderSummaryDTO;
import kr.co.greengarden.dto.my.ProductReviewDTO;
import kr.co.greengarden.entity.Order;
import kr.co.greengarden.mapper.my.MyMapper;
import kr.co.greengarden.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

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
    public void updateExchangeYn(String orderNo, String yn) {
        myMapper.updateExchangeYn(orderNo, yn);
    }
    public void updateReturnYn(String orderNo, String yn) {
        myMapper.updateReturnYn(orderNo, yn);
    }

    /** ✅ 리뷰 등록 로직 (파일 업로드 포함) */
    @Transactional
    public void writeProductReview(ProductReviewDTO dto) {
        try {
            // 파일 저장 처리
            if (dto.getReviewFile1() != null && !dto.getReviewFile1().isEmpty()) {
                dto.setImg1(saveFile(dto.getReviewFile1()));
            }
            if (dto.getReviewFile2() != null && !dto.getReviewFile2().isEmpty()) {
                dto.setImg2(saveFile(dto.getReviewFile2()));
            }
            if (dto.getReviewFile3() != null && !dto.getReviewFile3().isEmpty()) {
                dto.setImg3(saveFile(dto.getReviewFile3()));
            }

            // DB insert
            myMapper.insertProductReview(dto);
            log.info("✅ 리뷰 등록 완료 (orderNo={}, memId={})", dto.getOrderNo(), dto.getMemId());

            // REVIEW_YN 업데이트
            myMapper.updateReviewYn(dto.getOrderNo(), dto.getProId(), "Y");

        } catch (IOException e) {
            log.error("❌ 리뷰 파일 업로드 실패", e);
        }
    }

    /** ✅ 리뷰 목록 조회 */
    public List<ProductReviewDTO> getMyReviews(String memId) {
        return myMapper.getMyReviews(memId);
    }

    /** ✅ 파일 저장 로직 */
    private String saveFile(MultipartFile file) throws IOException {
        String uploadDir = "uploads/review/"; // 프로젝트 내 상대 경로
        File directory = new File(uploadDir);
        if (!directory.exists()) directory.mkdirs();

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File dest = new File(directory, fileName);
        file.transferTo(dest);

        // 브라우저 접근 경로로 반환
        return "/uploads/review/" + fileName;
    }


}
