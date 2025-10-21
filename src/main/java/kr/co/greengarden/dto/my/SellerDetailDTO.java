package kr.co.greengarden.dto.my;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerDetailDTO {

    private String sellerId;
    private String company;
    private String representative;
    private String tel;
    private String fax;
    private String businessNumber;
    private String tin;
    private String status;
    private String email;
    private String phone;
    private String zipCode;
    private String addressBasic;
    private String addressDetail;
    private String grade;
}
