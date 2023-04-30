package com.app.kokonut.policy.policyafter;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of="piaId")
@Data
@NoArgsConstructor
@Table(name="kn_privacy_policy_info_after")
public class PolicyAfter {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "pia_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long piaId;

    @ApiModelProperty("개인정보처리방침 ID")
    @Column(name = "pi_id")
    private Long piId;

    @ApiModelProperty("가입후 처리목적")
    @Column(name = "pia_purpose")
    private String piaPurpose;

    @ApiModelProperty("수집항목")
    @Column(name = "pia_info")
    private String piaInfo;

    @ApiModelProperty("필수/선택")
    @Column(name = "pia_chose")
    private String piaChose;

    @ApiModelProperty("처리및보유기간")
    @Column(name = "pia_period")
    private String piaPeriod;

    @ApiModelProperty("등록한 이메일")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("등록된 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}