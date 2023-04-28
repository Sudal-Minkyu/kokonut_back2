package com.app.kokonut.privacy.policy.policypurpose;

import com.app.kokonut.privacy.policy.policypurpose.dtos.PolicyPurposeSaveInfoListDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-25
 * Time :
 * Remark : 기존의 코코넛 프로젝트의 PolicyPurpose Sql 쿼리호출
 */
public interface PolicyPurposeRepositoryCustom {

    List<PolicyPurposeSaveInfoListDto> findByPolicyPurposeList(Long piId);

}