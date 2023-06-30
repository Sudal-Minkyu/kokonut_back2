package com.app.kokonut.history.encrypcounthistory;

import com.app.kokonut.index.dtos.EncrypCountHistoryCountDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-06-30
 * Time :
 * Remark : EncrypCountHistory Sql 쿼리호출
 */
public interface EncrypCountHistoryRepositoryCustom {

    List<EncrypCountHistoryCountDto> findTodayEncrypCountList(String cpCode);


}