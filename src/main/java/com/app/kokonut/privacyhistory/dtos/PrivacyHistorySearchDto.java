package com.app.kokonut.privacyhistory.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-05-15
 * Time :
 * Remark : PrivacyHistory 리스트 검색 데이터 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivacyHistorySearchDto {

    private LocalDateTime stimeStart;
    private LocalDateTime stimeEnd;

    private Long companyId;

    private String searchText;

    private String filterRole; // 선택한 권한
    private String filterState; // 선택한 처리내역

}
