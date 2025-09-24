package kr.co.greengarden.dto;

import kr.co.greengarden.entity.Member;
import kr.co.greengarden.entity.MemberGeneral;
import lombok.*;

import java.time.LocalDate;

/*
 * 날짜 : 2025/09/24
 * 이름 : 한탁원
 * 내용 : MemberGenralDTO(일반 회원) 초안 설정
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberGeneralDTO {

    private String memId;

    private String name;

    private String birth;

    private String gender;

    private String email;

    private String phone;

    private String lastLogin;

    private String note;

    private String status;

    public MemberGeneral toEntity(Member member) {
        return MemberGeneral.builder()
                .name(name)
                .birth(birth == null || birth.isBlank() ? null : LocalDate.parse(birth))
                .gender(gender)
                .email(email)
                .phone(phone)
                .lastLogin(lastLogin == null || lastLogin.isBlank() ? null : LocalDate.parse(lastLogin))
                .note(note)
                .status(status)
                .member(member)
                .build();
    }

}