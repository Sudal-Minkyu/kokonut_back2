package com.app.kokonutapi.personal;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.history.extra.apicallhistory.ApiCallHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Woody
 * Date : 2023-08-05
 * Time :
 * Remark :
 */
@Slf4j
@Service
public class PersonalApiService {

    private final AdminRepository adminRepository;
    private final ApiCallHistoryService apiCallHistoryService;

    @Autowired
    public PersonalApiService(AdminRepository adminRepository,
                              ApiCallHistoryService apiCallHistoryService){
        this.adminRepository = adminRepository;
        this.apiCallHistoryService = apiCallHistoryService;
    }

    // 개인정보 수
    public ResponseEntity<Map<String, Object>> count(JwtFilterDto jwtFilterDto) {
        log.info("count 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
        String cpCode = adminCompanyInfoDto.getCompanyCode();
        String basicTable = cpCode+"_1";

        // API 호출 로그 저장
        apiCallHistoryService.apiCallHistorySave(cpCode, "/v3/api/Personal/count");

        data.put("activeCount", 0);
        data.put("newCount", 0);
        data.put("secessionCount", 0);
        data.put("todayCount", 0);

        return ResponseEntity.ok(res.apisuccess(data));
    }

    // 개인정보 검색
    public ResponseEntity<Map<String, Object>> search(JwtFilterDto jwtFilterDto) {
        log.info("search 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
        String cpCode = adminCompanyInfoDto.getCompanyCode();
        String basicTable = cpCode+"_1";

        // API 호출 로그 저장
        apiCallHistoryService.apiCallHistorySave(cpCode, "/v3/api/Personal/search");

        List<String> persnalList = new ArrayList<>();

        data.put("persnalList", persnalList);



        return ResponseEntity.ok(res.apisuccess(data));
    }

    // 개인정보 삭제
    public ResponseEntity<Map<String, Object>> delete(JwtFilterDto jwtFilterDto) {
        log.info("delete 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
        String cpCode = adminCompanyInfoDto.getCompanyCode();
        String basicTable = cpCode+"_1";

        // API 호출 로그 저장
        apiCallHistoryService.apiCallHistorySave(cpCode, "/v3/api/Personal/delete");

//        data.put("result", "개인정보 삭제를 완료했습니다.");

        log.error("삭제할 개인정보를 선택해주세요.");
        return ResponseEntity.ok(res.apifail(ResponseErrorCode.ERROR_CODE_28.getCode(),ResponseErrorCode.ERROR_CODE_28.getDesc()));

//        return ResponseEntity.ok(res.apisuccess(data));
    }
}
