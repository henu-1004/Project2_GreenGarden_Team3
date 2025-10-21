package kr.co.greengarden.service;

import kr.co.greengarden.dto.my.CouponSummaryDTO;
import kr.co.greengarden.dto.my.CouponTabPageDTO;
import kr.co.greengarden.dto.my.MyCouponDTO;
import kr.co.greengarden.dto.my.PagedResult;
import kr.co.greengarden.dto.my.PaginationDTO;
import kr.co.greengarden.entity.Coupon;
import kr.co.greengarden.entity.CouponIssue;
import kr.co.greengarden.repository.CouponIssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static kr.co.greengarden.util.PaginationUtils.buildPagination;

@Service
@RequiredArgsConstructor
public class MyCouponService {

    private final CouponIssueRepository couponIssueRepository;

    public CouponTabPageDTO getCouponTabPage(String memId, String tab, int page, int size) {
        CouponBuckets buckets = classifyCoupons(memId);
        String activeTab = normalizeTab(tab);

        List<MyCouponDTO> target = switch (activeTab) {
            case "used" -> buckets.used();
            case "expired" -> buckets.expired();
            default -> buckets.available();
        };

        PagedResult<MyCouponDTO> pageResult = paginate(target, page, size);

        return CouponTabPageDTO.builder()
                .activeTab(activeTab)
                .page(pageResult)
                .summary(buckets.summary())
                .build();
    }

    public CouponSummaryDTO getCouponSummary(String memId) {
        return classifyCoupons(memId).summary();
    }

    private CouponBuckets classifyCoupons(String memId) {
        List<CouponIssue> issues = couponIssueRepository.findAllByMemberWithCoupon(memId);
        Comparator<CouponIssue> comparator = Comparator
                .comparing((CouponIssue ci) -> getEndDate(ci.getCoupon()), Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(CouponIssue::getIssueId);
        issues.sort(comparator);

        List<MyCouponDTO> available = new ArrayList<>();
        List<MyCouponDTO> used = new ArrayList<>();
        List<MyCouponDTO> expired = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate soon = today.plusDays(7);

        int estimatedSavings = 0;
        int expiringSoonCount = 0;

        for (CouponIssue issue : issues) {
            Coupon coupon = issue.getCoupon();
            LocalDate endDate = getEndDate(coupon);
            boolean usedStatus = isUsed(issue);
            boolean expiredStatus = !usedStatus && endDate != null && endDate.isBefore(today);

            MyCouponDTO dto = toDto(issue);

            if (usedStatus) {
                used.add(dto);
            } else if (expiredStatus) {
                expired.add(dto);
            } else {
                available.add(dto);
                if (coupon != null && "AMOUNT".equalsIgnoreCase(coupon.getDiscountType())) {
                    estimatedSavings += Math.max(0, coupon.getDiscountValue());
                }
                if (endDate != null && !endDate.isBefore(today) && !endDate.isAfter(soon)) {
                    expiringSoonCount++;
                }
            }
        }

        CouponSummaryDTO summary = CouponSummaryDTO.builder()
                .availableCount(available.size())
                .usedCount(used.size())
                .expiredCount(expired.size())
                .estimatedSavings(estimatedSavings)
                .expiringSoonCount(expiringSoonCount)
                .expiringReferenceDate(soon)
                .build();

        return new CouponBuckets(available, used, expired, summary);
    }

    private boolean isUsed(CouponIssue issue) {
        if (issue.getUsedAt() != null) {
            return true;
        }
        String status = issue.getStatus();
        return status != null && status.equalsIgnoreCase("USED");
    }

    private LocalDate getEndDate(Coupon coupon) {
        if (coupon == null || coupon.getEndDate() == null) {
            return null;
        }
        return coupon.getEndDate().toLocalDate();
    }

    private LocalDate getStartDate(Coupon coupon) {
        if (coupon == null || coupon.getStartDate() == null) {
            return null;
        }
        return coupon.getStartDate().toLocalDate();
    }

    private MyCouponDTO toDto(CouponIssue issue) {
        Coupon coupon = issue.getCoupon();
        LocalDateTime issuedAt = coupon != null ? coupon.getIssuedAt() : null;
        return MyCouponDTO.builder()
                .issueId(issue.getIssueId())
                .couponNo(coupon != null ? coupon.getCouponNo() : null)
                .name(coupon != null ? coupon.getName() : null)
                .benefit(coupon != null ? coupon.getBenefit() : null)
                .issuer(coupon != null ? coupon.getIssuer() : null)
                .status(issue.getStatus())
                .discountType(coupon != null ? coupon.getDiscountType() : null)
                .discountValue(coupon != null ? coupon.getDiscountValue() : 0)
                .startDate(getStartDate(coupon))
                .endDate(getEndDate(coupon))
                .issuedAt(issuedAt)
                .usedAt(issue.getUsedAt())
                .note(coupon != null ? coupon.getNote() : null)
                .build();
    }

    private PagedResult<MyCouponDTO> paginate(List<MyCouponDTO> source, int page, int size) {
        int pageSize = size > 0 ? size : 6;
        long totalCount = source.size();
        if (totalCount == 0) {
            return PagedResult.empty(pageSize);
        }

        PaginationDTO pagination = buildPagination(page, pageSize, totalCount);
        int offset = (pagination.getCurrentPage() - 1) * pagination.getPageSize();
        int endIndex = Math.min(offset + pagination.getPageSize(), source.size());
        List<MyCouponDTO> slice = source.subList(offset, endIndex);
        return new PagedResult<>(List.copyOf(slice), pagination);
    }

    private String normalizeTab(String tab) {
        if (tab == null) {
            return "available";
        }
        return switch (tab.toLowerCase()) {
            case "used" -> "used";
            case "expired" -> "expired";
            default -> "available";
        };
    }

    private record CouponBuckets(List<MyCouponDTO> available,
                                 List<MyCouponDTO> used,
                                 List<MyCouponDTO> expired,
                                 CouponSummaryDTO summary) {
    }
}
