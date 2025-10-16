package kr.co.greengarden.mapper.my;

import kr.co.greengarden.dto.my.OrderSummaryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyMapper {
    List<OrderSummaryDTO> selectRecentOrders(@Param("memId") String memId);
    // ✅ 상태 업데이트 쿼리 4종 추가
    void updateConfirmYn(@Param("orderNo") String orderNo, @Param("yn") String yn);

    void updateReviewYn(@Param("orderNo") String orderNo, @Param("yn") String yn);

    void updateExchangeYn(@Param("orderNo") String orderNo, @Param("yn") String yn);

    void updateReturnYn(@Param("orderNo") String orderNo, @Param("yn") String yn);
}
