package com.app.kokonut.history.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Woody
 * Date : 2022-11-03
 * Time :
 * Remark : ActivityHistory 리스트 검색 데이터 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorySearchDto {

    private LocalDateTime stimeStart;
    private LocalDateTime stimeEnd;

    private Long companyId;

    private String searchText;
    private List<ActivityCode> activityCodeList; // 선택한 활동


}
