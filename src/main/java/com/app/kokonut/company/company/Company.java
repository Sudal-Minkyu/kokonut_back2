package com.app.kokonut.company.company;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "companyId")
@Data
@NoArgsConstructor
@Table(name="kn_company")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ApiModelProperty("주키")
    @Column(name = "company_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    @ApiModelProperty("회사코드")
    @Column(name = "cp_code")
    private String cpCode;

    @ApiModelProperty("회사명")
    @Column(name = "cp_name")
    private String cpName;


    @ApiModelProperty("추가한 테이블 수의 카운트")
    @Column(name = "cp_table_count")
    private Integer cpTableCount;

    /**
     * 결제일(5일,10일 등 일자)
     */
    @Column(name = "cp_pay_day")
    @ApiModelProperty("결제일(5일,10일 등 일자)")
    private Integer cpPayDay;

    /**
     * 결제등록일
     */
    @Column(name = "cp_pay_date")
    @ApiModelProperty("결제등록일")
    private LocalDateTime cpPayDate;

    /**
     * 자동결제(1:자동결제안함, 2:첫결제신청, 3: 해제, 4:첫결제 이후 재결제, 6:강제해제)
     */
    @Column(name = "cp_is_auto_pay")
    @ApiModelProperty("자동결제(1:자동결제안함, 2:첫결제신청, 3: 해제, 4:첫결제 이후 재결제, 6:강제해제)")
    private Integer cpIsAutoPay;

    /**
     * 카드(빌링키)와 1:1로 대응하는 값
     */
    @Column(name = "cp_billing_key")
    @ApiModelProperty("카드(빌링키)와 1:1로 대응하는 값")
    private String cpBillingKey;

    /**
     * 서비스 결제 X 강제 해지시 결제 안한 금액
     */
    @Column(name = "cp_stop_service_price")
    @ApiModelProperty("서비스 결제 X 강제 해지시 결제 안한 금액")
    private Integer cpStopServicePrice;

    /**
     * 자동결제 해지일시
     */
    @ApiModelProperty("자동결제 해지일시")
    @Column(name = "cp_not_auto_pay_date")
    private LocalDateTime cpNotAutoPayDate;

    /**
     * 회원권 시작일
     */
    @ApiModelProperty("회원권 시작일")
    @Column(name = "cp_valid_start")
    private LocalDateTime cpValidStart;

    /**
     * 회원권 종료일
     */
    @Column(name = "cp_valid_end")
    @ApiModelProperty("회원권 종료일")
    private LocalDateTime cpValidEnd;

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
