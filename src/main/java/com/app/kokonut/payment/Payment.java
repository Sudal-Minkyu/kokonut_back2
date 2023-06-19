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

    @ApiModelProperty("회사코드")
    @Column(name = "cp_code")
    private String cpCode;

    @ApiModelProperty("결제금액")
    @Column(name = "pay_amount")
    private Integer payAmount;

    @ApiModelProperty("상태(0:결제오류, 1:결제완료, 2:결제예약중)")
    @Column(name = "pay_state")
    private String payState;

    @ApiModelProperty("결제방법(0:자동결제, 1:요금정산, 2:결제실패)")
    @Column(name = "pay_method")
    private String payMethod;

    @ApiModelProperty("결제일 기준 개인정보 평균수")
    @Column(name = "pay_privacy_count")
    private String payPrivacyCount;

    @ApiModelProperty("결제예약 취소 시 필요한 키")
    @Column(name = "pay_reserve_id")
    private String payReserveId;

    @ApiModelProperty("결제예약 실행시간")
    @Column(name = "pay_reserve_execute_date")
    private LocalDateTime payReserveExecuteDate;

    @ApiModelProperty("요금부과 기간 시작 날짜")
    @Column(name = "pay_billing_start_date")
    private LocalDate payBillingStartDate;

    @ApiModelProperty("요금부과 기간 끝 날짜")
    @Column(name = "pay_billing_end_date")
    private LocalDate payBillingEndDate;

    @ApiModelProperty("결제자 email")
    @Column(name = "insert_email")
    private String insert_email;

    @ApiModelProperty("결제일시")
    @Column(name = "insert_date")
    private LocalDateTime insert_date;

}
