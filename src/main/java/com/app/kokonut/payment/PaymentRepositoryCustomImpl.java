package com.app.kokonut.payment;

import com.app.kokonut.admin.QAdmin;
import com.app.kokonut.common.realcomponent.Utils;
import com.app.kokonut.policy.Policy;
import com.app.kokonut.policy.PolicyRepositoryCustom;
import com.app.kokonut.policy.QPolicy;
import com.app.kokonut.policy.dtos.*;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * @author Woody
 * Date : 2023-06-07
 * Time :
 * Remark : PaymentRepositoryCustom 쿼리문 선언부
 */
@Repository
public class PaymentRepositoryCustomImpl extends QuerydslRepositorySupport implements PaymentRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public PaymentRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(Payment.class);
        this.jpaResultMapper = jpaResultMapper;
    }

}
