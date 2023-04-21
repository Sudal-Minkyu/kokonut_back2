package com.app.kokonut.privacy.policy.policycollection;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of="piscId")
@Data
@NoArgsConstructor
@Table(name="kn_privacy_policy_info_service_collection")
public class PolicyCollection {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "pisc_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long piscId;

    @ApiModelProperty("개인정보처리방침 ID")
    @Column(name = "pi_id")
    private Long piId;

    @ApiModelProperty("서비스 가입시 처리목적")
    @Column(name = "pisc_purpose")
    private String piscPurpose;

    @ApiModelProperty("수집항목")
    @Column(name = "pics_info")
    private String picsInfo;

    @ApiModelProperty("처리및보유기간")
    @Column(name = "pics_period")
    private String picsPeriod;

    @ApiModelProperty("등록한 이메일")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("등록된 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}