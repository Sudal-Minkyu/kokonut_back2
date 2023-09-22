package com.app.kokonut.provision;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Woody
 * LocalDateTime : 2023-05-08
 * Time :
 * Remark : 개인정보제공 테이블
 */
@Entity
@EqualsAndHashCode(of="proId")
@Data
@NoArgsConstructor
@Table(name="kn_personal_info_provision")
public class Provision {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "pro_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long proId;

    @ApiModelProperty("회사코드")
    @Column(name = "cp_code")
    private String cpCode;

    @ApiModelProperty("개인정보제공 고유코드")
    @Column(name = "pro_code")
    private String proCode;

    @ApiModelProperty("제공여부 - 0: 내부제공, 1:외부제공")
    @Column(name = "pro_provide")
    private Integer proProvide;

    @ApiModelProperty("제공시작 기간")
    @Column(name = "pro_start_date")
    private LocalDate proStartDate;

    @ApiModelProperty("제공만료 기간")
    @Column(name = "pro_exp_date")
    private LocalDate proExpDate;

    @ApiModelProperty("제공종료 여부 : 0: 미종료상태, 1: 종료상태")
    @Column(name = "pro_exit_state")
    private Integer proExitState;

    @ApiModelProperty("다운로드 유무 - 0: NO, 1:YES")
    @Column(name = "pro_download_yn")
    private Integer proDownloadYn;

    @ApiModelProperty("다운로드 횟수")
    @Column(name = "pro_download_count")
    private Integer proDownloadCount;

    @ApiModelProperty("제공 개인정보 여부 - 0: 전체 개인정보, 1: 일부 개인정보")
    @Column(name = "pro_target_type")
    private Integer proTargetType;

    @ApiModelProperty("등록자 email")
    @Column(name = "insert_email")
    private String insert_email;

    @ApiModelProperty("등록 날짜")
    @Column(name = "insert_date")
    private LocalDateTime insert_date;

    @ApiModelProperty("수정자 email")
    @Column(name = "modify_email")
    private String modify_email;

    @ApiModelProperty("수정 날짜")
    @Column(name = "modify_date")
    private LocalDateTime modify_date;

}
