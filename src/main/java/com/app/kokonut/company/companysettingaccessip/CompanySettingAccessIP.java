package com.app.kokonut.company.companysettingaccessip;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "csipId")
@Data
@NoArgsConstructor
@Table(name="kn_company_setting_accessip")
public class CompanySettingAccessIP {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "csip_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long csipId;

    @ApiModelProperty("셋팅 id")
    @Column(name = "cs_id")
    private Long csId;

    @ApiModelProperty("접속 허용할 공인IP")
    @Column(name = "csip_ip")
    private String csipIp;

    @ApiModelProperty("메모")
    @Column(name = "csip_remarks")
    private String csipRemarks;

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

}
