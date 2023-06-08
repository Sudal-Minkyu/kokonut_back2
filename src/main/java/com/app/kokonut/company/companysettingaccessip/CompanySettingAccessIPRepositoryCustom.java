package com.app.kokonut.company.companysettingaccessip;

import com.app.kokonut.company.companysettingaccessip.dtos.CompanySettingAccessIPListDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-06-05
 * Time :
 * Remark : CompanySettingAccessIP Sql 쿼리호출
 */
public interface CompanySettingAccessIPRepositoryCustom {

    List<CompanySettingAccessIPListDto> findByCompanySettingIPList(Long csId);

}