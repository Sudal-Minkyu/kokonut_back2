package com.app.kokonut.apiKey.apicallhistory;

import com.app.kokonut.index.dtos.ApiCallHistoryCountDto;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-06-29
 * Time :
 * Remark :
 */
public interface ApiCallHistoryRepositoryCustom {

    List<ApiCallHistoryCountDto> findTodayApiCountList(String cpCode);

}