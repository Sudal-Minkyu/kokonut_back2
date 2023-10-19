package com.app.kokonut.policy;

import com.app.kokonut.policy.dtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Woody
 * Date : 2023-04-21
 * Time :
 * Remark : Policy Sql 쿼리호출
 */
public interface PolicyRepositoryCustom {

    PolicyFirstInfoDto findByPolicyFirst(Long piId);

    PolicySecondInfoDto findByPolicySecond(Long piId);

    PolicyThirdInfoDto findByPolicyThird(Long piId);

    PolicyWritingCheckDto findByWriting(String cpCode, String email);

    Page<PolicyListDto> findByPolicyList(PolicySearchDto policySearchDto, Pageable pageable);

    PolicyDetailDto findByPolicyDetail(Long piId, String cpCode);

}