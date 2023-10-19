package com.app.kokonut.policy.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-04-30
 * Time :
 * Remark : 개인정보처리방침 제작 세번째 뎁스 저장된 데이터받는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicySecondInfoDto {

    private String piStatute; // ',' 구분자 -> 기본항목의 법령에 따른 개인정보의 보유기간 체크항목 순서

}
