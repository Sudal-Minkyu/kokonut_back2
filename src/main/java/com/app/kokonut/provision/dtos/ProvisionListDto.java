package com.app.kokonut.provision.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    private String proState; // "0" -> 대기중, "1" -> 제공중, "2" -> 제공완료, "3" -> 제공종료

    private String knName; // 제공자

    private LocalDateTime insert_date; // 만든 날짜

    private LocalDate proStartDate; // 제공 시작 기간

    private LocalDate proExpDate; // 제공 만료 기간

    private Long offerCount;

    private Long downloadCount;

    private String offerType; // "1" : 제공함, "2" : 제공받음, "3" : 제공/제공받음

    public String getInsert_date() {
        return DateTimeFormatter.ofPattern("yyyy. MM. dd").format(insert_date);
    }

    public String getProStartDate() {
        return DateTimeFormatter.ofPattern("yyyy.MM.dd").format(proStartDate);
    }

    public String getProExpDate() {
        return DateTimeFormatter.ofPattern("yyyy.MM.dd").format(proExpDate);
    }

    public String getProState() {
        if(proState.equals("0")) {
            return "대기중";
        } else if(proState.equals("1")) {
            return "제공중";
        } else if(proState.equals("2")) {
            return "제공완료";
        } else {
            return "제공종료";
        }
    }

    public String getOfferType() {
        if(proState.equals("1")) {
            return "제공함";
        } else if(proState.equals("2")) {
            return "제공받음";
        } else {
            return "제공/제공받음";
        }
    }

}
