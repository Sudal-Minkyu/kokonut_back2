package com.app.kokonut.thirdparty.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-04-27
 * Time :
 * Remark : 알림톡 서트파티 셋팅값 가져오기
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThirdPartyAlimTalkSettingDto {

    private String tsBizmReceiverNumCode; // receiver_num 설정 고유코드

    private String tsBizmAppUserIdCode; // app_user_id 설정 고유코드

}
