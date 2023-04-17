package com.app.kokonut.bizMessage.kakaoChannel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2022-12-15
 * Time :
 * Remark : 카카오채널 channelId 리스트 가져오는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoChannelByChannelIdListDto {
    private String kcChannelId;
}
