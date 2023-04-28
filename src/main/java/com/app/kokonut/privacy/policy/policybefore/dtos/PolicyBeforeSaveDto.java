package com.app.kokonut.privacy.policy.policybefore.dtos;

import lombok.Data;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : 개인정보처리방침의 가입시 수집항목 세번째 뎁스 저장 데이터받는 Dto
 */
@Data
public class PolicyBeforeSaveDto {

    private Long pibId;

    private String pibPurpose; // 가입시 처리목적

    private String pibInfo; // 수집항목

    private String pibChose; // 필수/선택

    private String pibPeriod; // 처리및보유기간

}
