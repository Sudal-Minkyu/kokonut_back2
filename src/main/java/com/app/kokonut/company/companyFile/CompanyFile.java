package com.app.kokonut.company.companyFile;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "cfId")
@Data
@NoArgsConstructor
@Table(name="kn_company_file")
public class CompanyFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cf_id")
    private Long cfId;

    @ApiModelProperty("회사코드")
    @Column(name = "cp_code")
    private String cpCode;

    @ApiModelProperty("S3 파일 경로")
    @Column(name = "cf_path")
    private String cfPath;

    @ApiModelProperty("S3 파일 명")
    @Column(name = "cf_filename")
    private String cfFilename;

    @ApiModelProperty("원래 파일명")
    @Column(name = "cf_original_filename")
    private String cfOriginalFilename;

    @ApiModelProperty("용량")
    @Column(name = "cf_volume")
    private Long cfVolume;

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
     * 수정자 email
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
