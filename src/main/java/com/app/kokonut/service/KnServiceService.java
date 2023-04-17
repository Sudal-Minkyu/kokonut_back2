package com.app.kokonut.service;

import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Joy
 * Date : 2023-01-05
 * Time :
 * Remark : ServiceService 서비스
 */
@Slf4j
@Service
public class KnServiceService {
    private final AjaxResponse res = new AjaxResponse();
    private final HashMap<String, Object> data = new HashMap<>();
    private final KnKnServiceRepository knServiceRepository;

    public KnServiceService(KnKnServiceRepository knServiceRepository) {
        this.knServiceRepository = knServiceRepository;
    }

    public ResponseEntity<Map<String, Object>> serviceList() {
        log.info("serviceList 호출");
        data.put("serviceList", knServiceRepository.findServiceList());
        return ResponseEntity.ok(res.success(data));
    }

    public ResponseEntity<Map<String, Object>> serviceDetail(Long srId) {
        log.info("serviceDetail 호출, srId : " + srId);
        if(srId != null){
            data.put("serviceDto", knServiceRepository.findServiceByIdx(srId));
            return ResponseEntity.ok(res.success(data));
        }else{
            log.error("srId 값을 확인 할 수 없습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO057.getCode(), ResponseErrorCode.KO057.getDesc()));
        }
    }
}
