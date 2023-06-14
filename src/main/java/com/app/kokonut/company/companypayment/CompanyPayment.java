package com.app.kokonut.company.companypayment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-06-14
 * Time :
 * Remark : 기업 카드빌링 Table Entity
 */
@Entity
@EqualsAndHashCode(of="cpiId")
@Data
@NoArgsConstructor
@Table(name="kn_company_payment")
public class CompanyPayment {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "cpi_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cpiId;

    @ApiModelProperty("회사코드")
    @Column(name = "cp_code")
    private String cpCode;

    @ApiModelProperty("카드(빌링키)")
    @Column(name = "cpi_billing_key")
    private String cpiBillingKey;

    @ApiModelProperty("빌링키 발급 일자(부트페이제공 데이터)")
    @Column(name = "cpi_billing_date")
    private LocalDateTime cpiBillingDate;

    @ApiModelProperty("빌링키 만료 일자 (부트페이제공 데이터)")
    @Column(name = "cpi_billing_expire_date")
    private LocalDateTime cpiBillingExpireDate;

    @ApiModelProperty("빌링키 발급에 대한 부트페이 고유 영수증 ID")
    @Column(name = "cpi_receipt_id")
    private String cpiReceiptId;

    @ApiModelProperty("자동결제 고유ID(부트페이제공 데이터)")
    @Column(name = "cpi_subscription_id")
    private String cpiSubscriptionId;

    @ApiModelProperty("자동결제 부과 시작일")
    @Column(name = "cpi_valid_start")
    private LocalDateTime cpiValidStart;

    @ApiModelProperty("자동결제 부과 종료일")
    @Column(name = "cpi_valid_end")
    private LocalDateTime cpiValidEnd;

    @ApiModelProperty("결제타입 - '0' : 월 정기구독, '1' : 연 정기구독")
    @Column(name = "cpi_pay_type")
    private String cpiPayType;

    @ApiModelProperty("연 정기구독일 경우 만료날짜")
    @Column(name = "cpi_pay_expire_date")
    private LocalDateTime cpiPayExpireDate;

    @ApiModelProperty("등록자 email")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("등록 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

    @ApiModelProperty("변경자 email")
    @Column(name = "modify_email")
    private String modify_email;

    @ApiModelProperty("변경 날짜")
    @Column(name = "modify_date")
    private LocalDateTime modify_date;

}
