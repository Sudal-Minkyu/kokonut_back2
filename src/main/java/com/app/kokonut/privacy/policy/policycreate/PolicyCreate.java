package com.app.kokonut.privacy.policy.policycreate;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of="pisrId")
@Data
@NoArgsConstructor
@Table(name="kn_privacy_policy_info_service_create")
public class PolicyCreate {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "pisr_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pisrId;

    @ApiModelProperty("개인정보처리방침 ID")
    @Column(name = "pi_id")
    private Long piId;

    @ApiModelProperty("서비스 가입시 처리목적")
    @Column(name = "pisr_purpose")
    private String pisrPurpose;

    @ApiModelProperty("수집항목")
    @Column(name = "pisr_info")
    private String pisrInfo;

    @ApiModelProperty("수집방법")
    @Column(name = "pisr_methodology")
    private String pisrMethodology;

    @ApiModelProperty("처리및보유기간")
    @Column(name = "pisr_period")
    private String pisrPeriod;

    @ApiModelProperty("등록한 이메일")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("등록된 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}