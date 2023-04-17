package com.app.kokonutapi.personalInfoProvision.personalInfoProvisionAgree.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "ppaId")
@Data
@NoArgsConstructor
@Table(name="kn_personal_info_provision_agree")
public class PersonalInfoProvisionAgree implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 키
     */
    @Id
    @ApiModelProperty("키")
    @Column(name = "ppa_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ppaId;

    /**
     * personal_info_provision 관리번호
     */
    @Column(name = "pi_number", nullable = false)
    @ApiModelProperty("personal_info_provision 관리번호")
    private String piNumber;

    /**
     * 동의 날짜
     */
    @ApiModelProperty("동의 날짜")
    @Column(name = "ppa_agree_date")
    private LocalDateTime ppaAgreeDate;

    /**
     * 주의사항 동의여부 (Y/N)
     */
    @ApiModelProperty("주의사항 동의여부 (Y/N)")
    @Column(name = "ppa_agree_yn", nullable = false)
    private String ppaAgreeYn;

    /**
     * 대상 id
     */
    @ApiModelProperty("대상 id")
    @Column(name = "user_id", nullable = false)
    private String userId;

    /**
     * 등록자 email
     */
    @ApiModelProperty("등록자 email")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    /**
     * 등록 날짜
     */
    @ApiModelProperty("등록 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}
