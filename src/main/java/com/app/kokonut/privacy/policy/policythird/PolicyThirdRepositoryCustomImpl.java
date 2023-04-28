package com.app.kokonut.privacy.policy.policythird;

import com.app.kokonut.privacy.policy.policyafter.PolicyAfter;
import com.app.kokonut.privacy.policy.policyafter.PolicyAfterRepositoryCustom;
import com.app.kokonut.privacy.policy.policyafter.QPolicyAfter;
import com.app.kokonut.privacy.policy.policyafter.dtos.PolicyAfterSaveInfoListDto;
import com.app.kokonut.privacy.policy.policythird.dtos.PolicyThirdSaveInfoListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-26
 * Time :
 * Remark : PolicyThirdRepositoryCustom 쿼리문 선언부
 */
@Repository
public class PolicyThirdRepositoryCustomImpl extends QuerydslRepositorySupport implements PolicyThirdRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public PolicyThirdRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(PolicyThird.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    public List<PolicyThirdSaveInfoListDto> findByPolicyThirdList(Long piId) {

        QPolicyThird policyThird = QPolicyThird.policyThird;

        JPQLQuery<PolicyThirdSaveInfoListDto> query = from(policyThird)
            .where(policyThird.piId.eq(piId))
            .select(Projections.constructor(PolicyThirdSaveInfoListDto.class,
                    policyThird.pitId,
                    policyThird.pitRecipient,
                    policyThird.pitPurpose,
                    policyThird.pitInfo,
                    policyThird.pitPeriod
            ));

        return query.fetch();
    }


}
