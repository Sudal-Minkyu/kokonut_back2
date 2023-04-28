package com.app.kokonut.privacy.policy.policyout.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : 개인정보처리방침의 처리업무의 위탁에 관한사항 네번째 뎁스 저장 리스트 데이터받는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyOutSaveInfoListDto {

    private Long pioId;

    private String pioOutsourcingCompany; // 수탁업체

    private String pioChose; // 필수/선택

    private String pioConsignmentCompany; // 위탁업무

    private String pioPeriod; // 처리및보유기간

}
