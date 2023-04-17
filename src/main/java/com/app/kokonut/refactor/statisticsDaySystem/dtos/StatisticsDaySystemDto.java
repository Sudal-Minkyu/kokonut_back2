package com.app.kokonut.refactor.statisticsDaySystem.dtos;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
@ApiModel("")
public class StatisticsDaySystemDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer idx;


    /**
     * 날짜(일자로 기록)
     */
    @ApiModelProperty("날짜(일자로 기록)")
    private Date date;


    /**
     * 신규회원
     */
    @ApiModelProperty("신규회원")
    private Integer newMember;


    /**
     * 신규사업자회원
     */
    @ApiModelProperty("신규사업자회원")
    private Integer newMasterMember;


    /**
     * 신규개인회원
     */
    @ApiModelProperty("신규개인회원")
    private Integer newAdminMember;


    /**
     * 휴면계정전환
     */
    @ApiModelProperty("휴면계정전환")
    private Integer dormant;


    /**
     * 회원탈퇴,회원탈퇴해지(이탈총합은 더해서 표현)
     */
    @ApiModelProperty("회원탈퇴,회원탈퇴해지(이탈총합은 더해서 표현)")
    private Integer withdrawal;


    /**
     * 서비스 BASIC
     */
    @ApiModelProperty("서비스 BASIC")
    private Integer basic;


    /**
     * 서비스 STANDARD
     */
    @ApiModelProperty("서비스 STANDARD")
    private Integer standard;


    /**
     * 서비스 PREMIUM
     */
    @ApiModelProperty("서비스 PREMIUM")
    private Integer premium;


    /**
     * 자동결제해지
     */
    @ApiModelProperty("자동결제해지")
    private Integer autoCancel;


    /**
     * 회원탈퇴해지
     */
    @ApiModelProperty("회원탈퇴해지")
    private Integer withdrawalCancel;


    /**
     * BASIC 결제금액
     */
    @ApiModelProperty("BASIC 결제금액")
    private Integer basicAmount;


    /**
     * STANDARD 결제금액
     */
    @ApiModelProperty("STANDARD 결제금액")
    private Integer standardAmount;


    /**
     * PREMIUM 결제금액
     */
    @ApiModelProperty("PREMIUM 결제금액")
    private Integer premiumAmount;


    /**
     * 개인정보열람이력
     */
    @ApiModelProperty("개인정보열람이력")
    private Integer personalHistory;


    /**
     * 관리자열람이력
     */
    @ApiModelProperty("관리자열람이력")
    private Integer adminHistory;

}
