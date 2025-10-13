package kr.co.greengarden.service;

import kr.co.greengarden.dto.MemberDTO;
import kr.co.greengarden.dto.NoticeDTO;
import kr.co.greengarden.entity.Member;
import kr.co.greengarden.entity.Notice;
import kr.co.greengarden.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 이름 : 박효빈
 * 날짜 : 2025/10/02
 * 내용 : 고객센터 - 공지사항 Service 생성
 * */
@Service
@Slf4j
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final ModelMapper modelMapper;

    public List<Notice> getNoticesAll(){
        return noticeRepository.findAll();
    }

    public Optional<Notice> getNoticeId(Integer noticeId){
        return noticeRepository.findById(noticeId);
    }

    public void save(NoticeDTO NoticeDTO) {
        Notice notice = modelMapper.map(NoticeDTO, Notice.class);
        noticeRepository.save(notice);

    }

    public void delete(Integer noticeId){
        noticeRepository.deleteById(noticeId);
    }
}
