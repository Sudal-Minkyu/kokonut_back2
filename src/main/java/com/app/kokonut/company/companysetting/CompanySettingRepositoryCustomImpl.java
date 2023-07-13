package com.app.kokonut.company.companysetting;

import com.app.kokonut.company.company.QCompany;
import com.app.kokonut.company.companysetting.dtos.CompanySettingCheckDto;
import com.app.kokonut.company.companysetting.dtos.CompanySettingEmailDto;
import com.app.kokonut.company.companysetting.dtos.CompanySettingInfoDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
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
                        companySetting.csLongDisconnectionSetting,
                        new CaseBuilder()
                                .when(companySetting.csEmailCodeSetting.isNotNull()).then(companySetting.csEmailCodeSetting)
                                .otherwise("")
                ));

        return query.fetchOne();
    }

    @Override
    public CompanySettingCheckDto findByCompanySettingCheck(String cpCode) {

        QCompanySetting companySetting = QCompanySetting.companySetting;

        JPQLQuery<CompanySettingCheckDto> query = from(companySetting)
                .where(companySetting.cpCode.eq(cpCode))
                .select(Projections.constructor(CompanySettingCheckDto.class,
                        companySetting.csId,
                        companySetting.csOverseasBlockSetting,
                        companySetting.csAccessSetting,
                        companySetting.csPasswordErrorCountSetting
                ));

        return query.fetchOne();
    }

    @Override
    public CompanySettingEmailDto findByCompanySettingEmail(String cpCode) {

        QCompanySetting companySetting = QCompanySetting.companySetting;
        QCompany company = QCompany.company;

        JPQLQuery<CompanySettingEmailDto> query = from(companySetting)
                .where(companySetting.cpCode.eq(cpCode))
                .innerJoin(company).on(company.cpCode.eq(companySetting.cpCode))
                .select(Projections.constructor(CompanySettingEmailDto.class,
                        company.cpName,
                        new CaseBuilder()
                                .when(companySetting.csEmailCodeSetting.isNotNull()).then(companySetting.csEmailCodeSetting)
                                .otherwise("")
                ));

        return query.fetchOne();
    }

}
