package com.app.kokonut.history.extra.errorhistory;

import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.history.extra.errorhistory.dtos.ErrorHistorySaveDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Woody
 * Date : 2022-06-30
 * Time :
 * Remark :
 */
@Slf4j
@Service
public class ErrorHistoryService {

    private final ErrorHistoryRepository errorHistoryRepository;

    @Autowired
    public ErrorHistoryService(ErrorHistoryRepository errorHistoryRepository) {
        this.errorHistoryRepository = errorHistoryRepository;
    }

    // 에러메세지 저장 API
    @Transactional
    public ResponseEntity<Map<String, Object>> errorSave(ErrorHistorySaveDto errorHistorySaveDto, JwtFilterDto jwtFilterDto) {
        log.info("errorSave 호출");
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();

        ErrorHistory errorHistory = new ErrorHistory();
        errorHistory.setEtTitle(errorHistorySaveDto.getEtTitle());
        errorHistory.setEtMsg(errorHistorySaveDto.getEtMsg());
        errorHistory.setInsert_email(email);
        errorHistory.setInsert_date(LocalDateTime.now());

        errorHistoryRepository.save(errorHistory);

        return ResponseEntity.ok(res.success(data));
    }

}
