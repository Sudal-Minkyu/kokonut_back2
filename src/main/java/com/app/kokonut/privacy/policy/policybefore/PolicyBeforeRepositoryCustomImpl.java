package com.app.kokonut.privacy.policy.policybefore;

import com.app.kokonut.privacy.policy.policybefore.dtos.PolicyBeforeSaveInfoListDto;
import com.app.kokonut.privacy.policy.policypurpose.PolicyPurpose;
import com.app.kokonut.privacy.policy.policypurpose.PolicyPurposeRepositoryCustom;
import com.app.kokonut.privacy.policy.policypurpose.QPolicyPurpose;
import com.app.kokonut.privacy.policy.policypurpose.dtos.PolicyPurposeSaveInfoListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-25
 * Time :
 * Remark : PolicyBeforeRepositoryCustom 쿼리문 선언부
 */
@Repository
public class PolicyBeforeRepositoryCustomImpl extends QuerydslRepositorySupport implements PolicyBeforeRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public PolicyBeforeRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(PolicyBefore.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    public List<PolicyBeforeSaveInfoListDto> findByPolicyBeforeList(Long piId) {
        QPolicyBefore policyBefore = QPolicyBefore.policyBefore;

        JPQLQuery<PolicyBeforeSaveInfoListDto> query = from(policyBefore)
            .where(policyBefore.piId.eq(piId))
            .select(Projections.constructor(PolicyBeforeSaveInfoListDto.class,
                    policyBefore.pibId,
                    policyBefore.pibPurpose,
                    policyBefore.pibInfo,
                    policyBefore.pibChose,
                    policyBefore.pibPeriod
            ));
        return query.fetch();
    }


}
