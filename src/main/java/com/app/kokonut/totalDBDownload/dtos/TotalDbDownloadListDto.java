package com.app.kokonut.totalDBDownload.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Woody
 * Date : 2023-01-13
 * Time :
 * Remark : 회원 DB 다운로드 ListDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalDbDownloadListDto {

    private Long tdId;

    private String adminName; // 요청자 명

    private String tdReason; // 요청사유

    private LocalDateTime tdApplyDate; // 요청일자

//    private String link; // 다운로드 링크

    private Integer tdState; // 상태(1:다운로드요청, 2:다운로드 승인, 3:다운로드완료, 4:반려)

    private String tdReturnReason; // 반려사유

    private LocalDateTime tdDownloadDate; // 다운로드 일자

}
