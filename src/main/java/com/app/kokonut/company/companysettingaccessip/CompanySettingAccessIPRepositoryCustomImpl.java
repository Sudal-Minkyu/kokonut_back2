package com.app.kokonut.company.companysettingaccessip;

import com.app.kokonut.company.companysettingaccessip.dtos.CompanySettingAccessIPListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-06-05
 * Time :
 * Remark : CompanySettingAccessIPRepositoryCustom 쿼리문 선언부
 */
@Repository
public class CompanySettingAccessIPRepositoryCustomImpl extends QuerydslRepositorySupport implements CompanySettingAccessIPRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public CompanySettingAccessIPRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(CompanySettingAccessIP.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    @Override
    public List<CompanySettingAccessIPListDto> findByCompanySettingIPList(Long csId) {

        QCompanySettingAccessIP companySettingAccessIP = QCompanySettingAccessIP.companySettingAccessIP;

        JPQLQuery<CompanySettingAccessIPListDto> query = from(companySettingAccessIP)
                .where(companySettingAccessIP.csId.eq(csId))
                .select(Projections.constructor(CompanySettingAccessIPListDto.class,
                        companySettingAccessIP.csipIp,
                        companySettingAccessIP.csipRemarks
                ));

        return query.fetch();
    }

}
