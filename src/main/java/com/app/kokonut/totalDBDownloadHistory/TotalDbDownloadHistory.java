package com.app.kokonut.totalDBDownloadHistory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-01-20
 * Time :
 * Remark : 개인정보 데이터 엑셀 다운로드 로그 테이블
 */
@Entity
@EqualsAndHashCode(of = "tdhId")
@Data
@NoArgsConstructor
@Table(name="kn_total_db_download_history")
public class TotalDbDownloadHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 키
     */
    @Id
    @ApiModelProperty("키")
    @Column(name = "tdh_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tdhId;

    /**
     * 데이터 다운로드 요청 정보 id
     */
    @Column(name = "td_id")
    @ApiModelProperty("데이터 다운로드 요청 정보 id")
    private Long tdId;

    /**
     * 다운로드한 파일 이름
     */
    @Column(name = "tdh_file_name")
    @ApiModelProperty("다운로드한 파일 이름")
    private String tdhFileName;

    /**
     * 다운로드 사유
     */
    @Column(name = "tdh_reason")
    @ApiModelProperty("다운로드 사유")
    private String tdhReason;

    /**
     * 등록자 email
     */
    @ApiModelProperty("등록자 email")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    /**
     * 등록 날짜
     */
    @ApiModelProperty("등록 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;
}
