package com.app.kokonut.provision.provisiondownloadhistory.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author Woody
 * Date : 2023-05-11
 * Time :
 * Remark : ProvisionDownloadHistory 리스트 조회 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvisionDownloadHistoryListDto {

    private LocalDateTime insert_date; // 만든 날짜

    private LocalDateTime insert_time; // 만든 시간

    private String knName; // 다운로드한사람

    public String getKnName() {
        StringBuilder stars = new StringBuilder();
        int namelength = knName.length()-2;
        stars.append("*".repeat(Math.max(0, namelength)));
        return knName.substring(0,1)+stars+knName.substring(knName.length()-1);
    }

    public String getInsert_date() {
        return DateTimeFormatter.ofPattern("yyyy. MM. dd").format(insert_date);
    }

    public String getInsert_time() {
        return DateTimeFormatter.ofPattern("a hh:mm", Locale.KOREAN).format(insert_time);
    }

}
