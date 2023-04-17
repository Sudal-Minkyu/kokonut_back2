package com.app.kokonut.company.companydatakey;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@EqualsAndHashCode(of = "cdId")
@Data
@NoArgsConstructor
@Table(name="kn_company_datakey")
public class CompanyDataKey implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 주키
     */
    @Id
    @ApiModelProperty("주키")
    @Column(name = "cd_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cdId;

    /**
     * 사업자코드
     */
    @ApiModelProperty("사업자코드")
    @Column(name = "cp_code")
    private String cpCode;

    /**
     * 암호화에 사용될 데이터 키
     */
    @Column(name = "data_key")
    @ApiModelProperty("암호화에 사용될 데이터 키")
    private String dataKey;

}
