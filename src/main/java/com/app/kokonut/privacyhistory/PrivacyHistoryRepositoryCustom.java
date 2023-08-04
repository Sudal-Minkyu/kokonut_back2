package com.app.kokonut.privacyhistory;

import com.app.kokonut.privacyhistory.dtos.PrivacyHistoryExcelDownloadListDto;
import com.app.kokonut.privacyhistory.dtos.PrivacyHistoryListDto;
import com.app.kokonut.privacyhistory.dtos.PrivacyHistorySearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-05-15
 * Time :
 * Remark : PrivacyHistory Sql 쿼리호출
 */
public interface PrivacyHistoryRepositoryCustom {

    Page<PrivacyHistoryListDto> findByPrivacyHistoryPage(PrivacyHistorySearchDto privacyHistorySearchDto, Pageable pageable);

    List<PrivacyHistoryExcelDownloadListDto> findByPrivacyHistoryList(PrivacyHistorySearchDto privacyHistorySearchDto);

}