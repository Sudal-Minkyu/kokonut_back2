package com.app.kokonut.policy.policyout;

import com.app.kokonut.policy.policyout.dtos.PolicyOutSaveInfoListDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : PolicyOut Sql 쿼리호출
 */
public interface PolicyOutRepositoryCustom {

    List<PolicyOutSaveInfoListDto> findByPolicyOutList(Long piId);

}