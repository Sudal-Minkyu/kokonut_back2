package com.app.kokonut.history.extra.decrypcounthistory;

import com.app.kokonut.index.dtos.DecrypCountHistoryCountDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-06-30
 * Time :
 * Remark : DecrypCountHistory Sql 쿼리호출
 */
public interface DecrypCountHistoryRepositoryCustom {

    List<DecrypCountHistoryCountDto> findTodayDecrypCountList(String cpCode);

}