package kr.co.greengarden.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberGeneralListDTO {
    String memId;
    String name;
    String gender;
    private String grade;
    private int totalPoint;
    String email;
    String phone;
    LocalDateTime createdAt;
    String status;
}
