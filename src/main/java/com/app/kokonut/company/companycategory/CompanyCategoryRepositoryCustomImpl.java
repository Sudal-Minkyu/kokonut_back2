package com.app.kokonut.company.companycategory;

import com.app.kokonut.company.companycategory.dtos.CompanyCategoryListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-11
 * Time :
 * Remark : CompanyCategoryRepositoryCustom 쿼리문 선언부
 */
@Repository
public class CompanyCategoryRepositoryCustomImpl extends QuerydslRepositorySupport implements CompanyCategoryRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public CompanyCategoryRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(CompanyCategory.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    @Override
    public List<CompanyCategoryListDto> findByCategoryList(String cpCode) {

        QCompanyCategory companyCategory = QCompanyCategory.companyCategory;

        JPQLQuery<CompanyCategoryListDto> query = from(companyCategory)
                .where(companyCategory.cpCode.eq(cpCode))
                .select(Projections.constructor(CompanyCategoryListDto.class,
                        companyCategory.ccName,
                        companyCategory.ccSecurity
                ));

        return query.fetch();
    }


}
