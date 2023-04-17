package com.app.kokonut.company.companycategory;

import com.app.kokonut.company.companycategory.dtos.CompanyCategoryListDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-11
 * Time :
 * Remark : CompanyCategory Sql 쿼리호출
 */
public interface CompanyCategoryRepositoryCustom {

    List<CompanyCategoryListDto> findByCategoryList(String companyCode);

}