package com.app.kokonut.policy.dtos;

import com.app.kokonut.policy.policypurpose.dtos.PolicyPurposeSaveDto;
import lombok.Data;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-25
 * Time :
 * Remark : 개인정보처리방침 제작 두번째 뎁스 데이터받는 Dto
 */
@Data
public class PolicySaveSecondDto {

    private Long piId; // 개인정보처리방침 ID

    private List<PolicyPurposeSaveDto> policyPurposeSaveDtoList; // 개인정보처리목적 저장 리스트값

    private List<Long> policyPurposeDeleteIdList; // 개인정보처리목적 삭제 Id 리스트값

}
