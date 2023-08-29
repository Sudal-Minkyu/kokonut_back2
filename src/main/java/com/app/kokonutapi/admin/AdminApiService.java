package com.app.kokonutapi.admin;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.common.AjaxResponse;
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
 * Date : 2023-08-01
 * Time :
 * Remark :
 */
@Slf4j
@Service
public class AdminApiService {

    private final AdminRepository adminRepository;
    private final ApiCallHistoryService apiCallHistoryService;

    @Autowired
    public AdminApiService(AdminRepository adminRepository, ApiCallHistoryService apiCallHistoryService){
        this.adminRepository = adminRepository;
        this.apiCallHistoryService = apiCallHistoryService;
    }

    public ResponseEntity<Map<String, Object>> todayCount(JwtFilterDto jwtFilterDto) {
        log.info("todayCount 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
        String cpCode = adminCompanyInfoDto.getCompanyCode();
        String basicTable = cpCode+"_1";

        // API 호출 로그 저장
        apiCallHistoryService.apiCallHistorySave(cpCode, "/v3/api/Admin/todayCount");



        data.put("count", 0);

        return ResponseEntity.ok(res.apisuccess(data));
    }

    public ResponseEntity<Map<String, Object>> list(JwtFilterDto jwtFilterDto) {
        log.info("list 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
        String cpCode = adminCompanyInfoDto.getCompanyCode();
        String basicTable = cpCode+"_1";

        // API 호출 로그 저장
        apiCallHistoryService.apiCallHistorySave(cpCode, "/v3/api/Admin/list");

        List<String> adminList = new ArrayList<>();

        data.put("adminList", adminList);



        return ResponseEntity.ok(res.apisuccess(data));
    }


}
