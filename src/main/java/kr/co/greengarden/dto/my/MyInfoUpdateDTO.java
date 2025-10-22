package kr.co.greengarden.dto.my;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MyInfoUpdateDTO {
    private String memId;
    private String name;
    private LocalDate birth;
    private String gender;
    private String email;
    private String phone;
    private String zipCode;
    private String addressBasic;
    private String addressDetail;
    private String newPassword;
}
