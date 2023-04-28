package com.app.kokonut.privacy.policy.policypurpose;

import com.app.kokonut.privacy.policy.dtos.PolicyWritingCheckDto;
import com.app.kokonut.privacy.policy.policypurpose.dtos.PolicyPurposeSaveInfoListDto;
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
 * Remark : PolicyPurposeRepositoryCustom 쿼리문 선언부
 */
@Repository
public class PolicyPurposeRepositoryCustomImpl extends QuerydslRepositorySupport implements PolicyPurposeRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public PolicyPurposeRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(PolicyPurpose.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    public List<PolicyPurposeSaveInfoListDto> findByPolicyPurposeList(Long piId) {
        QPolicyPurpose policyPurpose = QPolicyPurpose.policyPurpose;

        JPQLQuery<PolicyPurposeSaveInfoListDto> query = from(policyPurpose)
            .where(policyPurpose.piId.eq(piId))
            .select(Projections.constructor(PolicyPurposeSaveInfoListDto.class,
                    policyPurpose.pipId,
                    policyPurpose.pipTitle,
                    policyPurpose.pipContent
            ));

        return query.fetch();
    }


}
