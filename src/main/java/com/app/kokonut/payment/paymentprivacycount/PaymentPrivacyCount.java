package com.app.kokonut.payment.paymentprivacycount;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author Woody
 * Date : 2023-06-15
 * Time :
 * Remark : 일일 개인정보 수 카운팅 Table Entity
 */
@Entity
@EqualsAndHashCode(of = "ppcId")
@Data
@NoArgsConstructor
@Table(name="kn_payment_privacy_count")
public class PaymentPrivacyCount {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "ppc_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ppcId;

    @ApiModelProperty("회사코드")
    @Column(name = "cp_code")
    private String cpCode;

    @ApiModelProperty("테이블명(= 회사코드+'_'+테이블 순번)")
    @Column(name = "ct_name")
    private String ctName;

    @ApiModelProperty("금일 개인정보 수")
    @Column(name = "ppc_count")
    private Integer ppcCount;

    @ApiModelProperty("금일 날짜")
    @Column(name = "ppc_date")
    private LocalDate ppcDate;

}
