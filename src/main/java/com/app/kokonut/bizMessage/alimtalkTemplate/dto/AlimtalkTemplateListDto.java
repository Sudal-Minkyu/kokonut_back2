package com.app.kokonut.bizMessage.alimtalkTemplate.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Woody
 * Date : 2022-12-15
 * Time :
 * Remark : AlimtalkTemplate 리스트 조회 Dto
 * 사용 메서드 :
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlimtalkTemplateListDto {

    /**
     * 채널ID
     */
    @ApiModelProperty("채널ID")
    private String kcChannelId;


    /**
     * 템플릿 코드
     */
    @ApiModelProperty("템플릿 코드")
    private String atTemplateCode;


    /**
     * 템플릿 이름
     */
    @ApiModelProperty("템플릿 이름")
    private String atTemplateName;

    /**
     * 등록일시
     */
    @ApiModelProperty("등록일시")
    private LocalDateTime insert_date;

    /**
     * 상태: ACCEPT - 수락 REGISTER - 등록 INSPECT - 검수 중 COMPLETE - 완료 REJECT - 반려
     */
    @ApiModelProperty("상태: ACCEPT - 수락 REGISTER - 등록 INSPECT - 검수 중 COMPLETE - 완료 REJECT - 반려")
    private String atStatus;

}
