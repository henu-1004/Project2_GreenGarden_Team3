package kr.co.greengarden.service;

import kr.co.greengarden.dto.FaqDTO;
import kr.co.greengarden.dto.PageRequestDTO;
import kr.co.greengarden.dto.PageResponseDTO;
import kr.co.greengarden.mapper.FaqMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 이름 : 박효빈
 * 날짜 : 2025/10/13
 * 내용 : 고객센터 - FAQ Service 구현
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class FaqService {

    private final FaqMapper faqMapper; // 의존성 주입 (만들어둔 Mapper)

    // 1. 목록 조회
    public PageResponseDTO<FaqDTO> getFaqList(PageRequestDTO pageRequestDTO) {
    List<FaqDTO> dtoList = faqMapper.selectFaqList(pageRequestDTO);
    log.info("FAQ 목록 조회 성공 ! 개수 : {} ",dtoList.size());

    // 2. 조건에 맞는 전체 faq 개수 조회 (추후 페이징 확장 대비)
     int total = faqMapper.selectFaqCount(pageRequestDTO);
     log.info("FAQ 전체 개수 : {} ",total);

     // 3. PageResponseDTO 객체 생성 및 반환

        return PageResponseDTO.<FaqDTO>builder()
                .dtoList(dtoList)
                .total(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    /**
     * Faq 상세보기 (view) 및 조회수 증가 처리
     * @param faqId -> 상세 보기할 FAQ 고유 번호 */

    @Transactional
    public FaqDTO getFaq(int faqId) {

        // 1. 조회수 증가 처리
        faqMapper.updateFaqViews(faqId);
        log.info("FAQ 조회수 + 1 (증가) : {} ", faqId);

        // 2. 상세 정보 조회 ( view )
        FaqDTO faqDTO = faqMapper.selectFaq(faqId);
        if (faqDTO == null) {
            return null;
        }

        // 3. 내용 줄바꿈처리
        // Notice와 마찬가지로 DB의 줄바꿈 문자를 HTML <br/> 태그로 변환
        String content = faqDTO.getContent();
        if (content != null) {
            content = content.replace("\r\n", "<br/>").replace("\n", "<br/>");
            faqDTO.setContent(content);
        }

        log.info("FAQ 상세 조회 완료 제목: {}", faqDTO.getTitle());
        return faqDTO;
    }

    }


