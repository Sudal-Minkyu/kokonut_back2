package com.app.kokonut.privacy.policy.policyafter;

import com.app.kokonut.privacy.policy.policyafter.dtos.PolicyAfterSaveInfoListDto;
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
public class PolicyAfterRepositoryCustomImpl extends QuerydslRepositorySupport implements PolicyAfterRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public PolicyAfterRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(PolicyAfter.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    public List<PolicyAfterSaveInfoListDto> findByPolicyAfterList(Long piId) {
        QPolicyAfter policyAfter = QPolicyAfter.policyAfter;

        JPQLQuery<PolicyAfterSaveInfoListDto> query = from(policyAfter)
            .where(policyAfter.piId.eq(piId))
            .select(Projections.constructor(PolicyAfterSaveInfoListDto.class,
                    policyAfter.piaId,
                    policyAfter.piaPurpose,
                    policyAfter.piaInfo,
                    policyAfter.piaChose,
                    policyAfter.piaPeriod
            ));

        return query.fetch();
    }


}
