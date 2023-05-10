package com.app.kokonut.company.companytable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "ctId")
@Data
@NoArgsConstructor
@Table(name="kn_company_table")
public class CompanyTable {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "ct_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ctId;

    @ApiModelProperty("회사코드")
    @Column(name = "cp_code")
    private String cpCode;

    @ApiModelProperty("테이블명(= 회사코드+_+테이블 순번)")
    @Column(name = "ct_name")
    private String ctName;

    @ApiModelProperty("테이블 순번 (=회사별 테이블 수의 카운트)")
    @Column(name = "ct_table_count")
    private String ctTableCount;

    @ApiModelProperty("테이블의 명칭")
    @Column(name = "ct_designation")
    private String ctDesignation;

    @ApiModelProperty("해당 추가한 컬럼 수의 카운트")
    @Column(name = "ct_add_column_count")
    private Integer ctAddColumnCount;

    @ApiModelProperty("해당 테이블의 이름 컬럼 유무 - 공백:포함되지않음, 필드명:포함됨")
    @Column(name = "ct_name_status")
    private String ctNameStatus;

    @ApiModelProperty("해당 테이블의 휴대폰번호 컬럼 유무 - 공백:포함되지않음, 필드명:포함됨")
    @Column(name = "ct_phone_status")
    private String ctPhoneStatus;

    @ApiModelProperty("해당 테이블의 성별 컬럼 유무 - 공백:포함되지않음, 필드명:포함됨")
    @Column(name = "ct_gender_status")
    private String ctGenderStatus;

    @ApiModelProperty("해당 테이블의 이메일 컬럼 유무 - 공백:포함되지않음, 필드명:포함됨")
    @Column(name = "ct_email_status")
    private String ctEmailStatus;

    @ApiModelProperty("해당 테이블의 생년월일 컬럼 유무 - 공백:포함되지않음, 필드명:포함됨")
    @Column(name = "ct_birth_status")
    private String ctBirthStatus;

    @ApiModelProperty("등록자 email")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("등록 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

    @ApiModelProperty("수정자 email")
    @Column(name = "modify_email")
    private String modify_email;

    @ApiModelProperty("수정 날짜")
    @Column(name = "modify_date")
    private LocalDateTime modify_date;

}
