package com.app.kokonut.company.companycategory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of="ccId")
@Data
@NoArgsConstructor
@Table(name="kn_company_category")
public class CompanyCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ApiModelProperty("주키")
    @Column(name = "cc_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ccId;

    @ApiModelProperty("회사코드")
    @Column(name = "cp_code")
    private String cpCode;

    @ApiModelProperty("추가항목명")
    @Column(name = "cc_name")
    private String ccName;

    @ApiModelProperty("암호화여부 -> 0: 비암호화, 1 : 암호화")
    @Column(name = "cc_security")
    private String ccSecurity;

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
