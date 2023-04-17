package com.app.kokonut.totalDBDownloadHistory.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalDbDownloadHistoryDto {

    private Long tdhId;

    private Long tdId;

    private Integer tdh_file_name;

    private String tdh_reason;

}
