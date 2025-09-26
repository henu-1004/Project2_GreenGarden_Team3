package kr.co.greengarden.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberGerneralModifyDTO {
    private String memId;
    private String name;
    private String gender;
    private String grade;
    private String status;
    private String email;
    private String phone;
    private String zipCode;
    private String addressBasic;
    private String addressDetail;
    private LocalDateTime createdAt;
    private LocalDate lastLogin;
    private String note;
}
