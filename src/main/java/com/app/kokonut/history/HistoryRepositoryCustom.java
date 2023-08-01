package com.app.kokonut.history;

import com.app.kokonut.history.dtos.*;
import com.app.kokonut.index.dtos.HistoryMyConnectListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Woody
 * Date : 2022-11-03
 * Time :
 * Remark : History Sql 쿼리호출
 */
public interface HistoryRepositoryCustom {

    Page<HistoryListDto> findByHistoryPage(HistorySearchDto historySearchDto, Pageable pageable);

    List<HistoryExcelDownloadListDto> findByHistoryList(HistorySearchDto historySearchDto);

    HistoryDto findByHistoryByIdx(Long idx); // SelectHistoryByIdx -> 변경후

    HistoryDto findByHistoryBycompanyIdAndReasonaAndAtivityIdx(Long companyId, String ahReason); // SelectHistoryBycompanyIdAndReason -> 변경후

    List<HistoryInfoListDto> findByHistoryBycompanyIdAndTypeList(Long companyId, Integer ahType); // SelectByTypeAndcompanyId -> 변경후

    List<Column> findByHistoryColumnList(); // selectColumns -> 변경후

    HistoryStatisticsDto findByHistoryStatistics(Long companyId, int day); // SelectStatisticsHistory -> 변경후

    void deleteExpiredHistory(int activityIdx, int month);

    HistoryLoginInfoDto findByLoginHistory(String knEmail);




    List<HistoryMyConnectListDto> findByMyConnectList(Long adminId, String cpCode); // 인덱스에 보여줄 나의접속현황 쿼리

    LocalDateTime findByHistoryInsertDate(Long adminId);

}