package com.app.kokonut.policy.policystatute;

import com.app.kokonut.policy.policystatute.dtos.PolicyStatuteSaveInfoListDto;
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
 * Remark : PolicyServiceAutoRepositoryCustom 쿼리문 선언부
 */
@Repository
public class PolicyStatuteRepositoryCustomImpl extends QuerydslRepositorySupport implements PolicyStatuteRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public PolicyStatuteRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(PolicyStatute.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    public List<PolicyStatuteSaveInfoListDto> findByPolicyStatuteList(Long piId) {
        QPolicyStatute policyStatute = QPolicyStatute.policyStatute;

        JPQLQuery<PolicyStatuteSaveInfoListDto> query = from(policyStatute)
            .where(policyStatute.piId.eq(piId))
            .select(Projections.constructor(PolicyStatuteSaveInfoListDto.class,
                    policyStatute.pistId,
                    policyStatute.pisaTitle,
                    policyStatute.pisaContents,
                    policyStatute.pistPeriod,
                    policyStatute.pistCheck
            ));

        return query.fetch();
    }


}
