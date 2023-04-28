package com.app.kokonut.privacy.policy.policyoutdetail;

import com.app.kokonut.privacy.policy.policyoutdetail.dtos.PolicyOutDetailSaveInfoListDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : 기존의 코코넛 프로젝트의 PolicyOutDetail Sql 쿼리호출
 */
public interface PolicyOutDetailRepositoryCustom {

    List<PolicyOutDetailSaveInfoListDto> findByPolicyOutDetailList(Long piId);

}