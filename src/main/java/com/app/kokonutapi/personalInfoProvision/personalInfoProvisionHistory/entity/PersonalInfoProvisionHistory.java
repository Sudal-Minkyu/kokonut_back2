package com.app.kokonutapi.personalInfoProvision.personalInfoProvisionHistory.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Woody
 * LocalDateTime : 2023-01-25
 * Time :
 * Remark : 정보제공관리 이력 Table Entity
 */
@Entity
@EqualsAndHashCode(of = "pphId")
@Data
@NoArgsConstructor
@Table(name="kn_personal_info_provision_history")
public class PersonalInfoProvisionHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 키
     */
    @Id
    @ApiModelProperty("키")
    @Column(name = "pph_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pphId;

    /**
     * personal_info_provision 고유번호
     */
    @Column(name = "pi_number", nullable = false)
    @ApiModelProperty("personal_info_provision 고유번호")
    private String piNumber;

    /**
     * 회사 키
     */
    @ApiModelProperty("회사 키")
    @Column(name = "company_id", nullable = false)
    private Long companyId;

    /**
     * 관리자 키 (=수정자)
     */
    @Column(name = "admin_id")
    @ApiModelProperty("관리자 키 (=수정자)")
    private Long adminId;

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

    /**
     * 수정자 이름
     */
    @ApiModelProperty("수정자 email")
    @Column(name = "modify_email")
    private String modify_email;

    /**
     * 수정 날짜
     */
    @ApiModelProperty("수정 날짜")
    @Column(name = "modify_date")
    private LocalDateTime modify_date;

}
