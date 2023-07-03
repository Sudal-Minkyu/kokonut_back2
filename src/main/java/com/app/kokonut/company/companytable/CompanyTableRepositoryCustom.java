package com.app.kokonut.company.companytable;

import com.app.kokonut.company.companytable.dtos.CompanyPrivacyTableListDto;
import com.app.kokonut.company.companytable.dtos.CompanyTableListDto;
import com.app.kokonut.index.dtos.PrivacyItemCountDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-11
 * Time :
 * Remark : CompanyTable Sql 쿼리호출
 */
public interface CompanyTableRepositoryCustom {

    PrivacyItemCountDto findByPrivacyItemSum(String cpCode);

    List<CompanyTableListDto> findByTableList(String cpCode);

    List<CompanyPrivacyTableListDto> findByPrivacyTableList(String cpCode);

}