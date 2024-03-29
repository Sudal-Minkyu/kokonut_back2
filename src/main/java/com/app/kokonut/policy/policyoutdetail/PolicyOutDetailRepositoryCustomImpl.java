package com.app.kokonut.policy.policyoutdetail;

import com.app.kokonut.policy.policyoutdetail.dtos.PolicyOutDetailSaveInfoListDto;
import com.app.kokonut.policy.policyoutdetail.QPolicyOutDetail;
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
 * Date : 2023-04-25
 * Time :
 * Remark : PolicyOutDetailRepositoryCustom 쿼리문 선언부
 */
@Repository
public class PolicyOutDetailRepositoryCustomImpl extends QuerydslRepositorySupport implements PolicyOutDetailRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public PolicyOutDetailRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(PolicyOutDetail.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    public List<PolicyOutDetailSaveInfoListDto> findByPolicyOutDetailList(Long piId) {

        QPolicyOutDetail policyOutDetail = QPolicyOutDetail.policyOutDetail;

        JPQLQuery<PolicyOutDetailSaveInfoListDto> query = from(policyOutDetail)
            .where(policyOutDetail.piId.eq(piId))
            .select(Projections.constructor(PolicyOutDetailSaveInfoListDto.class,
                    policyOutDetail.piodId,
                    policyOutDetail.piodCompany,
                    policyOutDetail.piodLocation,
                    policyOutDetail.piodMethod,
                    policyOutDetail.piodContact,
                    policyOutDetail.piodInfo,
                    policyOutDetail.piodDetail,
                    policyOutDetail.piodPeriod
            ));

        return query.fetch();
    }

    public void findByPolicyOutDetailDelete(Long piId) {
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("DELETE FROM kn_privacy_policy_info_out_detail \n");
        sb.append("WHERE pi_id = ?1 \n");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, piId);

        query.executeUpdate();

    }

}
