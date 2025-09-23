package kr.co.greengarden.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.co.greengarden.dto.MemberDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/*
 * 날짜 : 2025/09/23
 * 이름 : 한탁원
 * 내용 : MemberEntity 초안 설정
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_MEMBER")
public class Member {

    @Id
    private String memId;

    @Column
    private String password;

    @Column
    private String role;

    @CreationTimestamp
    @Column
    private LocalDateTime joinDate;

    @Column
    private String addressBasic;

    @Column
    private String addressDetail;

    @Column
    private String zipCode;

    @Column
    private String status;

    public MemberDTO toDTO() {
        return MemberDTO.builder()
                        .memId(memId)
                        .password(password)
                        .role(role)
                        .zipCode(zipCode)
                        .addressBasic(addressBasic)
                        .addressDetail(addressDetail)
                        .status(status)
                        .build();
    }
}
