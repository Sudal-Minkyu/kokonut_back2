package com.app.kokonut.history.extra.decrypcounthistory;

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
public class DecrypCountHistoryService {

    private final DecrypCountHistoryRepository decrypCountHistoryRepository;

    @Autowired
    public DecrypCountHistoryService(DecrypCountHistoryRepository decrypCountHistoryRepository) {
        this.decrypCountHistoryRepository = decrypCountHistoryRepository;
    }

    @Transactional
    public DecrypCountHistory decrypCountHistorySave(String cpCode, Integer dchCount) {
        log.info("decrypCountHistorySave 호출");
        DecrypCountHistory decrypCountHistory = new DecrypCountHistory();
        decrypCountHistory.setCpCode(cpCode);
        decrypCountHistory.setDchCount(dchCount);
        decrypCountHistory.setInsert_date(LocalDateTime.now());
        return decrypCountHistoryRepository.save(decrypCountHistory);
    }

}
