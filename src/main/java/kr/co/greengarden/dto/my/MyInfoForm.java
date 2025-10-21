package kr.co.greengarden.dto.my;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class MyInfoForm {
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    private String gender;
    private String email;
    private String phone;
    private String zipCode;
    private String addressBasic;
    private String addressDetail;

    public static MyInfoForm from(MyInfoDTO dto) {
        MyInfoForm form = new MyInfoForm();
        if (dto == null) {
            return form;
        }
        form.setName(dto.getName());
        form.setBirth(dto.getBirth());
        form.setGender(dto.getGender());
        form.setEmail(dto.getEmail());
        form.setPhone(dto.getPhone());
        form.setZipCode(dto.getZipCode());
        form.setAddressBasic(dto.getAddressBasic());
        form.setAddressDetail(dto.getAddressDetail());
        return form;
    }
}
