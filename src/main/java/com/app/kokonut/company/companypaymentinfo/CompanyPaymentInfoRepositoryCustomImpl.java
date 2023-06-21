package com.app.kokonut.company.companypaymentinfo;

import com.app.kokonut.company.companypayment.QCompanyPayment;
import com.app.kokonut.company.companypaymentinfo.dtos.CompanyPaymentInfoDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2023-06-14
 * Time :
 * Remark : CompanyPaymentInfoRepositoryCustom 쿼리문 선언부
 */
@Repository
public class CompanyPaymentInfoRepositoryCustomImpl extends QuerydslRepositorySupport implements CompanyPaymentInfoRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public CompanyPaymentInfoRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(CompanyPaymentInfo.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    @Override
    public CompanyPaymentInfoDto findByCompanyPaymentInfo(Long cpiId) {

        QCompanyPayment companyPayment = QCompanyPayment.companyPayment;
        QCompanyPaymentInfo companyPaymentInfo = QCompanyPaymentInfo.companyPaymentInfo;

        JPQLQuery<CompanyPaymentInfoDto> query = from(companyPaymentInfo)
                .where(companyPaymentInfo.cpiId.eq(cpiId))
                .innerJoin(companyPayment).on(companyPayment.cpiId.eq(companyPaymentInfo.cpiId))
                .select(Projections.constructor(CompanyPaymentInfoDto.class,
                        companyPayment.cpiPayType,
                        companyPaymentInfo.cpiInfoCardName
                ));

        return query.fetchOne();
    }

}
