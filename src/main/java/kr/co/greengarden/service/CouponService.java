package kr.co.greengarden.service;
/*
*   날짜 : 2025/10/15
*   이름 : 이수연
*   내용 : 관리자 쿠폰 - 등록 구현
*/

import jakarta.transaction.Transactional;
import kr.co.greengarden.dto.admin.CouponDTO;
import kr.co.greengarden.entity.Coupon;
import kr.co.greengarden.entity.CouponIssue;
import kr.co.greengarden.repository.CouponIssueRepository;
import kr.co.greengarden.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor // Repository를 주입받기 위함
public class CouponService {

    private final CouponRepository couponRepository;
    private final ModelMapper modelMapper;
    private final CouponIssueRepository couponIssueRepository;

    // --- 쿠폰 종류별 식별 코드 (상수) ---
    // private static final String TYPE_INDIVIDUAL = "1";  // 개별상품할인
    // private static final String TYPE_ORDER = "2";       // 주문상품할인
    // private static final String TYPE_SHIPPING = "3";    // 배송비 무료

    // ----------------------------------------------------------------------
    //  11자리 쿠폰 번호 조합 로직 (타입 + 년월 + 시퀀스)
    // ----------------------------------------------------------------------
    private String generateCouponNo(String couponType) {

        // 1. 타입 코드 (1자리) couponType

        // 2. 년월 코드 (4자리: YYMM)
        String dateCode = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMM"));

        // 3. 순차 번호 (6자리: 오라클 SEQUENCE 사용)
        Long nextVal = couponRepository.getNextSequenceValue();
        // %06d: 숫자를 무조건 6자리로 만들고, 앞자리는 0으로 채운다.
        String uniqueCode = String.format("%06d", nextVal);

        // 4. 11자리 최종 조합
        return couponType + dateCode + uniqueCode;
    }

    // ----------------------------------------------------------------------
    // 1. 등록 (C: Create)
    // ----------------------------------------------------------------------
    @Transactional
    public CouponDTO register(CouponDTO dto) {

        // 1. 쿠폰 번호 조합 및 생성 (타입종류 + 년월 + 순차번호)
        String newCouponNo = generateCouponNo(dto.getType());

        // 2. DTO -> Entity 변환 (ModelMapper 사용)
        Coupon coupon = modelMapper.map(dto, Coupon.class);

        // 3. Entity에 서버 설정 값 주입
        coupon.setCouponNo(newCouponNo);
        coupon.setIssuedAt(LocalDateTime.now());
        coupon.setStatus("발급 중"); // 발급 완료 상태로 설정

        // 4. DB에 저장
        Coupon savedCoupon = couponRepository.save(coupon);

        // 5. 저장된 Entity -> DTO로 변환하여 반환
        return modelMapper.map(savedCoupon, CouponDTO.class);
    }

    public List<CouponDTO> getCouponList() {
        // 1. Repository 호출: DB Entity 목록을 가져온다.
        List<Coupon> couponEntities = couponRepository.findAllByOrderByIssuedAtDesc();

        // 2. [핵심] Entity 목록을 DTO 목록으로 변환하여 반환한다.
        // DTO에 있는 필드(couponNo, name, status 등)만 자동으로 복사된다.
        return couponEntities.stream()
                .map(coupon -> modelMapper.map(coupon, CouponDTO.class))
                .collect(Collectors.toList());
    }

    public List<CouponDTO> getCouponListWithCounts(){
        List<CouponDTO> list = getCouponList(); // 기존 메서드 그대로 호출

        for(CouponDTO dto : list){
            String couponNo = dto.getCouponNo();
            dto.setIssueCount((int) couponIssueRepository.countByCouponNoAndStatusIn(
            couponNo, java.util.List.of("ISSUED", "USED")));
            dto.setUsedCount((int) couponIssueRepository.countByCouponNoAndStatus(
                    couponNo, "USED"));
        }
        return list;
    }

    public long getIssueCount(String couponNo){
        return couponIssueRepository.countByCouponNoAndStatusIn(
                couponNo, java.util.List.of("ISSUED", "USED"));
    }

    public long getUsedCount(String couponNo){
        return couponIssueRepository.countByCouponNoAndStatus(couponNo, "USED");
    }

    public java.util.List<kr.co.greengarden.repository.IssuedRow> getIssuedListSimple(){
        return couponIssueRepository.findIssuedRows();
    }

    // 쿠폰 발급현황 관리(중단)
    @Transactional
        public void stopIssued(String issueId){
        CouponIssue ci = couponIssueRepository.findByIdForUpdate(issueId)
        .orElseThrow(() -> new IllegalArgumentException("발급행 없음"));
        if("USED".equals(ci.getStatus())) throw new IllegalStateException("이미 사용됨");
        if("CANCELLED".equals(ci.getStatus())) throw new IllegalStateException("이미 중단됨");
        ci.setStatus("CANCELLED");
    }






}
