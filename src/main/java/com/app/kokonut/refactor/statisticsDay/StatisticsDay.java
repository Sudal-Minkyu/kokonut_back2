package com.app.kokonut.refactor.statisticsDay;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "sdId")
@Data
@NoArgsConstructor
@Table(name="kn_statistics_day")
public class StatisticsDay implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "sd_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sdId;

    /**
     * COMPANY IDX
     */
    @Column(name = "company_id")
    @ApiModelProperty("COMPANY IDX")
    private Long companyId;

    /**
     * 날짜(일자로 기록)
     */
    @Column(name = "sd_date")
    @ApiModelProperty("날짜(일자로 기록)")
    private LocalDateTime sdDate;

    /**
     * 일평균 회원 수
     */
    @Column(name = "sd_all_member")
    @ApiModelProperty("일평균 회원 수")
    private Integer sdAllMember;

    /**
     * 개인회원(신규가입총합은 더해서 표현)
     */
    @Column(name = "sd_new_member")
    @ApiModelProperty("개인회원(신규가입총합은 더해서 표현)")
    private Integer sdNewMember;

    /**
     * 휴면계정전환
     */
    @Column(name = "sd_dormant")
    @ApiModelProperty("휴면계정전환")
    private Integer sdDormant;

    /**
     * 회원탈퇴
     */
    @ApiModelProperty("회원탈퇴")
    @Column(name = "sd_withdrawal")
    private Integer sdWithdrawal;

    /**
     * 개인정보 열람 이력
     */
    @ApiModelProperty("개인정보 열람 이력")
    @Column(name = "sd_personal_history")
    private Integer sdPersonalHistory;

    /**
     * 관리자 열람 이력
     */
    @ApiModelProperty("관리자 열람 이력")
    @Column(name = "sd_admin_history")
    private Integer sdAdminHistory;

    /**
     * 등록 날짜
     */
    @ApiModelProperty("등록 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}
