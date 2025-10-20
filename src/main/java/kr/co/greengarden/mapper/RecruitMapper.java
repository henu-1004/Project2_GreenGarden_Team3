package kr.co.greengarden.mapper;


import kr.co.greengarden.dto.PageRequestDTO;
import kr.co.greengarden.dto.RecruitDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RecruitMapper {

    List<RecruitDTO> selectRecruitList(@Param("pageRequestDTO")PageRequestDTO pageRequestDTO);

    int selectRecruitCount(@Param("pageRequestDTO")PageRequestDTO pageRequestDTO);

    RecruitDTO selectRecruit(@Param("recruitId")int recruitId);

    List<RecruitDTO> selectLatestRecruit(@Param("limit") int limit);

}
