package com.app.kokonut.company.companytable;

import com.app.kokonut.company.companytable.dtos.CompanyPrivacyTableListDto;
import com.app.kokonut.company.companytable.dtos.CompanyTableListDto;
import com.app.kokonut.index.dtos.EncrypCountHistoryCountDto;
import com.app.kokonut.index.dtos.PrivacyItemCountDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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
    public PrivacyItemCountDto findByPrivacyItemSum(String cpCode) {
        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT \n");
        sb.append("IFNULL(SUM(cpTable.ct_add_column_security_count), 0) AS securityCount, \n");
        sb.append("IFNULL(SUM(cpTable.ct_add_column_unique_count), 0) AS uniqueCount, \n");
        sb.append("IFNULL(SUM(cpTable.ct_add_column_sensitive_count), 0) AS sensitiveCount \n");
        sb.append("FROM kn_company_table as cpTable \n");
        sb.append("WHERE cp_code = ?1 \n");
        sb.append("GROUP BY cpTable.cp_code; \n");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, cpCode);

        return jpaResultMapper.uniqueResult(query, PrivacyItemCountDto.class);
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
