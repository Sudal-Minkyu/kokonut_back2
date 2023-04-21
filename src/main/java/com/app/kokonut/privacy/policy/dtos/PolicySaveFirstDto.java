package com.app.kokonut.privacy.policy.dtos;

import com.app.kokonut.privacy.policy.policypurpose.dtos.PolicyPurposeSaveDto;
import lombok.Data;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-21
 * Time :
 * Remark : 개인정보처리방침 제작 첫번째 뎁스 데이터받는 Dto
 */
@Data
public class PolicySaveFirstDto {

    private Double piVersion; // 개정본 버전

    private String piDate; // 개정일

    private String piHeader; // 머리말 기업명

    private List<PolicyPurposeSaveDto> policyPurposeSaveDtoList; // 개인정보처리목적의 리스트 데이터

}
