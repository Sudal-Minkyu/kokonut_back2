package com.app.kokonut.policy.policythirdoverseas;

import com.app.kokonut.policy.policythirdoverseas.dtos.PolicyThirdOverseasSaveInfoListDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : PolicyThirdOverseas Sql 쿼리호출
 */
public interface PolicyThirdOverseasRepositoryCustom {

    List<PolicyThirdOverseasSaveInfoListDto> findByPolicyThirdOverseasList(Long piId);

    void findByPolicyThirdOverseasDelete(Long piId);

}