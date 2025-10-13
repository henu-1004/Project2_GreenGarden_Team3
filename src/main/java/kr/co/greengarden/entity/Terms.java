package kr.co.greengarden.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_TERMS")
public class Terms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TERMS_ID")
    private int termsId;

    @Column(name = "MEMBER_TYPE")
    private String memberType;

    @Column(name = "TERMS_USE")
    private String termsUse;

    @Column(name = "TERMS_FIN")
    private String termsFin;

    @Column(name = "TERMS_LOC")
    private String termsLoc;

    @Column(name = "TERMS_PRIV")
    private String termsPriv;

}
