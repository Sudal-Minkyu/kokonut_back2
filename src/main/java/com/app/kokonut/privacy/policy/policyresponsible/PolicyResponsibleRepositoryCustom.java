package com.app.kokonut.privacy.policy.policyresponsible;

import com.app.kokonut.privacy.policy.policyresponsible.dtos.PolicyResponsibleSaveInfoListDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : 기존의 코코넛 프로젝트의 PolicyResponsible Sql 쿼리호출
 */
public interface PolicyResponsibleRepositoryCustom {

    List<PolicyResponsibleSaveInfoListDto> findByPolicyResponsibleList(Long piId);

}