package com.app.kokonut.history.extra.apicallhistory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-06-30
 * Remark :
 */
@Slf4j
@Service
public class ApiCallHistoryService {

    private final ApiCallHistoryRepository apiCallHistoryRepository;

    @Autowired
    public ApiCallHistoryService(ApiCallHistoryRepository apiCallHistoryRepository) {
        this.apiCallHistoryRepository = apiCallHistoryRepository;
    }

    @Transactional
    public ApiCallHistory apiCallHistorySave(String cpCode, String akhUrl) {
        log.info("apiCallHistorySave 호출");
        ApiCallHistory apiCallHistory = new ApiCallHistory();
        apiCallHistory.setCpCode(cpCode);
        apiCallHistory.setAkhUrl(akhUrl);
        apiCallHistory.setInsert_date(LocalDateTime.now());
        return apiCallHistoryRepository.save(apiCallHistory);
    }

}
