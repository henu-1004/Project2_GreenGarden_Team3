package kr.co.greengarden.mapper.my;

import kr.co.greengarden.dto.my.PointLedgerDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface PointMapper {
    List<PointLedgerDTO> selectPointLedger(String memId);
    int selectTotalPoint(String memId);
}
