package kr.co.greengarden.mapper;

import kr.co.greengarden.dto.FaqDTO;
import kr.co.greengarden.dto.PageRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FaqMapper {

    // 목록 조회 ( 일단 페이징은 js 처리 , 백-> 전체 목록 조회 )
    List<FaqDTO> selectFaqList(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);

    // 전체 갯수 ( total) - 혹시 모르니
    int selectFaqCount(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);

    // 상세보기 (View)
    FaqDTO selectFaq(@Param("faqId") int faqId);

    // 조회수 증가 ( 관리자 Page )
    void updateFaqViews(@Param("faqId") int faqId);
}
