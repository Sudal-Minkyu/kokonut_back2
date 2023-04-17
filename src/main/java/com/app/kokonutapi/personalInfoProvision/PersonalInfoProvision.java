package com.app.kokonutapi.personalInfoProvision;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Woody
 * LocalDateTime : 2022-11-01
 * Time :
 * Remark : 정보제공관리 Table Entity
 */
@Entity
@EqualsAndHashCode(of = "piId")
@Data
@NoArgsConstructor
@Table(name="kn_personal_info_provision")
public class PersonalInfoProvision implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 키
     */
    @Id
    @ApiModelProperty("키")
    @Column(name = "pi_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long piId;

    /**
     * 관리자 키 (=등록자)
     */
    @ApiModelProperty("관리자 키 (=등록자)")
    @Column(name = "admin_id", nullable = false)
    private Long adminId;

    /**
     * 관리번호
     */
    @ApiModelProperty("관리번호")
    @Column(name = "pi_number", nullable = false)
    private String piNumber;

    /**
     * 필요사유 (1: 서비스운영, 2: 이벤트/프로모션, 3: 제휴, 4: 광고/홍보)
     */
    @Column(name = "pi_reason", nullable = false)
    @ApiModelProperty("필요사유 (1: 서비스운영, 2: 이벤트/프로모션, 3: 제휴, 4: 광고/홍보)")
    private Integer piReason;

    /**
     * 항목유형 (1: 회원정보 전체 항목, 2: 개인 식별 정보를 포함한 일부 항목, 3: 개인 식별 정보를 포함하지 않는 일부 항목)
     */
    @Column(name = "pi_type", nullable = false)
    @ApiModelProperty("항목유형 (1: 회원정보 전체 항목, 2: 개인 식별 정보를 포함한 일부 항목, 3: 개인 식별 정보를 포함하지 않는 일부 항목)")
    private Integer piType;

    /**
     * 받는사람 유형 (1: 내부직원, 2: 제3자, 3: 본인, 4: 위수탁)
     */
    @Column(name = "pi_recipient_type", nullable = false)
    @ApiModelProperty("받는사람 유형 (1: 내부직원, 2: 제3자, 3: 본인, 4: 위수탁)")
    private Integer piRecipientType;

    /**
     * 정보제공 동의여부 (Y/N)
     */
    @ApiModelProperty("정보제공 동의여부 (Y/N)")
    @Column(name = "pi_agree_yn", nullable = false)
    private String piAgreeYn;

    /**
     * 정보제공 동의유형 (1: 고정필드, 2: 별도수집) (AGREE_YN 이 'Y'인 경우에만 저장)
     */
    @Column(name = "pi_agree_type")
    @ApiModelProperty("정보제공 동의유형 (1: 고정필드, 2: 별도수집) (AGREE_YN 이 'Y'인 경우에만 저장)")
    private Integer piAgreeType;

    /**
     * 목적
     */
    @ApiModelProperty("목적")
    @Column(name = "pi_purpose", nullable = false)
    private String piPurpose;

    /**
     * 태그
     */
    @ApiModelProperty("태그")
    @Column(name = "pi_tag", nullable = false)
    private String piTag;

    /**
     * 제공 시작일
     */
    @ApiModelProperty("제공 시작일")
    @Column(name = "pi_start_date", nullable = false)
    private LocalDateTime piStartDate;

    /**
     * 제공 만료일 (=제공 시작일+제공 기간)
     */
    @ApiModelProperty("제공 만료일 (=제공 시작일+제공 기간)")
    @Column(name = "pi_exp_date", nullable = false)
    private LocalDateTime piExpDate;

    /**
     * 보유기간 (사용후 즉시 삭제: IMMEDIATELY, 한달: MONTH, 일년: YEAR)
     */
    @Column(name = "pi_retention_period", nullable = false)
    @ApiModelProperty("보유기간 (사용후 즉시 삭제: IMMEDIATELY, 한달: MONTH, 일년: YEAR)")
    private String piRetentionPeriod;

    /**
     * 제공 항목 (컬럼 목록, 구분자: ',')
     */
    @Column(name = "pi_columns", nullable = false)
    @ApiModelProperty("제공 항목 (컬럼 목록, 구분자: ',')")
    private String piColumns;

    /**
     * 받는사람 이메일REASON
     */
    @ApiModelProperty("받는사람 이메일")
    @Column(name = "pi_recipient_email", nullable = false)
    private String piRecipientEmail;

    /**
     * 제공 대상 (키 목록, 구분자: ',')
     */
    @Column(name = "pi_targets", nullable = false)
    @ApiModelProperty("제공 대상 (키 목록, 구분자: ',')")
    private String piTargets;

    /**
     * 제공 대상 상태 (전체: ALL, 선택완료: SELETED)
     */
    @Column(name = "pi_target_status", nullable = false)
    @ApiModelProperty("제공 대상 상태 (전체: ALL, 선택완료: SELETED)")
    private String piTargetStatus;

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

    /**
     * 수정자 email
     */
    @ApiModelProperty("수정자 email")
    @Column(name = "modify_email")
    private String modify_email;

    /**
     * 수정 날짜
     */
    @ApiModelProperty("수정 날짜")
    @Column(name = "modify_date")
    private LocalDateTime modify_date;

}
