package com.app.kokonut.privacy.policy.policyoutdetail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of="piodId")
@Data
@NoArgsConstructor
@Table(name="kn_privacy_policy_info_out_detail")
public class PolicyOutDetail {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "piod_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long piodId;

    @ApiModelProperty("개인정보처리방침 ID")
    @Column(name = "pi_id")
    private Long piId;

    @ApiModelProperty("수탁업체")
    @Column(name = "piod_company")
    private String piodCompany;

    @ApiModelProperty("수탁업체 위치")
    @Column(name = "piod_location")
    private String piodLocation;

    @ApiModelProperty("위탁일시 및 방법")
    @Column(name = "piod_method")
    private String piod_method;

    @ApiModelProperty("책임자연락처")
    @Column(name = "piod_contact")
    private String piod_contact;

    @ApiModelProperty("위탁하는 개인정보 항목")
    @Column(name = "piod_info")
    private String piodInfo;

    @ApiModelProperty("위탁 업무 내용")
    @Column(name = "piod_detail")
    private String piodDetail;

    @ApiModelProperty("위탁 보유및이용기간")
    @Column(name = "piod_period")
    private String piodPeriod;

    @ApiModelProperty("등록한 이메일")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("등록된 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}