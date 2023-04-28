package com.app.kokonut.privacy.policy.policyafter.dtos;

import lombok.Data;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : 개인정보처리방침의 가입후 수집항목 세번째 뎁스 저장 데이터받는 Dto
 */
@Data
public class PolicyAfterSaveDto {

    private Long piaId;

    private String piaPurpose; // 가입후 처리목적

    private String piaInfo; // 수집항목

    private String piaChose; // 필수/선택

    private String piaPeriod; // 처리및보유기간

}
