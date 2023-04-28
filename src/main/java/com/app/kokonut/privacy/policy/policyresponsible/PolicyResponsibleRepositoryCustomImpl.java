package com.app.kokonut.privacy.policy.policyresponsible;

import com.app.kokonut.privacy.policy.policyresponsible.dtos.PolicyResponsibleSaveInfoListDto;
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
 * Remark : PolicyResponsibleRepositoryCustom 쿼리문 선언부
 */
@Repository
public class PolicyResponsibleRepositoryCustomImpl extends QuerydslRepositorySupport implements PolicyResponsibleRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public PolicyResponsibleRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(PolicyResponsible.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    public List<PolicyResponsibleSaveInfoListDto> findByPolicyResponsibleList(Long piId) {
        QPolicyResponsible policyResponsible = QPolicyResponsible.policyResponsible;

        JPQLQuery<PolicyResponsibleSaveInfoListDto> query = from(policyResponsible)
            .where(policyResponsible.piId.eq(piId))
            .select(Projections.constructor(PolicyResponsibleSaveInfoListDto.class,
                    policyResponsible.pirId,
                    policyResponsible.pirName,
                    policyResponsible.pirPosition,
                    policyResponsible.pirEmail,
                    policyResponsible.pirContact,
                    policyResponsible.pirDepartment
            ));

        return query.fetch();
    }


}
