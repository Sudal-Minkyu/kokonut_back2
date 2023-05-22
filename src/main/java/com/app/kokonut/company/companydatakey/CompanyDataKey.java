package com.app.kokonut.company.companydatakey;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@EqualsAndHashCode(of = "cdId")
@Data
@NoArgsConstructor
@Table(name="kn_company_datakey")
public class CompanyDataKey {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "cd_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cdId;

    @ApiModelProperty("사업자코드")
    @Column(name = "cp_code")
    private String cpCode;

    @Column(name = "data_key")
    @ApiModelProperty("암호화에 사용될 데이터 키")
    private String dataKey;

    @Column(name = "iv_key")
    @ApiModelProperty("해석에 사용될 데이터 키")
    private String ivKey;

}
