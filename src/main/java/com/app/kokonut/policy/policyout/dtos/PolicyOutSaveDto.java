package com.app.kokonut.policy.policyout.dtos;

import lombok.Data;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : 개인정보처리방침의 처리업무의 위탁에 관한사항 네번째 뎁스 저장 데이터받는 Dto
 */
@Data
public class PolicyOutSaveDto {

    private Long pioId;

    private String pioOutsourcingCompany; // 수탁업체

    private String pioChose; // 필수/선택

    private String pioConsignmentCompany; // 위탁업무

    private String pioPeriod; // 처리및보유기간

}
