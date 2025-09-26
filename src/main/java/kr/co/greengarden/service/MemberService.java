package kr.co.greengarden.service;

import kr.co.greengarden.dto.MemberDTO;
import kr.co.greengarden.entity.Member;
import kr.co.greengarden.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    
}
