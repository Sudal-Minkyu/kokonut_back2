package com.app.kokonut.refactor.statisticsDaySystem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "sdsId")
@Data
@NoArgsConstructor
@Table(name="kn_statistics_day_system")
public class StatisticsDaySystem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "sds_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sdsId;

    /**
     * 날짜(일자로 기록)
     */
    @Column(name = "sds_date")
    @ApiModelProperty("날짜(일자로 기록)")
    private LocalDateTime sdsDate;

    /**
     * 신규회원
     */
    @ApiModelProperty("신규회원")
    @Column(name = "sds_new_member")
    private Integer sds_new_member;

    /**
     * 신규사업자회원
     */
    @ApiModelProperty("신규사업자회원")
    @Column(name = "sbs_new_master_member")
    private Integer sbsNewMasterMember;

    /**
     * 신규개인회원
     */
    @ApiModelProperty("신규개인회원")
    @Column(name = "sbs_new_admin_member")
    private Integer sbsNewAdminMember;

    /**
     * 휴면계정전환
     */
    @Column(name = "sbs_dormant")
    @ApiModelProperty("휴면계정전환")
    private Integer sbsDormant;

    /**
     * 회원탈퇴,회원탈퇴해지(이탈총합은 더해서 표현)
     */
    @Column(name = "sbs_withdrawal")
    @ApiModelProperty("회원탈퇴,회원탈퇴해지(이탈총합은 더해서 표현)")
    private Integer sbsWithdrawal;

    /**
     * 서비스 BASIC
     */
    @Column(name = "sbs_basic")
    @ApiModelProperty("서비스 BASIC")
    private Integer sbsBasic;

    /**
     * 서비스 STANDARD
     */
    @Column(name = "sbs_standard")
    @ApiModelProperty("서비스 STANDARD")
    private Integer sbsStandard;

    /**
     * 서비스 PREMIUM
     */
    @Column(name = "sbs_premium")
    @ApiModelProperty("서비스 PREMIUM")
    private Integer sbsPremium;

    /**
     * 자동결제해지
     */
    @ApiModelProperty("자동결제해지")
    @Column(name = "sbs_auto_cancel")
    private Integer sbsAutoCancel;

    /**
     * 회원탈퇴해지
     */
    @ApiModelProperty("회원탈퇴해지")
    @Column(name = "sbs_withdrawal_cancel")
    private Integer sbsWithdrawalCancel;

    /**
     * BASIC 결제금액
     */
    @Column(name = "sbs_basic_amount")
    @ApiModelProperty("BASIC 결제금액")
    private Integer sbsBasicAmount;

    /**
     * STANDARD 이용자
     */
    @Column(name = "sbs_standard_user")
    @ApiModelProperty("STANDARD 이용자")
    private Integer sbsStandardUser;

    /**
     * STANDARD 결제금액
     */
    @Column(name = "sbs_standard_amount")
    @ApiModelProperty("STANDARD 결제금액")
    private Integer sbsStandardAmount;

    /**
     * PREMIUM 결제금액
     */
    @Column(name = "sbs_premium_amount")
    @ApiModelProperty("PREMIUM 결제금액")
    private Integer sbsPremiumAmount;

    /**
     * 개인정보열람이력
     */
    @ApiModelProperty("개인정보열람이력")
    @Column(name = "sbs_personal_history")
    private Integer sbsPersonalHistory;

    /**
     * 관리자열람이력
     */
    @ApiModelProperty("관리자열람이력")
    @Column(name = "sbs_admin_history")
    private Integer sbsAdminHistory;

    /**
     * 등록 날짜
     */
    @ApiModelProperty("등록 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}
