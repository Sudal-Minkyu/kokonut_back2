package com.app.kokonut.privacy.policy.policyserviceauto;

import com.app.kokonut.privacy.policy.policypurpose.dtos.PolicyPurposeSaveInfoListDto;
import com.app.kokonut.privacy.policy.policyserviceauto.dtos.PolicyServiceAutoSaveInfoListDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : 기존의 코코넛 프로젝트의 PolicyServiceAuto Sql 쿼리호출
 */
public interface PolicyServiceAutoRepositoryCustom {

    List<PolicyServiceAutoSaveInfoListDto> findByPolicyServiceAutoList(Long piId);

}