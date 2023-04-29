package com.app.kokonut.privacy.policy.policythird;

import com.app.kokonut.privacy.policy.policythird.dtos.PolicyThirdSaveInfoListDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : 기존의 코코넛 프로젝트의 PolicyThird Sql 쿼리호출
 */
public interface PolicyThirdRepositoryCustom {

    List<PolicyThirdSaveInfoListDto> findByPolicyThirdList(Long piId);

    void findByPolicyThirdDelete(Long piId);

}