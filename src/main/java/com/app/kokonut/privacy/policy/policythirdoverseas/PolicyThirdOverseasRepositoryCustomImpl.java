package com.app.kokonut.privacy.policy.policythirdoverseas;

import com.app.kokonut.privacy.policy.policythirdoverseas.dtos.PolicyThirdOverseasSaveInfoListDto;
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
 * Remark : PolicyAfterRepositoryCustom 쿼리문 선언부
 */
@Repository
public class PolicyThirdOverseasRepositoryCustomImpl extends QuerydslRepositorySupport implements PolicyThirdOverseasRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public PolicyThirdOverseasRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(PolicyThirdOverseas.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    public List<PolicyThirdOverseasSaveInfoListDto> findByPolicyThirdOverseasList(Long piId) {
        QPolicyThirdOverseas policyThirdOverseas = QPolicyThirdOverseas.policyThirdOverseas;

        JPQLQuery<PolicyThirdOverseasSaveInfoListDto> query = from(policyThirdOverseas)
            .where(policyThirdOverseas.piId.eq(piId))
            .select(Projections.constructor(PolicyThirdOverseasSaveInfoListDto.class,
                    policyThirdOverseas.pitoId,
                    policyThirdOverseas.pitoRecipient,
                    policyThirdOverseas.pitoLocation,
                    policyThirdOverseas.pitoPurpose,
                    policyThirdOverseas.pitoInfo,
                    policyThirdOverseas.pitoPeriod
            ));

        return query.fetch();
    }


}
