package com.app.kokonut.privacy.policy;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.realcomponent.CommonUtil;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dto.ActivityCode;
import com.app.kokonut.privacy.policy.dtos.PolicySaveFirstDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Woody
 * Date : 2023-04-21
 * Time :
 * Remark :
 */
@Slf4j
@Service
public class PolicyService {

//      AC_28("AC_28", "개인정보 처리방침 작성중"),
//      AC_29("AC_29", "개인정보 처리방침 추가"),

    private final HistoryService historyService;

    private final AdminRepository adminRepository;
    private final PolicyRepository policyRepository;

    @Autowired
    public PolicyService(HistoryService historyService, AdminRepository adminRepository, PolicyRepository policyRepository){
        this.historyService = historyService;
        this.adminRepository = adminRepository;
        this.policyRepository = policyRepository;
    }

    // 개인정보처리방침 리스트 조회
    public ResponseEntity<Map<String, Object>> policyList(JwtFilterDto jwtFilterDto, Pageable pageable) {
        log.info("policyList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        return ResponseEntity.ok(res.success(data));
    }

    // 개인정보처리방침 상세내용 조회
    public ResponseEntity<Map<String, Object>> policyDetail(Long qnaId, JwtFilterDto jwtFilterDto) {
        log.info("policyDetail 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        return ResponseEntity.ok(res.success(data));
    }

    // 개인정보보호 첫번째 뎁스 등록
    @Transactional
    public ResponseEntity<Map<String, Object>> privacyPolicyFirstSave(PolicySaveFirstDto policySaveFirstDto, JwtFilterDto jwtFilterDto) {

        log.info("privacyPolicyFirstSave 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();
        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

        Long adminId = adminCompanyInfoDto.getAdminId();
        Long companyId = adminCompanyInfoDto.getCompanyId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();
        log.info("adminId : "+adminId);
        log.info("companyId : "+companyId);
        log.info("companyCode : "+companyCode);

        log.info("policySaveFirstDto : "+policySaveFirstDto);

        ActivityCode activityCode;
        String ip = CommonUtil.clientIp();
        Long activityHistoryId;

        // 개인정보 처리방침 작성중 코드
        activityCode = ActivityCode.AC_28;

        // 활동이력 저장 -> 비정상 모드
        activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                companyCode+" - "+activityCode.getDesc()+" 첫번째 뎁스 시도 이력", "", ip, 0, jwtFilterDto.getEmail());

        Policy policy = new Policy();
        policy.setCpCode(companyCode);
        policy.setPiVersion(policySaveFirstDto.getPiVersion());

        String getPiDate = policySaveFirstDto.getPiDate()+" 00:00:00.000";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. MM. dd HH:mm:ss.SSS");
        LocalDateTime piDate = LocalDateTime.parse(getPiDate, formatter);
        log.info("piDate : "+piDate);

        policy.setPiDate(piDate);
        policy.setPiHeader(policySaveFirstDto.getPiHeader());
        policy.setPiAutosave(0);
        policy.setInsert_email(email);
        policy.setInsert_date(LocalDateTime.now());

        log.info("policy : "+policy);

        Policy policySave = policyRepository.save(policy);

        historyService.updateHistory(activityHistoryId,
                companyCode+" - "+activityCode.getDesc()+" 첫번째 뎁스 시도 이력", "", 1);

        data.put("saveId",policySave.getPiId());

        return ResponseEntity.ok(res.success(data));
    }



}
