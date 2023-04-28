package com.app.kokonut.privacy.policy;

import com.app.kokonut.privacy.policy.dtos.PolicyInfoDto;
import com.app.kokonut.privacy.policy.dtos.PolicyWritingCheckDto;

/**
 * @author Woody
 * Date : 2023-04-21
 * Time :
 * Remark : 기존의 코코넛 프로젝트의 Policy Sql 쿼리호출
 */
public interface PolicyRepositoryCustom {

    PolicyInfoDto findByPiId(Long piId);

    PolicyWritingCheckDto findByWiring(String cpCode, String email);

}