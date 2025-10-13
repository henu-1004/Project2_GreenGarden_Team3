package kr.co.greengarden.dto;

import kr.co.greengarden.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
 * 날짜 : 2025/09/23
 * 이름 : 한탁원
 * 내용 : MemberDTO 초안 설정
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDTO {

    private String memId;

    private String password;

    private String role;

    private String joinDate;

    private String zipCode;

    private String addressBasic;

    private String addressDetail;

    public Member toEntity() {
        return Member.builder()
                     .memId(memId)
                     .password(password)
                     .role(role)
                     .zipCode(zipCode)
                     .addressBasic(addressBasic)
                     .addressDetail(addressDetail)
                     .build();
    }
}
