package com.app.kokonut.policy.policystatute;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of="pistId")
@Data
@NoArgsConstructor
@Table(name="kn_privacy_policy_info_statute")
public class PolicyStatute {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "pist_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pistId; // 주키

    @ApiModelProperty("개인정보처리방침 ID")
    @Column(name = "pi_id")
    private Long piId; // 개인정보처리방침 ID

    @ApiModelProperty("수집항목")
    @Column(name = "pisa_title")
    private String pisaTitle; // 수집항목

    @ApiModelProperty("근거법")
    @Column(name = "pisa_contents")
    private String pisaContents; // 근거법

    @ApiModelProperty("보유기간")
    @Column(name = "pist_period")
    private String pistPeriod; // 보유기간

    @ApiModelProperty("체크여부 : 0 : 미체크, 1 : 체크함")
    @Column(name = "pist_check")
    private Boolean pistCheck; // 체크여부 : 0 : 미체크, 1 : 체크함

    @ApiModelProperty("등록한 이메일")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("등록된 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}