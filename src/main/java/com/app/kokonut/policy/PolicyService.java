package com.app.kokonut.policy;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.CommonUtil;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.Utils;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dtos.ActivityCode;
import com.app.kokonut.policy.dtos.*;
import com.app.kokonut.policy.policyafter.PolicyAfter;
import com.app.kokonut.policy.policyafter.PolicyAfterRepository;
import com.app.kokonut.policy.policyafter.dtos.PolicyAfterSaveDto;
import com.app.kokonut.policy.policyafter.dtos.PolicyAfterSaveInfoListDto;
import com.app.kokonut.policy.policybefore.PolicyBefore;
import com.app.kokonut.policy.policybefore.PolicyBeforeRepository;
import com.app.kokonut.policy.policybefore.dtos.PolicyBeforeSaveDto;
import com.app.kokonut.policy.policybefore.dtos.PolicyBeforeSaveInfoListDto;
import com.app.kokonut.policy.policyout.PolicyOut;
import com.app.kokonut.policy.policyout.PolicyOutRepository;
import com.app.kokonut.policy.policyout.dtos.PolicyOutSaveDto;
import com.app.kokonut.policy.policyout.dtos.PolicyOutSaveInfoListDto;
import com.app.kokonut.policy.policyoutdetail.PolicyOutDetail;
import com.app.kokonut.policy.policyoutdetail.PolicyOutDetailRepository;
import com.app.kokonut.policy.policyoutdetail.dtos.PolicyOutDetailSaveDto;
import com.app.kokonut.policy.policyoutdetail.dtos.PolicyOutDetailSaveInfoListDto;
import com.app.kokonut.policy.policypurpose.PolicyPurpose;
import com.app.kokonut.policy.policypurpose.PolicyPurposeRepository;
import com.app.kokonut.policy.policypurpose.dtos.PolicyPurposeSaveDto;
import com.app.kokonut.policy.policypurpose.dtos.PolicyPurposeSaveInfoListDto;
import com.app.kokonut.policy.policyresponsible.PolicyResponsible;
import com.app.kokonut.policy.policyresponsible.PolicyResponsibleRepository;
import com.app.kokonut.policy.policyresponsible.dtos.PolicyResponsibleSaveDto;
import com.app.kokonut.policy.policyresponsible.dtos.PolicyResponsibleSaveInfoListDto;
import com.app.kokonut.policy.policyserviceauto.PolicyServiceAuto;
import com.app.kokonut.policy.policyserviceauto.PolicyServiceAutoRepository;
import com.app.kokonut.policy.policyserviceauto.dtos.PolicyServiceAutoSaveDto;
import com.app.kokonut.policy.policyserviceauto.dtos.PolicyServiceAutoSaveInfoListDto;
import com.app.kokonut.policy.policystatute.PolicyStatute;
import com.app.kokonut.policy.policystatute.PolicyStatuteRepository;
import com.app.kokonut.policy.policystatute.dtos.PolicyStatuteSaveInfoListDto;
import com.app.kokonut.policy.policythird.PolicyThird;
import com.app.kokonut.policy.policythird.PolicyThirdRepository;
import com.app.kokonut.policy.policythird.dtos.PolicyThirdSaveDto;
import com.app.kokonut.policy.policythird.dtos.PolicyThirdSaveInfoListDto;
import com.app.kokonut.policy.policythirdoverseas.PolicyThirdOverseas;
import com.app.kokonut.policy.policythirdoverseas.PolicyThirdOverseasRepository;
import com.app.kokonut.policy.policythirdoverseas.dtos.PolicyThirdOverseasSaveDto;
import com.app.kokonut.policy.policythirdoverseas.dtos.PolicyThirdOverseasSaveInfoListDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Woody
 * Date : 2023-04-21
 * Time :
 * Remark :
 */
@Slf4j
@Service
public class PolicyService {

    private final HistoryService historyService;

    private final AdminRepository adminRepository;

    // 1
    private final PolicyRepository policyRepository;

    // 2
    private final PolicyPurposeRepository policyPurposeRepository;

    // 3
    private final PolicyBeforeRepository policyBeforeRepository;
    private final PolicyAfterRepository policyAfterRepository;
    private final PolicyServiceAutoRepository policyServiceAutoRepository;
    private final PolicyStatuteRepository policyStatuteRepository;

    // 4
    private final PolicyOutRepository policyOutRepository;
    private final PolicyOutDetailRepository policyOutDetailRepository;

    // 5
    private final PolicyThirdRepository policyThirdRepository;
    private final PolicyThirdOverseasRepository policyThirdOverseasRepository;

    // 6
    private final PolicyResponsibleRepository policyResponsibleRepository;

    @Autowired
    public PolicyService(HistoryService historyService, AdminRepository adminRepository,
                         PolicyRepository policyRepository,
                         PolicyPurposeRepository policyPurposeRepository,
                         PolicyBeforeRepository policyBeforeRepository, PolicyAfterRepository policyAfterRepository, PolicyServiceAutoRepository policyServiceAutoRepository,
                         PolicyStatuteRepository policyStatuteRepository, PolicyOutRepository policyOutRepository, PolicyOutDetailRepository policyOutDetailRepository,
                         PolicyThirdRepository policyThirdRepository, PolicyThirdOverseasRepository policyThirdOverseasRepository,
                         PolicyResponsibleRepository policyResponsibleRepository){
        this.historyService = historyService;
        this.adminRepository = adminRepository;
        this.policyRepository = policyRepository;
        this.policyPurposeRepository = policyPurposeRepository;
        this.policyBeforeRepository = policyBeforeRepository;
        this.policyAfterRepository = policyAfterRepository;
        this.policyServiceAutoRepository = policyServiceAutoRepository;
        this.policyStatuteRepository = policyStatuteRepository;
        this.policyOutRepository = policyOutRepository;
        this.policyOutDetailRepository = policyOutDetailRepository;
        this.policyThirdRepository = policyThirdRepository;
        this.policyThirdOverseasRepository = policyThirdOverseasRepository;
        this.policyResponsibleRepository = policyResponsibleRepository;
    }

    // 개인정보처리방침 리스트 조회
    public ResponseEntity<Map<String, Object>> policyList(String searchText, String stime, String filterDate, JwtFilterDto jwtFilterDto, Pageable pageable) {
        log.info("policyList 호출");

        AjaxResponse res = new AjaxResponse();

        log.info("searchText : "+searchText);
        log.info("stime : "+stime);
        log.info("filterDate : "+filterDate);

        String email = jwtFilterDto.getEmail();
        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
        Long adminId = adminCompanyInfoDto.getAdminId();
        String cpCode = adminCompanyInfoDto.getCompanyCode();

        PolicySearchDto policySearchDto = new PolicySearchDto();
        policySearchDto.setCpCode(cpCode);
        policySearchDto.setSearchText(searchText);
        policySearchDto.setFilterDate(filterDate);

        if(!stime.equals("")) {
            List<LocalDateTime> stimeList = Utils.getStimeList(stime);
            policySearchDto.setStimeStart(stimeList.get(0));
            policySearchDto.setStimeEnd(stimeList.get(1).plusHours(23).plusMinutes(59));
        }

        log.info("policySearchDto : "+policySearchDto);

        ActivityCode activityCode;
        String ip = CommonUtil.publicIp();
        Long activityHistoryId;

        // 개인정보 처리방침 리스트조회 코드
        activityCode = ActivityCode.AC_46;

        // 활동이력 저장 -> 비정상 모드
        activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                cpCode+" - ", "", ip, 0, email);

        Page<PolicyListDto> policyListDtos = policyRepository.findByPolicyList(policySearchDto, pageable);

        historyService.updateHistory(activityHistoryId,
                null, "", 1);
        return ResponseEntity.ok(res.ResponseEntityPage(policyListDtos));
    }

    // 개인정보처리방침 상세내용 조회
    public ResponseEntity<Map<String, Object>> policyDetail(Long piId, JwtFilterDto jwtFilterDto) {
        log.info("policyDetail 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();
        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
        String cpCode = adminCompanyInfoDto.getCompanyCode();

        PolicyDetailDto policyDetailDto = policyRepository.findByPolicyDetail(piId, cpCode);
        if(policyDetailDto != null){

            data.put("policyData", policyDetailDto);

            // Step2
            List<PolicyPurposeSaveInfoListDto> policyPurposeSaveInfoListDtoList = policyPurposeRepository.findByPolicyPurposeList(piId);
            data.put("purposeDataList", policyPurposeSaveInfoListDtoList);

            // Step3
            List<PolicyBeforeSaveInfoListDto> policyBeforeSaveInfoListDtos = policyBeforeRepository.findByPolicyBeforeList(piId);
            data.put("beforeDataList", policyBeforeSaveInfoListDtos);
            List<PolicyAfterSaveInfoListDto> policyAfterSaveInfoListDtos = policyAfterRepository.findByPolicyAfterList(piId);
            data.put("afterDataList", policyAfterSaveInfoListDtos);
            List<PolicyServiceAutoSaveInfoListDto> policyServiceAutoSaveInfoListDtoList = policyServiceAutoRepository.findByPolicyServiceAutoList(piId);
            data.put("serviceAutoDataList", policyServiceAutoSaveInfoListDtoList);
            List<PolicyStatuteSaveInfoListDto> policyStatuteSaveInfoListDtoList = policyStatuteRepository.findByPolicyStatuteList(piId);
            data.put("piChoseCustomList", policyStatuteSaveInfoListDtoList);

            // Step4
            List<PolicyOutSaveInfoListDto> policyOutSaveInfoListDtos = policyOutRepository.findByPolicyOutList(piId);
            data.put("outDataList", policyOutSaveInfoListDtos);
            List<PolicyOutDetailSaveInfoListDto> policyOutDetailSaveInfoListDtos = policyOutDetailRepository.findByPolicyOutDetailList(piId);
            data.put("outDetailDataList", policyOutDetailSaveInfoListDtos);

            // Step5
            List<PolicyThirdSaveInfoListDto> policyThirdSaveInfoListDtoList = policyThirdRepository.findByPolicyThirdList(piId);
            data.put("thirdDataList", policyThirdSaveInfoListDtoList);
            List<PolicyThirdOverseasSaveInfoListDto> policyThirdOverseasSaveInfoListDtoList = policyThirdOverseasRepository.findByPolicyThirdOverseasList(piId);
            data.put("thirdOverseasDataList", policyThirdOverseasSaveInfoListDtoList);

            // Step6
            List<PolicyResponsibleSaveInfoListDto> policyResponsibleSaveInfoListDtoList = policyResponsibleRepository.findByPolicyResponsibleList(piId);
            data.put("reponsibleDataList", policyResponsibleSaveInfoListDtoList);

        } else {
            log.info("조회된 데이터가 없습니다.");
        }


        return ResponseEntity.ok(res.success(data));
    }

    // 개인정보 처리방침 작성중인글 체크
    public ResponseEntity<Map<String, Object>> policyCheck(JwtFilterDto jwtFilterDto) {
        log.info("policyCheck 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();
        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

        String cpCode = adminCompanyInfoDto.getCompanyCode();

        PolicyWritingCheckDto policyWritingCheckDto = policyRepository.findByWriting(cpCode, email);
        if(policyWritingCheckDto != null) {
            data.put("result", true);
            data.put("piId", policyWritingCheckDto.getPiId());
            data.put("piStage", policyWritingCheckDto.getPiStage());
        } else {
            data.put("result", false);
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 작성중단 클릭후 작동하는 함수
    @Transactional
    public ResponseEntity<Map<String, Object>> privacyPolicyDelete(Long piId, JwtFilterDto jwtFilterDto) {
        log.info("privacyPolicyDelete 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("piId : "+piId);
        Optional<Policy> optionalPolicy = policyRepository.findById(piId);
        if(optionalPolicy.isPresent()) {

            String email = jwtFilterDto.getEmail();
            AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

            Long adminId = adminCompanyInfoDto.getAdminId();
            String companyCode = adminCompanyInfoDto.getCompanyCode();
            log.info("adminId : "+adminId);
            log.info("companyCode : "+companyCode);

            ActivityCode activityCode;
            String ip = CommonUtil.publicIp();
            Long activityHistoryId;

            if(optionalPolicy.get().getPiAutosave() == 0) {
                // 개인정보 처리방침 작성중인 글삭제 코드
                activityCode = ActivityCode.AC_43;

                // 활동이력 저장 -> 비정상 모드
                activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                        companyCode+" - ", "", ip, 0, email);

                // 2. 개인정보 처리목적 삭제
                policyPurposeRepository.deleteField(optionalPolicy.get().getPiId());

                // 3. 서비스 가입 시 수집하는 개인정보 삭제
                policyBeforeRepository.deleteField(optionalPolicy.get().getPiId());

                // 3. 서비스 가입 후 수집하는 개인정보 삭제
                policyAfterRepository.deleteField(optionalPolicy.get().getPiId());

                // 3. 서비스 이용 중 자동 생성 및 수집하는 정보 삭제
                policyServiceAutoRepository.deleteField(optionalPolicy.get().getPiId());

                // 3. 법령에 따른 개인정보의 보유기간 삭제
                policyStatuteRepository.deleteField(optionalPolicy.get().getPiId());

                // 4. 개인정보처리방침의 처리업무의 위탁에 관한사항 목록 삭제
                policyOutRepository.deleteField(optionalPolicy.get().getPiId());

                // 4. 개인정보처리방침의 처리업무의 국외 위탁에 관한사항 목록 삭제
                policyOutDetailRepository.deleteField(optionalPolicy.get().getPiId());

                // 5. 개인정보처리방침의 제3자 제공 항목 목록 삭제
                policyThirdRepository.deleteField(optionalPolicy.get().getPiId());

                // 5. 개인정보처리방침의 국외 제3자 제공에 관한 사항 삭제
                policyThirdOverseasRepository.deleteField(optionalPolicy.get().getPiId());

                // 6. 개인정보처리방침의 책임자 항목 목록 삭제
                policyResponsibleRepository.deleteField(optionalPolicy.get().getPiId());

                // 개인정보처리방침의 본체 삭제
                policyRepository.delete(optionalPolicy.get());

                historyService.updateHistory(activityHistoryId, null, "", 1);
            }


        } else {
            log.error("삭제할 개인정보처리방침이 없습니다.");
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 개인정보 처리방침 게시글 데이터 가져오기
    public ResponseEntity<Map<String, Object>> privacyPolicyWriting(Long piId) {
        log.info("privacyPolicyWriting 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // Step1
        PolicyFirstInfoDto policyFirstInfoDto = policyRepository.findByPolicyFirst(piId);
        data.put("policyInfo1", policyFirstInfoDto);

        // Step2
        List<PolicyPurposeSaveInfoListDto> policyPurposeSaveInfoListDtoList = policyPurposeRepository.findByPolicyPurposeList(piId);
        data.put("purposeInfo", policyPurposeSaveInfoListDtoList);

        // Step3
        // -> 서비스 가입 시 수집하는 개인정보
        List<PolicyBeforeSaveInfoListDto> policyBeforeSaveInfoListDtos = policyBeforeRepository.findByPolicyBeforeList(piId);
        data.put("beforeDataList", policyBeforeSaveInfoListDtos);
        // -> 서비스 가입 후 수집하는 개인정보
        List<PolicyAfterSaveInfoListDto> policyAfterSaveInfoListDtos = policyAfterRepository.findByPolicyAfterList(piId);
        data.put("afterDataList", policyAfterSaveInfoListDtos);
        // -> 서비스 이용 중 자동 생성 및 수집하는 정보
        List<PolicyServiceAutoSaveInfoListDto> policyServiceAutoSaveInfoListDtoList = policyServiceAutoRepository.findByPolicyServiceAutoList(piId);
        data.put("serviceAutoDataList", policyServiceAutoSaveInfoListDtoList);
        // -> 법령에 따른 개인정보의 보유기간
        PolicySecondInfoDto policySecondInfoDto = policyRepository.findByPolicySecond(piId);
        List<PolicyStatuteSaveInfoListDto> policyStatuteSaveInfoListDtoList = policyStatuteRepository.findByPolicyStatuteList(piId);
        data.put("piChoseListString", policySecondInfoDto.getPiStatute());
        data.put("piChoseCustomList", policyStatuteSaveInfoListDtoList);

        // Step4
        List<PolicyOutSaveInfoListDto> policyOutSaveInfoListDtos = policyOutRepository.findByPolicyOutList(piId);
        data.put("outDataList", policyOutSaveInfoListDtos);
        List<PolicyOutDetailSaveInfoListDto> policyOutDetailSaveInfoListDtos = policyOutDetailRepository.findByPolicyOutDetailList(piId);
        data.put("outDetailDataList", policyOutDetailSaveInfoListDtos);

        // Step5
        List<PolicyThirdSaveInfoListDto> policyThirdSaveInfoListDtoList = policyThirdRepository.findByPolicyThirdList(piId);
        data.put("thirdDataList", policyThirdSaveInfoListDtoList);
        List<PolicyThirdOverseasSaveInfoListDto> policyThirdOverseasSaveInfoListDtoList = policyThirdOverseasRepository.findByPolicyThirdOverseasList(piId);
        data.put("thirdOverseasDataList", policyThirdOverseasSaveInfoListDtoList);

        // Step6
        List<PolicyResponsibleSaveInfoListDto> policyResponsibleSaveInfoListDtoList = policyResponsibleRepository.findByPolicyResponsibleList(piId);
        data.put("reponsibleDataList", policyResponsibleSaveInfoListDtoList);
        PolicyThirdInfoDto policyThirdInfoDto = policyRepository.findByPolicyThird(piId);
        data.put("policyInfo3", policyThirdInfoDto);

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
        String companyCode = adminCompanyInfoDto.getCompanyCode();

        log.info("policySaveFirstDto : "+policySaveFirstDto);

        ActivityCode activityCode;
        String ip = CommonUtil.publicIp();
        Long activityHistoryId;

        // 개인정보 처리방침 작성중 코드
        activityCode = ActivityCode.AC_28;

        // 활동이력 저장 -> 비정상 모드
        activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                companyCode+" - "+" 첫번째 뎁스", "", ip, 0, email);

        Policy policySave = new Policy();
        if(policySaveFirstDto.getPiId() != 0) {
            Optional<Policy> optionalPolicy = policyRepository.findById(policySaveFirstDto.getPiId());
            if(optionalPolicy.isPresent()) {
                optionalPolicy.get().setPiVersion(policySaveFirstDto.getPiVersion());
                optionalPolicy.get().setPiHeader(policySaveFirstDto.getPiHeader());
                optionalPolicy.get().setPiDate(policySaveFirstDto.getPiDate());
                optionalPolicy.get().setModify_email(email);
                optionalPolicy.get().setModify_date(LocalDateTime.now());

                policySave = policyRepository.save(optionalPolicy.get());
            }
        } else {
            Policy policy = new Policy();
            policy.setCpCode(companyCode);
            policy.setPiVersion(policySaveFirstDto.getPiVersion());
            policy.setPiDate(policySaveFirstDto.getPiDate());
            policy.setPiHeader(policySaveFirstDto.getPiHeader());
            policy.setPiStage(1);
            policy.setPiAutosave(0);
            policy.setInsert_email(email);
            policy.setInsert_date(LocalDateTime.now());

            policySave = policyRepository.save(policy);
        }

        historyService.updateHistory(activityHistoryId, companyCode+" - "+" 첫번째 뎁스", "", 1);

        data.put("saveId",policySave.getPiId());

        return ResponseEntity.ok(res.success(data));
    }

    // 개인정보보호 두번째 뎁스 등록
    @Transactional
    public ResponseEntity<Map<String, Object>> privacyPolicySecondSave(PolicySaveSecondDto policySaveSecondDto, JwtFilterDto jwtFilterDto) {
        log.info("privacyPolicySecondSave 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();
        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

        Long adminId = adminCompanyInfoDto.getAdminId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();


//        log.info("policySaveSecondDto : "+policySaveSecondDto);

        ActivityCode activityCode;
        String ip = CommonUtil.publicIp();
        Long activityHistoryId;

        // 개인정보 처리방침 작성중 코드
        activityCode = ActivityCode.AC_28;

        Optional<Policy> optionalPolicy = policyRepository.findById(policySaveSecondDto.getPiId());
        if(optionalPolicy.isPresent()) {
            // 활동이력 저장 -> 비정상 모드
            activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    companyCode+" - "+" 두번째 뎁스", "", ip, 0, email);

            List<PolicyPurpose> policyPurposeSaveList = new ArrayList<>();
            PolicyPurpose policyPurpose;
            List<PolicyPurpose> policyPurposeDeleteList = new ArrayList<>();
            List<Long> purposeDeleteIdList = policySaveSecondDto.getPolicyPurposeDeleteIdList();
            for(PolicyPurposeSaveDto policyPurposeSaveDto : policySaveSecondDto.getPolicyPurposeSaveDtoList()) {
                if(policyPurposeSaveDto.getPipId() != 0) {
                    Optional<PolicyPurpose> optionalPolicyPurpose = policyPurposeRepository.findById(policyPurposeSaveDto.getPipId());
                    if(purposeDeleteIdList.contains(policyPurposeSaveDto.getPipId())) {
                        if(optionalPolicyPurpose.isPresent()) {
                            policyPurposeDeleteList.add(optionalPolicyPurpose.get());
                        } else {
                            log.error("삭제 privacyPolicySecondSave 존재하지 않은 항목");
                        }
                    } else {
                        if(optionalPolicyPurpose.isPresent()) {
                            optionalPolicyPurpose.get().setPipTitle(policyPurposeSaveDto.getPipTitle());
                            optionalPolicyPurpose.get().setPipContent(policyPurposeSaveDto.getPipContent());
                            policyPurposeSaveList.add(optionalPolicyPurpose.get());
                        } else {
                            log.error("수정 privacyPolicySecondSave 존재하지 않은 항목");
                        }
                    }
                } else {
                    policyPurpose = new PolicyPurpose();
                    policyPurpose.setPiId(policySaveSecondDto.getPiId());
                    policyPurpose.setPipTitle(policyPurposeSaveDto.getPipTitle());
                    policyPurpose.setPipContent(policyPurposeSaveDto.getPipContent());
                    policyPurpose.setInsert_email(email);
                    policyPurpose.setInsert_date(LocalDateTime.now());
                    policyPurposeSaveList.add(policyPurpose);
                }
            }

            optionalPolicy.get().setPiStage(2);
            optionalPolicy.get().setModify_email(email);
            optionalPolicy.get().setModify_date(LocalDateTime.now());

            // 작성중인 개인정보처리방침 업데이트
            policyRepository.save(optionalPolicy.get());

            // 개인정보 처리목적  업데이트
            policyPurposeRepository.saveAll(policyPurposeSaveList);
            policyPurposeRepository.deleteAll(policyPurposeDeleteList);

            historyService.updateHistory(activityHistoryId, companyCode+" - "+" 두번째 뎁스", "", 1);

        } else {
            log.error("존재하지 않은 개인정보처리방침 입니다. 새로고침이후 진행해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO090.getCode(), ResponseErrorCode.KO090.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 개인정보보호 세번째 뎁스 등록
    @Transactional
    public ResponseEntity<Map<String, Object>> privacyPolicyThirdSave(PolicySaveThirdDto policySaveThirdDto, JwtFilterDto jwtFilterDto) {
        log.info("privacyPolicyThirdSave 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();
        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

        Long adminId = adminCompanyInfoDto.getAdminId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();


//        log.info("policySaveThirdDto : "+policySaveThirdDto);

        ActivityCode activityCode;
        String ip = CommonUtil.publicIp();
        Long activityHistoryId;

        // 개인정보 처리방침 작성중 코드
        activityCode = ActivityCode.AC_28;

        Optional<Policy> optionalPolicy = policyRepository.findById(policySaveThirdDto.getPiId());
        if(optionalPolicy.isPresent()) {

            // 활동이력 저장 -> 비정상 모드
            activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    companyCode+" - "+" 세번째 뎁스", "", ip, 0, email);

            List<PolicyBefore> policyBeforeSaveList = new ArrayList<>();
            List<PolicyAfter> policyAfterSaveList = new ArrayList<>();
            List<PolicyServiceAuto> policyServiceAutoSaveList = new ArrayList<>();
            List<PolicyStatute> policyStatuteSaveList = new ArrayList<>();

            PolicyBefore policyBefore;
            PolicyAfter policyAfter;
            PolicyServiceAuto policyServiceAuto;
            PolicyStatute policyStatute;

            List<PolicyBefore> policyBeforeDeleteList = new ArrayList<>();
            List<Long> beforeDeleteIdList = policySaveThirdDto.getPolicyBeforeDeleteIdList();
            for(PolicyBeforeSaveDto policyBeforeSaveDto : policySaveThirdDto.getPolicyBeforeSaveDtoList()) {
                if(policyBeforeSaveDto.getPibId() != 0) {
                    Optional<PolicyBefore> optionalPolicyBefore = policyBeforeRepository.findById(policyBeforeSaveDto.getPibId());
                    if(beforeDeleteIdList.contains(policyBeforeSaveDto.getPibId())) {
                        if(optionalPolicyBefore.isPresent()) {
                            policyBeforeDeleteList.add(optionalPolicyBefore.get());
                        } else {
                            log.error("삭제 privacyPolicyThirdSave 존재하지 않은 항목");
                        }
                    } else {
                        if(optionalPolicyBefore.isPresent()) {
                            optionalPolicyBefore.get().setPibPurpose(policyBeforeSaveDto.getPibPurpose());
                            optionalPolicyBefore.get().setPibInfo(policyBeforeSaveDto.getPibInfo());
                            optionalPolicyBefore.get().setPibChose(policyBeforeSaveDto.getPibChose());
                            optionalPolicyBefore.get().setPibPeriod(policyBeforeSaveDto.getPibPeriod());
                            policyBeforeSaveList.add(optionalPolicyBefore.get());
                        } else {
                            log.error("수정 privacyPolicyThirdSave 존재하지 않은 항목");
                        }
                    }
                } else {
                    policyBefore = new PolicyBefore();
                    policyBefore.setPiId(policySaveThirdDto.getPiId());
                    policyBefore.setPibPurpose(policyBeforeSaveDto.getPibPurpose());
                    policyBefore.setPibInfo(policyBeforeSaveDto.getPibInfo());
                    policyBefore.setPibChose(policyBeforeSaveDto.getPibChose());
                    policyBefore.setPibPeriod(policyBeforeSaveDto.getPibPeriod());
                    policyBefore.setInsert_email(email);
                    policyBefore.setInsert_date(LocalDateTime.now());
                    policyBeforeSaveList.add(policyBefore);
                }
            }

            List<PolicyAfter> policyAfterDeleteList = new ArrayList<>();
            List<Long> afterDeleteIdList = policySaveThirdDto.getPolicyAfterDeleteIdList();
            for(PolicyAfterSaveDto policyAfterSaveDto : policySaveThirdDto.getPolicyAfterSaveDtoList()) {
                if(policyAfterSaveDto.getPiaId() != 0) {
                    Optional<PolicyAfter> optionalPolicyAfter = policyAfterRepository.findById(policyAfterSaveDto.getPiaId());
                    if(afterDeleteIdList.contains(policyAfterSaveDto.getPiaId())) {
                        if(optionalPolicyAfter.isPresent()) {
                            policyAfterDeleteList.add(optionalPolicyAfter.get());
                        } else {
                            log.error("삭제 privacyPolicyThirdSave 존재하지 않은 항목");
                        }
                    } else {
                        if(optionalPolicyAfter.isPresent()) {
                            optionalPolicyAfter.get().setPiaPurpose(policyAfterSaveDto.getPiaPurpose());
                            optionalPolicyAfter.get().setPiaInfo(policyAfterSaveDto.getPiaInfo());
                            optionalPolicyAfter.get().setPiaChose(policyAfterSaveDto.getPiaChose());
                            optionalPolicyAfter.get().setPiaPeriod(policyAfterSaveDto.getPiaPeriod());
                            policyAfterSaveList.add(optionalPolicyAfter.get());
                        } else {
                            log.error("수정 privacyPolicyThirdSave 존재하지 않은 항목");
                        }
                    }
                } else {
                    policyAfter = new PolicyAfter();
                    policyAfter.setPiId(policySaveThirdDto.getPiId());
                    policyAfter.setPiaPurpose(policyAfterSaveDto.getPiaPurpose());
                    policyAfter.setPiaInfo(policyAfterSaveDto.getPiaInfo());
                    policyAfter.setPiaChose(policyAfterSaveDto.getPiaChose());
                    policyAfter.setPiaPeriod(policyAfterSaveDto.getPiaPeriod());
                    policyAfter.setInsert_email(email);
                    policyAfter.setInsert_date(LocalDateTime.now());
                    policyAfterSaveList.add(policyAfter);
                }
            }

            List<PolicyServiceAuto> policyServiceAutoDeleteList = new ArrayList<>();
            List<Long> serviceAutoDeleteIdList = policySaveThirdDto.getPolicyBeforeDeleteIdList();
            for(PolicyServiceAutoSaveDto policyServiceAutoSaveDto : policySaveThirdDto.getPolicyServiceAutoSaveDtoList()) {
                if(policyServiceAutoSaveDto.getPisaId() != 0) {
                    Optional<PolicyServiceAuto> optionalPolicyServiceAuto = policyServiceAutoRepository.findById(policyServiceAutoSaveDto.getPisaId());
                    if(serviceAutoDeleteIdList.contains(policyServiceAutoSaveDto.getPisaId())) {
                        if(optionalPolicyServiceAuto.isPresent()) {
                            policyServiceAutoDeleteList.add(optionalPolicyServiceAuto.get());
                        } else {
                            log.error("삭제 privacyPolicyThirdSave 존재하지 않은 항목");
                        }
                    } else {
                        if(optionalPolicyServiceAuto.isPresent()) {
                            optionalPolicyServiceAuto.get().setPisaPurpose(policyServiceAutoSaveDto.getPisaPurpose());
                            optionalPolicyServiceAuto.get().setPisaInfo(policyServiceAutoSaveDto.getPisaInfo());
                            optionalPolicyServiceAuto.get().setPisaMethodology(policyServiceAutoSaveDto.getPisaMethodology());
                            optionalPolicyServiceAuto.get().setPisaPeriod(policyServiceAutoSaveDto.getPisaPeriod());
                            policyServiceAutoSaveList.add(optionalPolicyServiceAuto.get());
                        } else {
                            log.error("수정 privacyPolicyThirdSave 존재하지 않은 항목");
                        }
                    }
                } else {
                    policyServiceAuto = new PolicyServiceAuto();
                    policyServiceAuto.setPiId(policySaveThirdDto.getPiId());
                    policyServiceAuto.setPisaPurpose(policyServiceAutoSaveDto.getPisaPurpose());
                    policyServiceAuto.setPisaInfo(policyServiceAutoSaveDto.getPisaInfo());
                    policyServiceAuto.setPisaMethodology(policyServiceAutoSaveDto.getPisaMethodology());
                    policyServiceAuto.setPisaPeriod(policyServiceAutoSaveDto.getPisaPeriod());
                    policyServiceAuto.setInsert_email(email);
                    policyServiceAuto.setInsert_date(LocalDateTime.now());
                    policyServiceAutoSaveList.add(policyServiceAuto);
                }
            }

            optionalPolicy.get().setPiStatute(policySaveThirdDto.getPiChoseListString());
            List<PolicyStatute> policyStatuteDeleteList = new ArrayList<>();
            List<Long> policyStatuteDeleteIdList = policySaveThirdDto.getPiChoseCustomDeleteIdList();
            for(piChoseCustomDto piChoseCustomDto : policySaveThirdDto.getPiChoseCustomList()) {
                if(piChoseCustomDto.getPistId() != 0) {
                    Optional<PolicyStatute> optionalPolicyStatute = policyStatuteRepository.findById(piChoseCustomDto.getPistId());
                    if(policyStatuteDeleteIdList.contains(piChoseCustomDto.getPistId())) {
                        if(optionalPolicyStatute.isPresent()) {
                            policyStatuteDeleteList.add(optionalPolicyStatute.get());
                        } else {
                            log.error("삭제 privacyPolicyThirdSave 존재하지 않은 항목");
                        }
                    } else {
                        if(optionalPolicyStatute.isPresent()) {
                            optionalPolicyStatute.get().setPisaTitle(piChoseCustomDto.getPisaTitle());
                            optionalPolicyStatute.get().setPisaContents(piChoseCustomDto.getPisaContents());
                            optionalPolicyStatute.get().setPistPeriod(piChoseCustomDto.getPistPeriod());
                            optionalPolicyStatute.get().setPistCheck(piChoseCustomDto.getPistCheck());
                            policyStatuteSaveList.add(optionalPolicyStatute.get());
                        } else {
                            log.error("수정 PolicyStatute 존재하지 않은 항목");
                        }
                    }
                } else {
                    policyStatute = new PolicyStatute();
                    policyStatute.setPiId(policySaveThirdDto.getPiId());
                    policyStatute.setPisaTitle(piChoseCustomDto.getPisaTitle());
                    policyStatute.setPisaContents(piChoseCustomDto.getPisaContents());
                    policyStatute.setPistPeriod(piChoseCustomDto.getPistPeriod());
                    policyStatute.setPistCheck(piChoseCustomDto.getPistCheck());
                    policyStatute.setInsert_email(email);
                    policyStatute.setInsert_date(LocalDateTime.now());
                    policyStatuteSaveList.add(policyStatute);
                }
            }

            optionalPolicy.get().setPiStage(3);
            optionalPolicy.get().setModify_email(email);
            optionalPolicy.get().setModify_date(LocalDateTime.now());

            // 작성중인 개인정보처리방침 업데이트
            policyRepository.save(optionalPolicy.get());

            // 서비스 가입시 수집하는 개인정보 CUD
            policyBeforeRepository.saveAll(policyBeforeSaveList);
            policyBeforeRepository.deleteAll(policyBeforeDeleteList);

            // 서비스 가입후 수집하는 개인정보 CUD
            policyAfterRepository.saveAll(policyAfterSaveList);
            policyAfterRepository.deleteAll(policyAfterDeleteList);

            // 서비스 이용중 자동생성 및 수집항목 CUD
            policyServiceAutoRepository.saveAll(policyServiceAutoSaveList);
            policyServiceAutoRepository.deleteAll(policyServiceAutoDeleteList);

            // 법령에 따른 개인정보의 보유기간 CUD
            policyStatuteRepository.saveAll(policyStatuteSaveList);
            policyStatuteRepository.deleteAll(policyStatuteDeleteList);

            historyService.updateHistory(activityHistoryId, companyCode+" - "+" 세번째 뎁스", "", 1);

        } else {
            log.error("존재하지 않은 개인정보처리방침 입니다. 새로고침이후 진행해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO090.getCode(), ResponseErrorCode.KO090.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 개인정보보호 네번째 뎁스 등록
    @Transactional
    public ResponseEntity<Map<String, Object>> privacyPolicyFourthSave(PolicySaveFourthDto policySaveFourthDto, JwtFilterDto jwtFilterDto) {
        log.info("privacyPolicyFourthSave 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();
        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

        Long adminId = adminCompanyInfoDto.getAdminId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();


        log.info("policySaveFourthDto : "+policySaveFourthDto);

        ActivityCode activityCode;
        String ip = CommonUtil.publicIp();
        Long activityHistoryId;

        // 개인정보 처리방침 작성중 코드
        activityCode = ActivityCode.AC_28;

        Optional<Policy> optionalPolicy = policyRepository.findById(policySaveFourthDto.getPiId());
        if(optionalPolicy.isPresent()) {

            // 활동이력 저장 -> 비정상 모드
            activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    companyCode+" - "+" 네번째 뎁스", "", ip, 0, email);

            List<PolicyOut> policyOutSaveList = new ArrayList<>();
            List<PolicyOutDetail> policyOutDetailSaveList = new ArrayList<>();

            PolicyOut policyOut;
            PolicyOutDetail policyOutDetail;

            //  처리업무의 위탁에
            List<PolicyOut> policyOutDeleteList = new ArrayList<>();
            List<Long> outDeleteIdList = policySaveFourthDto.getPolicyOutDeleteIdList();
            for(PolicyOutSaveDto policyOutSaveDto : policySaveFourthDto.getPolicyOutSaveDtoList()) {
                if(policyOutSaveDto.getPioId() != 0) {
                    Optional<PolicyOut> optionalPolicyOut = policyOutRepository.findById(policyOutSaveDto.getPioId());
                    if(outDeleteIdList.contains(policyOutSaveDto.getPioId())) {
                        if(optionalPolicyOut.isPresent()) {
                            policyOutDeleteList.add(optionalPolicyOut.get());
                        } else {
                            log.error("삭제 privacyPolicyFourthSave 존재하지 않은 항목");
                        }
                    } else {
                        if(optionalPolicyOut.isPresent()) {
                            optionalPolicyOut.get().setPioOutsourcingCompany(policyOutSaveDto.getPioOutsourcingCompany());
                            optionalPolicyOut.get().setPioChose(policyOutSaveDto.getPioChose());
                            optionalPolicyOut.get().setPioConsignmentCompany(policyOutSaveDto.getPioConsignmentCompany());
                            optionalPolicyOut.get().setPioPeriod(policyOutSaveDto.getPioPeriod());
                            policyOutSaveList.add(optionalPolicyOut.get());
                        } else {
                            log.error("수정 privacyPolicyFourthSave 존재하지 않은 항목");
                        }
                    }
                } else {
                    policyOut = new PolicyOut();
                    policyOut.setPiId(policySaveFourthDto.getPiId());
                    policyOut.setPioOutsourcingCompany(policyOutSaveDto.getPioOutsourcingCompany());
                    policyOut.setPioChose(policyOutSaveDto.getPioChose());
                    policyOut.setPioConsignmentCompany(policyOutSaveDto.getPioConsignmentCompany());
                    policyOut.setPioPeriod(policyOutSaveDto.getPioPeriod());
                    policyOut.setInsert_email(email);
                    policyOut.setInsert_date(LocalDateTime.now());
                    policyOutSaveList.add(policyOut);
                }
            }

            // 처리업무의 국외 위탁
            List<PolicyOutDetail> policyOutDetailDeleteList = new ArrayList<>();
            List<Long> outDetailDeleteIdList = policySaveFourthDto.getPolicyOutDetailDeleteIdList();
            for(PolicyOutDetailSaveDto policyOutDetailSaveDto : policySaveFourthDto.getPolicyOutDetailSaveDtoList()) {
                if(policyOutDetailSaveDto.getPiodId() != 0) {
                    Optional<PolicyOutDetail> optionalPolicyOutDetail = policyOutDetailRepository.findById(policyOutDetailSaveDto.getPiodId());
                    if(outDetailDeleteIdList.contains(policyOutDetailSaveDto.getPiodId())) {
                        if(optionalPolicyOutDetail.isPresent()) {
                            policyOutDetailDeleteList.add(optionalPolicyOutDetail.get());
                        } else {
                            log.error("삭제 privacyPolicyFourthSave 존재하지 않은 항목");
                        }
                    } else {
                        if(optionalPolicyOutDetail.isPresent()) {
                            optionalPolicyOutDetail.get().setPiodCompany(policyOutDetailSaveDto.getPiodCompany());
                            optionalPolicyOutDetail.get().setPiodLocation(policyOutDetailSaveDto.getPiodLocation());
                            optionalPolicyOutDetail.get().setPiodMethod(policyOutDetailSaveDto.getPiodMethod());
                            optionalPolicyOutDetail.get().setPiodContact(policyOutDetailSaveDto.getPiodContact());
                            optionalPolicyOutDetail.get().setPiodInfo(policyOutDetailSaveDto.getPiodInfo());
                            optionalPolicyOutDetail.get().setPiodDetail(policyOutDetailSaveDto.getPiodDetail());
                            optionalPolicyOutDetail.get().setPiodPeriod(policyOutDetailSaveDto.getPiodPeriod());
                            policyOutDetailSaveList.add(optionalPolicyOutDetail.get());
                        } else {
                            log.error("수정 privacyPolicyFourthSave 존재하지 않은 항목");
                        }
                    }
                } else {
                    policyOutDetail = new PolicyOutDetail();
                    policyOutDetail.setPiId(policySaveFourthDto.getPiId());
                    policyOutDetail.setPiodCompany(policyOutDetailSaveDto.getPiodCompany());
                    policyOutDetail.setPiodLocation(policyOutDetailSaveDto.getPiodLocation());
                    policyOutDetail.setPiodMethod(policyOutDetailSaveDto.getPiodMethod());
                    policyOutDetail.setPiodContact(policyOutDetailSaveDto.getPiodContact());
                    policyOutDetail.setPiodInfo(policyOutDetailSaveDto.getPiodInfo());
                    policyOutDetail.setPiodDetail(policyOutDetailSaveDto.getPiodDetail());
                    policyOutDetail.setPiodPeriod(policyOutDetailSaveDto.getPiodPeriod());
                    policyOutDetail.setInsert_email(email);
                    policyOutDetail.setInsert_date(LocalDateTime.now());
                    policyOutDetailSaveList.add(policyOutDetail);
                }
            }

            optionalPolicy.get().setPiStage(4);
            optionalPolicy.get().setModify_email(email);
            optionalPolicy.get().setModify_date(LocalDateTime.now());

            // 작성중인 개인정보처리방침 업데이트
            policyRepository.save(optionalPolicy.get());

            // 처리업무의 위탁에 관한사항 CUD
            policyOutRepository.saveAll(policyOutSaveList);
            policyOutRepository.deleteAll(policyOutDeleteList);

            // 처리업무의 국외 위탁에 관한사항 CUD
            policyOutDetailRepository.saveAll(policyOutDetailSaveList);
            policyOutDetailRepository.deleteAll(policyOutDetailDeleteList);

            historyService.updateHistory(activityHistoryId, companyCode+" - "+" 네번째 뎁스", "", 1);

        } else {
            log.error("존재하지 않은 개인정보처리방침 입니다. 새로고침이후 진행해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO090.getCode(), ResponseErrorCode.KO090.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 개인정보보호 다섯번째 뎁스 등록
    @Transactional
    public ResponseEntity<Map<String, Object>> privacyPolicyFifthSave(PolicySaveFifthDto policySaveFifthDto, JwtFilterDto jwtFilterDto) {
        log.info("privacyPolicyFifthSave 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();
        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

        Long adminId = adminCompanyInfoDto.getAdminId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();


//        log.info("policySaveFifthDto : "+policySaveFifthDto);

        ActivityCode activityCode;
        String ip = CommonUtil.publicIp();
        Long activityHistoryId;

        // 개인정보 처리방침 작성중 코드
        activityCode = ActivityCode.AC_28;

        Optional<Policy> optionalPolicy = policyRepository.findById(policySaveFifthDto.getPiId());
        if(optionalPolicy.isPresent()) {

            // 활동이력 저장 -> 비정상 모드
            activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    companyCode+" - "+" 네번째 뎁스", "", ip, 0, email);

            List<PolicyThird> policyThirdSaveList = new ArrayList<>();
            PolicyThird policyThird;

            List<PolicyThirdOverseas> policyThirdOverseasSaveList = new ArrayList<>();
            PolicyThirdOverseas policyThirdOverseas;

            // 개인정보처리방침의 제3자 제공 항목
            List<PolicyThird> policyThirdDeleteList = new ArrayList<>();
            List<Long> thirdDeleteIdList = policySaveFifthDto.getPolicyThirdDeleteIdList();
            for(PolicyThirdSaveDto policyThirdSaveDto : policySaveFifthDto.getPolicyThirdSaveDtoList()) {
                if(policyThirdSaveDto.getPitId() != 0) {
                    Optional<PolicyThird> optionalPolicyThird = policyThirdRepository.findById(policyThirdSaveDto.getPitId());
                    if(thirdDeleteIdList.contains(policyThirdSaveDto.getPitId())) {
                        if(optionalPolicyThird.isPresent()) {
                            policyThirdDeleteList.add(optionalPolicyThird.get());
                        } else {
                            log.error("삭제 privacyPolicyFifthSave 존재하지 않은 항목");
                        }
                    } else {
                        if(optionalPolicyThird.isPresent()) {
                            optionalPolicyThird.get().setPitRecipient(policyThirdSaveDto.getPitRecipient());
                            optionalPolicyThird.get().setPitPurpose(policyThirdSaveDto.getPitPurpose());
                            optionalPolicyThird.get().setPitInfo(policyThirdSaveDto.getPitInfo());
                            optionalPolicyThird.get().setPitPeriod(policyThirdSaveDto.getPitPeriod());
                            policyThirdSaveList.add(optionalPolicyThird.get());
                        } else {
                            log.error("수정 privacyPolicyFifthSave 존재하지 않은 항목");
                        }
                    }
                } else {
                    policyThird = new PolicyThird();
                    policyThird.setPiId(policySaveFifthDto.getPiId());
                    policyThird.setPitRecipient(policyThirdSaveDto.getPitRecipient());
                    policyThird.setPitPurpose(policyThirdSaveDto.getPitPurpose());
                    policyThird.setPitInfo(policyThirdSaveDto.getPitInfo());
                    policyThird.setPitPeriod(policyThirdSaveDto.getPitPeriod());
                    policyThird.setInsert_email(email);
                    policyThird.setInsert_date(LocalDateTime.now());
                    policyThirdSaveList.add(policyThird);
                }
            }

            // 개인정보처리방침의 국외 제3자 제공 항목
            List<PolicyThirdOverseas> policyThirdOverseasDeleteList = new ArrayList<>();
            List<Long> thirdOverseasDeleteIdList = policySaveFifthDto.getPolicyThirdOverseasDeleteIdList();
            for(PolicyThirdOverseasSaveDto policyThirdOverseasSaveDto : policySaveFifthDto.getPolicyThirdOverseasSaveDtoList()) {
                if(policyThirdOverseasSaveDto.getPitoId() != 0) {
                    Optional<PolicyThirdOverseas> optionalPolicyThirdOverseas = policyThirdOverseasRepository.findById(policyThirdOverseasSaveDto.getPitoId());
                    if(thirdOverseasDeleteIdList.contains(policyThirdOverseasSaveDto.getPitoId())) {
                        if(optionalPolicyThirdOverseas.isPresent()) {
                            policyThirdOverseasDeleteList.add(optionalPolicyThirdOverseas.get());
                        } else {
                            log.error("삭제 privacyPolicyFifthSave 존재하지 않은 항목");
                        }
                    } else {
                        if(optionalPolicyThirdOverseas.isPresent()) {
                            optionalPolicyThirdOverseas.get().setPitoRecipient(policyThirdOverseasSaveDto.getPitoRecipient());
                            optionalPolicyThirdOverseas.get().setPitoLocation(policyThirdOverseasSaveDto.getPitoLocation());
                            optionalPolicyThirdOverseas.get().setPitoPurpose(policyThirdOverseasSaveDto.getPitoPurpose());
                            optionalPolicyThirdOverseas.get().setPitoInfo(policyThirdOverseasSaveDto.getPitoInfo());
                            optionalPolicyThirdOverseas.get().setPitoPeriod(policyThirdOverseasSaveDto.getPitoPeriod());
                            policyThirdOverseasSaveList.add(optionalPolicyThirdOverseas.get());
                        } else {
                            log.error("수정 privacyPolicyFifthSave 존재하지 않은 항목");
                        }
                    }
                } else {
                    policyThirdOverseas = new PolicyThirdOverseas();
                    policyThirdOverseas.setPiId(policySaveFifthDto.getPiId());
                    policyThirdOverseas.setPitoRecipient(policyThirdOverseasSaveDto.getPitoRecipient());
                    policyThirdOverseas.setPitoLocation(policyThirdOverseasSaveDto.getPitoLocation());
                    policyThirdOverseas.setPitoPurpose(policyThirdOverseasSaveDto.getPitoPurpose());
                    policyThirdOverseas.setPitoInfo(policyThirdOverseasSaveDto.getPitoInfo());
                    policyThirdOverseas.setPitoPeriod(policyThirdOverseasSaveDto.getPitoPeriod());
                    policyThirdOverseas.setInsert_email(email);
                    policyThirdOverseas.setInsert_date(LocalDateTime.now());
                    policyThirdOverseasSaveList.add(policyThirdOverseas);
                }
            }

            // 개인정보처리방침의 제3자 제공에 관한사항 CUD
            policyThirdRepository.saveAll(policyThirdSaveList);
            policyThirdRepository.deleteAll(policyThirdDeleteList);

            // 개인정보처리방침의 국외 제3자 제공에 관한사항 CUD
            policyThirdOverseasRepository.saveAll(policyThirdOverseasSaveList);
            policyThirdOverseasRepository.deleteAll(policyThirdOverseasDeleteList);

            optionalPolicy.get().setPiStage(5);
            optionalPolicy.get().setModify_email(email);
            optionalPolicy.get().setModify_date(LocalDateTime.now());

            historyService.updateHistory(activityHistoryId, companyCode+" - "+" 네번째 뎁스", "", 1);

        } else {
            log.error("존재하지 않은 개인정보처리방침 입니다. 새로고침이후 진행해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO090.getCode(), ResponseErrorCode.KO090.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 개인정보보호 여섯번째 뎁스 등록
    @Transactional
    public ResponseEntity<Map<String, Object>> privacyPolicySixthSave(PolicySaveSixthDto policySaveSixthDto, JwtFilterDto jwtFilterDto) {
        log.info("privacyPolicySixthSave 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();
        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

        Long adminId = adminCompanyInfoDto.getAdminId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();


//        log.info("policySaveSixthDto : "+policySaveSixthDto);

        ActivityCode activityCode;
        String ip = CommonUtil.publicIp();
        Long activityHistoryId;

        // 개인정보 처리방침 작성중 코드
        activityCode = ActivityCode.AC_28;

        Optional<Policy> optionalPolicy = policyRepository.findById(policySaveSixthDto.getPiId());
        if(optionalPolicy.isPresent()) {

            // 활동이력 저장 -> 비정상 모드
            activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    companyCode + " - " + " 여섯번째 뎁스", "", ip, 0, email);

            List<PolicyResponsible> policyResponsibleSaveList = new ArrayList<>();
            PolicyResponsible policyResponsible;

            // 책임자 항목
            List<PolicyResponsible> policyResponsibleDeleteList = new ArrayList<>();
            List<Long> responsibleDeleteIdList = policySaveSixthDto.getPolicyResponsibleDeleteIdList();
            for (PolicyResponsibleSaveDto policyResponsibleSaveDto : policySaveSixthDto.getPolicyResponsibleSaveDtoList()) {
                if (policyResponsibleSaveDto.getPirId() != 0) {
                    Optional<PolicyResponsible> optionalPolicyResponsible = policyResponsibleRepository.findById(policyResponsibleSaveDto.getPirId());
                    if (responsibleDeleteIdList.contains(policyResponsibleSaveDto.getPirId())) {
                        if (optionalPolicyResponsible.isPresent()) {
                            policyResponsibleDeleteList.add(optionalPolicyResponsible.get());
                        } else {
                            log.error("삭제 privacyPolicySixthSave 존재하지 않은 항목");
                        }
                    } else {
                        if (optionalPolicyResponsible.isPresent()) {
                            optionalPolicyResponsible.get().setPirName(policyResponsibleSaveDto.getPirName());
                            optionalPolicyResponsible.get().setPirPosition(policyResponsibleSaveDto.getPirPosition());
                            optionalPolicyResponsible.get().setPirEmail(policyResponsibleSaveDto.getPirEmail());
                            optionalPolicyResponsible.get().setPirContact(policyResponsibleSaveDto.getPirContact());
                            optionalPolicyResponsible.get().setPirDepartment(policyResponsibleSaveDto.getPirDepartment());
                            policyResponsibleSaveList.add(optionalPolicyResponsible.get());
                        } else {
                            log.error("수정 privacyPolicySixthSave 존재하지 않은 항목");
                        }
                    }
                } else {
                    policyResponsible = new PolicyResponsible();
                    policyResponsible.setPiId(policySaveSixthDto.getPiId());
                    policyResponsible.setPirName(policyResponsibleSaveDto.getPirName());
                    policyResponsible.setPirPosition(policyResponsibleSaveDto.getPirPosition());
                    policyResponsible.setPirEmail(policyResponsibleSaveDto.getPirEmail());
                    policyResponsible.setPirContact(policyResponsibleSaveDto.getPirContact());
                    policyResponsible.setPirDepartment(policyResponsibleSaveDto.getPirDepartment());
                    policyResponsible.setInsert_email(email);
                    policyResponsible.setInsert_date(LocalDateTime.now());
                    policyResponsibleSaveList.add(policyResponsible);
                }
            }

            // 책임자 항목에 관한사항 CUD
            policyResponsibleRepository.saveAll(policyResponsibleSaveList);
            policyResponsibleRepository.deleteAll(policyResponsibleDeleteList);

            optionalPolicy.get().setPiYear(policySaveSixthDto.getPiYear());
            optionalPolicy.get().setPiMonth(policySaveSixthDto.getPiMonth());
            optionalPolicy.get().setPiDay(policySaveSixthDto.getPiDay());

            optionalPolicy.get().setPiStage(6);
            optionalPolicy.get().setModify_email(email);
            optionalPolicy.get().setModify_date(LocalDateTime.now());

            policyRepository.save(optionalPolicy.get());

            historyService.updateHistory(activityHistoryId, companyCode+" - "+" 여섯번째 뎁스", "", 1);

        } else {
            log.error("존재하지 않은 개인정보처리방침 입니다. 새로고침이후 진행해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO090.getCode(), ResponseErrorCode.KO090.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 개인정보보호 마지막 뎁스 등록
    @Transactional
    public ResponseEntity<Map<String, Object>> privacyPolicyFinalSave(Long piId, JwtFilterDto jwtFilterDto) {
        log.info("privacyPolicyFinalSave 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();
        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

        Long adminId = adminCompanyInfoDto.getAdminId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();
        log.info("adminId : "+adminId);
        log.info("companyCode : "+companyCode);

        log.info("piId : "+piId);

        ActivityCode activityCode;
        String ip = CommonUtil.publicIp();
        Long activityHistoryId;

        // 개인정보 처리방침 작성중 코드
        activityCode = ActivityCode.AC_29;

        Optional<Policy> optionalPolicy = policyRepository.findById(piId);
        if(optionalPolicy.isPresent()) {

            // 활동이력 저장 -> 비정상 모드
            activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    companyCode + " - " + " 마지막 뎁스", "", ip, 0, email);

            optionalPolicy.get().setPiStage(7);
            optionalPolicy.get().setPiAutosave(1);
            optionalPolicy.get().setModify_email(email);
            optionalPolicy.get().setModify_date(LocalDateTime.now());

            policyRepository.save(optionalPolicy.get());

            historyService.updateHistory(activityHistoryId, companyCode+" - "+" 마지막 뎁스", "", 1);
            
        } else {
            log.error("존재하지 않은 개인정보처리방침 입니다. 새로고침이후 진행해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO090.getCode(), ResponseErrorCode.KO090.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }
    
}
