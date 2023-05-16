package com.app.kokonut.policy.policypurpose;

import com.app.kokonut.policy.policypurpose.dtos.PolicyPurposeSaveInfoListDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-25
 * Time :
 * Remark : PolicyPurpose Sql 쿼리호출
 */
public interface PolicyPurposeRepositoryCustom {

    List<PolicyPurposeSaveInfoListDto> findByPolicyPurposeList(Long piId);

}