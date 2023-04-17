package com.app.kokonut.bizMessage.friendtalkMessage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author Woody
 * Date : 2022-12-21
 * Time :
 * Remark : 친구톡 발송 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendtalkMessageSendDto {

    @NotBlank(message = "발송인 값은 필수 값입니다.")
    private List<FriendtalkMessageSendSubDto> friendtalkMessageSendSubDtoList; // 버튼타입 리스트 -> 기존의  프론트에선 recipients로 보내줌

    @NotBlank(message = "채널ID 값은 필수 값입니다.")
    private String kcChannelId; // 채널ID -> plusFriendId 랑 같은 의미

    @NotBlank(message = "메세지내용 값은 필수 값입니다.")
    private String messageContent; // 메세지내용 -> 기존의  프론트에선 content로 보내줌

    private MultipartFile multipartFile; // 업로드 이미지 -> 기존의  프론트에선 file로 보내줌

    private String transmitType; // 즉시발송 : immediate, 예약발송 : reservation -> transmitDateType에서 transmitType 으로변경

    private String reservationDate; // 예약발송 일경우 : 날짜 시간 분

    private Integer btnSize; // 버튼추가 개수

    private List<String> btnTypeList; // 버튼타입 리스트

    private List<String> btnNameList; // 버튼명 리스트

    private List<String> btnLink1List; // 버튼링크 1번 리스트 -> 앱링크 또는 웹링크를 선택했을 경우

    private List<String> btnLink2List; // 버튼링크 2번 리스트 -> 앱링크 또는 웹링크를 선택했을 경우

}
