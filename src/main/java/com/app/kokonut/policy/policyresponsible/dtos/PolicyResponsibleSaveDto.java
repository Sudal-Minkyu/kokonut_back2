package com.app.kokonut.policy.policyresponsible.dtos;

import lombok.Data;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : 개인정보처리방침의 책임자항목 여섯번째 뎁스 저장 데이터받는 Dto
 */
@Data
public class PolicyResponsibleSaveDto {

    private Long pirId;

    private String pirName; // 성명

    private String pirPosition; // 직책

    private String pirEmail; // 이메일

    private String pirContact; // 연락처

    private String pirDepartment; // 담당부서

}
