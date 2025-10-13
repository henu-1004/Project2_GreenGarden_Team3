package kr.co.greengarden.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindResultDTO {

    private String name;
    private String email;
    private String memId;
    private LocalDateTime joinDate;
}
