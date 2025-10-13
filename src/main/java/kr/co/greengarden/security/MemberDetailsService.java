package kr.co.greengarden.security;

import kr.co.greengarden.entity.Member;
import kr.co.greengarden.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*
 * 날짜 : 2025/09/23
 * 이름 : 한탁원
 * 내용 : Spring Security 설정
 */
@RequiredArgsConstructor
@Service
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> optMember = memberRepository.findById(username);

        if(optMember.isPresent()) {
            Member member = optMember.get();

            MemberDetails memberDetails = MemberDetails.builder()
                                                       .member(member)
                                                       .build();

            return memberDetails;
        }
        return null;
    }
}
