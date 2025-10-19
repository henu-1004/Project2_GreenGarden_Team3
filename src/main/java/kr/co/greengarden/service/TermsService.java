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

    private boolean isBlank(String s){
        return s == null || s.trim().isEmpty();
    }

    public Terms getTermsByMemberType(String memberType) {
        return termsRepository.findByMemberType(memberType);
    }

    public String getFinanceTerms(){
        Terms t = termsRepository.findFirstByTermsFinIsNotNullOrderByTermsIdDesc();
        return (t == null || isBlank(t.getTermsFin())) ? null : t.getTermsFin();
    }

    public String getLocationTerms(){
        Terms t = termsRepository.findFirstByTermsLocIsNotNullOrderByTermsIdDesc();
        return (t == null || isBlank(t.getTermsLoc())) ? null : t.getTermsLoc();
    }

    public String getPrivacyTerms(){
        Terms t = termsRepository.findFirstByTermsPrivIsNotNullOrderByTermsIdDesc();
        return (t == null || isBlank(t.getTermsPriv())) ? null : t.getTermsPriv();
    }



}
