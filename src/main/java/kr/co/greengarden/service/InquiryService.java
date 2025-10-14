package kr.co.greengarden.service;

import kr.co.greengarden.dto.InquiryDTO;
import kr.co.greengarden.dto.PageRequestDTO;
import kr.co.greengarden.dto.PageResponseDTO;
import kr.co.greengarden.entity.Inquiry;
import kr.co.greengarden.mapper.InquiryMapper;
import kr.co.greengarden.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class InquiryService {

    private final InquiryMapper inquiryMapper; // mybatis(List, Count, View)용
    private final InquiryRepository inquiryRepository; // 서비스 호출 (JPA)
    private final ModelMapper modelMapper;

    // 1. 문의 리스트 조회 및 페이징 처리 (Mybatis)
    public PageResponseDTO<InquiryDTO> getInquiryList(@Param("pageRequestDTO") PageRequestDTO pageRequestDTO) {
        log.info("로그 확인 : {}",pageRequestDTO);

        // 1. 목록 조회
        List<InquiryDTO> dtoList = inquiryMapper.selectInquiryList(pageRequestDTO);
        log.info("문의사항 목록 조회 : {}",dtoList.size());

        // 2. 조건 갯 수 조회
        int total = inquiryMapper.selectInquiryCount(pageRequestDTO);
        log.info("문의사항 전체 수 : {}",total);

        // 3. PageReponseDTO 객체 생성 및 반환

        return PageResponseDTO.<InquiryDTO>builder()
                .dtoList(dtoList)
                .total(total)
                .pageRequestDTO(pageRequestDTO)
                .build();

    }
    // 2. 문의 상세보기 (View) page
    public InquiryDTO getInquiry(int inquiryId) {
        InquiryDTO inquiryDTO = inquiryMapper.selectInquiry(inquiryId);

        if(inquiryDTO == null) {
            return null;
        }

        // 내용 줄바꿈 처리
        String content = inquiryDTO.getContent();
        if(content != null) {
            content = content.replace("\r\n","<br/>").replace("\n","<br/>");
            inquiryDTO.setContent(content);
        }
        log.info("문의하기 상제 조회 완료 제목 : {}",inquiryDTO.getTitle());
        return inquiryDTO;

    }

    // JPA 사용 (등록 수정 삭제) + ModelMapper 사용

    @Transactional
    public int registerInquiry(InquiryDTO inquiryDTO) {
        //DTO -> Entity 변환 (ModelMapper)
        Inquiry inquiry = modelMapper.map(inquiryDTO, Inquiry.class);

        Inquiry savedInquiry = inquiryRepository.save(inquiry);

        log.info("문의 등록 완료 ID:{}",savedInquiry.getInquiryId());
        return savedInquiry.getInquiryId();
    }

    @Transactional
    public void modifyInquiry(InquiryDTO inquiryDTO) {
        //ID가 존재하고, 수정 가능 한 상태인지 확인 로직 필요

        if (inquiryRepository.existsById(inquiryDTO.getInquiryId())) {
            //DTO -> Entity
            Inquiry inquiry = modelMapper.map(inquiryDTO, Inquiry.class);
            inquiryRepository.save(inquiry);
            log.info("문의 수정 완료 ID:{}",inquiry.getInquiryId());
        }else {
            log.warn("수정할 문의 찾을 수 없음:{}",inquiryDTO.getInquiryId());
            throw new RuntimeException("해당 문의을 찾을 수 없습니다 쩔쩔수 ㅠ:" + inquiryDTO.getInquiryId());
        }
    }

    // 5. 문의 삭제
    @Transactional
    public void removeInquiry(int inquiryId) {
        inquiryRepository.deleteById(inquiryId);
    }


}
