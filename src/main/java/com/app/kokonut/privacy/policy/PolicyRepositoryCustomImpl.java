package com.app.kokonut.privacy.policy;

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

}
