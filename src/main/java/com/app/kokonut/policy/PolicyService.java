package com.app.kokonut.policy;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.realcomponent.CommonUtil;
import com.app.kokonut.common.realcomponent.Utils;
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

import java.io.IOException;
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
                         PolicyOutRepository policyOutRepository, PolicyOutDetailRepository policyOutDetailRepository,
                         PolicyThirdRepository policyThirdRepository, PolicyThirdOverseasRepository policyThirdOverseasRepository, PolicyResponsibleRepository policyResponsibleRepository){
        this.historyService = historyService;
        this.adminRepository = adminRepository;
        this.policyRepository = policyRepository;
        this.policyPurposeRepository = policyPurposeRepository;
        this.policyBeforeRepository = policyBeforeRepository;
        this.policyAfterRepository = policyAfterRepository;
        this.policyServiceAutoRepository = policyServiceAutoRepository;
        this.policyOutRepository = policyOutRepository;
        this.policyOutDetailRepository = policyOutDetailRepository;
        this.policyThirdRepository = policyThirdRepository;
        this.policyThirdOverseasRepository = policyThirdOverseasRepository;
        this.policyResponsibleRepository = policyResponsibleRepository;
    }

    // 개인정보처리방침 리스트 조회
    public ResponseEntity<Map<String, Object>> policyList(String searchText, String stime, String filterDate, JwtFilterDto jwtFilterDto, Pageable pageable) throws IOException {
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
        String ip = CommonUtil.clientIp();
        Long activityHistoryId;

        // 개인정보 처리방침 리스트조회 코드
        activityCode = ActivityCode.AC_46;

        // 활동이력 저장 -> 비정상 모드
        activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, CommonUtil.publicIp(), 0, email);

        Page<PolicyListDto> policyListDtos = policyRepository.findByPolicyList(policySearchDto, pageable);

        historyService.updateHistory(activityHistoryId,
                cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", 1);

        if(policyListDtos.getTotalPages() == 0) {
            log.info("조회된 데이터가 없습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO003.getCode(), ResponseErrorCode.KO003.getDesc()));
        } else {
            return ResponseEntity.ok(res.ResponseEntityPage(policyListDtos));
        }
    }

    // 개인정보처리방침 상세내용 조회
    public ResponseEntity<Map<String, Object>> policyDetail(Long piId, JwtFilterDto jwtFilterDto) throws IOException {
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

            // Step4
            List<PolicyOutSaveInfoListDto> policyOutSaveInfoListDtos = policyOutRepository.findByPolicyOutList(piId);
            data.put("outDataList", policyOutSaveInfoListDtos);
            if(policyDetailDto.getPiOutChose()) {
                List<PolicyOutDetailSaveInfoListDto> policyOutDetailSaveInfoListDtos = policyOutDetailRepository.findByPolicyOutDetailList(piId);
                data.put("outDetailDataList", policyOutDetailSaveInfoListDtos);
            }

            // Step5
            if(policyDetailDto.getPiThirdChose()) {
                List<PolicyThirdSaveInfoListDto> policyThirdSaveInfoListDtoList = policyThirdRepository.findByPolicyThirdList(piId);
                data.put("thirdDataList", policyThirdSaveInfoListDtoList);
            }
            if(policyDetailDto.getPiThirdOverseasChose()) {
                List<PolicyThirdOverseasSaveInfoListDto> policyThirdOverseasSaveInfoListDtoList = policyThirdOverseasRepository.findByPolicyThirdOverseasList(piId);
                data.put("thirdOverseasDataList", policyThirdOverseasSaveInfoListDtoList);
            }

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
    public ResponseEntity<Map<String, Object>> privacyPolicyDelete(Long piId, JwtFilterDto jwtFilterDto) throws IOException {
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
            String ip = CommonUtil.clientIp();
            Long activityHistoryId;

            int piStage = optionalPolicy.get().getPiStage();
            if(optionalPolicy.get().getPiAutosave() == 0) {
                // 개인정보 처리방침 작성중 코드
                activityCode = ActivityCode.AC_43;

                // 활동이력 저장 -> 비정상 모드
                activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                        companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, CommonUtil.publicIp(), 0, email);

                if(piStage < 6) {

//                    if(piStage == 2) {
//                        // 개인정보 처리목적까지 삭제
//
//                    }
//
//                    if(piStage == 3) {
//                        // 서비스가입시 수집하는 개인정보 삭제
//
//                        // 수집하는정보까지 삭제
//
//                    }
//
//                    if(piStage == 4) {
//                        // 처리업무의 위탁에 관한사항 삭제
//
//                        // 처리업무의 국외 위탁에 관한사항 삭제(선택사항)
//                        if(optionalPolicy.get().getPiOutChose() == 1) {
//
//                        }
//                    }
//
//                    if(piStage == 5) {
//                        // 제3자 삭제(선택사항)
//                        if(optionalPolicy.get().getPiThirdChose() == 1) {
//
//                        }
//
//                        // 제3자 국외 삭제(선택사항)
//                        if(optionalPolicy.get().getPiThirdOverseasChose() == 1) {
//
//                        }
//                    }

                    policyRepository.delete(optionalPolicy.get());
                }

                historyService.updateHistory(activityHistoryId,
                        companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", 1);
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
        List<PolicyBeforeSaveInfoListDto> policyBeforeSaveInfoListDtos = policyBeforeRepository.findByPolicyBeforeList(piId);
        data.put("beforeDataList", policyBeforeSaveInfoListDtos);
        List<PolicyAfterSaveInfoListDto> policyAfterSaveInfoListDtos = policyAfterRepository.findByPolicyAfterList(piId);
        data.put("afterDataList", policyAfterSaveInfoListDtos);
        List<PolicyServiceAutoSaveInfoListDto> policyServiceAutoSaveInfoListDtoList = policyServiceAutoRepository.findByPolicyServiceAutoList(piId);
        data.put("serviceAutoDataList", policyServiceAutoSaveInfoListDtoList);
        PolicySecondInfoDto policySecondInfoDto = policyRepository.findByPolicySecond(piId);
        data.put("policyInfo2", policySecondInfoDto);

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
    public ResponseEntity<Map<String, Object>> privacyPolicyFirstSave(PolicySaveFirstDto policySaveFirstDto, JwtFilterDto jwtFilterDto) throws IOException {
        log.info("24privacyPolicyFirstSave 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();
        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

        Long adminId = adminCompanyInfoDto.getAdminId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();

        log.info("policySaveFirstDto : "+policySaveFirstDto);

        ActivityCode activityCode;
        String ip = CommonUtil.clientIp();
        Long activityHistoryId;

        // 개인정보 처리방침 작성중 코드
        activityCode = ActivityCode.AC_28;

        // 활동이력 저장 -> 비정상 모드
        activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                companyCode+" - "+activityCode.getDesc()+" 첫번째 뎁스 시도 이력", "", ip, CommonUtil.publicIp(), 0, email);

//        String getPiDate = policySaveFirstDto.getPiDate()+" 00:00:00.000";
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy. MM. dd HH:mm:ss.SSS");
//        LocalDateTime piDate = LocalDateTime.parse(getPiDate, formatter);
//        log.info("piDate : "+piDate);

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

        historyService.updateHistory(activityHistoryId,
                companyCode+" - "+activityCode.getDesc()+" 첫번째 뎁스 시도 이력", "", 1);

        data.put("saveId",policySave.getPiId());

        return ResponseEntity.ok(res.success(data));
    }

    // 개인정보보호 두번쨰 뎁스 등록
    @Transactional
    public ResponseEntity<Map<String, Object>> privacyPolicySecondSave(PolicySaveSecondDto policySaveSecondDto, JwtFilterDto jwtFilterDto) throws IOException {
        log.info("privacyPolicySecondSave 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();
        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

        Long adminId = adminCompanyInfoDto.getAdminId();
        Long companyId = adminCompanyInfoDto.getCompanyId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();
//        log.info("adminId : "+adminId);
//        log.info("companyId : "+companyId);
//        log.info("companyCode : "+companyCode);

//        log.info("policySaveSecondDto : "+policySaveSecondDto);

        ActivityCode activityCode;
        String ip = CommonUtil.clientIp();
        Long activityHistoryId;

        // 개인정보 처리방침 작성중 코드
        activityCode = ActivityCode.AC_28;

        Optional<Policy> optionalPolicy = policyRepository.findById(policySaveSecondDto.getPiId());
        if(optionalPolicy.isPresent()) {
            // 활동이력 저장 -> 비정상 모드
            activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    companyCode+" - "+activityCode.getDesc()+" 두번째 뎁스 시도 이력", "", ip, CommonUtil.publicIp(), 0, email);

            List<PolicyPurpose> policyPurposeSaveList = new ArrayList<>();
            PolicyPurpose policyPurpose;
            for(PolicyPurposeSaveDto policyPurposeSaveDto : policySaveSecondDto.getPolicyPurposeSaveDtoList()) {
                if(policyPurposeSaveDto.getPipId() != 0) {
                    Optional<PolicyPurpose> optionalPolicyPurpose = policyPurposeRepository.findById(policyPurposeSaveDto.getPipId());
                    if(optionalPolicyPurpose.isPresent()) {
                        optionalPolicyPurpose.get().setPipTitle(policyPurposeSaveDto.getPipTitle());
                        optionalPolicyPurpose.get().setPipContent(policyPurposeSaveDto.getPipContent());
                        policyPurposeSaveList.add(optionalPolicyPurpose.get());
                    } else {
                        log.error("수정 privacyPolicySecondSave 존재하지 않은 항목");
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

            List<PolicyPurpose> policyPurposeDeleteList = new ArrayList<>();
            List<Long> purposeDeleteIdList = policySaveSecondDto.getPolicyPurposeDeleteIdList();
            for(Long pipId : purposeDeleteIdList) {
                Optional<PolicyPurpose> optionalPolicyPurpose = policyPurposeRepository.findById(pipId);
                if(optionalPolicyPurpose.isPresent()) {
                    policyPurposeDeleteList.add(optionalPolicyPurpose.get());
                } else {
                    log.error("삭제 privacyPolicySecondSave 존재하지 않은 항목");
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

            historyService.updateHistory(activityHistoryId,
                    companyCode+" - "+activityCode.getDesc()+" 두번째 뎁스 시도 이력", "", 1);

        } else {
            log.error("존재하지 않은 개인정보처리방침 입니다. 새로고침이후 진행해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO090.getCode(), ResponseErrorCode.KO090.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 개인정보보호 세번쨰 뎁스 등록
    public ResponseEntity<Map<String, Object>> privacyPolicyThirdSave(PolicySaveThirdDto policySaveThirdDto, JwtFilterDto jwtFilterDto) throws IOException {
        log.info("privacyPolicyThirdSave 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();
        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

        Long adminId = adminCompanyInfoDto.getAdminId();
        Long companyId = adminCompanyInfoDto.getCompanyId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();
//        log.info("adminId : "+adminId);
//        log.info("companyId : "+companyId);
//        log.info("companyCode : "+companyCode);

        log.info("policySaveThirdDto : "+policySaveThirdDto);

        ActivityCode activityCode;
        String ip = CommonUtil.clientIp();
        Long activityHistoryId;

        // 개인정보 처리방침 작성중 코드
        activityCode = ActivityCode.AC_28;

        Optional<Policy> optionalPolicy = policyRepository.findById(policySaveThirdDto.getPiId());
        if(optionalPolicy.isPresent()) {

            // 활동이력 저장 -> 비정상 모드
            activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    companyCode+" - "+activityCode.getDesc()+" 세번째 뎁스 시도 이력", "", ip, CommonUtil.publicIp(), 0, email);

            List<PolicyBefore> policyBeforeSaveList = new ArrayList<>();
            List<PolicyAfter> policyAfterSaveList = new ArrayList<>();
            List<PolicyServiceAuto> policyServiceAutoSaveList = new ArrayList<>();

            PolicyBefore policyBefore;
            PolicyAfter policyAfter;
            PolicyServiceAuto policyServiceAuto;

            for(PolicyBeforeSaveDto policyBeforeSaveDto : policySaveThirdDto.getPolicyBeforeSaveDtoList()) {
                if(policyBeforeSaveDto.getPibId() != 0) {
                    Optional<PolicyBefore> optionalPolicyBefore = policyBeforeRepository.findById(policyBeforeSaveDto.getPibId());
                    if(optionalPolicyBefore.isPresent()) {
                        optionalPolicyBefore.get().setPibPurpose(policyBeforeSaveDto.getPibPurpose());
                        optionalPolicyBefore.get().setPibInfo(policyBeforeSaveDto.getPibInfo());
                        optionalPolicyBefore.get().setPibChose(policyBeforeSaveDto.getPibChose());
                        optionalPolicyBefore.get().setPibPeriod(policyBeforeSaveDto.getPibPeriod());
                        policyBeforeSaveList.add(optionalPolicyBefore.get());
                    } else {
                        log.error("수정 privacyPolicyThirdSave 존재하지 않은 항목");
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

            List<PolicyBefore> policyBeforeDeleteList = new ArrayList<>();
            List<Long> beforeDeleteIdList = policySaveThirdDto.getPolicyBeforeDeleteIdList();
            for(Long pibId : beforeDeleteIdList) {
                Optional<PolicyBefore> optionalPolicyBefore = policyBeforeRepository.findById(pibId);
                if(optionalPolicyBefore.isPresent()) {
                    policyBeforeDeleteList.add(optionalPolicyBefore.get());
                } else {
                    log.error("삭제 privacyPolicyThirdSave 존재하지 않은 항목");
                }
            }

            for(PolicyAfterSaveDto policyAfterSaveDto : policySaveThirdDto.getPolicyAfterSaveDtoList()) {
                if(policyAfterSaveDto.getPiaId() != 0) {
                    Optional<PolicyAfter> optionalPolicyAfter = policyAfterRepository.findById(policyAfterSaveDto.getPiaId());
                    if(optionalPolicyAfter.isPresent()) {
                        optionalPolicyAfter.get().setPiaPurpose(policyAfterSaveDto.getPiaPurpose());
                        optionalPolicyAfter.get().setPiaInfo(policyAfterSaveDto.getPiaInfo());
                        optionalPolicyAfter.get().setPiaChose(policyAfterSaveDto.getPiaChose());
                        optionalPolicyAfter.get().setPiaPeriod(policyAfterSaveDto.getPiaPeriod());
                        policyAfterSaveList.add(optionalPolicyAfter.get());
                    } else {
                        log.error("수정 privacyPolicyThirdSave 존재하지 않은 항목");
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

            List<PolicyAfter> policyAfterDeleteList = new ArrayList<>();
            List<Long> afterDeleteIdList = policySaveThirdDto.getPolicyAfterDeleteIdList();
            for(Long piaId : afterDeleteIdList) {
                Optional<PolicyAfter> optionalPolicyAfter = policyAfterRepository.findById(piaId);
                if(optionalPolicyAfter.isPresent()) {
                    policyAfterDeleteList.add(optionalPolicyAfter.get());
                } else {
                    log.error("삭제 privacyPolicyThirdSave 존재하지 않은 항목");
                }
            }

            for(PolicyServiceAutoSaveDto policyServiceAutoSaveDto : policySaveThirdDto.getPolicyServiceAutoSaveDtoList()) {
                if(policyServiceAutoSaveDto.getPisaId() != 0) {
                    Optional<PolicyServiceAuto> optionalPolicyServiceAuto = policyServiceAutoRepository.findById(policyServiceAutoSaveDto.getPisaId());
                    if(optionalPolicyServiceAuto.isPresent()) {
                        optionalPolicyServiceAuto.get().setPisaPurpose(policyServiceAutoSaveDto.getPisaPurpose());
                        optionalPolicyServiceAuto.get().setPisaInfo(policyServiceAutoSaveDto.getPisaInfo());
                        optionalPolicyServiceAuto.get().setPisaMethodology(policyServiceAutoSaveDto.getPisaMethodology());
                        optionalPolicyServiceAuto.get().setPisaPeriod(policyServiceAutoSaveDto.getPisaPeriod());
                        policyServiceAutoSaveList.add(optionalPolicyServiceAuto.get());
                    } else {
                        log.error("수정 privacyPolicyThirdSave 존재하지 않은 항목");
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
            List<PolicyServiceAuto> policyServiceAutoDeleteList = new ArrayList<>();
            List<Long> serviceAutoDeleteIdList = policySaveThirdDto.getPolicyBeforeDeleteIdList();
            for(Long pisaId : serviceAutoDeleteIdList) {
                Optional<PolicyServiceAuto> optionalPolicyServiceAuto = policyServiceAutoRepository.findById(pisaId);
                if(optionalPolicyServiceAuto.isPresent()) {
                    policyServiceAutoDeleteList.add(optionalPolicyServiceAuto.get());
                } else {
                    log.error("삭제 privacyPolicyThirdSave 존재하지 않은 항목");
                }
            }

            if(policySaveThirdDto.getPiInternetChose() == null || !policySaveThirdDto.getPiInternetChose()) {
                optionalPolicy.get().setPiInternetChose(false);
            } else {
                optionalPolicy.get().setPiInternetChose(true);
            }
            if(policySaveThirdDto.getPiContractChose() == null || !policySaveThirdDto.getPiContractChose()) {
                optionalPolicy.get().setPiContractChose(false);
            } else {
                optionalPolicy.get().setPiContractChose(true);
            }
            if(policySaveThirdDto.getPiPayChose() == null || !policySaveThirdDto.getPiPayChose()) {
                optionalPolicy.get().setPiPayChose(false);
            } else {
                optionalPolicy.get().setPiPayChose(true);
            }
            if(policySaveThirdDto.getPiConsumerChose() == null || !policySaveThirdDto.getPiConsumerChose()) {
                optionalPolicy.get().setPiConsumerChose(false);
            } else {
                optionalPolicy.get().setPiConsumerChose(true);
            }
            if(policySaveThirdDto.getPiAdvertisementChose() == null || !policySaveThirdDto.getPiAdvertisementChose()) {
                optionalPolicy.get().setPiAdvertisementChose(false);
            } else {
                optionalPolicy.get().setPiAdvertisementChose(true);
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

            historyService.updateHistory(activityHistoryId,
                    companyCode+" - "+activityCode.getDesc()+" 세번째 뎁스 시도 이력", "", 1);

        } else {
            log.error("존재하지 않은 개인정보처리방침 입니다. 새로고침이후 진행해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO090.getCode(), ResponseErrorCode.KO090.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 개인정보보호 네번쨰 뎁스 등록
    public ResponseEntity<Map<String, Object>> privacyPolicyFourthSave(PolicySaveFourthDto policySaveFourthDto, JwtFilterDto jwtFilterDto) throws IOException {
        log.info("privacyPolicyFourthSave 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();
        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

        Long adminId = adminCompanyInfoDto.getAdminId();
        Long companyId = adminCompanyInfoDto.getCompanyId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();
//        log.info("adminId : "+adminId);
//        log.info("companyId : "+companyId);
//        log.info("companyCode : "+companyCode);

        log.info("policySaveFourthDto : "+policySaveFourthDto);

        ActivityCode activityCode;
        String ip = CommonUtil.clientIp();
        Long activityHistoryId;

        // 개인정보 처리방침 작성중 코드
        activityCode = ActivityCode.AC_28;

        Optional<Policy> optionalPolicy = policyRepository.findById(policySaveFourthDto.getPiId());
        if(optionalPolicy.isPresent()) {

            // 활동이력 저장 -> 비정상 모드
            activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    companyCode+" - "+activityCode.getDesc()+" 네번째 뎁스 시도 이력", "", ip, CommonUtil.publicIp(), 0, email);

            List<PolicyOut> policyOutSaveList = new ArrayList<>();
            List<PolicyOutDetail> policyOutDetailSaveList = new ArrayList<>();

            PolicyOut policyOut;
            PolicyOutDetail policyOutDetail;

            for(PolicyOutSaveDto policyOutSaveDto : policySaveFourthDto.getPolicyOutSaveDtoList()) {
                if(policyOutSaveDto.getPioId() != 0) {
                    Optional<PolicyOut> optionalPolicyOut = policyOutRepository.findById(policyOutSaveDto.getPioId());
                    if(optionalPolicyOut.isPresent()) {
                        optionalPolicyOut.get().setPioOutsourcingCompany(policyOutSaveDto.getPioOutsourcingCompany());
                        optionalPolicyOut.get().setPioChose(policyOutSaveDto.getPioChose());
                        optionalPolicyOut.get().setPioConsignmentCompany(policyOutSaveDto.getPioConsignmentCompany());
                        optionalPolicyOut.get().setPioPeriod(policyOutSaveDto.getPioPeriod());
                        policyOutSaveList.add(optionalPolicyOut.get());
                    } else {
                        log.error("수정 privacyPolicyFourthSave 존재하지 않은 항목");
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

            List<PolicyOut> policyOutDeleteList = new ArrayList<>();
            List<Long> outDeleteIdList = policySaveFourthDto.getPolicyOutDeleteIdList();
            for(Long pioId : outDeleteIdList) {
                Optional<PolicyOut> optionalPolicyOut = policyOutRepository.findById(pioId);
                if(optionalPolicyOut.isPresent()) {
                    policyOutDeleteList.add(optionalPolicyOut.get());
                } else {
                    log.error("삭제 privacyPolicyFourthSave 존재하지 않은 항목");
                }
            }

            List<PolicyOutDetail> policyOutDetailDeleteList = new ArrayList<>();
            List<Long> outDetailDeleteIdList = policySaveFourthDto.getPolicyOutDetailDeleteIdList();

            int outDetailState = 0;
            if(!policySaveFourthDto.getPolicyOutDetailYn() && (optionalPolicy.get().getPiOutChose() != null && optionalPolicy.get().getPiOutChose())) {
                // 미포함인데 이전에 포함한 여부가 존재하면 삭제하기
                policyOutDetailRepository.findByPolicyOutDetailDelete(policySaveFourthDto.getPiId());
            } else if(policySaveFourthDto.getPolicyOutDetailYn()) {
                // 처리업무의 국외 위탁에 관한사항 CUD -> 포함 할 경우만 저장
                for(PolicyOutDetailSaveDto policyOutDetailSaveDto : policySaveFourthDto.getPolicyOutDetailSaveDtoList()) {
                    if(policyOutDetailSaveDto.getPiodId() != 0) {
                        Optional<PolicyOutDetail> optionalPolicyOutDetail = policyOutDetailRepository.findById(policyOutDetailSaveDto.getPiodId());
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

                for(Long piodId : outDetailDeleteIdList) {
                    Optional<PolicyOutDetail> optionalPolicyOutDetail = policyOutDetailRepository.findById(piodId);
                    if(optionalPolicyOutDetail.isPresent()) {
                        policyOutDetailDeleteList.add(optionalPolicyOutDetail.get());
                    } else {
                        log.error("삭제 privacyPolicyFourthSave 존재하지 않은 항목");
                    }
                }

                outDetailState = 1;
            }

            if(outDetailState == 1) {
                optionalPolicy.get().setPiOutChose(true);
                // 처리업무의 국외 위탁에 관한사항 CUD
                policyOutDetailRepository.saveAll(policyOutDetailSaveList);
                policyOutDetailRepository.deleteAll(policyOutDetailDeleteList);
            } else {
                optionalPolicy.get().setPiOutChose(false);
            }

            optionalPolicy.get().setPiStage(4);
            optionalPolicy.get().setModify_email(email);
            optionalPolicy.get().setModify_date(LocalDateTime.now());

            // 작성중인 개인정보처리방침 업데이트
            policyRepository.save(optionalPolicy.get());

            // 처리업무의 위탁에 관한사항 CUD
            policyOutRepository.saveAll(policyOutSaveList);
            policyOutRepository.deleteAll(policyOutDeleteList);

            historyService.updateHistory(activityHistoryId,
                    companyCode+" - "+activityCode.getDesc()+" 네번째 뎁스 시도 이력", "", 1);

        } else {
            log.error("존재하지 않은 개인정보처리방침 입니다. 새로고침이후 진행해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO090.getCode(), ResponseErrorCode.KO090.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 개인정보보호 다섯번쨰 뎁스 등록
    public ResponseEntity<Map<String, Object>> privacyPolicyFifthSave(PolicySaveFifthDto policySaveFifthDto, JwtFilterDto jwtFilterDto) throws IOException {
        log.info("privacyPolicyFifthSave 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();
        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

        Long adminId = adminCompanyInfoDto.getAdminId();
        Long companyId = adminCompanyInfoDto.getCompanyId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();
//        log.info("adminId : "+adminId);
//        log.info("companyId : "+companyId);
//        log.info("companyCode : "+companyCode);

        log.info("policySaveFifthDto : "+policySaveFifthDto);

        ActivityCode activityCode;
        String ip = CommonUtil.clientIp();
        Long activityHistoryId;

        // 개인정보 처리방침 작성중 코드
        activityCode = ActivityCode.AC_28;

        Optional<Policy> optionalPolicy = policyRepository.findById(policySaveFifthDto.getPiId());
        if(optionalPolicy.isPresent()) {

            // 활동이력 저장 -> 비정상 모드
            activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    companyCode+" - "+activityCode.getDesc()+" 네번째 뎁스 시도 이력", "", ip, CommonUtil.publicIp(), 0, email);

            int thirdState = 0;
            int thirdOverseasState = 0;

            List<PolicyThird> policyThirdSaveList = new ArrayList<>();
            PolicyThird policyThird;

            List<PolicyThird> policyThirdDeleteList = new ArrayList<>();
            List<Long> thirdDeleteIdList = policySaveFifthDto.getPolicyThirdDeleteIdList();

            List<PolicyThirdOverseas> policyThirdOverseasSaveList = new ArrayList<>();
            PolicyThirdOverseas policyThirdOverseas;

            List<PolicyThirdOverseas> policyThirdOverseasDeleteList = new ArrayList<>();
            List<Long> thirdOverseasDeleteIdList = policySaveFifthDto.getPolicyThirdOverseasDeleteIdList();


            if(!policySaveFifthDto.getPolicyThirdYn() && (optionalPolicy.get().getPiThirdChose() != null && optionalPolicy.get().getPiThirdChose())) {
                // 미포함인데 이전에 포함한 여부가 존재하면 삭제하기
                policyThirdRepository.findByPolicyThirdDelete(policySaveFifthDto.getPiId());
            } else if(policySaveFifthDto.getPolicyThirdYn()) {
                // 저장 및 수정 및 삭제 로직 수행
                for(PolicyThirdSaveDto policyThirdSaveDto : policySaveFifthDto.getPolicyThirdSaveDtoList()) {
                    if(policyThirdSaveDto.getPitId() != 0) {
                        Optional<PolicyThird> optionalPolicyThird = policyThirdRepository.findById(policyThirdSaveDto.getPitId());

                        if(optionalPolicyThird.isPresent()) {
                            optionalPolicyThird.get().setPitRecipient(policyThirdSaveDto.getPitRecipient());
                            optionalPolicyThird.get().setPitPurpose(policyThirdSaveDto.getPitPurpose());
                            optionalPolicyThird.get().setPitInfo(policyThirdSaveDto.getPitInfo());
                            optionalPolicyThird.get().setPitPeriod(policyThirdSaveDto.getPitPeriod());
                            policyThirdSaveList.add(optionalPolicyThird.get());
                        } else {
                            log.error("수정 privacyPolicyFifthSave 존재하지 않은 항목");
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

                for(Long pitId : thirdDeleteIdList) {
                    Optional<PolicyThird> optionalPolicyThird = policyThirdRepository.findById(pitId);
                    if(optionalPolicyThird.isPresent()) {
                        policyThirdDeleteList.add(optionalPolicyThird.get());
                    } else {
                        log.error("삭제 privacyPolicyFifthSave 존재하지 않은 항목");
                    }
                }

                thirdState = 1;

            }

            if(!policySaveFifthDto.getPolicyThirdOverseasYn() && (optionalPolicy.get().getPiThirdOverseasChose() != null && optionalPolicy.get().getPiThirdOverseasChose())) {
                // 미포함인데 이전에 포함한 여부가 존재하면 삭제하기
                policyThirdOverseasRepository.findByPolicyThirdOverseasDelete(policySaveFifthDto.getPiId());
            } else if(policySaveFifthDto.getPolicyThirdOverseasYn()) {
                // 저장 및 수정 및 삭제 로직 수행
                for(PolicyThirdOverseasSaveDto policyThirdOverseasSaveDto : policySaveFifthDto.getPolicyThirdOverseasSaveDtoList()) {
                    if(policyThirdOverseasSaveDto.getPitoId() != 0) {
                        Optional<PolicyThirdOverseas> optionalPolicyThirdOverseas = policyThirdOverseasRepository.findById(policyThirdOverseasSaveDto.getPitoId());

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

                for(Long pitoId : thirdOverseasDeleteIdList) {
                    Optional<PolicyThirdOverseas> optionalPolicyThirdOverseas = policyThirdOverseasRepository.findById(pitoId);
                    if(optionalPolicyThirdOverseas.isPresent()) {
                        policyThirdOverseasDeleteList.add(optionalPolicyThirdOverseas.get());
                    } else {
                        log.error("삭제 privacyPolicyFifthSave 존재하지 않은 항목");
                    }
                }

                thirdOverseasState = 1;
            }

            if(thirdState == 1) {
                optionalPolicy.get().setPiThirdChose(true);
                policyThirdRepository.saveAll(policyThirdSaveList);
                policyThirdRepository.deleteAll(policyThirdDeleteList);
            } else {
                optionalPolicy.get().setPiThirdChose(false);
            }

            if(thirdOverseasState == 1) {
                optionalPolicy.get().setPiThirdOverseasChose(true);
                policyThirdOverseasRepository.saveAll(policyThirdOverseasSaveList);
                policyThirdOverseasRepository.deleteAll(policyThirdOverseasDeleteList);
            } else {
                optionalPolicy.get().setPiThirdOverseasChose(false);
            }

            optionalPolicy.get().setPiStage(5);
            optionalPolicy.get().setModify_email(email);
            optionalPolicy.get().setModify_date(LocalDateTime.now());

            historyService.updateHistory(activityHistoryId,
                    companyCode+" - "+activityCode.getDesc()+" 네번째 뎁스 시도 이력", "", 1);

        } else {
            log.error("존재하지 않은 개인정보처리방침 입니다. 새로고침이후 진행해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO090.getCode(), ResponseErrorCode.KO090.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 개인정보보호 여섯번쨰 뎁스 등록
    public ResponseEntity<Map<String, Object>> privacyPolicySixthSave(PolicySaveSixthDto policySaveSixthDto, JwtFilterDto jwtFilterDto) throws IOException {
        log.info("privacyPolicySixthSave 호출");

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

        log.info("policySaveSixthDto : "+policySaveSixthDto);

        ActivityCode activityCode;
        String ip = CommonUtil.clientIp();
        Long activityHistoryId;

        // 개인정보 처리방침 작성중 코드
        activityCode = ActivityCode.AC_28;

        Optional<Policy> optionalPolicy = policyRepository.findById(policySaveSixthDto.getPiId());
        if(optionalPolicy.isPresent()) {

            // 활동이력 저장 -> 비정상 모드
            activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    companyCode + " - " + activityCode.getDesc() + " 여섯번째 뎁스 시도 이력", "", ip, CommonUtil.publicIp(), 0, email);

            List<PolicyResponsible> policyResponsibleSaveList = new ArrayList<>();
            PolicyResponsible policyResponsible;

            List<PolicyResponsible> policyResponsibleDeleteList = new ArrayList<>();
            List<Long> responsibleDeleteIdList = policySaveSixthDto.getPolicyResponsibleDeleteIdList();
            
            // 책임자 항목에 관한사항 CUD
            for (PolicyResponsibleSaveDto policyResponsibleSaveDto : policySaveSixthDto.getPolicyResponsibleSaveDtoList()) {
                if (policyResponsibleSaveDto.getPirId() != 0) {
                    Optional<PolicyResponsible> optionalPolicyResponsible = policyResponsibleRepository.findById(policyResponsibleSaveDto.getPirId());
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

            for(Long pirId : responsibleDeleteIdList) {
                Optional<PolicyResponsible> optionalPolicyResponsible = policyResponsibleRepository.findById(pirId);
                if(optionalPolicyResponsible.isPresent()) {
                    policyResponsibleDeleteList.add(optionalPolicyResponsible.get());
                } else {
                    log.error("삭제 privacyPolicySixthSave 존재하지 않은 항목");
                }
            }

            policyResponsibleRepository.saveAll(policyResponsibleSaveList);
            policyResponsibleRepository.deleteAll(policyResponsibleDeleteList);

            if(policySaveSixthDto.getPiChangeChose()) {
                optionalPolicy.get().setPiChangeChose(true);
                optionalPolicy.get().setPiYear(policySaveSixthDto.getPiYear());
                optionalPolicy.get().setPiMonth(policySaveSixthDto.getPiMonth());
                optionalPolicy.get().setPiDay(policySaveSixthDto.getPiDay());
            } else {
                optionalPolicy.get().setPiChangeChose(false);
            }

            optionalPolicy.get().setPiStage(6);
            optionalPolicy.get().setModify_email(email);
            optionalPolicy.get().setModify_date(LocalDateTime.now());

            policyRepository.save(optionalPolicy.get());

            historyService.updateHistory(activityHistoryId,
                    companyCode+" - "+activityCode.getDesc()+" 여섯번째 뎁스 시도 이력", "", 1);

        } else {
            log.error("존재하지 않은 개인정보처리방침 입니다. 새로고침이후 진행해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO090.getCode(), ResponseErrorCode.KO090.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 개인정보보호 마지막 뎁스 등록
    public ResponseEntity<Map<String, Object>> privacyPolicyFinalSave(Long piId, JwtFilterDto jwtFilterDto) throws IOException {
        log.info("privacyPolicyFinalSave 호출");

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

        log.info("piId : "+piId);

        ActivityCode activityCode;
        String ip = CommonUtil.clientIp();
        Long activityHistoryId;

        // 개인정보 처리방침 작성중 코드
        activityCode = ActivityCode.AC_29;

        Optional<Policy> optionalPolicy = policyRepository.findById(piId);
        if(optionalPolicy.isPresent()) {

            // 활동이력 저장 -> 비정상 모드
            activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    companyCode + " - " + activityCode.getDesc() + " 마지막 뎁스 시도 이력", "", ip, CommonUtil.publicIp(), 0, email);

            optionalPolicy.get().setPiStage(7);
            optionalPolicy.get().setPiAutosave(1);
            optionalPolicy.get().setModify_email(email);
            optionalPolicy.get().setModify_date(LocalDateTime.now());

            policyRepository.save(optionalPolicy.get());

            historyService.updateHistory(activityHistoryId,
                    companyCode+" - "+activityCode.getDesc()+" 마지막 뎁스 시도 이력", "", 1);
            
        } else {
            log.error("존재하지 않은 개인정보처리방침 입니다. 새로고침이후 진행해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO090.getCode(), ResponseErrorCode.KO090.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }
    
}
