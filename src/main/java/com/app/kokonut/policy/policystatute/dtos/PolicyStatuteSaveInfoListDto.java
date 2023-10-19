package com.app.kokonut.policy.policystatute.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : 개인정보처리방침의 이용중 자동 수집항목 세번째 뎁스 저장 리스트 데이터받는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyStatuteSaveInfoListDto {

    private Long pistId;

    private String pisaTitle; // 수집항목

    private String pisaContents; // 근거법

    private String pistPeriod; // 보유기간

    private Boolean pistCheck; // 체크여부 : 0 : 미체크, 1 : 체크함

}
