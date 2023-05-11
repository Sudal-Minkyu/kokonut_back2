package com.app.kokonut.provision.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-05-10
 * Time :
 * Remark : Policy 리스트 검색 데이터 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvisionSearchDto {

    private LocalDateTime stimeStart;
    private LocalDateTime stimeEnd;

    private Long adminId;
    private String cpCode;

    private String searchText;

    private String filterDownload;
    private String filterState;

    public Integer getFilterDownload() {
        if(filterDownload.equals("N")) {
            return 0;
        } else if(filterDownload.equals("Y")) {
            return 1;
        } else {
            return 2;
        }
    }

    public String getFilterState() {
        if(filterState.equals("대기중")) {
            return "0";
        } else if(filterState.equals("제공중")) {
            return "1";
        } else if(filterState.equals("제공완료")) {
            return "2";
        } else {
            return "전체";
        }
    }

}
