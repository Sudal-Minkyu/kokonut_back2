package com.app.kokonutuser.dtos.use;

import lombok.Data;

/**
 * @author Woody
 * Date : 2023-07-26
 * Time :
 * Remark : 알림톡 발송할 대상의 리스트를 받아오는 Dto
 */
@Data
public class KokonutUserAlimTalkFieldDto {

    private final Object receiverNum; // receiver_num

    private final Object appUserId; // app_user_id

    public KokonutUserAlimTalkFieldDto(Object receiverNum, Object appUserId) {
        this.receiverNum = receiverNum;
        this.appUserId = appUserId;
    }

}
