package com.app.kokonut.bizMessage.kakaoChannel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2022-12-15
 * Time :
 * Remark : 카카오 채널 검색 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoChannelSearchDto {

    private String kcStatus; // 채널 상태

    private String kcChannelName; // 채널명

    private LocalDateTime stimeStart; // 시작 날짜

    private LocalDateTime stimeEnd; // 끝 날짜

}
