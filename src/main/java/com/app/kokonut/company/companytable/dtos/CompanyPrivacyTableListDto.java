package com.app.kokonut.company.companytable.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-05-26
 * Time :
 * Remark : 개인정보검색용 테이블 리스트 호출 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyPrivacyTableListDto {

    private String ctName; // 테이블 일련번호

    private String ctDesignation;

    public String getCtName() {
        return ctName.substring(9);
    }

}
