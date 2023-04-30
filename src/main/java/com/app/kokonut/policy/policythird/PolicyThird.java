package com.app.kokonut.policy.policythird;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of="pitId")
@Data
@NoArgsConstructor
@Table(name="kn_privacy_policy_info_third")
public class PolicyThird {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "pit_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pitId;

    @ApiModelProperty("개인정보처리방침 ID")
    @Column(name = "pi_id")
    private Long piId;

    @ApiModelProperty("제공받는 자")
    @Column(name = "pit_recipient")
    private String pitRecipient;

    @ApiModelProperty("제공받는 목적")
    @Column(name = "pit_purpose")
    private String pitPurpose;

    @ApiModelProperty("제공받는 항목")
    @Column(name = "pit_info")
    private String pitInfo;

    @ApiModelProperty("제공받는자의 보유 및 이용기간")
    @Column(name = "pit_period")
    private String pitPeriod;

    @ApiModelProperty("등록한 이메일")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("등록된 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}