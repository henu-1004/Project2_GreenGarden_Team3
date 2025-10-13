package kr.co.greengarden.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import kr.co.greengarden.entity.Member;
import kr.co.greengarden.entity.MemberSeller;
import lombok.*;

/*
 * 날짜 : 2025/09/24
 * 이름 : 한탁원
 * 내용 : MemberSellerDTO(판매 회원) 초안 설정
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberSellerDTO {

    private String memId;
    private String company;
    private String representative;
    private String tin;
    private String businessNumber;
    private String tel;
    private String fax;
    private String status;

    public MemberSeller toEntity(Member member) {
        return MemberSeller.builder()
                           .company(company)
                           .representative(representative)
                           .tin(tin)
                           .businessNumber(businessNumber)
                           .tel(tel)
                           .fax(fax)
                           .status(status)
                           .member(member)
                           .build();
    }
}
