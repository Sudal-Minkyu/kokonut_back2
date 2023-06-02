package com.app.kokonut.company.companytable;

import com.app.kokonut.company.companytable.dtos.CompanyPrivacyTableListDto;
import com.app.kokonut.company.companytable.dtos.CompanyTableListDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-11
 * Time :
 * Remark : CompanyTable Sql 쿼리호출
 */
public interface CompanyTableRepositoryCustom {

    List<CompanyTableListDto> findByTableList(String cpCode);

    List<CompanyPrivacyTableListDto> findByPrivacyTableList(String cpCode);

}