package com.app.kokonut.company.companytablecolumninfo;

import com.app.kokonut.company.companytablecolumninfo.dtos.CompanyTableColumnInfoCheckList;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-05-26
 * Time :
 * Remark : CompanyTableColumnInfoRepositoryCustomImpl 쿼리문 선언부
 */
@Repository
public class CompanyTableColumnInfoRepositoryCustomImpl extends QuerydslRepositorySupport implements CompanyTableColumnInfoRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public CompanyTableColumnInfoRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(CompanyTableColumnInfo.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    @Override
    public List<CompanyTableColumnInfoCheckList> findByCheckList(List<String> codes) {

        QCompanyTableColumnInfo companyTableColumnInfo = QCompanyTableColumnInfo.companyTableColumnInfo;

        JPQLQuery<CompanyTableColumnInfoCheckList> query = from(companyTableColumnInfo)
                .where(companyTableColumnInfo.ctciCode.in(codes))
                .select(Projections.constructor(CompanyTableColumnInfoCheckList.class,
                        companyTableColumnInfo.ctciCode,
                        companyTableColumnInfo.ctciSecuriy
                ));

        return query.fetch();
    }

}
