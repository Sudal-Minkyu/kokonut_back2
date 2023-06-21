package com.app.kokonut.payment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-06-07
 * Time :
 * Remark : 결제정보 내역 Table Entity
 */
@Entity
@EqualsAndHashCode(of = "payId")
@Data
@NoArgsConstructor
@Table(name="kn_payment")
public class Payment {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "pay_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payId;

    @ApiModelProperty("결제 orderid(고유코드)")
    @Column(name = "pay_orderid")
    private String payOrderid;

    @ApiModelProperty("자동결제가 실행된 이후 receipt_id(실행 전에는 값이 없음)")
    @Column(name = "pay_receiptid")
    private String payReceiptid;

    @ApiModelProperty("회사코드")
    @Column(name = "cp_code")
    private String cpCode;

    @ApiModelProperty("결제금액")
    @Column(name = "pay_amount")
    private Integer payAmount;

    @ApiModelProperty("상태(0:결제실패, 1:결제완료, 2:결제예약중)")
    @Column(name = "pay_state")
    private String payState;

    @ApiModelProperty("결제방법(0:자동결제, 1:요금정산)")
    @Column(name = "pay_method")
    private String payMethod;

    @ApiModelProperty("결제일 기준 개인정보 평균수")
    @Column(name = "pay_privacy_count")
    private Integer payPrivacyCount;

    @ApiModelProperty("요금부과 기간 시작 날짜")
    @Column(name = "pay_billing_start_date")
    private LocalDate payBillingStartDate;

    @ApiModelProperty("요금부과 기간 끝 날짜")
    @Column(name = "pay_billing_end_date")
    private LocalDate payBillingEndDate;

    @ApiModelProperty("결제예약 취소 시 필요한 키")
    @Column(name = "pay_reserve_id")
    private String payReserveId;

    @ApiModelProperty("결제예약 실행시간 또는 결제 일시")
    @Column(name = "pay_reserve_execute_date")
    private LocalDateTime payReserveExecuteDate;

    @ApiModelProperty("예약결제가 실행 시작된 시간(부트페이데이터)")
    @Column(name = "pay_reserve_started_date")
    private LocalDateTime payReserveStartedDate;

    @ApiModelProperty("예약결제가 실행이 완료된 시간(부트페이데이터)")
    @Column(name = "pay_reserve_finished_date")
    private LocalDateTime payReserveFinishedDate;
}
