package com.app.kokonut.bizMessage.kakaoChannel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2022-12-15
 * Time :
 * Remark : 카카오 채널 리스트 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoChannelListDto {

    private Long kcId;

    private Long companyId;

    private String kcChannelId;

    private String kcChannelName;

    private String kcStatus;

    private LocalDateTime insert_date;

    // 회사명 호출
    private String companyName;
}
