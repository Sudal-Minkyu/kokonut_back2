package com.app.kokonut.alimtalk.dtos;

import lombok.Data;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-07-25
 * Time :
 * Remark : 알림톡 템플릿 정보반환 Dto
 */
@Data
public class AlimtalkTemplateInfoDto {

    private String result; // success or fail

    private List<String> variableList; // 메세지 변수리스트

    private String resultMessage; // 반환 메세지

}