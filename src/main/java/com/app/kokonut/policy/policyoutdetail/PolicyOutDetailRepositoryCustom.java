package com.app.kokonut.policy.policyoutdetail;

import com.app.kokonut.policy.policyoutdetail.dtos.PolicyOutDetailSaveInfoListDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : PolicyOutDetail Sql 쿼리호출
 */
public interface PolicyOutDetailRepositoryCustom {

    List<PolicyOutDetailSaveInfoListDto> findByPolicyOutDetailList(Long piId);

    void findByPolicyOutDetailDelete(Long piId);
}