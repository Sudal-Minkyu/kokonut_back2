package com.app.kokonut.privacy.policy.policyafter;

import com.app.kokonut.privacy.policy.policyafter.dtos.PolicyAfterSaveInfoListDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : 기존의 코코넛 프로젝트의 PolicyAfter Sql 쿼리호출
 */
public interface PolicyAfterRepositoryCustom {

    List<PolicyAfterSaveInfoListDto> findByPolicyAfterList(Long piId);

}