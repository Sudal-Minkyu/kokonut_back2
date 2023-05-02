package com.app.kokonut.policy;

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
    private String piDate;

    @ApiModelProperty("머리말 기업명")
    @Column(name = "pi_header")
    private String piHeader;

    @ApiModelProperty("인터넷접속로그 여부 0: 미선택, 1: 선택")
    @Column(name = "pi_internet_chose")
    private Boolean piInternetChose;

    @ApiModelProperty("계약또는청약철회 여부 0: 미선택, 1: 선택")
    @Column(name = "pi_contract_chose")
    private Boolean piContractChose;

    @ApiModelProperty("대금결제 및 재화 여부 0: 미선택, 1: 선택")
    @Column(name = "pi_pay_chose")
    private Boolean piPayChose;

    @ApiModelProperty("소피자의 불만 또는 분쟁처리 여부: 0: 미선택, 1: 선택")
    @Column(name = "pi_consumer_chose")
    private Boolean piConsumerChose;

    @ApiModelProperty("표시광고 0: 미선택, 1: 선택")
    @Column(name = "pi_advertisement_chose")
    private Boolean piAdvertisementChose;

    @ApiModelProperty("개인정보 처리업무의 국외 위탁에 관한 사항: 0: 미선택, 1: 선택")
    @Column(name = "pi_out_chose")
    private Boolean piOutChose;

    @ApiModelProperty("제3자 제공에 관한 사항: 0: 미선택, 1: 선택")
    @Column(name = "pi_third_chose")
    private Boolean piThirdChose;

    @ApiModelProperty("국외 제3자 제공에 관한 사항: 0: 미선택, 1: 선택")
    @Column(name = "pi_third_overseas_chose")
    private Boolean piThirdOverseasChose;

    @ApiModelProperty("시행일자 년")
    @Column(name = "pi_year")
    private String piYear;

    @ApiModelProperty("시행일자 월")
    @Column(name = "pi_month")
    private String piMonth;

    @ApiModelProperty("시행일자 일")
    @Column(name = "pi_day")
    private String piDay;

    @ApiModelProperty("작성단계(1,2,3,4,5,6(완료)")
    @Column(name = "pi_stage")
    private Integer piStage;

    @ApiModelProperty("작정완료 여부 0:미완료, 1:완료")
    @Column(name = "pi_autosave")
    private Integer piAutosave;
    
    @ApiModelProperty("등록한 이메일")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("등록된 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

    @ApiModelProperty("수정한 이메일")
    @Column(name = "modify_email", nullable = false)
    private String modify_email;

    @ApiModelProperty("수정한 날짜")
    @Column(name = "modify_date", nullable = false)
    private LocalDateTime modify_date;
}