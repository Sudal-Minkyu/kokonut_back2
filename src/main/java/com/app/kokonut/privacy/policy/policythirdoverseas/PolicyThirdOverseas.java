package com.app.kokonut.privacy.policy.policythirdoverseas;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of="pitoId")
@Data
@NoArgsConstructor
@Table(name="kn_privacy_policy_info_third_overseas")
public class PolicyThirdOverseas {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "pito_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pitoId;

    @ApiModelProperty("개인정보처리방침 ID")
    @Column(name = "pi_id")
    private Long piId;

    @ApiModelProperty("제공받는자")
    @Column(name = "pito_recipient")
    private String pitoRecipient;

    @ApiModelProperty("제공받는자의 위치")
    @Column(name = "pito_location")
    private String pitoLocation;

    @ApiModelProperty("제공받는자의 목적")
    @Column(name = "pito_purpose")
    private String pitoPurpose;

    @ApiModelProperty("제공받는자의 항목")
    @Column(name = "pito_info")
    private String pitoInfo;

    @ApiModelProperty("제공받는자의 개인정보 보유 및 이용기간")
    @Column(name = "pito_period")
    private String pitoPeriod;

    @ApiModelProperty("등록한 이메일")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("등록된 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}