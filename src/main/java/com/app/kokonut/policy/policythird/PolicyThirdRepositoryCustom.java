package com.app.kokonut.policy.policythird;

import com.app.kokonut.policy.policythird.dtos.PolicyThirdSaveInfoListDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : PolicyThird Sql 쿼리호출
 */
public interface PolicyThirdRepositoryCustom {

    List<PolicyThirdSaveInfoListDto> findByPolicyThirdList(Long piId);

    void findByPolicyThirdDelete(Long piId);

}