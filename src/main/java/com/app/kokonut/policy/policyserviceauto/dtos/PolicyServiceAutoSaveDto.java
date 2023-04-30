package com.app.kokonut.policy.policyserviceauto.dtos;

import lombok.Data;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : 개인정보처리방침의 이용중 자동 수집항목 세번째 뎁스 저장 데이터받는 Dto
 */
@Data
public class PolicyServiceAutoSaveDto {

    private Long pisaId;

    private String pisaPurpose; // 처리목적

    private String pisaInfo; // 수집항목

    private String pisaMethodology; // 수집방법

    private String pisaPeriod; // 처리및보유기간

}
