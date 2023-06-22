package com.app.kokonut.payment.paymenterror;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-06-22
 * Time :
 * Remark : 결제시도 에러건 카운팅 Table Entity
 */
@Entity
@EqualsAndHashCode(of = "peId")
@Data
@NoArgsConstructor
@Table(name="kn_payment_error")
public class PaymentError {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "pe_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long peId;

    @ApiModelProperty("결제데이터 주키")
    @Column(name = "pay_id")
    private Long payId;

    @ApiModelProperty("'0' : 결제오류, '1' : 결제완료")
    @Column(name = "pe_state")
    private String peState;

    @ApiModelProperty("결제 시도횟수")
    @Column(name = "pe_count")
    private Integer peCount;

    @ApiModelProperty("첫 결제시도 날짜")
    @Column(name = "insert_date")
    private LocalDateTime insert_date;

    @ApiModelProperty("최근 결제시도 날짜")
    @Column(name = "modify_date")
    private LocalDateTime modify_date;

}
