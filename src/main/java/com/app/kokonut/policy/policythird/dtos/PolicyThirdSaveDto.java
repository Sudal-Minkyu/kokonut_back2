package com.app.kokonut.policy.policythird.dtos;

import lombok.Data;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : 개인정보처리방침의 제3자 제공에 관한 사항 항목 다섯번째 뎁스 저장 데이터받는 Dto
 */
@Data
public class PolicyThirdSaveDto {

    private Long pitId;

    private String pitRecipient; // 제공받는 자

    private String pitPurpose; // 제공받는 목적

    private String pitInfo; // 제공받는 항목

    private String pitPeriod; // 제공받는자의 보유 및 이용기간

}
