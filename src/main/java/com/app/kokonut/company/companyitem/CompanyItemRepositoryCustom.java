package com.app.kokonut.company.companyitem;

import com.app.kokonut.company.companyitem.dtos.CompanyItemListDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-11
 * Time :
 * Remark : CompanyItem Sql 쿼리호출
 */
public interface CompanyItemRepositoryCustom {

    List<CompanyItemListDto> findByItemList(String companyCode);

}