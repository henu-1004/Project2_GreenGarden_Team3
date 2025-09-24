package kr.co.greengarden.entity;

import jakarta.persistence.*;
import kr.co.greengarden.dto.MemberDTO;
import kr.co.greengarden.dto.MemberGeneralDTO;
import lombok.*;

import java.time.LocalDate;

/*
 * 날짜 : 2025/09/24
 * 이름 : 한탁원
 * 내용 : MemberGeneralEntity(일반 회원) 초안 설정
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_MEMBER_GENERAL")
public class MemberGeneral {

    @Id
    private String memId;

    @Column
    private String name;

    @Column
    private LocalDate birth;

    @Column
    private String gender;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private LocalDate lastLogin;

    @Column
    private String note;

    @Column
    private String status;

    @OneToOne
    @MapsId
    @JoinColumn(name="memId")
    private Member member;

    public MemberGeneralDTO toDTO() {
        return MemberGeneralDTO.builder()
                .memId(memId)
                .name(name)
                .birth(birth != null ? birth.toString() : null)
                .gender(gender)
                .email(email)
                .phone(phone)
                .lastLogin(lastLogin != null ? lastLogin.toString() : null)
                .note(note)
                .status(status)
                .build();
    }
}
