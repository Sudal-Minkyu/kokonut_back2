package com.app.kokonut.privacy.policy.policyout;

import com.app.kokonut.privacy.policy.policyout.dtos.PolicyOutSaveInfoListDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : 기존의 코코넛 프로젝트의 PolicyOut Sql 쿼리호출
 */
public interface PolicyOutRepositoryCustom {

    List<PolicyOutSaveInfoListDto> findByPolicyOutList(Long piId);

}