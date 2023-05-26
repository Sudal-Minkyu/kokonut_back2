package com.app.kokonut.company.companytable;

import com.app.kokonut.company.companytable.dtos.CompanyPrivacyTableListDto;
import com.app.kokonut.company.companytable.dtos.CompanyTableListDto;
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
    public List<CompanyTableListDto> findByTableList(String cpCode) {

        QCompanyTable companyTable = QCompanyTable.companyTable;

        JPQLQuery<CompanyTableListDto> query = from(companyTable)
                .where(companyTable.cpCode.eq(cpCode))
                .select(Projections.constructor(CompanyTableListDto.class,
                        companyTable.ctName,
                        companyTable.ctDesignation
                ));

        query.orderBy(companyTable.insert_date.asc());

        return query.fetch();
    }

    @Override
    public List<CompanyPrivacyTableListDto> findByPrivacyTableList(String cpCode) {

        QCompanyTable companyTable = QCompanyTable.companyTable;

        JPQLQuery<CompanyPrivacyTableListDto> query = from(companyTable)
                .where(companyTable.cpCode.eq(cpCode))
                .select(Projections.constructor(CompanyPrivacyTableListDto.class,
                        companyTable.ctName,
                        companyTable.ctDesignation
                ));

        query.orderBy(companyTable.insert_date.asc());

        return query.fetch();
    }

}
