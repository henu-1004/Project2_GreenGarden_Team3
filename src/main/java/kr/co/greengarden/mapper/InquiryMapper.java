package kr.co.greengarden.mapper;

import kr.co.greengarden.dto.InquiryDTO;
import kr.co.greengarden.dto.NoticeDTO;
import kr.co.greengarden.dto.PageRequestDTO;
import kr.co.greengarden.entity.Inquiry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InquiryMapper {

    // 1. 문의리스트 조회
    List<InquiryDTO> selectInquiryList(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);

    // 2. 전체 항목 수 조회
    int selectInquiryCount(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO);

    // 3. 상세보기 (view page)
    InquiryDTO selectInquiry(@Param("inquiryId") int inquiryId);

    // 4. 1차 분류 목록 조회

    List<InquiryDTO> selectLatestInquiry(@Param("limit") int limit);


}
