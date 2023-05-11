package com.app.kokonut.provision.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Woody
 * Date : 2023-05-10
 * Time :
 * Remark : Provision 리스트 조회 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvisionListDto {

    private String proCode;

    private String proState; // "0" -> 대기중, "1" -> 제공중, "2" -> 제공완료

    private String knName; // 제공자

    private LocalDateTime insert_date; // 만든 날짜

    private LocalDateTime proStartDate; // 제공 시작 기간

    private LocalDateTime proExpDate; // 제공 만료 기간

    private Integer proDownloadYn;

    private Long offerCount;

    private Long downloadCount;

    public String getProDownloadYn() {
        if(proDownloadYn == 0) {
            return "N";
        } else {
            return "Y";
        }
    }

    public String getKnName() {
        StringBuilder stars = new StringBuilder();
        int namelength = knName.length()-2;
        for(int i=0; i<namelength; i++) {
            stars.append("*");
        }
        return knName.substring(0,1)+stars+knName.substring(knName.length()-1);
    }

    public String getInsert_date() {
        return DateTimeFormatter.ofPattern("yyyy. MM. dd").format(insert_date);
    }

    public String getProStartDate() {
        return DateTimeFormatter.ofPattern("yyyy.MM.dd").format(proStartDate);
    }

    public String getProExpDate() {
        return DateTimeFormatter.ofPattern("yyyy.MM.dd").format(proExpDate);
    }

}