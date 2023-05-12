package com.app.kokonut.policy.policyafter;

import com.app.kokonut.policy.policyafter.dtos.PolicyAfterSaveInfoListDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : PolicyAfter Sql 쿼리호출
 */
public interface PolicyAfterRepositoryCustom {

    List<PolicyAfterSaveInfoListDto> findByPolicyAfterList(Long piId);

}