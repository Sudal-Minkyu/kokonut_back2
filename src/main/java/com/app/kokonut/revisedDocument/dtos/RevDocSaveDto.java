package com.app.kokonut.revisedDocument.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Joy
 * Date : 2023-01-14
 * Time :
 * Remark : 개인정보 처리방침 - 처리방침 문서 조회, 저장 위한 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevDocSaveDto implements Serializable {

    @ApiModelProperty("시행시작일자")
    private LocalDateTime rdEnforceStartDate;

    @ApiModelProperty("시행종료일자")
    private LocalDateTime rdEnforceEndDate;

    private MultipartFile multipartFile;

}
