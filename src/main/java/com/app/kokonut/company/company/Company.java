package com.app.kokonut.company.company;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    /**
     * 자동결제(1:자동결제안함, 2:첫결제신청, 3: 해제, 4:첫결제 이후 재결제, 6:강제해제)
     */
//    @Column(name = "cp_is_auto_pay")
//    @ApiModelProperty("자동결제(1:자동결제안함, 2:첫결제신청, 3: 해제, 4:첫결제 이후 재결제, 6:강제해제)")
//    private Integer cpIsAutoPay;

    @Column(name = "cp_receipt_id")
    @ApiModelProperty("카드(빌링키)")
    private String cpReceiptId;

    @Column(name = "cp_receipt_date")
    @ApiModelProperty("빌링키 발급 및 변경 일자")
    private LocalDateTime cpReceiptDate;

    @ApiModelProperty("자동결제 부과 시작일")
    @Column(name = "cp_valid_start")
    private LocalDateTime cpValidStart;

    @ApiModelProperty("자동결제 부과 종료일")
    @Column(name = "cp_valid_end")
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
