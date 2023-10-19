package com.app.kokonut.policy.policystatute;

import com.app.kokonut.policy.policystatute.dtos.PolicyStatuteSaveInfoListDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-10-16
 * Time :
 * Remark : PolicyStatute Sql 쿼리호출
 */
public interface PolicyStatuteRepositoryCustom {

    List<PolicyStatuteSaveInfoListDto> findByPolicyStatuteList(Long piId);

}