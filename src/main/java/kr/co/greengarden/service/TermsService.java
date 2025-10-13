package kr.co.greengarden.service;

import kr.co.greengarden.entity.Terms;
import kr.co.greengarden.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TermsService {

    private final TermsRepository termsRepository;

    public Terms getTermsByMemberType(String memberType) {
        return termsRepository.findByMemberType(memberType);
    }

}
