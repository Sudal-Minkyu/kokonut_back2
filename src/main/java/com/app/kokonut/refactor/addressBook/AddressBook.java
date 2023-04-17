package com.app.kokonut.refactor.addressBook;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@EqualsAndHashCode(of = "abId")
@Data
@NoArgsConstructor
@Table(name="kn_address_book")
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ab_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long abId;

    /**
     * 회사 키(주소록 보는 권한이 개인이면 삭제해도 되는 컬럼)
     */
    @Column(name = "company_id")
    @ApiModelProperty("회사 키(주소록 보는 권한이 개인이면 삭제해도 되는 컬럼)")
    private Integer companyId;

    /**
     * 관리자 키
     */
    @ApiModelProperty("관리자 키")
    @Column(name = "admin_id")
    private Long adminId;

    /**
     * IP
     */
    @Column(name = "ab_ip_addr")
    @ApiModelProperty("접속 IP 주소")
    private String abIpAddr;

    /**
     * 만료일시
     */
    @ApiModelProperty("만료일시")
    @Column(name = "ab_exp_date", nullable = false)
    private LocalDateTime abExpDate;

    /**
     * 발송여부(Y/N)
     */
    @Column(name = "ab_is_sended")
    @ApiModelProperty("발송여부(Y/N)")
    private String abIsSended;

    /**
     * 발송일
     */
    @ApiModelProperty("발송일")
    @Column(name = "ab_send_date")
    private LocalDateTime abSendDate;

    /**
     * 주소록 용도
     */
    @Column(name = "ab_use")
    @ApiModelProperty("주소록 용도")
    private String abUse;

    /**
     * 발송목적(NOTICE: 주요공지, AD:광고/홍보)
     */
    @Column(name = "ab_purpose")
    @ApiModelProperty("발송목적(NOTICE: 주요공지, AD:광고/홍보)")
    private String abPurpose;

    /**
     * 발송대상(ALL: 전체회원, SELECTED: 선택회원)
     */
    @Column(name = "ab_target")
    @ApiModelProperty("발송대상(ALL: 전체회원, SELECTED: 선택회원)")
    private String abTarget;

    /**
     * 메시지종류(EMAIL: 이메일, alimTalk ALIMTALK: 알림톡)
     */
    @Column(name = "abType")
    @ApiModelProperty("메시지종류(EMAIL: 이메일, alimTalk ALIMTALK: 알림톡)")
    private String abType;

    /**
     * 발신자 이메일
     */
    @ApiModelProperty("발신자 이메일")
    @Column(name = "ab_sender_email")
    private String abSenderEmail;

    /**
     * 메시지 제목
     */
    @Column(name = "ab_title")
    @ApiModelProperty("메시지 제목")
    private String abTitle;

    /**
     * 메시지 내용
     */
    @Column(name = "ab_content")
    @ApiModelProperty("메시지 내용")
    private String abContent;

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
     * 수정자 이름
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
