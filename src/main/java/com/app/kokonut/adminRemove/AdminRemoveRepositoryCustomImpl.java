package com.app.kokonut.adminRemove;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2022-11-29
 * Time :
 * Remark : AdminRemoveRepositoryCustom 쿼리문 선언부
 */
@Repository
public class AdminRemoveRepositoryCustomImpl extends QuerydslRepositorySupport implements AdminRemoveRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public AdminRemoveRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(AdminRemove.class);
        this.jpaResultMapper = jpaResultMapper;
    }

}
