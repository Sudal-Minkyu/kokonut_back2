package com.app.kokonut.bizMessage.alimtalkTemplate.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

/**
 * @author Woody
 * Date : 2022-12-20
 * Time :
 * Remark : AlimtalkMessage의 템플릿 정보조회용 리스트 Dto
 * 사용 메서드 :
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlimtalkMessageTemplateInfoListDto {

    private String atTemplateCode;

    private String atMessageType;

    private String atExtraContent;

    private String atAdContent;

    private String atEmphasizeType;

    private String atEmphasizeTitle;

    private String atEmphasizeSubTitle;

}
