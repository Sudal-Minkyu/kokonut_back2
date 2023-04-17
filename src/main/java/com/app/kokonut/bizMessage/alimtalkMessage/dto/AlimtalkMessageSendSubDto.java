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
 * Remark : 알림톡 메시지 발송인정보 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlimtalkMessageSendSubDto {

    private String email; // 유저이메일

    private String phoneNumber; // 유저번호

    private String userName; // 유저명

}
