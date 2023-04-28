package com.app.kokonut.privacy.policy.policyresponsible.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : 개인정보처리방침의 가입 후 수집하는 개인정보 세번째 뎁스 저장 리스트 데이터받는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyResponsibleSaveInfoListDto {

    private Long pirId;

    private String pirName; // 성명

    private String pirPosition; // 직책

    private String pirEmail; // 이메일

    private String pirContact; // 연락처

    private String pirDepartment; // 담당부서

}
