package com.app.kokonut.bizMessage.friendtalkMessage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2022-12-20
 * Time :
 * Remark : 친구톡 메세지 리스트 검색 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendtalkMessageSearchDto {

    private String fmStatus; // 발송상태

    private String fmRequestId; // 요청ID

    private LocalDateTime stimeStart; // 시작 날짜

    private LocalDateTime stimeEnd; // 끝 날짜

}
