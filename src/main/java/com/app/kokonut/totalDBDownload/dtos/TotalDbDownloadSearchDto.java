package com.app.kokonut.totalDBDownload.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-01-13
 * Time :
 * Remark : 회원 DB 다운로드 SearchDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalDbDownloadSearchDto {

    private Integer tdState; // 상태(1:다운로드요청, 2:다운로드대기, 3:다운로드완료, 4:다운로드반려)
    private LocalDateTime stimeStart;
    private LocalDateTime stimeEnd;

}
