package com.app.kokonut.bizMessage.alimtalkMessage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author Woody
 * Date : 2022-12-20
 * Time :
 * Remark : 알림톡 발송 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlimtalkMessageSendDto {

    @NotBlank(message = "발송인 값은 필수 값입니다.")
    private List<AlimtalkMessageSendSubDto> alimtalkMessageSendSubDtoList; // 버튼타입 리스트 -> 기존의 프론트에선 recipients로 보내줌

    @NotBlank(message = "채널ID 값은 필수 값입니다.")
    private String kcChannelId; // 채널ID -> plusFriendId 랑 같은 의미

    @NotBlank(message = "템플릿코드 값은 필수 값입니다.")
    private String atTemplateCode; // 템플릿 코드

    private String templateContent; // 템플릿내용 -> 프론트에선 content 로 보내줌

    private String atMessageType; // 메세지 유형(BA:기본형, EX:부가정보형, AD:광고 추가형, MI:복합형)

    private String atExtraContent; // 부가 정보 내용

    private String atAdContent; // 광고 추가 내용

    private String atEmphasizeType; // 알림톡 강조표기 유형

    private String atEmphasizeTitle; // 알림톡 강조표시 제목

    private String atEmphasizeSubTitle; // 알림톡 강조표시 부제목

    private String amTransmitType; // 즉시발송 : immediate, 예약발송 : reservation -> transmitDateType에서 transmitType 으로변경

    private String amReservationDate; // 예약발송 일경우 : 날짜 시간 분

    private Integer btnSize; // 버튼추가 개수

    private List<String> btnTypeList; // 버튼타입 리스트

    private List<String> btnNameList; // 버튼명 리스트

    private List<String> btnLink1List; // 버튼링크 1번 리스트 -> 앱링크 또는 웹링크를 선택했을 경우

    private List<String> btnLink2List; // 버튼링크 2번 리스트 -> 앱링크 또는 웹링크를 선택했을 경우

}
