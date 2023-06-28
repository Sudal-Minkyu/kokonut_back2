package com.app.kokonut.company.companytableleavehistory;

import java.time.LocalDate;

/**
 * @author Woody
 * Date : 2023-06-25
 * Time :
 * Remark : CompanyTableLeaveHistoryRepository Sql 쿼리호출
 */
public interface CompanyTableLeaveHistoryRepositoryCustom {

    Integer findByLeaveHistoryCount(String cpCode, String dateType, LocalDate now, LocalDate filterDate);

}