package com.app.kokonut.policy.policyresponsible;

import com.app.kokonut.policy.policyresponsible.dtos.PolicyResponsibleSaveInfoListDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : PolicyResponsible Sql 쿼리호출
 */
public interface PolicyResponsibleRepositoryCustom {

    List<PolicyResponsibleSaveInfoListDto> findByPolicyResponsibleList(Long piId);

    void findByPolicyResponsibleDelete(Long piId);

}