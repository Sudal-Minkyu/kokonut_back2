package com.app.kokonut.company.company;

import com.app.kokonut.auth.dtos.CompanyEncryptDto;
import com.app.kokonut.company.companydatakey.QCompanyDataKey;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2022-12-22
 * Time :
 * Remark : CompanyRepositoryCustom 쿼리문 선언부
 */
@Repository
public class CompanyRepositoryCustomImpl extends QuerydslRepositorySupport implements CompanyRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public CompanyRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(Company.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    @Override
    public CompanyEncryptDto findByDataKey(Long companyId) {

        QCompany company = QCompany.company;
        QCompanyDataKey companyDataKey = QCompanyDataKey.companyDataKey;

        JPQLQuery<CompanyEncryptDto> query = from(company)
                .where(company.companyId.eq(companyId))
                .innerJoin(companyDataKey).on(companyDataKey.cpCode.eq(company.cpCode))
                .select(Projections.constructor(CompanyEncryptDto.class,
                        company.cpCode,
                        companyDataKey.dataKey
                ));

        return query.fetchOne();
    }


}
