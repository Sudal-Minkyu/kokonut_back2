package com.app.kokonut.bizMessage.alimtalkMessage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2022-12-19
 * Time :
 * Remark : 알림톡 메세지 검색 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlimtalkMessageSearchDto {

    private String amStatus; // 검수 상태

    private String searchText; // templateCode + requestId

    private LocalDateTime stimeStart; // 시작 날짜

    private LocalDateTime stimeEnd; // 끝 날짜

}
