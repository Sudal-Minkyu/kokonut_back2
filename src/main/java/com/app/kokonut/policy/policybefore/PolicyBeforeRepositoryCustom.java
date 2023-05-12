package com.app.kokonut.policy.policybefore;

import com.app.kokonut.policy.policybefore.dtos.PolicyBeforeSaveInfoListDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : PolicyBefore Sql 쿼리호출
 */
public interface PolicyBeforeRepositoryCustom {

    List<PolicyBeforeSaveInfoListDto> findByPolicyBeforeList(Long piId);

}