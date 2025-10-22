package kr.co.greengarden.service;

import kr.co.greengarden.dto.MemberDTO;
import kr.co.greengarden.entity.Member;
import kr.co.greengarden.repository.MemberGeneralRepository;
import kr.co.greengarden.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/*
 * 날짜 : 2025/09/23
 * 이름 : 한탁원
 * 내용 : MemberService 작성
 */
@RequiredArgsConstructor
@Service
public class MemberService {
    
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final MemberGeneralRepository memberGeneralRepository;

    public List<Member> getUsers(){
        return memberRepository.findAll();
    }

    public Optional<Member> getUser(String memId){
        return memberRepository.findById(memId);
    }

    public void modify(MemberDTO memberDTO) {
        memberRepository.save(memberDTO.toEntity());
    }

    public void delete(String memId){
        memberRepository.deleteById(memId);
    }

    public int countMember(String type, String value){
        int count = 0;

        if(type.equals("memId")){
            count = memberRepository.countByMemId(value);
        }else if(type.equals("email")){
            count = memberGeneralRepository.countByEmail(value);

            if(count == 0){
                // 인증코드 이메일 전송
                emailService.sendCode(value);
            }

        }else if(type.equals("phone")){
            count = memberGeneralRepository.countByPhone(value);
        }
        return count;
    }

    // 관리자 인덱스용
    public int getMemberCount(){
        List<Member> mer = memberRepository.findAll();
        return mer.size();
    }

    public int getMemberTodayCount(){
        LocalDate today = LocalDate.now();  // 오늘 날짜
        LocalDateTime startOfDay = today.atStartOfDay(); // 오늘 00:00:00
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX); // 오늘 23:59:59.999999999

        List<Member> mer = memberRepository.findMembersJoinedDate(startOfDay, endOfDay);
        return mer.size();
    }

    public int getMemberYesterdayCount(){
        LocalDate yesterday = LocalDate.now().minusDays(1);  // 어제 날짜
        LocalDateTime startOfDay = yesterday.atStartOfDay(); // 어제 00:00:00
        LocalDateTime endOfDay = yesterday.atTime(LocalTime.MAX);

        List<Member> mer = memberRepository.findMembersJoinedDate(startOfDay, endOfDay);
        return mer.size();
    }
}

