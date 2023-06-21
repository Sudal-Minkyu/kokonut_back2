package com.app.kokonut.payment.paymentprivacycount;

import com.app.kokonut.payment.paymentprivacycount.dtos.PaymentPrivacyCountDayDto;
import com.app.kokonut.payment.paymentprivacycount.dtos.PaymentPrivacyCountMonthAverageDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Woody
 * Date : 2023-06-20
 * Time :
 * Remark : PaymentPrivacyCountRepositoryCustom 쿼리문 선언부
 */
@Repository
public class PaymentPrivacyCountRepositoryCustomImpl extends QuerydslRepositorySupport implements PaymentPrivacyCountRepositoryCustom {

    @Autowired
    JpaResultMapper jpaResultMapper;

    public PaymentPrivacyCountRepositoryCustomImpl() {
        super(PaymentPrivacyCount.class);
    }


    @Override
    public PaymentPrivacyCountMonthAverageDto findByMonthPrivacyCount(String cpCode, String ctName, LocalDate firstDate, LocalDate lastDate) {

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT MIN(privacyCnt.ppc_date) as lowDate, MAX(privacyCnt.ppc_date) as bigDate, CAST(sum(privacyCnt.ppc_count)/count(*) AS SIGNED) \n");
        sb.append("FROM kn_payment_privacy_count as privacyCnt \n");
        sb.append("WHERE privacyCnt.cp_code = ?1 \n");
        sb.append("AND privacyCnt.ct_name = ?2 \n");
        sb.append("AND privacyCnt.ppc_date >= ?3 \n");
        sb.append("AND privacyCnt.ppc_date <= ?4 \n");
        sb.append("GROUP BY privacyCnt.cp_code; \n");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, cpCode);
        query.setParameter(2, ctName);
        query.setParameter(3, firstDate);
        query.setParameter(4, lastDate);

        return jpaResultMapper.uniqueResult(query, PaymentPrivacyCountMonthAverageDto.class);
    }

    @Override
    public List<PaymentPrivacyCountDayDto> findByDayPrivacyCount(LocalDate firstDate, LocalDate lastDate) {

        QPaymentPrivacyCount paymentPrivacyCount = QPaymentPrivacyCount.paymentPrivacyCount;

        JPQLQuery<PaymentPrivacyCountDayDto> query = from(paymentPrivacyCount)
                .where(paymentPrivacyCount.ppcDate.goe(firstDate).and(paymentPrivacyCount.ppcDate.loe(lastDate)))
                .select(Projections.constructor(PaymentPrivacyCountDayDto.class,
                        paymentPrivacyCount.ppcDate,
                        paymentPrivacyCount.ppcCount
                ));

        query.orderBy(paymentPrivacyCount.ppcDate.asc());

        return query.fetch();
    }



}
