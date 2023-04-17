package com.app.kokonut.bizMessage.alimtalkTemplate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author Woody
 * Date : 2022-12-19
 * Time :
 * Remark : 알림톡 템플릿 삭제 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlimtalkTemplateDeleteDto {

    @NotBlank(message = "채널ID는 필수 값입니다.")
    private String kcChannelId; // 채널ID

    @NotBlank(message = "템플릿코드는 필수 값입니다.")
    private String atTemplateCode; // 템플릿 코드

}
