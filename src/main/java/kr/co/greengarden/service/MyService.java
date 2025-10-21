package kr.co.greengarden.service;

import jakarta.transaction.Transactional;
import kr.co.greengarden.dto.my.ExchangeRequestDTO;
import kr.co.greengarden.dto.my.MyInfoDTO;
import kr.co.greengarden.dto.my.MyInfoUpdateDTO;
import kr.co.greengarden.dto.my.MyInquiryDTO;
import kr.co.greengarden.dto.my.MyInquirySummaryDTO;
import kr.co.greengarden.dto.my.OrderDetailDTO;
import kr.co.greengarden.dto.my.OrderDetailItemDTO;
import kr.co.greengarden.dto.my.OrderHistoryCriteria;
import kr.co.greengarden.dto.my.OrderHistoryPageDTO;
import kr.co.greengarden.dto.my.OrderItemStatusDTO;
import kr.co.greengarden.dto.my.OrderSummaryDTO;
import kr.co.greengarden.dto.my.PagedResult;
import kr.co.greengarden.dto.my.PaginationDTO;
import kr.co.greengarden.dto.my.ProductReviewDTO;
import kr.co.greengarden.dto.my.ReturnRequestDTO;
import kr.co.greengarden.dto.my.ReviewSummaryDTO;
import kr.co.greengarden.dto.my.SellerInfoDTO;
import kr.co.greengarden.entity.Order;
import kr.co.greengarden.handler.ImageHandler;
import kr.co.greengarden.mapper.my.MyMapper;
import kr.co.greengarden.repository.MemberRepository;
import kr.co.greengarden.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static kr.co.greengarden.util.PaginationUtils.buildPagination;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyService {

    private final OrderRepository orderRepository;
    private final MyMapper myMapper;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageHandler imageHandler;

    private static final int REVIEW_PAGE_SIZE = 5;
    private static final int QNA_PAGE_SIZE = 10;

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

        orders.forEach(this::applyOrderActionFlags);

        return orders;
    }

    public OrderHistoryPageDTO getOrderHistory(OrderHistoryCriteria criteria) {
        int requestedPage = Math.max(criteria.getPage(), 1);
        int pageSize = criteria.getSize() > 0 ? criteria.getSize() : 10;
        criteria.setPage(requestedPage);
        criteria.setSize(pageSize);

        long totalCount = myMapper.countOrderHistory(criteria);
        if (totalCount == 0) {
            return OrderHistoryPageDTO.builder()
                    .orders(Collections.emptyList())
                    .currentPage(requestedPage)
                    .pageSize(pageSize)
                    .totalCount(0)
                    .totalPages(0)
                    .startPage(0)
                    .endPage(0)
                    .hasPrev(false)
                    .hasNext(false)
                    .prevPage(0)
                    .nextPage(0)
                    .build();
        }

        int totalPages = (int) Math.ceil(totalCount / (double) pageSize);
        int currentPage = Math.min(requestedPage, totalPages);
        int startRow = (currentPage - 1) * pageSize + 1;
        int endRow = startRow + pageSize - 1;
        criteria.setPage(currentPage);
        criteria.setStartRow(startRow);
        criteria.setEndRow(endRow);

        List<OrderSummaryDTO> orders = myMapper.selectOrderHistory(criteria);
        orders.forEach(this::applyOrderActionFlags);

        int blockSize = 5;
        int currentBlock = (currentPage - 1) / blockSize;
        int startPage = currentBlock * blockSize + 1;
        int endPage = Math.min(startPage + blockSize - 1, totalPages);

        boolean hasPrev = currentPage > 1;
        boolean hasNext = currentPage < totalPages;
        int prevPage = hasPrev ? currentPage - 1 : 1;
        int nextPage = hasNext ? currentPage + 1 : totalPages;

        return OrderHistoryPageDTO.builder()
                .orders(orders)
                .currentPage(currentPage)
                .pageSize(pageSize)
                .totalCount(totalCount)
                .totalPages(totalPages)
                .startPage(startPage)
                .endPage(endPage)
                .hasPrev(hasPrev)
                .hasNext(hasNext)
                .prevPage(prevPage)
                .nextPage(nextPage)
                .build();
    }

    // 🔹 [JPA] 전체 주문 내역 (나중에 상세 페이지용)
    public List<Order> findAllByMember_MemId(String memberId) {
        return orderRepository.findAllByMember_MemId(memberId);
    }

    public Optional<OrderDetailDTO> getOrderDetail(String memId, String orderNo) {
        if (memId == null || memId.isBlank() || orderNo == null || orderNo.isBlank()) {
            return Optional.empty();
        }

        OrderDetailDTO detail = myMapper.selectOrderDetail(memId, orderNo);
        if (detail == null || detail.getItems() == null || detail.getItems().isEmpty()) {
            return Optional.empty();
        }

        int itemsTotal = detail.getItems().stream()
                .mapToInt(OrderDetailItemDTO::getLineTotal)
                .sum();
        int deliveryTotal = detail.getItems().stream()
                .mapToInt(OrderDetailItemDTO::getDeliveryFee)
                .sum();
        int discountTotal = detail.getItems().stream()
                .mapToInt(OrderDetailItemDTO::getDiscountAmount)
                .sum();
        int paymentTotal = itemsTotal + deliveryTotal - discountTotal;

        detail.setItemsTotal(itemsTotal);
        detail.setDeliveryTotal(deliveryTotal);
        detail.setDiscountTotal(Math.max(discountTotal, 0));
        detail.setPaymentTotal(paymentTotal);

        return Optional.of(detail);
    }

    public Optional<SellerInfoDTO> getSellerInfo(String sellerId) {
        if (sellerId == null || sellerId.isBlank()) {
            return Optional.empty();
        }

        SellerInfoDTO info = myMapper.selectSellerInfo(sellerId);
        if (info == null) {
            return Optional.empty();
        }

        if (info.getGradeName() == null || info.getGradeName().isBlank()) {
            info.setGradeName("일반판매자");
        }

        return Optional.of(info);
    }

    public void updateConfirmYn(String orderNo, Long proId, String yn) {
        myMapper.updateConfirmYn(orderNo, proId, yn);
    }

    public void updateReviewYn(String orderNo, Long proId, String yn) {
        myMapper.updateReviewYn(orderNo, proId, yn);
    }

    public void updateExchangeYn(String orderNo, Long proId, String yn) {
        myMapper.updateExchangeYn(orderNo, proId, yn);
    }

    public void updateReturnYn(String orderNo, Long proId, String yn) {
        myMapper.updateReturnYn(orderNo, proId, yn);
    }

    @Transactional
    public void submitExchange(String memId,
                               String orderNo,
                               Long proId,
                               String type,
                               String detail,
                               MultipartFile proof) {
        OrderItemStatusDTO status = requireOrderItemStatus(memId, orderNo, proId);

        if ("Y".equalsIgnoreCase(valueOrDefault(status.getCancelYn()))) {
            throw new IllegalStateException("이미 취소된 주문입니다.");
        }
        if ("Y".equalsIgnoreCase(valueOrDefault(status.getExchangeYn()))) {
            throw new IllegalStateException("이미 교환을 신청한 주문입니다.");
        }

        String normalizedType = normalize(type);
        if (normalizedType == null) {
            normalizedType = "기타";
        }
        String reasonDetail = (detail == null || detail.isBlank()) ? "사유 미입력" : detail.trim();

        String imgPath = null;
        if (proof != null && !proof.isEmpty()) {
            try {
                imgPath = imageHandler.saveImage(proof, "exchange");
            } catch (RuntimeException ex) {
                throw new IllegalStateException("교환 증빙 이미지를 저장하는 중 오류가 발생했습니다.", ex);
            }
        }

        ExchangeRequestDTO requestDTO = ExchangeRequestDTO.builder()
                .orderNo(orderNo)
                .orderItemId(status.getOrderItemId())
                .type(normalizedType)
                .detail(reasonDetail)
                .imgPath(imgPath)
                .build();

        myMapper.insertExchangeRequest(requestDTO);
        myMapper.updateExchangeYn(orderNo, proId, "Y");
        log.info("✅ 교환 신청 완료 orderNo={}, orderItemId={}", orderNo, status.getOrderItemId());
    }

    @Transactional
    public void submitReturnRequest(String memId,
                                    String orderNo,
                                    Long proId,
                                    String type,
                                    String detail,
                                    MultipartFile proof) {
        OrderItemStatusDTO status = requireOrderItemStatus(memId, orderNo, proId);

        if ("Y".equalsIgnoreCase(valueOrDefault(status.getCancelYn()))) {
            throw new IllegalStateException("이미 취소된 주문입니다.");
        }
        if ("Y".equalsIgnoreCase(valueOrDefault(status.getReturnYn()))) {
            throw new IllegalStateException("이미 반품을 신청한 주문입니다.");
        }

        String normalizedType = normalize(type);
        if (normalizedType == null) {
            normalizedType = "기타";
        }
        String reasonDetail = (detail == null || detail.isBlank()) ? "사유 미입력" : detail.trim();

        String imgPath = null;
        if (proof != null && !proof.isEmpty()) {
            try {
                imgPath = imageHandler.saveImage(proof, "return");
            } catch (RuntimeException ex) {
                throw new IllegalStateException("반품 증빙 이미지를 저장하는 중 오류가 발생했습니다.", ex);
            }
        }

        ReturnRequestDTO requestDTO = ReturnRequestDTO.builder()
                .orderNo(orderNo)
                .orderItemId(status.getOrderItemId())
                .type(normalizedType)
                .detail(reasonDetail)
                .imgPath(imgPath)
                .build();

        myMapper.insertReturnRequest(requestDTO);
        myMapper.updateReturnYn(orderNo, proId, "Y");
        log.info("✅ 반품 신청 완료 orderNo={}, orderItemId={}", orderNo, status.getOrderItemId());
    }

    @Transactional
    public void cancelOrderItem(String memId, String orderNo, Long proId) {
        OrderItemStatusDTO status = requireOrderItemStatus(memId, orderNo, proId);

        if ("Y".equalsIgnoreCase(valueOrDefault(status.getCancelYn()))) {
            throw new IllegalStateException("이미 취소된 주문입니다.");
        }

        if (!containsStatus(status.getDeliveryStatus(), "배송준비")) {
            throw new IllegalStateException("배송준비 상태에서만 취소할 수 있습니다.");
        }

        myMapper.updateCancelYn(orderNo, proId, "Y");
        log.info("✅ 주문 취소 완료 orderNo={}, orderItemId={}", orderNo, status.getOrderItemId());
    }

    public MyInfoDTO getMyInfo(String memId) {
        if (memId == null || memId.isBlank()) {
            return null;
        }
        return myMapper.getMyInfo(memId);
    }

    @Transactional
    public void updateMyInfo(MyInfoUpdateDTO dto) {
        if (dto == null || dto.getMemId() == null || dto.getMemId().isBlank()) {
            throw new IllegalArgumentException("회원 정보가 올바르지 않습니다.");
        }

        MyInfoUpdateDTO sanitized = MyInfoUpdateDTO.builder()
                .memId(dto.getMemId())
                .name(normalize(dto.getName()))
                .birth(dto.getBirth())
                .gender(normalize(dto.getGender()))
                .email(normalize(dto.getEmail()))
                .phone(normalize(dto.getPhone()))
                .zipCode(normalize(dto.getZipCode()))
                .addressBasic(normalize(dto.getAddressBasic()))
                .addressDetail(normalize(dto.getAddressDetail()))
                .build();

        myMapper.updateMyGeneralInfo(sanitized);
        myMapper.updateMyMemberInfo(sanitized);
    }

    /** ✅ 리뷰 등록 로직 (파일 업로드 포함) */
    @Transactional
    public void writeProductReview(ProductReviewDTO dto) {
        try {
            String img1 = imageHandler.saveImage(dto.getReviewFile1(), "review");
            String img2 = imageHandler.saveImage(dto.getReviewFile2(), "review");
            String img3 = imageHandler.saveImage(dto.getReviewFile3(), "review");

            if (img1 != null) dto.setImg1(img1);
            if (img2 != null) dto.setImg2(img2);
            if (img3 != null) dto.setImg3(img3);

            // DB insert
            myMapper.insertProductReview(dto);
            log.info("✅ 리뷰 등록 완료 (orderNo={}, memId={})", dto.getOrderNo(), dto.getMemId());

            // REVIEW_YN 업데이트
            myMapper.updateReviewYn(dto.getOrderNo(), dto.getProId(), "Y");

        } catch (RuntimeException e) {
            log.error("❌ 리뷰 파일 업로드 실패", e);
        }
    }

    /** ✅ 리뷰 목록 조회 */
    public List<ProductReviewDTO> getMyReviews(String memId) {
        return myMapper.getMyReviews(memId);
    }

    public PagedResult<ProductReviewDTO> getMyReviewsPage(String memId, int page, int size) {
        int pageSize = size > 0 ? size : REVIEW_PAGE_SIZE;
        long totalCount = myMapper.countMyReviews(memId);
        if (totalCount == 0) {
            return PagedResult.empty(pageSize);
        }

        PaginationDTO pagination = buildPagination(page, pageSize, totalCount);
        int offset = (pagination.getCurrentPage() - 1) * pagination.getPageSize();
        List<ProductReviewDTO> items = myMapper.getMyReviewsPage(memId, offset, pagination.getPageSize());
        return new PagedResult<>(items, pagination);
    }

    public ReviewSummaryDTO buildReviewSummary(List<ProductReviewDTO> reviews) {
        List<ProductReviewDTO> safeReviews = reviews == null ? Collections.emptyList() : reviews;

        double averageRating = safeReviews.stream()
                .map(ProductReviewDTO::getRating)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        long photoCount = safeReviews.stream()
                .filter(ProductReviewDTO::hasPhoto)
                .count();

        long answeredCount = safeReviews.stream()
                .filter(review -> review.getContent() != null && !review.getContent().isBlank())
                .count();

        return ReviewSummaryDTO.builder()
                .averageRating(averageRating)
                .totalCount(safeReviews.size())
                .photoReviewCount(photoCount)
                .answeredCount(answeredCount)
                .build();
    }

    public List<MyInquiryDTO> getMyInquiries(String memId) {
        return myMapper.getMyInquiries(memId);
    }

    public long countMyOrders(String memId) {
        if (memId == null || memId.isBlank()) {
            return 0;
        }
        return myMapper.countMyOrders(memId);
    }

    public long countMyInquiries(String memId) {
        if (memId == null || memId.isBlank()) {
            return 0;
        }
        return myMapper.countMyInquiries(memId);
    }

    public PagedResult<MyInquiryDTO> getMyInquiryPage(String memId, int page, int size) {
        int pageSize = size > 0 ? size : QNA_PAGE_SIZE;
        long totalCount = myMapper.countMyInquiries(memId);
        if (totalCount == 0) {
            return PagedResult.empty(pageSize);
        }

        PaginationDTO pagination = buildPagination(page, pageSize, totalCount);
        int offset = (pagination.getCurrentPage() - 1) * pagination.getPageSize();
        List<MyInquiryDTO> items = myMapper.getMyInquiriesPage(memId, offset, pagination.getPageSize());
        return new PagedResult<>(items, pagination);
    }

    public MyInquirySummaryDTO buildInquirySummary(List<MyInquiryDTO> inquiries) {
        List<MyInquiryDTO> safeInquiries = inquiries == null ? Collections.emptyList() : inquiries;

        long completedCount = safeInquiries.stream()
                .filter(MyInquiryDTO::isCompleted)
                .count();

        long waitingCount = safeInquiries.stream()
                .filter(MyInquiryDTO::isWaiting)
                .count();

        Map<String, Long> typeCounts = safeInquiries.stream()
                .collect(Collectors.groupingBy(MyInquiryDTO::getNormalizedType, Collectors.counting()));

        return MyInquirySummaryDTO.builder()
                .totalCount(safeInquiries.size())
                .completedCount(completedCount)
                .waitingCount(waitingCount)
                .typeCounts(typeCounts)
                .build();
    }

    public boolean verifyPassword(String memId, String rawPassword) {
        if (memId == null || memId.isBlank() || rawPassword == null || rawPassword.isBlank()) {
            return false;
        }

        return memberRepository.findById(memId)
                .map(member -> passwordEncoder.matches(rawPassword, member.getPassword()))
                .orElse(false);
    }

    @Transactional
    public void withdrawMember(String memId) {
        if (memId == null || memId.isBlank()) {
            throw new IllegalArgumentException("회원 정보가 올바르지 않습니다.");
        }

        String note = "회원 요청 탈퇴 (" + LocalDate.now() + ")";
        int updated = myMapper.updateMemberStatus(memId, "탈퇴", note);
        if (updated == 0) {
            throw new IllegalStateException("회원 정보를 찾을 수 없습니다.");
        }
    }

    private void applyOrderActionFlags(OrderSummaryDTO order) {
        String status = normalizeStatusValue(order.getStatus());
        String deliveryStatus = normalizeStatusValue(order.getDeliveryStatus());

        String confirmYn = valueOrDefault(order.getConfirmYn());
        String reviewYn = valueOrDefault(order.getReviewYn());
        String exchangeYn = valueOrDefault(order.getExchangeYn());
        String returnYn = valueOrDefault(order.getReturnYn());
        String cancelYn = valueOrDefault(order.getCancelYn());

        order.setCancelYn(cancelYn);
        order.setDeliveryStatus(deliveryStatus);

        boolean isCancelled = "Y".equalsIgnoreCase(cancelYn);
        boolean isDelivered = containsStatus(status, "배송완료")
                || containsStatus(status, "구매확정")
                || containsStatus(deliveryStatus, "배송완료");

        boolean readyToCancel = !isCancelled && containsStatus(deliveryStatus, "배송준비");

        order.setCanConfirm(!isCancelled && isDelivered && !"Y".equalsIgnoreCase(confirmYn));
        order.setCanReview(!isCancelled && "Y".equalsIgnoreCase(confirmYn) && !"Y".equalsIgnoreCase(reviewYn));
        order.setCanExchange(!isCancelled && !"Y".equalsIgnoreCase(exchangeYn) && !"Y".equalsIgnoreCase(confirmYn));
        order.setCanReturn(!isCancelled && !"Y".equalsIgnoreCase(returnYn) && !"Y".equalsIgnoreCase(confirmYn));
        order.setCanCancel(readyToCancel);

        if (isCancelled) {
            order.setStatus("취소완료");
            order.setCanExchange(false);
            order.setCanReturn(false);
            order.setCanReview(false);
            order.setCanConfirm(false);
            order.setCanCancel(false);
        }
    }

    private String valueOrDefault(String value) {
        if (value == null) {
            return "N";
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? "N" : trimmed;
    }

    private String normalizeStatusValue(String status) {
        return status == null ? "" : status.trim();
    }

    private boolean containsStatus(String status, String keyword) {
        if (status == null || keyword == null) {
            return false;
        }
        String normalizedStatus = status.replaceAll("\\s+", "").toLowerCase(Locale.ROOT);
        String normalizedKeyword = keyword.replaceAll("\\s+", "").toLowerCase(Locale.ROOT);
        return normalizedStatus.contains(normalizedKeyword);
    }

    private OrderItemStatusDTO requireOrderItemStatus(String memId, String orderNo, Long proId) {
        OrderItemStatusDTO status = myMapper.selectOrderItemStatus(memId, orderNo, proId);
        if (status == null || status.getOrderItemId() == null) {
            throw new IllegalArgumentException("주문 정보를 찾을 수 없습니다.");
        }
        return status;
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }


}
