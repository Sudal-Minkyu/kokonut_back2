package com.app.kokonut.company.companyitem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of="ciId")
@Data
@NoArgsConstructor
@Table(name="kn_company_item")
public class CompanyItem {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "ci_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ciId;

    @ApiModelProperty("회사코드")
    @Column(name = "cp_code")
    private String cpCode;

    @ApiModelProperty("추가항목명")
    @Column(name = "ci_name")
    private String ciName;

    @ApiModelProperty("암호화여부 -> 0: 비암호화, 1 : 암호화")
    @Column(name = "ci_security")
    private Integer ciSecurity;

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
