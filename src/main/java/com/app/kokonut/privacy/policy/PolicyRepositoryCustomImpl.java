package com.app.kokonut.privacy.policy;

import com.app.kokonut.privacy.policy.dtos.PolicyInfoDto;
import com.app.kokonut.privacy.policy.dtos.PolicyWritingCheckDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2023-04-21
 * Time :
 * Remark : PolicyRepositoryCustom 쿼리문 선언부
 */
@Repository
public class PolicyRepositoryCustomImpl extends QuerydslRepositorySupport implements PolicyRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public PolicyRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(Policy.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    public PolicyInfoDto findByPiId(Long piId) {
        QPolicy policy = QPolicy.policy;

        JPQLQuery<PolicyInfoDto> query = from(policy)
            .where(policy.piId.eq(piId))
            .select(Projections.constructor(PolicyInfoDto.class,
                policy.piVersion,
                policy.piDate,
                policy.piHeader
            ));
        return query.fetchOne();
    }

    public PolicyWritingCheckDto findByWiring(String cpCode, String email) {
        QPolicy policy = QPolicy.policy;

        JPQLQuery<PolicyWritingCheckDto> query = from(policy)
                .where(policy.cpCode.eq(cpCode).and(policy.insert_email.eq(email)).and(policy.piAutosave.eq(0)))
                .orderBy(policy.piId.desc()).limit(1)
                .select(Projections.constructor(PolicyWritingCheckDto.class,
                        policy.piId,
                        policy.piStage
                ));

        return query.fetchOne();
    }


}
