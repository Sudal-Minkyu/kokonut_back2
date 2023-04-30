package com.app.kokonut.policy.policyoutdetail.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : 개인정보처리방침의 처리업무의 국외 위탁에 관한사항 네번째 뎁스 저장 리스트 데이터받는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyOutDetailSaveInfoListDto {

    private Long piodId;

    private String piodCompany; // 수탁업체

    private String piodLocation; // 수탁업체 위치

    private String piodMethod; // 위탁일시 및 방법

    private String piodContact; // 책임자연락처

    private String piodInfo; // 위탁하는 개인정보 항목

    private String piodDetail; // 위탁 업무 내용

    private String piodPeriod; // 위탁 보유및이용기간

}
