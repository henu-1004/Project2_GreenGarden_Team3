package kr.co.greengarden.mapper.my;

import kr.co.greengarden.dto.my.OrderSummaryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyMapper {
    List<OrderSummaryDTO> selectRecentOrders(@Param("memId") String memId);
}
