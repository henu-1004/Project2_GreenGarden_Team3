package kr.co.greengarden.dto.my;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MyInfoDTO {
    private String memId;
    private String name;
    private LocalDate birth;
    private String gender;
    private String email;
    private String phone;
    private String status;
    private String note;
    private LocalDate lastLogin;
    private LocalDateTime joinDate;
    private String grade;
    private Integer discountRate;
    private Integer totalPoint;
    private Integer availableCouponCount;
    private String zipCode;
    private String addressBasic;
    private String addressDetail;
}
