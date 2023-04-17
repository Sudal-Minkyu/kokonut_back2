package com.app.kokonut.totalDBDownload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Woody
 * LocalDateTime : 2023-01-25
 * Time :
 * Remark : 유저테이블 DB 다운로드 신청 Table Entity
 */
@Entity
@EqualsAndHashCode(of = "tdId")
@Data
@NoArgsConstructor
@Table(name="kn_total_db_download")
public class TotalDbDownload {

    /**
     * 키
     */
    @Id
    @ApiModelProperty("키")
    @Column(name = "td_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tdId;

    /**
     * 요청자
     */
    @ApiModelProperty("요청자")
    @Column(name = "admin_id")
    private Long adminId;

    /**
     * 요청사유
     */
    @Column(name = "td_reason")
    @ApiModelProperty("요청사유")
    private String tdReason;

    /**
     * 요청일자
     */
    @ApiModelProperty("요청일자")
    @Column(name = "td_apply_date")
    private LocalDateTime tdApplyDate;

    /**
     * 다운로드 링크
     */
    @Column(name = "td_link")
    @ApiModelProperty("다운로드 링크")
    private String tdLink;

    /**
     * 상태(1:다운로드요청, 2:다운로드승인(다운로드대기), 3:다운로드완료, 4:반려)
     */
    @Column(name = "td_state")
    @ApiModelProperty("상태(1:다운로드요청, 2:다운로드승인(다운로드대기), 3:다운로드완료, 4:반려)")
    private Integer tdState;

    /**
     * 반려사유
     */
    @ApiModelProperty("반려사유")
    @Column(name = "td_return_reason")
    private String tdReturnReason;

    /**
     * 횟수제한
     */
    @Column(name = "td_limit")
    @ApiModelProperty("횟수제한")
    private Integer tdLimit;

    /**
     * 기간제한 시작일자
     */
    @ApiModelProperty("기간제한 시작일자")
    @Column(name = "td_limit_date_start")
    private LocalDateTime tdLimitDateStart;

    /**
     * 기간제한 종료일자
     */
    @ApiModelProperty("기간제한 종료일자")
    @Column(name = "td_limit_date_end")
    private LocalDateTime tdLimitDateEnd;

    /**
     * 다운로드 일자
     */
    @ApiModelProperty("다운로드 일자")
    @Column(name = "td_download_date")
    private LocalDateTime tdDownloadDate;

    /**
     * IP주소(다운로드정보에 표현)
     */
    @Column(name = "td_ip_addr")
    @ApiModelProperty("IP주소(다운로드정보에 표현)")
    private String tdIpAddr;

    /**
     * 등록 날짜
     */
    @ApiModelProperty("등록 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}
