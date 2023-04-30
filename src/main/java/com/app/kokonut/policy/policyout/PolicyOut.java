package com.app.kokonut.policy.policyout;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of="pioId")
@Data
@NoArgsConstructor
@Table(name="kn_privacy_policy_info_out")
public class PolicyOut {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "pio_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pioId;

    @ApiModelProperty("개인정보처리방침 ID")
    @Column(name = "pi_id")
    private Long piId;

    @ApiModelProperty("수탁업체")
    @Column(name = "pio_outsourcing_company")
    private String pioOutsourcingCompany;

    @ApiModelProperty("필수/선택")
    @Column(name = "pio_chose")
    private String pioChose;

    @ApiModelProperty("위탁업무")
    @Column(name = "pio_consignment_company")
    private String pioConsignmentCompany;

    @ApiModelProperty("처리및보유기간")
    @Column(name = "pio_period")
    private String pioPeriod;

    @ApiModelProperty("등록한 이메일")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("등록된 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}