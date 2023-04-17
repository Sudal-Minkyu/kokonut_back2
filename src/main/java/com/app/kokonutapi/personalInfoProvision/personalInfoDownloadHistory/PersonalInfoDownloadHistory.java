package com.app.kokonutapi.personalInfoProvision.personalInfoDownloadHistory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@EqualsAndHashCode(of = "pdhId")
@Data
@NoArgsConstructor
@Table(name="kn_personal_info_download_history")
public class PersonalInfoDownloadHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 키
     */
    @Id
    @ApiModelProperty("키")
    @Column(name = "pdh_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pdhId;

    /**
     * personal_info_provision 고유번호
     */
    @Column(name = "pi_number")
    @ApiModelProperty("personal_info_provision 고유번호")
    private String piNumber;

    /**
     * 보유기간 만료일
     */
    @ApiModelProperty("보유기간 만료일")
    @Column(name = "pdh_retention_date", nullable = false)
    private LocalDateTime pdhRetentionDate;

    /**
     * 이메일
     */
    @ApiModelProperty("이메일")
    @Column(name = "pdh_email", nullable = false)
    private String pdhEmail;

    /**
     * 파일명
     */
    @ApiModelProperty("파일명")
    @Column(name = "pdh_file_name", nullable = false)
    private String pdhFileName;

    /**
     * 주의사항 동의여부 (Y/N)
     */
    @ApiModelProperty("주의사항 동의여부 (Y/N)")
    @Column(name = "pdh_agree_yn", nullable = false)
    private String pdhAgreeYn;

    /**
     * 정보제공 파기 주의사항 동의여부 (Y/N)
     */
    @ApiModelProperty("정보제공 파기 주의사항 동의여부 (Y/N)")
    @Column(name = "pdh_destruction_agree_yn", nullable = false)
    private String pdhDestructionAgreeYn;

    /**
     * 정보제공 파기 최근 등록일
     */
    @Column(name = "pdh_destruction_date")
    @ApiModelProperty("정보제공 파기 최근 등록일")
    private LocalDateTime pdhDestructionDate;

    /**
     * 정보제공 파기 등록자
     */
    @ApiModelProperty("정보제공 파기 등록자")
    @Column(name = "pdh_destruction_register_name")
    private String pdhDestructionRegisterName;

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
