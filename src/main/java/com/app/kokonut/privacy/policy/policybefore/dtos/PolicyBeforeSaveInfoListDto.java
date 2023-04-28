package com.app.kokonut.privacy.policy.policybefore.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : 개인정보처리방침의 가입 시 수집하는 개인정보 세번째 뎁스 저장 리스트 데이터받는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyBeforeSaveInfoListDto {

    private Long pibId;

    private String pibPurpose; // 가입시 처리목적

    private String pibInfo; // 수집항목

    private String pibChose; // 필수/선택

    private String pibPeriod; // 처리및보유기간

}
