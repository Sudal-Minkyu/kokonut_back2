package com.app.kokonut.company.companysetting;

import com.app.kokonut.auth.dtos.CompanyEncryptDto;
import com.app.kokonut.company.company.Company;
import com.app.kokonut.company.company.CompanyRepositoryCustom;
import com.app.kokonut.company.company.QCompany;
import com.app.kokonut.company.companydatakey.QCompanyDataKey;
import com.app.kokonut.company.companysetting.dtos.CompanySettingInfoDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2023-06-05
 * Time :
 * Remark : CompanySettingRepositoryCustom 쿼리문 선언부
 */
@Repository
public class CompanySettingRepositoryCustomImpl extends QuerydslRepositorySupport implements CompanySettingRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public CompanySettingRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(CompanySetting.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    @Override
    public CompanySettingInfoDto findByCompanySettingInfo(String cpCode) {

        QCompanySetting companySetting = QCompanySetting.companySetting;

        JPQLQuery<CompanySettingInfoDto> query = from(companySetting)
                .where(companySetting.cpCode.eq(cpCode))
                .select(Projections.constructor(CompanySettingInfoDto.class,
                        companySetting.csId,
                        companySetting.csOverseasBlockSetting,
                        companySetting.csAccessSetting,
                        companySetting.csPasswordChangeSetting,
                        companySetting.csPasswordErrorCountSetting,
                        companySetting.csAutoLogoutSetting,
                        companySetting.csLongDisconnectionSetting
                ));

        return query.fetchOne();
    }

}
