package com.app.kokonut.policy.policyserviceauto;

import com.app.kokonut.policy.policyserviceauto.dtos.PolicyServiceAutoSaveInfoListDto;
import com.app.kokonut.policy.policyserviceauto.QPolicyServiceAuto;
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
public class PolicyServiceAutoRepositoryCustomImpl extends QuerydslRepositorySupport implements PolicyServiceAutoRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public PolicyServiceAutoRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(PolicyServiceAuto.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    public List<PolicyServiceAutoSaveInfoListDto> findByPolicyServiceAutoList(Long piId) {
        QPolicyServiceAuto policyServiceAuto = QPolicyServiceAuto.policyServiceAuto;

        JPQLQuery<PolicyServiceAutoSaveInfoListDto> query = from(policyServiceAuto)
            .where(policyServiceAuto.piId.eq(piId))
            .select(Projections.constructor(PolicyServiceAutoSaveInfoListDto.class,
                    policyServiceAuto.pisaId,
                    policyServiceAuto.pisaPurpose,
                    policyServiceAuto.pisaInfo,
                    policyServiceAuto.pisaMethodology,
                    policyServiceAuto.pisaPeriod
            ));

        return query.fetch();
    }


}
