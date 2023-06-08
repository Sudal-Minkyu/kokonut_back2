package com.app.kokonut.company.companysetting;

import com.app.kokonut.company.companysetting.dtos.CompanySettingCheckDto;
import com.app.kokonut.company.companysetting.dtos.CompanySettingInfoDto;

/**
 * @author Woody
 * Date : 2023-06-05
 * Time :
 * Remark : CompanySetting Sql 쿼리호출
 */
public interface CompanySettingRepositoryCustom {

    CompanySettingInfoDto findByCompanySettingInfo(String cpCode);

    CompanySettingCheckDto findByCompanySettingCheck(String cpCode);

}