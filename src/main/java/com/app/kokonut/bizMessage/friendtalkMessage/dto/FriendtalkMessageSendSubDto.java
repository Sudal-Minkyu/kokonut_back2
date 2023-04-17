package com.app.kokonut.bizMessage.friendtalkMessage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2022-12-21
 * Time :
 * Remark : 친구톡 메시지 발송인정보 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendtalkMessageSendSubDto {

    private String email; // 유저이메일

    private String phoneNumber; // 유저번호

    private String userName; // 유저명

}
