package com.app.kokonut.policy.policythird;

import com.app.kokonut.policy.policythird.dtos.PolicyThirdSaveInfoListDto;
import com.app.kokonut.policy.policythird.QPolicyThird;
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

    public void findByPolicyThirdDelete(Long piId) {
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("DELETE FROM kn_privacy_policy_info_third \n");
        sb.append("WHERE pi_id = ?1 \n");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, piId);

        query.executeUpdate();

    }


}
