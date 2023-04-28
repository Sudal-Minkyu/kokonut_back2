package com.app.kokonut.company.companyitem;

import com.app.kokonut.company.companyitem.dtos.CompanyItemListDto;
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
 * Remark : CompanyItemRepositoryCustom 쿼리문 선언부
 */
@Repository
public class CompanyItemRepositoryCustomImpl extends QuerydslRepositorySupport implements CompanyItemRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public CompanyItemRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(CompanyItem.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    @Override
    public List<CompanyItemListDto> findByItemList(String cpCode) {

        QCompanyItem companyItem = QCompanyItem.companyItem;

        JPQLQuery<CompanyItemListDto> query = from(companyItem)
                .where(companyItem.cpCode.eq(cpCode))
                .select(Projections.constructor(CompanyItemListDto.class,
                        companyItem.ciId,
                        companyItem.ciName,
                        companyItem.ciSecurity
                ));

        return query.fetch();
    }


}
