package kr.co.greengarden.mapper.my;

import kr.co.greengarden.dto.my.PointLedgerCriteria;
import kr.co.greengarden.dto.my.PointLedgerDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PointMapper {
    List<PointLedgerDTO> selectPointLedger(String memId);
    int selectTotalPoint(String memId);
    List<PointLedgerDTO> selectPointLedgerPage(PointLedgerCriteria criteria);
    long countPointLedger(PointLedgerCriteria criteria);
    Integer sumAmountBefore(PointLedgerCriteria criteria);
    Integer sumEarned(@Param("memId") String memId,
                      @Param("startDate") java.time.LocalDate startDate,
                      @Param("endDate") java.time.LocalDate endDate);
    Integer sumUsed(@Param("memId") String memId,
                    @Param("startDate") java.time.LocalDate startDate,
                    @Param("endDate") java.time.LocalDate endDate);
    Integer sumExpiringSoon(@Param("memId") String memId,
                            @Param("fromDate") java.time.LocalDate fromDate,
                            @Param("toDate") java.time.LocalDate toDate);
    Integer countExpiringSoon(@Param("memId") String memId,
                              @Param("fromDate") java.time.LocalDate fromDate,
                              @Param("toDate") java.time.LocalDate toDate);
}
