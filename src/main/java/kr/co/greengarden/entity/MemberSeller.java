package kr.co.greengarden.entity;

import jakarta.persistence.*;
import kr.co.greengarden.dto.MemberDTO;
import kr.co.greengarden.dto.MemberSellerDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/*
 * 날짜 : 2025/09/24
 * 이름 : 한탁원
 * 내용 : MemberSellerEntity(판매 회원) 초안 설정
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_MEMBER_SELLER")
@ToString(exclude = {"products"})
public class MemberSeller {

    @Id
    private int memId;
    @Column
    private String company;
    @Column
    private String representative;
    @Column
    private String tin;
    @Column
    private String business_number;
    @Column
    private String tel;
    @Column
    private String fax;
    @Column
    private String status;

    @OneToOne
    @MapsId
    @JoinColumn(name="memId")
    private Member member;

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    public MemberSellerDTO toDTO() {
        return MemberSellerDTO.builder()
                               .memId(memId)
                               .company(company)
                               .representative(representative)
                               .tin(tin)
                               .business_number(business_number)
                               .tel(tel)
                               .fax(fax)
                               .status(status)
                               .build();
    }

}
