package com.app.kokonut.company.companytable;

import com.app.kokonut.company.companytable.dtos.CompanyTableSubListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-13
 * Time :
 * Remark : CompanyTableRepositoryCustomImpl 쿼리문 선언부
 */
@Repository
public class CompanyTableRepositoryCustomImpl extends QuerydslRepositorySupport implements CompanyTableRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public CompanyTableRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(CompanyTable.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    @Override
    public List<CompanyTableSubListDto> findByTableList(String cpCode) {

        QCompanyTable companyTable = QCompanyTable.companyTable;

        JPQLQuery<CompanyTableSubListDto> query = from(companyTable)
                .where(companyTable.cpCode.eq(cpCode))
                .select(Projections.constructor(CompanyTableSubListDto.class,
                        companyTable.ctName,
                        companyTable.ctDesignation
                ));

        query.orderBy(companyTable.insert_date.asc());

        return query.fetch();
    }

}
