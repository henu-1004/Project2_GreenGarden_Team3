package kr.co.greengarden.mapper.my;

import kr.co.greengarden.dto.my.MyInfoDTO;
import kr.co.greengarden.dto.my.MyInfoUpdateDTO;
import kr.co.greengarden.dto.my.MyInquiryDTO;
import kr.co.greengarden.dto.my.OrderDetailRowDTO;
import kr.co.greengarden.dto.my.OrderHistoryCriteria;
import kr.co.greengarden.dto.my.OrderSummaryDTO;
import kr.co.greengarden.dto.my.ProductReviewDTO;
import kr.co.greengarden.dto.my.SellerDetailDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyMapper {
    List<OrderSummaryDTO> selectRecentOrders(@Param("memId") String memId);

    List<OrderSummaryDTO> selectOrderHistory(OrderHistoryCriteria criteria);

    long countOrderHistory(OrderHistoryCriteria criteria);

    List<OrderDetailRowDTO> selectOrderDetail(@Param("memId") String memId,
                                              @Param("orderNo") String orderNo);

    SellerDetailDTO selectSellerDetail(@Param("sellerId") String sellerId);

    // ✅ 상태 업데이트 쿼리 4종 (모두 상품단위로)
    void updateConfirmYn(@Param("orderNo") String orderNo,
                         @Param("proId") Long proId,
                         @Param("yn") String yn);

    void updateReviewYn(@Param("orderNo") String orderNo,
                        @Param("proId") Long proId,
                        @Param("yn") String yn);

    void updateExchangeYn(@Param("orderNo") String orderNo,
                          @Param("proId") Long proId,
                          @Param("yn") String yn);

    void updateReturnYn(@Param("orderNo") String orderNo,
                        @Param("proId") Long proId,
                        @Param("yn") String yn);
    // ✅ 리뷰 등록
    void insertProductReview(ProductReviewDTO reviewDTO);

    // ✅ 내가 작성한 리뷰 조회
    List<ProductReviewDTO> getMyReviews(@Param("memId") String memId);

    long countMyReviews(@Param("memId") String memId);

    List<ProductReviewDTO> getMyReviewsPage(@Param("memId") String memId,
                                            @Param("offset") int offset,
                                            @Param("limit") int limit);

    // ✅ 내가 작성한 문의 조회
    List<MyInquiryDTO> getMyInquiries(@Param("memId") String memId);

    long countMyInquiries(@Param("memId") String memId);

    List<MyInquiryDTO> getMyInquiriesPage(@Param("memId") String memId,
                                          @Param("offset") int offset,
                                          @Param("limit") int limit);

    MyInfoDTO getMyInfo(@Param("memId") String memId);

    int updateMyGeneralInfo(MyInfoUpdateDTO dto);

    int updateMyMemberInfo(MyInfoUpdateDTO dto);
}
