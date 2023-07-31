package com.app.kokonut.company.companytablecolumninfo;

import com.app.kokonut.company.companytablecolumninfo.dtos.CompanyTableColumnInfoCheck;
import com.app.kokonut.company.companytablecolumninfo.dtos.CompanyTableColumnInfoCheckList;
import com.app.kokonut.company.companytablecolumninfo.dtos.CompanyTableColumnNameSearch;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-05-26
 * Time :
 * Remark : CompanyTableColumnInfo Sql 쿼리호출
 */
public interface CompanyTableColumnInfoRepositoryCustom {

    List<CompanyTableColumnInfoCheckList> findByCheckList(String ctName);

    CompanyTableColumnInfoCheck findByCheck(String ctName, String ctciCode);

    CompanyTableColumnNameSearch findByColumnName(String ctciName);

}