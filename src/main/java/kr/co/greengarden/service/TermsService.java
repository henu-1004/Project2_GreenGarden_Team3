package kr.co.greengarden.service;

import kr.co.greengarden.dto.admin.TermsModifyDTO;
import kr.co.greengarden.entity.Terms;
import kr.co.greengarden.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class TermsService {

    private final TermsRepository termsRepository;

    private static final Set<String> ALLOWED =
            Set.of("BUYER", "SELLER", "FINANCE", "LOCATION", "PRIVACY");

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

    // 약관 수정
    @Transactional
    public void update(TermsModifyDTO dto){
        final String type = dto.getType();
        final String content = dto.getContent() == null ? "" : dto.getContent();


        switch (type) {
            case "BUYER" -> termsRepository.updateTermsUseByMemberType("USER", content);
            case "SELLER" -> termsRepository.updateTermsUseByMemberType("SELLER", content);
            case "FINANCE" -> termsRepository.updateTermsFinForAll(content);
            case "LOCATION" -> termsRepository.updateTermsLocForAll(content);
            case "PRIVACY" -> termsRepository.updateTermsPrivForAll(content);
            default -> throw new IllegalArgumentException("Invalid type" + type);
            }
        }

    }



