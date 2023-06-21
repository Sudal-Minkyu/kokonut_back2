package com.app.kokonut.company.company;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2022-12-22
 * Time :
 * Remark : 기업 Table Entity
 */
@Entity
@EqualsAndHashCode(of = "companyId")
@Data
@NoArgsConstructor
@Table(name="kn_company")
public class Company {

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

    @ApiModelProperty("전자상거래법 대상 여부 : 0(여부묻기필요), 1(전자상거래법대상), 2(1년간안보이기)")
    @Column(name = "cp_electronic")
    private Integer cpElectronic;

    @ApiModelProperty("전자상거랩 1년후 다시 등장")
    @Column(name = "cp_electronic_date")
    private LocalDate cpElectronicDate;

    @ApiModelProperty("빌링테이블 주키")
    @Column(name = "cpi_id")
    private Long cpiId;

    @ApiModelProperty("구독상태 : '0' : 가입하고 카드빌링하지않음, '1' : 구독중, '2' : 구독해지")
    @Column(name = "cp_subscribe")
    private String cpSubscribe;

    @ApiModelProperty("'구독해지 날짜'")
    @Column(name = "cp_subscribe_date")
    private LocalDateTime cpSubscribeDate;

    @ApiModelProperty("등록자 email")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("등록 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

    @ApiModelProperty("수정자 email")
    @Column(name = "modify_email")
    private String modify_email;

    @ApiModelProperty("수정 날짜")
    @Column(name = "modify_date")
    private LocalDateTime modify_date;

}
