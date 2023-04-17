package com.app.kokonut.refactor.payment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "payment")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 키
     */
    @Id
    @ApiModelProperty("키")
    @Column(name = "IDX", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    /**
     * COMPANY IDX
     */
    @Column(name = "COMPANY_IDX")
    @ApiModelProperty("COMPANY IDX")
    private Long companyId;

    /**
     * 결제자 키(관리자)
     */
    @Column(name = "ADMIN_IDX")
    @ApiModelProperty("결제자 키(관리자)")
    private Long adminId;

    /**
     * 결제UID
     */
    @ApiModelProperty("결제UID")
    @Column(name = "PAY_REQUEST_UID")
    private String payRequestUid;

    /**
     * 상점거래ID
     */
    @ApiModelProperty("상점거래ID")
    @Column(name = "MERCHANT_UID")
    private String merchantUid;

    /**
     * 결제번호
     */
    @Column(name = "IMP_UID")
    @ApiModelProperty("결제번호")
    private String impUid;

    /**
     * 고유거래번호
     */
    @Column(name = "PG_TID")
    @ApiModelProperty("고유거래번호")
    private String pgTid;

    /**
     * 상품(PREMIUM, STANDARD, PREMIUM)
     */
    @Column(name = "SERVICE")
    @ApiModelProperty("상품(PREMIUM, STANDARD, PREMIUM)")
    private String service;

    /**
     * 요금부과기간 시작일
     */
    @Column(name = "VALID_START")
    @ApiModelProperty("요금부과기간 시작일")
    private Date validStart;

    /**
     * 요금부과기간 종료일
     */
    @Column(name = "VALID_END")
    @ApiModelProperty("요금부과기간 종료일")
    private Date validEnd;

    /**
     * 기준 회원수
     */
    @ApiModelProperty("기준 회원수")
    @Column(name = "USER_COUNT")
    private Integer userCount;

    /**
     * 결제금액
     */
    @Column(name = "AMOUNT")
    @ApiModelProperty("결제금액")
    private Integer amount;

    /**
     * 상태(0:결제오류,1:결제완료)
     */
    @Column(name = "STATE")
    @ApiModelProperty("상태(0:결제오류,1:결제완료)")
    private Integer state;

    /**
     * 카드이름
     */
    @ApiModelProperty("카드이름")
    @Column(name = "CARD_NAME")
    private String cardName;

    /**
     * 카드번호
     */
    @ApiModelProperty("카드번호")
    @Column(name = "CARD_NUMBER")
    private String cardNumber;

    /**
     * 결제방법(AUTO_CARD:자동결제, FEE_CALCULATE:요금정산, FAIL : 결제실패)
     */
    @Column(name = "PAY_METHOD")
    @ApiModelProperty("결제방법(AUTO_CARD:자동결제, FEE_CALCULATE:요금정산, FAIL : 결제실패)")
    private String payMethod;

    /**
     * 거래전표 URL
     */
    @Column(name = "RECEIPT_URL")
    @ApiModelProperty("거래전표 URL")
    private String receiptUrl;

    /**
     * 환불신청상태
     */
    @ApiModelProperty("환불신청상태")
    @Column(name = "IS_APPLY_REFUND")
    private String applyRefund;

    /**
     * 환불신청날짜
     */
    @ApiModelProperty("환불신청날짜")
    @Column(name = "REFUND_APPLY_DATE")
    private Date refundApplyDate;

    /**
     * 환불상태
     */
    @ApiModelProperty("환불상태")
    @Column(name = "REFUND_STATE")
    private String refundState;

    /**
     * 환불사유
     */
    @ApiModelProperty("환불사유")
    @Column(name = "REFUND_REASON")
    private String refundReason;

    /**
     * 환불날짜
     */
    @ApiModelProperty("환불날짜")
    @Column(name = "REFUND_DATE")
    private Date refundDate;

    /**
     * 결제일시
     */
    @ApiModelProperty("결제일시")
    @Column(name = "REGDATE", nullable = false)
    private Date regdate;

}
