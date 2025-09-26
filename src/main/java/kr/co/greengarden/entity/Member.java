package kr.co.greengarden.entity;

import jakarta.persistence.*;
import kr.co.greengarden.dto.MemberDTO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/*
 * 날짜 : 2025/09/23
 * 이름 : 한탁원
 * 내용 : MemberEntity 초안 설정
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_MEMBER")
@ToString(exclude = {"general", "seller", "grade", "point"})
public class Member {

    @Id
    private String memId;

    @Column
    private String password;

    @Column
    private String role;

    @CreationTimestamp
    @Column
    private LocalDateTime joinDate;

    @Column
    private String zipCode;

    @Column
    private String addressBasic;

    @Column
    private String addressDetail;

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
    private MemberGeneral general;

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
    private MemberSeller seller;

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
    private Grade grade;

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
    private Point point;

    public MemberDTO toDTO() {
        return MemberDTO.builder()
                        .memId(memId)
                        .password(password)
                        .role(role)
                        .zipCode(zipCode)
                        .addressBasic(addressBasic)
                        .addressDetail(addressDetail)
                        .build();
    }
}
