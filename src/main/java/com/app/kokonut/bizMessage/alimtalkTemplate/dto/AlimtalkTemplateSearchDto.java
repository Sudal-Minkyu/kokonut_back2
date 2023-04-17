package com.app.kokonut.bizMessage.alimtalkTemplate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2022-12-15
 * Time :
 * Remark : 알림톡 템플릿 검색 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlimtalkTemplateSearchDto {

    private String atStatus; // 검수 상태

    private String atTemplateName; // 템플릿명

    private LocalDateTime stimeStart; // 시작 날짜

    private LocalDateTime stimeEnd; // 끝 날짜

}
