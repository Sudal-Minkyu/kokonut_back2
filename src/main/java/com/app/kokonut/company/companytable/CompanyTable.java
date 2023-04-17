package com.app.kokonut.company.companytable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "ctId")
@Data
@NoArgsConstructor
@Table(name="kn_company_table")
public class CompanyTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ApiModelProperty("주키")
    @Column(name = "ct_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ctId;

    @ApiModelProperty("회사코드")
    @Column(name = "cp_code")
    private String cpCode;

    @ApiModelProperty("테이블명(= 회사코드+_+테이블 순번)")
    @Column(name = "ct_name")
    private String ctName;

    @ApiModelProperty("테이블 순번 (=회사별 테이블 수의 카운트)")
    @Column(name = "ct_table_count")
    private String ctTableCount;

    @ApiModelProperty("테이블의 명칭")
    @Column(name = "ct_designation")
    private String ctDesignation;

    @ApiModelProperty("해당 추가한 컬럼 수의 카운트")
    @Column(name = "ct_add_column_count")
    private Integer ctAddColumnCount;

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
