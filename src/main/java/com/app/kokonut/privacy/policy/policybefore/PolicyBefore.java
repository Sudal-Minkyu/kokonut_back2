package com.app.kokonut.privacy.policy.policybefore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of="pibId")
@Data
@NoArgsConstructor
@Table(name="kn_privacy_policy_info_before")
public class PolicyBefore {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "pib_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pibId;

    @ApiModelProperty("개인정보처리방침 ID")
    @Column(name = "pi_id")
    private Long piId;

    @ApiModelProperty("가입시 처리목적")
    @Column(name = "pib_purpose")
    private String pibPurpose;

    @ApiModelProperty("수집항목")
    @Column(name = "pib_info")
    private String pibInfo;
    
    @ApiModelProperty("필수/선택")
    @Column(name = "pib_chose")
    private String pibChose;
    
    @ApiModelProperty("처리및보유기간")
    @Column(name = "pib_period")
    private String pibPeriod;

    @ApiModelProperty("등록한 이메일")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("등록된 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}