package com.app.kokonut.company.companytablecolumninfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "ctciId")
@Data
@NoArgsConstructor
@Table(name="kn_company_table_column_info")
public class CompanyTableColumnInfo {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "ctci_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ctciId;

    @ApiModelProperty("테이블명(= 회사코드+_+테이블 순번)")
    @Column(name = "ct_name")
    private String ctName;

    @ApiModelProperty("고유코드")
    @Column(name = "ctci_code")
    private String ctciCode;

    @ApiModelProperty("암호화여부 -> 0:비암호화, 1:암호화, 2:휴대전화번호, 3:이메일 등...")
    @Column(name = "ctci_securiy")
    private String ctciSecuriy;

    @ApiModelProperty("등록자 email")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("등록 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}
