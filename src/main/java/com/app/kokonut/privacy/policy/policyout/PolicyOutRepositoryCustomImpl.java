package com.app.kokonut.privacy.policy.policyout;

import com.app.kokonut.privacy.policy.policyout.dtos.PolicyOutSaveInfoListDto;
import com.app.kokonut.privacy.policy.policyoutdetail.PolicyOutDetail;
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
 * Remark : PolicyOutRepositoryCustom 쿼리문 선언부
 */
@Repository
public class PolicyOutRepositoryCustomImpl extends QuerydslRepositorySupport implements PolicyOutRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public PolicyOutRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(PolicyOutDetail.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    public  List<PolicyOutSaveInfoListDto> findByPolicyOutList(Long piId) {
        QPolicyOut policyOut = QPolicyOut.policyOut;

        JPQLQuery<PolicyOutSaveInfoListDto> query = from(policyOut)
            .where(policyOut.piId.eq(piId))
            .select(Projections.constructor(PolicyOutSaveInfoListDto.class,
                    policyOut.pioId,
                    policyOut.pioOutsourcingCompany,
                    policyOut.pioChose,
                    policyOut.pioConsignmentCompany,
                    policyOut.pioPeriod
            ));

        return query.fetch();
    }


}
