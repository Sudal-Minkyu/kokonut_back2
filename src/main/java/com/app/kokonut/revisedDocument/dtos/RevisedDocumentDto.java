package com.app.kokonut.revisedDocument.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Joy
 * Date : 2023-01-14
 * Time :
 * Remark : 기본 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevisedDocumentDto implements Serializable {

    @ApiModelProperty("키")
    private Long rdId;

    @ApiModelProperty("회사(Company) 키")
    private Long companyId;

    @ApiModelProperty("시행시작일자")
    private LocalDateTime rdEnforceStartDate;

    @ApiModelProperty("시행종료일자")
    private LocalDateTime rdEnforceEndDate;

    @ApiModelProperty("등록자")
    private Long adminId;

    @ApiModelProperty("등록자이름")
    private String knName;

    @ApiModelProperty("등록일자")
    private LocalDateTime insert_date;

}
