package com.app.kokonut.company.companytable;

import com.app.kokonut.company.companytable.dtos.CompanyTableSubListDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-11
 * Time :
 * Remark : CompanyCategory Sql 쿼리호출
 */
public interface CompanyTableRepositoryCustom {

    List<CompanyTableSubListDto> findByTableList(String cpCode);

}