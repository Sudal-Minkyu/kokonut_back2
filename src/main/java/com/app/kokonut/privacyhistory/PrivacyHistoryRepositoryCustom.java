package com.app.kokonut.privacyhistory;

import com.app.kokonut.privacyhistory.dtos.PrivacyHistoryListDto;
import com.app.kokonut.privacyhistory.dtos.PrivacyHistorySearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Woody
 * Date : 2023-05-15
 * Time :
 * Remark : PrivacyHistory Sql 쿼리호출
 */
public interface PrivacyHistoryRepositoryCustom {

    Page<PrivacyHistoryListDto> findByPrivacyHistoryList(PrivacyHistorySearchDto privacyHistorySearchDto, Pageable pageable);

}