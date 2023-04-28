package com.app.kokonut.privacy.policy.policyserviceauto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of="pisaId")
@Data
@NoArgsConstructor
@Table(name="kn_privacy_policy_info_service_auto")
public class PolicyServiceAuto {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "pisa_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pisaId;

    @ApiModelProperty("개인정보처리방침 ID")
    @Column(name = "pi_id")
    private Long piId;

    @ApiModelProperty("처리목적")
    @Column(name = "pisa_purpose")
    private String pisaPurpose;

    @ApiModelProperty("수집항목")
    @Column(name = "pisa_info")
    private String pisaInfo;

    @ApiModelProperty("수집방법")
    @Column(name = "pisa_methodology")
    private String pisaMethodology;

    @ApiModelProperty("처리및보유기간")
    @Column(name = "pisa_period")
    private String pisaPeriod;

    @ApiModelProperty("등록한 이메일")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("등록된 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}