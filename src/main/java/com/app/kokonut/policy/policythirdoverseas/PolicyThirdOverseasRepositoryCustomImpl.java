package com.app.kokonut.policy.policythirdoverseas;

import com.app.kokonut.policy.policythirdoverseas.dtos.PolicyThirdOverseasSaveInfoListDto;
import com.app.kokonut.policy.policythirdoverseas.QPolicyThirdOverseas;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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

    public void findByPolicyThirdOverseasDelete(Long piId) {
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("DELETE FROM kn_privacy_policy_info_third_overseas \n");
        sb.append("WHERE pi_id = ?1 \n");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, piId);

        query.executeUpdate();

    }

}
