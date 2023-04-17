package com.app.kokonut.email.emailhistory;

import com.app.kokonut.email.emailhistory.dtos.EmailHistoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class EmailHistoryService {

    @Autowired
    private EmailHistoryRepository emailHistoryRepository;

    /**
     * 이메일 발송 내역 저장
     * @param emailHistoryDto 이메일 발송 정보
     */
    @Transactional
    public boolean saveEmailHistory(EmailHistoryDto emailHistoryDto) {
        log.info("### saveEmailHistory 호출");
        EmailHistory reciveHistory = new EmailHistory();

        if (emailHistoryDto == null) {
            log.error("### 이메일 발송 내역 정보를 확인할 수 없습니다.");
            return false;
        } else {
            log.info("### saveEmailHistory 저장 시작");
            reciveHistory.setEhFrom(emailHistoryDto.getEhFrom());
            reciveHistory.setEhFromName(emailHistoryDto.getEhFromName());
            reciveHistory.setEhTo(emailHistoryDto.getEhTo());
            reciveHistory.setEhToName(emailHistoryDto.getEhToName());
            reciveHistory.setEhTitle(emailHistoryDto.getEhTitle());
            reciveHistory.setEhContents(emailHistoryDto.getEhContents());

            log.info("### saveEmailHistory 저장");
            EmailHistory saveHistory = emailHistoryRepository.save(reciveHistory);
            if (emailHistoryRepository.existsById(saveHistory.getEhId())) {
                log.info("### 이메일 전송이력 저장에 성공했습니다. : " + saveHistory.getEhId());
                return true;
            } else {
                log.info("### 이메일 전송이력 저장에 실패했습니다. : " + saveHistory.getEhId());
                return false;
            }
        }
    }
}