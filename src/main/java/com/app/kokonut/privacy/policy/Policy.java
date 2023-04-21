package com.app.kokonut.privacy.policy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of="piId")
@Data
@NoArgsConstructor
@Table(name="kn_privacy_policy_info")
public class Policy {

    @Id
    @ApiModelProperty("개인정보처리방침 ID")
    @Column(name = "pi_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long piId;

    @ApiModelProperty("회사코드")
    @Column(name = "cp_code")
    private String cpCode;

    @ApiModelProperty("개정본 버전")
    @Column(name = "pi_version")
    private Double piVersion;

    @ApiModelProperty("개정일")
    @Column(name = "pi_date")
    private LocalDateTime piDate;

    @ApiModelProperty("머리말 기업명")
    @Column(name = "pi_header")
    private String piHeader;

    @ApiModelProperty("인터넷접속로그 여부 0: 미선택, 1: 선택")
    @Column(name = "pi_internet_log")
    private Integer piInternetLog;

    @ApiModelProperty("계약또는청약철회 여부 0: 미선택, 1: 선택")
    @Column(name = "pi_contract_log")
    private Integer piContractLog;

    @ApiModelProperty("계약또는청약철회 여부 0: 미선택, 1: 선택")
    @Column(name = "pi_pay_log")
    private Integer piPayLog;

    @ApiModelProperty("소피자의 불만 또는 분쟁처리 여부: 0: 미선택, 1: 선택")
    @Column(name = "pi_consumer_log")
    private Integer piConsumerLog;

    @ApiModelProperty("시행일자 년")
    @Column(name = "pi_year")
    private String piYear;

    @ApiModelProperty("시행일자 월")
    @Column(name = "pi_month")
    private String piMonth;

    @ApiModelProperty("시행일자 일")
    @Column(name = "pi_day")
    private String piDay;

    @ApiModelProperty("작정완료 여부 0:미완료, 1:완료")
    @Column(name = "pi_autosave")
    private Integer piAutosave;
    
    @ApiModelProperty("등록한 이메일")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("등록된 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}