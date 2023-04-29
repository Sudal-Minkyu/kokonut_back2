package com.app.kokonut.privacy.policy.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Woody
 * Date : 2023-04-25
 * Time :
 * Remark : 개인정보처리방침 제작 첫번째 뎁스 저장된 데이터받는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyInfoDto {

    private Double piVersion; // 개정본 버전

    private String piDate; // 개정일

    private String piHeader; // 머리말 기업명

//    public String getPiDate() {
//        if(piDate != null) {
//            return DateTimeFormatter.ofPattern("yyyy. MM. dd").format(piDate);
//        } else {
//            return "";
//        }
//    }
}
