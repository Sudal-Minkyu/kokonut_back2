package com.app.kokonut.company.companypaymentinfo;

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
 * Remark : 카드빌링 정보 Table Entity
 */
@Entity
@EqualsAndHashCode(of = "cpiInfoId")
@Data
@NoArgsConstructor
@Table(name="kn_company_payment_info")
public class CompanyPaymentInfo {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "cpi_info_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cpiInfoId;

    @ApiModelProperty("빌링테이블 주키")
    @Column(name = "cpi_id")
    private Long cpiId;

    @ApiModelProperty("카드사명(부트페이제공 데이터)")
    @Column(name = "cpi_info_card_name")
    private String cpiInfoCardName;

    @ApiModelProperty("마스킹처리된 카드번호(부트페이제공 데이터)")
    @Column(name = "cpi_info_card_no")
    private String cpiInfoCardNo;

    @ApiModelProperty("카드종류 0:신용카드, 1:체크카드(부트페이제공 데이터)")
    @Column(name = "cpi_info_card_type")
    private String cpiInfoCardType;

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
