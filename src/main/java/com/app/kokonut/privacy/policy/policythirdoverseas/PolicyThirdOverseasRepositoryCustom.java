package com.app.kokonut.privacy.policy.policythirdoverseas;

import com.app.kokonut.privacy.policy.policythirdoverseas.dtos.PolicyThirdOverseasSaveInfoListDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : 기존의 코코넛 프로젝트의 PolicyThirdOverseas Sql 쿼리호출
 */
public interface PolicyThirdOverseasRepositoryCustom {

    List<PolicyThirdOverseasSaveInfoListDto> findByPolicyThirdOverseasList(Long piId);

}