package com.app.kokonut.policy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of="piId")
@Data
@NoArgsConstructor
@Table(name="kn_privacy_policy_info")
public class Policy {

    @Id
    @ApiModelProperty("개인정보처리방침 ID")
    @Column(name = "pi_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long piId;

    @ApiModelProperty("회사코드")
    @Column(name = "cp_code")
    private String cpCode;

    @ApiModelProperty("개정본 버전")
    @Column(name = "pi_version")
    private String piVersion;

    @ApiModelProperty("개정일")
    @Column(name = "pi_date")
    private String piDate;

    @ApiModelProperty("머리말 기업명")
    @Column(name = "pi_header")
    private String piHeader;

    @ApiModelProperty("',' 구분자 -> 기본항목의 법령에 따른 개인정보의 보유기간 체크항목 순서")
    @Column(name = "pi_statute")
    private String piStatute;

    @ApiModelProperty("이전 시행일자 년")
    @Column(name = "pi_year")
    private String piYear;

    @ApiModelProperty("이전 시행일자 월")
    @Column(name = "pi_month")
    private String piMonth;

    @ApiModelProperty("이전 시행일자 일")
    @Column(name = "pi_day")
    private String piDay;

    @ApiModelProperty("작성단계(1,2,3,4,5,6(완료)")
    @Column(name = "pi_stage")
    private Integer piStage;

    @ApiModelProperty("작정완료 여부 0:미완료, 1:완료")
    @Column(name = "pi_autosave")
    private Integer piAutosave;
    
    @ApiModelProperty("등록한 이메일")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("등록된 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

    @ApiModelProperty("수정한 이메일")
    @Column(name = "modify_email", nullable = false)
    private String modify_email;

    @ApiModelProperty("수정한 날짜")
    @Column(name = "modify_date", nullable = false)
    private LocalDateTime modify_date;
}