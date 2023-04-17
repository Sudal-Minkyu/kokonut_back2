package com.app.kokonut.bizMessage.alimtalkTemplate.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;

/**
 * @author Woody
 * Date : 2022-12-16
 * Time :
 * Remark : 알림톡 템플릿 신규저장 + 수정 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlimtalkTemplateSaveAndUpdateDto {

    private String kcChannelId; // 채널ID

    private String atTemplateCode; // 템플릿 코드

    private String atTemplateName; // 템플릿이름

    private String atMessageType; // 메세지 유형(BA:기본형, EX:부가정보형, AD:광고 추가형, MI:복합형)

    private String atExtraContent; // 부가 정보 내용

    private String atAdContent; // 광고 추가 내용

    private String atEmphasizeType; // 알림톡 강조표기 유형

    private String atEmphasizeTitle; // 알림톡 강조표시 제목

    private String atEmphasizeSubTitle; // 알림톡 강조표시 부제목

    private Integer securityFlag; // 보안설정여부

    private Integer btnSize; // 버튼추가 개수

    private List<String> btnTypeList; // 버튼타입 리스트

    private List<String> btnNameList; // 버튼명 리스트

    private List<String> btnLink1List; // 버튼링크 1번 리스트 -> 앱링크 또는 웹링크를 선택했을 경우

    private List<String> btnLink2List; // 버튼링크 2번 리스트 -> 앱링크 또는 웹링크를 선택했을 경우

}
