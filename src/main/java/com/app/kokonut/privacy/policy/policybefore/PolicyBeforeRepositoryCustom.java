package com.app.kokonut.privacy.policy.policybefore;

import com.app.kokonut.privacy.policy.policybefore.dtos.PolicyBeforeSaveInfoListDto;
import com.app.kokonut.privacy.policy.policypurpose.dtos.PolicyPurposeSaveInfoListDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : 기존의 코코넛 프로젝트의 PolicyBefore Sql 쿼리호출
 */
public interface PolicyBeforeRepositoryCustom {

    List<PolicyBeforeSaveInfoListDto> findByPolicyBeforeList(Long piId);

}