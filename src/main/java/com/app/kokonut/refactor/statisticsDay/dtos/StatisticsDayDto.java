package com.app.kokonut.refactor.statisticsDay.dtos;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
@ApiModel("")
public class StatisticsDayDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer idx;


    /**
     * COMPANY IDX
     */
    @ApiModelProperty("COMPANY IDX")
    private Long companyId;


    /**
     * 날짜(일자로 기록)
     */
    @ApiModelProperty("날짜(일자로 기록)")
    private Date date;


    /**
     * 일평균 회원 수
     */
    @ApiModelProperty("일평균 회원 수")
    private Integer allMember;


    /**
     * 개인회원(신규가입총합은 더해서 표현)
     */
    @ApiModelProperty("개인회원(신규가입총합은 더해서 표현)")
    private Integer newMember;


    /**
     * 휴면계정전환
     */
    @ApiModelProperty("휴면계정전환")
    private Integer dormant;


    /**
     * 회원탈퇴
     */
    @ApiModelProperty("회원탈퇴")
    private Integer withdrawal;


    /**
     * 개인정보 열람 이력
     */
    @ApiModelProperty("개인정보 열람 이력")
    private Integer personalHistory;


    /**
     * 관리자 열람 이력
     */
    @ApiModelProperty("관리자 열람 이력")
    private Integer adminHistory;

}
