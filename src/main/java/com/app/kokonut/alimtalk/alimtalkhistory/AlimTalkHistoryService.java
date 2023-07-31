package com.app.kokonut.alimtalk.alimtalkhistory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-07-27
 * Time :
 * Remark : AlimTalkHistoryService
 */
@Slf4j
@Service
public class AlimTalkHistoryService {

    private final AlimTalkHistoryRepository alimTalkHistoryRepository;

    @Autowired
    public AlimTalkHistoryService(AlimTalkHistoryRepository alimTalkHistoryRepository) {
        this.alimTalkHistoryRepository = alimTalkHistoryRepository;
    }

    // 알림톡 전송건수 기록
    @Transactional
    public void alimTalkHistorySave(String cpCode, String email, int alimSuccessCount, int alimFailCount) {
        log.info("alimTalkHistorySave 호출");

        AlimTalkHistory alimTalkHistory = new AlimTalkHistory();
        alimTalkHistory.setCpCode(cpCode);
        alimTalkHistory.setAlimAllCount(alimSuccessCount+alimFailCount);
        alimTalkHistory.setAlimSuccessCount(alimSuccessCount);
        alimTalkHistory.setAlimFailCount(alimFailCount);
        alimTalkHistory.setInsert_email(email);
        alimTalkHistory.setInsert_date(LocalDateTime.now());

        alimTalkHistoryRepository.save(alimTalkHistory);
    }

}
