package com.app.kokonut.policy.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Woody
 * Date : 2023-05-01
 * Time :
 * Remark : Policy 상세보기 조회 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyDetailDto {

    private String piVersion; // 개정본 버전

    private LocalDateTime modify_date; // 개정일
    
    private String piDate; // 시행일
    
    private String piHeader; // 머리말 기업명

    private String knName;

    private String piChoseListString; // ',' 구분자 -> 기본항목의 법령에 따른 개인정보의 보유기간 체크항목 순서

    private String piYear; // 시행일자 년

    private String piMonth; // 시행일자 월

    private String piDay; // 시행일자 일

    public String getModify_date() {
        return DateTimeFormatter.ofPattern("yyyy. MM. dd").format(modify_date);
    }

//    public String getPiDate() {
//        return piDate.replaceAll("-",". ");
//    }

}
