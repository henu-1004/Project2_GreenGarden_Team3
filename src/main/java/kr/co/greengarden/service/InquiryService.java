package kr.co.greengarden.service;

import kr.co.greengarden.dto.InquiryDTO;
import kr.co.greengarden.dto.NoticeDTO;
import kr.co.greengarden.dto.PageRequestDTO;
import kr.co.greengarden.dto.PageResponseDTO;
import kr.co.greengarden.entity.Inquiry;
import kr.co.greengarden.mapper.InquiryMapper;
import kr.co.greengarden.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
/**
 * 이름 : 박효빈
 * 날짜 : 2025/10/15
 * 내용 : 고객센터 - Inquiry Service 구현
 */
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

        // 1. 현재 로그인 사용자 ID 가져오기
        String writerId = getLoggedInUserId();

        // 2. Builder로 새 Entity 생성 (ID 제외 - 자동 생성되도록)
        Inquiry inquiry = Inquiry.builder()
                .category1(inquiryDTO.getCategory1())
                .category2(inquiryDTO.getCategory2())
                .title(inquiryDTO.getTitle())
                .content(inquiryDTO.getContent())
                .channel(inquiryDTO.getChannel())
                .writer(writerId)
                .createdAt(LocalDateTime.now())
                .status("대기")  // 초기 상태
                // inquiryId는 절대 설정하지 않음!
                .build();

        // 3. DB 저장
        Inquiry savedInquiry = inquiryRepository.save(inquiry);

        log.info("문의 등록 완료 ID:{}", savedInquiry.getInquiryId());
        return savedInquiry.getInquiryId().intValue();

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
    public void delete(int inquiryId) {
        inquiryRepository.deleteById(inquiryId);
    }

    //공지사항 5개 가져오는 서비스 ? 만들기
    public List<InquiryDTO> getLatestInquiry(int limit) {
        // 컨트롤러에서 넘어온 limit(5) 그대로 Mapper에 전달
        return inquiryMapper.selectLatestInquiry(limit);
    }


    // --- 유틸리티 메소드: 로그인 ID 가져오기 ---
    private String getLoggedInUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "익명사용자"; // 로그인하지 않은 경우
        }
        // Spring Security UserDetails 객체에서 사용자 ID 추출
        return auth.getName();
    }


    // 관리자 인덱스용
    public int getInquiryCount() {
        List<Inquiry> inquiryList = inquiryRepository.findAll();

        return inquiryList.size();
    }

}
