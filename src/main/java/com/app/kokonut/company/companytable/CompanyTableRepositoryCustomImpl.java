package com.app.kokonut.company.companytable;

import com.app.kokonut.company.companytable.dtos.CompanyPrivacyTableListDto;
import com.app.kokonut.company.companytable.dtos.CompanyTableListDto;
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
        sb.append("sumTable.securityCount, sumTable.uniqueCount, sumTable.sensitiveCount, \n");
        sb.append("infoCount.count AS totalAddCount \n");
        sb.append("FROM \n");
        sb.append("(SELECT \n");
        sb.append("IFNULL(SUM(ct_add_column_security_count), 0) AS securityCount, \n");
        sb.append("IFNULL(SUM(ct_add_column_unique_count), 0) AS uniqueCount, \n");
        sb.append("IFNULL(SUM(ct_add_column_sensitive_count), 0) AS sensitiveCount, \n");
        sb.append("cp_code \n");
        sb.append("FROM kn_company_table \n");
        sb.append("WHERE cp_code = ?1 \n");
        sb.append("GROUP BY cp_code) AS sumTable \n");
        sb.append("JOIN \n");
        sb.append("(SELECT \n");
        sb.append("COUNT(ct_name) AS count, \n");
        sb.append("SUBSTRING(ct_name, 1, 16) AS code \n");
        sb.append("FROM kn_company_table_column_info \n");
        sb.append("WHERE ctci_name != 'ID_1_id' \n");
        sb.append("GROUP BY code) AS infoCount \n");
        sb.append("ON sumTable.cp_code = infoCount.code; \n");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, cpCode);

        List<PrivacyItemCountDto> results = jpaResultMapper.list(query, PrivacyItemCountDto.class);
        return results.isEmpty() ? null : results.get(0);
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
