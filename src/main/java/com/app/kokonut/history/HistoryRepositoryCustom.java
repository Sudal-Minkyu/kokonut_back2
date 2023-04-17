package com.app.kokonut.history;

import com.app.kokonut.history.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Woody
 * Date : 2022-11-03
 * Time :
 * Remark : 기존의 코코넛 프로젝트의 History Sql 쿼리호출
 */
public interface HistoryRepositoryCustom {

    Page<HistoryListDto> findByHistoryList(HistorySearchDto historySearchDto, Pageable pageable);

    HistoryDto findByHistoryByIdx(Long idx); // SelectHistoryByIdx -> 변경후

    HistoryDto findByHistoryBycompanyIdAndReasonaAndAtivityIdx(Long companyId, String ahReason); // SelectHistoryBycompanyIdAndReason -> 변경후

    List<HistoryInfoListDto> findByHistoryBycompanyIdAndTypeList(Long companyId, Integer ahType); // SelectByTypeAndcompanyId -> 변경후

    List<Column> findByHistoryColumnList(); // selectColumns -> 변경후

    HistoryStatisticsDto findByHistoryStatistics(Long companyId, int day); // SelectStatisticsHistory -> 변경후

    void deleteExpiredHistory(int activityIdx, int month);

    HistoryLoginInfoDto findByLoginHistory(String knEmail);
}