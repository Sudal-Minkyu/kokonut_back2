package com.app.kokonut.history.extra.encrypcounthistory;

import com.app.kokonut.history.extra.decrypcounthistory.DecrypCountHistoryRepository;
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
public class EncrypCountHistoryService {

    private final EncrypCountHistoryRepository encrypCountHistoryRepository;

    @Autowired
    public EncrypCountHistoryService(EncrypCountHistoryRepository encrypCountHistoryRepository) {
        this.encrypCountHistoryRepository = encrypCountHistoryRepository;
    }

    @Transactional
    public EncrypCountHistory encrypCountHistorySave(String cpCode, Integer echCount) {
        log.info("encrypCountHistorySave 호출");
        EncrypCountHistory encrypCountHistory = new EncrypCountHistory();
        encrypCountHistory.setCpCode(cpCode);
        encrypCountHistory.setEchCount(echCount);
        encrypCountHistory.setInsert_date(LocalDateTime.now());
        return encrypCountHistoryRepository.save(encrypCountHistory);
    }

}
