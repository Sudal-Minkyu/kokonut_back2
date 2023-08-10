package com.app.kokonut.provision;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.awskmshistory.dto.AwsKmsResultDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.component.ReqUtils;
import com.app.kokonut.common.realcomponent.CommonUtil;
import com.app.kokonut.common.realcomponent.Utils;
import com.app.kokonut.company.companydatakey.CompanyDataKeyService;
import com.app.kokonut.company.companytablecolumninfo.CompanyTableColumnInfoRepository;
import com.app.kokonut.company.companytablecolumninfo.dtos.CompanyTableColumnInfoCheck;
import com.app.kokonut.company.companytablecolumninfo.dtos.CompanyTableColumnInfoCheckList;
import com.app.kokonut.configs.ExcelService;
import com.app.kokonut.configs.GoogleOTP;
import com.app.kokonut.configs.KeyGenerateService;
import com.app.kokonut.configs.MailSender;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dtos.ActivityCode;
import com.app.kokonut.history.extra.decrypcounthistory.DecrypCountHistoryService;
import com.app.kokonut.provision.dtos.ProvisionDownloadCheckDto;
import com.app.kokonut.provision.dtos.ProvisionListDto;
import com.app.kokonut.provision.dtos.ProvisionSaveDto;
import com.app.kokonut.provision.dtos.ProvisionSearchDto;
import com.app.kokonut.provision.provisiondownloadhistory.ProvisionDownloadHistory;
import com.app.kokonut.provision.provisiondownloadhistory.ProvisionDownloadHistoryRepository;
import com.app.kokonut.provision.provisiondownloadhistory.dtos.ProvisionDownloadHistoryListDto;
import com.app.kokonut.provision.provisionentry.ProvisionEntry;
import com.app.kokonut.provision.provisionentry.ProvisionEntryRepository;
import com.app.kokonut.provision.provisionentry.dtos.ProvisionEntrySaveDto;
import com.app.kokonut.provision.provisionentry.dtos.ProvisionEntryTargetsDto;
import com.app.kokonut.provision.provisionlist.ProvisionList;
import com.app.kokonut.provision.provisionlist.ProvisionListRepository;
import com.app.kokonut.provision.provisionlist.dtos.ProvisionTargetIdxDto;
import com.app.kokonut.provision.provisionroster.ProvisionRoster;
import com.app.kokonut.provision.provisionroster.ProvisionRosterRepository;
import com.app.kokonutuser.DynamicUserRepositoryCustom;
import com.app.kokonutuser.KokonutUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Woody
 * Date : 2023-05-10
 * Remark :
 */
@Slf4j
@Service
public class ProvisionService {

    private final KeyGenerateService keyGenerateService;

    private final GoogleOTP googleOTP;
    private final ExcelService excelService;
    private final MailSender mailSender;

    private final HistoryService historyService;
    private final KokonutUserService kokonutUserService;
    private final DecrypCountHistoryService decrypCountHistoryService;
    private final CompanyDataKeyService companyDataKeyService;

    private final AdminRepository adminRepository;
    private final ProvisionRepository provisionRepository;
    private final ProvisionDownloadHistoryRepository provisionDownloadHistoryRepository;
    private final ProvisionRosterRepository provisionRosterRepository;
    private final ProvisionEntryRepository provisionEntryRepository;
    private final ProvisionListRepository provisionListRepository;
    private final CompanyTableColumnInfoRepository companyTableColumnInfoRepository;

    private final DynamicUserRepositoryCustom dynamicUserRepositoryCustom;

    @Autowired
    public ProvisionService(KeyGenerateService keyGenerateService, GoogleOTP googleOTP, HistoryService historyService,
                            ExcelService excelService, MailSender mailSender, KokonutUserService kokonutUserService,
                            CompanyDataKeyService companyDataKeyService, AdminRepository adminRepository, ProvisionRepository provisionRepository,
                            ProvisionDownloadHistoryRepository provisionDownloadHistoryRepository, ProvisionRosterRepository provisionRosterRepository,
                            ProvisionEntryRepository provisionEntryRepository, ProvisionListRepository provisionListRepository,
                            DecrypCountHistoryService decrypCountHistoryService, CompanyTableColumnInfoRepository companyTableColumnInfoRepository,
                            DynamicUserRepositoryCustom dynamicUserRepositoryCustom){
        this.keyGenerateService = keyGenerateService;
        this.googleOTP = googleOTP;
        this.historyService = historyService;
        this.excelService = excelService;
        this.mailSender = mailSender;
        this.kokonutUserService = kokonutUserService;
        this.companyDataKeyService = companyDataKeyService;
        this.adminRepository = adminRepository;
        this.provisionRepository = provisionRepository;
        this.provisionDownloadHistoryRepository = provisionDownloadHistoryRepository;
        this.provisionRosterRepository = provisionRosterRepository;
        this.provisionEntryRepository = provisionEntryRepository;
        this.provisionListRepository = provisionListRepository;
        this.decrypCountHistoryService = decrypCountHistoryService;
        this.companyTableColumnInfoRepository = companyTableColumnInfoRepository;
        this.dynamicUserRepositoryCustom = dynamicUserRepositoryCustom;
    }

    // 개인정보제공 등록
    @Transactional
    public ResponseEntity<Map<String, Object>> provisionSave(ProvisionSaveDto provisionSaveDto, JwtFilterDto jwtFilterDto) {
        log.info("provisionSave 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();
        log.info("email : " + email);

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
        long adminId;
        String cpCode;

        if (adminCompanyInfoDto == null) {
            log.error("이메일 정보가 존재하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(), "해당 이메일의 정보가 " + ResponseErrorCode.KO004.getDesc()));
        } else {
            adminId = adminCompanyInfoDto.getAdminId();
            cpCode = adminCompanyInfoDto.getCompanyCode();
        }

        log.info("provisionSaveDto : " + provisionSaveDto);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 제공 시작기간
        LocalDate proStartDate = LocalDate.parse(provisionSaveDto.getProStartDate(), formatter);
        // 제공 만료기간
        LocalDate proExpDate = LocalDate.parse(provisionSaveDto.getProExpDate(), formatter);
        log.info("proStartDate : " + proStartDate);
        log.info("proExpDate : " + proExpDate);

        // 정보제공 등록 코드
        ActivityCode activityCode;
        String ip = CommonUtil.publicIp();
        Long activityHistoryId;

        // 개인정보제공 등록 코드
        activityCode = ActivityCode.AC_48;

        String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 고유코드
        String proCode = keyGenerateService.keyGenerate("kn_personal_info_provision", nowDate+cpCode, "KokonutSystem");

        Provision provision = new Provision();
        provision.setCpCode(cpCode);
        provision.setProCode(proCode);
        provision.setProProvide(provisionSaveDto.getProProvide());
        provision.setProStartDate(proStartDate);
        provision.setProExpDate(proExpDate);
        provision.setProDownloadYn(provisionSaveDto.getProDownloadYn());
        provision.setProDownloadCount(0);

        Integer proTargetType = provisionSaveDto.getProTargetType();
        provision.setProTargetType(proTargetType);
        provision.setInsert_email(email);
        provision.setInsert_date(LocalDateTime.now());

        // 활동이력 저장 -> 비정상 모드
        activityHistoryId = historyService.insertHistory(2, adminId, activityCode,
                cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip,0, email);

        try {
            Provision saveprovision = provisionRepository.save(provision);

            // 제공받는자 저장
            List<ProvisionRoster> provisionRosters = new ArrayList<>();
            ProvisionRoster provisionRoster;
            List<String> adminEmailList = provisionSaveDto.getAdminEmailList();
            for(String knEmail : adminEmailList) {
                provisionRoster = new ProvisionRoster();
                provisionRoster.setProCode(saveprovision.getProCode());
                provisionRoster.setInsert_email(email);
                provisionRoster.setInsert_date(LocalDate.now());
                AdminCompanyInfoDto choseAdmin = adminRepository.findByCompanyInfo(knEmail);
                if(choseAdmin != null) {
                    provisionRoster.setAdminId(choseAdmin.getAdminId());
                }
                provisionRosters.add(provisionRoster);
            }

            List<ProvisionEntry> provisionEntries = new ArrayList<>();

            ProvisionEntry provisionEntry = new ProvisionEntry();
            // 제공 할 테이블+컬럼 저장 -> 모든 항목일 경우 저장하지 않음
            if(proTargetType == 1) {
                ProvisionEntrySaveDto provisionEntrySaveDtos = provisionSaveDto.getProvisionEntrySaveDtos();
                log.info("provisionEntrySaveDtos : "+provisionEntrySaveDtos);
                if(provisionEntrySaveDtos.getPipeTableTargets().size() != 0) {
                    provisionEntry = new ProvisionEntry();
                    provisionEntry.setProCode(saveprovision.getProCode());
                    provisionEntry.setInsert_email(email);
                    provisionEntry.setInsert_date(LocalDateTime.now());

                    provisionEntry.setPipeTableName(cpCode+"_1");

                    String pipeTableTargets = String.join(",", provisionEntrySaveDtos.getPipeTableTargets());
                    log.info("pipeTableTargets : "+pipeTableTargets);
                    provisionEntry.setPipeTableTargets(pipeTableTargets);
                }
            }

            // 제공할 개인정보의 idx
            ProvisionList provisionList = new ProvisionList();
            if(provisionSaveDto.getPiplTargetIdxs().size() != 0) {
                String piplTargetIdxs = provisionSaveDto.getPiplTargetIdxs().stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));
                log.info("piplTargetIdxs : "+piplTargetIdxs);

                provisionList.setProCode(saveprovision.getProCode());
                provisionList.setPiplTargetIdxs(piplTargetIdxs);
                provisionList.setInsert_email(email);
                provisionList.setInsert_date(LocalDateTime.now());
            }else {
                log.error("제공할 개인정보가 존재하지 않습니다. 제공할 개인정보를 선택해주세요.");

                provisionRepository.delete(saveprovision);

                // 실패이력 업데이트
                historyService.updateHistory(activityHistoryId,
                        cpCode+" - "+activityCode.getDesc()+" 실패 이력", "제공할 개인정보를 선택해주세요.", 1);

                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO093.getCode(),ResponseErrorCode.KO093.getDesc()));
            }

            if(proTargetType == 1) {
                provisionEntryRepository.save(provisionEntry);
            }
            provisionListRepository.save(provisionList);
            provisionRosterRepository.saveAll(provisionRosters);

            // 성공이력 업데이트
            historyService.updateHistory(activityHistoryId,
                    cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", 1);

            return ResponseEntity.ok(res.success(data));

        } catch (Exception e){
            log.error("개인정보제공 등록 실패");
            log.error("개인정보 제공등록을 실패했습니다. 새로고침이후 진행해주세요.");

            // 실패이력 업데이트
            historyService.updateHistory(activityHistoryId,
                    cpCode+" - "+activityCode.getDesc()+" 실패 이력", "개인정보제공 등록에 실패했습니다.", 1);

            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO092.getCode(),ResponseErrorCode.KO092.getDesc()));
        }


//        String memo = "밑에 메모 별도수집대상?";
//
//        // 별도수집인 경우 별도수집 대상 목록을 미리 저장한다.
//        if(data.getRecipientType() == 2 && (data.getType() == 1 || data.getType() == 2) && data.getAgreeYn() == 'Y' && data.getAgreeType() == 2) {
//            String tableName = dynamicUserService.SelectTableName(data.getCompanyId());
//            List<Map<String, Object>> userList = dynamicUserService.SelectUserListByTableName(tableName);
//
//            if(data.getTargetStatus().equals("ALL")) {
//                List<String> idList = new ArrayList<String>();
//                for(Map<String, Object> userInfo : userList) {
//                    String id = userInfo.get("ID").toString();
//                    idList.add(id);
//                }
//
//                personalInfoService.saveTempProvisionAgree(number, idList);
//            } else {
//
//                List<String> idList = new ArrayList<String>();
//
//                String[] userIdxList = data.getTargets().split(",");
//                for(String userIdx : userIdxList) {
//                    for(int i = userList.size()-1; i >= 0; i--) {
//                        Map<String, Object> userInfo = userList.get(i);
//                        if(userInfo.get("IDX").toString().equals(userIdx)) {
//                            idList.add(userInfo.get("ID").toString());
//                            userList.remove(i);
//                            break;
//                        }
//                    }
//                }
//
//                personalInfoService.saveTempProvisionAgree(number, idList);
//            }
//        }

//        Integer period = Math.toIntExact(ChronoUnit.DAYS.between(personalInfoProvisionSaveDto.getPiStartDate(), personalInfoProvisionSaveDto.getPiExpDate()));
//        log.info("시작과 종료일까지 "+period+"일 차이");

        // 담당자가 내부직원인지 검증
        // 로직확인후 작업할것

//        // 받는사람에게 이메일 전송
//        boolean mailResult = sendEmailToRecipient(piNumber, personalInfoProvisionSaveDto.getPiRecipientEmail(),
//                personalInfoProvisionSaveDto.getPiStartDate(), personalInfoProvisionSaveDto.getPiExpDate(), period);
//        if(mailResult) {
//            log.info("메일전송 성공");
//        } else {
//            log.info("메일전송 실패");
//        }

    }

    // 개인정보제공 리스트 조회
    public ResponseEntity<Map<String, Object>> provisionList(String searchText, String stime,
                                                             String filterOfferType, String filterState, JwtFilterDto jwtFilterDto, Pageable pageable) throws IOException {
        log.info("provisionList 호출");

        AjaxResponse res = new AjaxResponse();

        String email = jwtFilterDto.getEmail();
        log.info("email : " + email);

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

        long adminId = adminCompanyInfoDto.getAdminId();
        String cpCode = adminCompanyInfoDto.getCompanyCode();

        ProvisionSearchDto provisionSearchDto = new ProvisionSearchDto();
        provisionSearchDto.setAdminId(adminId);
        provisionSearchDto.setCpCode(cpCode);
        provisionSearchDto.setSearchText(searchText);
        provisionSearchDto.setFilterOfferType(filterOfferType);
        provisionSearchDto.setFilterState(filterState);

        if(!stime.equals("")) {
            List<LocalDateTime> stimeList = Utils.getStimeList(stime);
            provisionSearchDto.setStimeStart(stimeList.get(0));
            provisionSearchDto.setStimeEnd(stimeList.get(1).plusHours(23).plusMinutes(59));
        }

        log.info("provisionSearchDto : "+provisionSearchDto);

        ActivityCode activityCode;
        String ip = CommonUtil.publicIp();
        Long activityHistoryId;

        // 개인정보 제공 리스트조회 코드
        activityCode = ActivityCode.AC_14;

        // 활동이력 저장 -> 비정상 모드
        activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, 0, email);

        Page<ProvisionListDto> provisionListDtos = provisionRepository.findByProvisionList(provisionSearchDto, pageable);

        historyService.updateHistory(activityHistoryId,
                cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", 1);

        if(provisionListDtos.getTotalPages() == 0) {
            log.info("조회된 데이터가 없습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO003.getCode(), ResponseErrorCode.KO003.getDesc()));
        } else {
            return ResponseEntity.ok(res.ResponseEntityPage(provisionListDtos));
        }
    }

    // 개인정보제공 다운로드 리스트 조회
    public ResponseEntity<Map<String, Object>> provisionDownloadList(String proCode, JwtFilterDto jwtFilterDto, Pageable pageable) throws IOException {
        log.info("provisionDownloadList 호출");

        AjaxResponse res = new AjaxResponse();

        log.info("proCode : " + proCode);

        String email = jwtFilterDto.getEmail();
        log.info("email : " + email);

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

        long adminId = adminCompanyInfoDto.getAdminId();
        String cpCode = adminCompanyInfoDto.getCompanyCode();

        ActivityCode activityCode;
        String ip = CommonUtil.publicIp();
        Long activityHistoryId;

        // 개인정보 제공 리스트조회 코드
        activityCode = ActivityCode.AC_47_1;

        // 활동이력 저장 -> 비정상 모드
        activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, 0, email);

        Page<ProvisionDownloadHistoryListDto> provisionDownloadList = provisionDownloadHistoryRepository.findByProvisionDownloadList(proCode, pageable);

        historyService.updateHistory(activityHistoryId,
                cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", 1);

        if(provisionDownloadList.getTotalPages() == 0) {
            log.info("조회된 데이터가 없습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO003.getCode(), ResponseErrorCode.KO003.getDesc()));
        } else {
            return ResponseEntity.ok(res.ResponseEntityPage(provisionDownloadList));
        }
    }

    // 개인정보제공 다운로드 API
    @Transactional
    public ResponseEntity<Map<String, Object>> provisionDownloadExcel(String proCode, String otpValue, String downloadReason, JwtFilterDto jwtFilterDto) throws IOException {
        log.info("provisionDownloadExcel 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data;

//        log.info("proCode : " + proCode);
//        log.info("otpValue : " + otpValue);
//        log.info("downloadReason : " + downloadReason);

        if(otpValue == null || otpValue.equals("")) {
            log.error("구글 OTP 값이 존재하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO010.getCode(),ResponseErrorCode.KO010.getDesc()));
        }


        String email = jwtFilterDto.getEmail();
//        log.info("email : " + email);

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

        long adminId = adminCompanyInfoDto.getAdminId();
        String cpCode = adminCompanyInfoDto.getCompanyCode();
        String knOtpKey = adminCompanyInfoDto.getKnOtpKey();

        boolean auth = googleOTP.checkCode(otpValue, knOtpKey);
        log.info("auth : " + auth);

        if (!auth) {
            log.error("입력된 구글 OTP 값이 일치하지 않습니다. 다시 확인해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO012.getCode(), ResponseErrorCode.KO012.getDesc()));
        } else {
            log.info("OTP인증완료 -> 개인정보 제공엑셀 다운로드 시작");
        }

        int dchCount = 0; // 복호화 카운팅

        ActivityCode activityCode = ActivityCode.AC_47_2;
        String ip = CommonUtil.publicIp();
        Long activityHistoryId;

        ProvisionDownloadCheckDto provisionDownloadCheckDto = provisionRepository.findByProvisionDownloadCheck(cpCode, proCode, 1);
        if(provisionDownloadCheckDto != null) {

            LocalDate now = LocalDate.now();
            //  제공기간내의 다운로드인지 체크
            if (provisionDownloadCheckDto.getProStartDate().isAfter(now)) {
                log.error("개인정보제공 시작전 입니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO113_1.getCode(), ResponseErrorCode.KO113_1.getDesc()));
            }

            if (provisionDownloadCheckDto.getProExpDate().isBefore(now)) {
                log.error("개인정보제공 다운로드 기간이 지났습니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO113_2.getCode(), ResponseErrorCode.KO113_2.getDesc()));
            }

            // 개인정보제공 다운로드 권한이 있는지 체크(제공받은 사람인지)
            boolean rosterCheck = provisionRosterRepository.existsByProCodeAndAdminId(proCode, adminId);
            if(!rosterCheck) {
                log.error("해당 개인정보제공에 등록되지 않은 관리자입니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO114.getCode(), ResponseErrorCode.KO114.getDesc()));
            }

            String fileName;
            String sheetName;

            if(provisionDownloadCheckDto.getProProvide() == 0) {
                // 내부제공
                fileName = "개인정보_내부제공 압축파일";
                sheetName = proCode+"_내부제공";
            } else {
                // 외부제공
                fileName = "개인정보_외부제공 압축파일";
                sheetName = proCode+"_외부제공";
            }

            // 활동이력 저장 -> 비정상 모드
            activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    cpCode+" - "+activityCode.getDesc()+" 시도 이력", downloadReason, ip, 0, email);

            // 제공할 개인정보가 존재하는지 체크
            ProvisionTargetIdxDto provisionTargetIdxDto = provisionListRepository.findByProvisionIdxList(proCode);
            String piplTargetList = provisionTargetIdxDto.getPipeTableTargets();

            String ctName = cpCode+"_1";
            List<String> targetList = new ArrayList<>();
            List<String> securityList = new ArrayList<>();
            List<String> headerName = new ArrayList<>();

            if(provisionDownloadCheckDto.getProTargetType() == 1) {
                // 일부개인정보 조회
                ProvisionEntryTargetsDto provisionEntryTargetsDto = provisionEntryRepository.findByProvisionEntryTargets(proCode, ctName);
                if (provisionEntryTargetsDto != null) {
                    targetList = provisionEntryTargetsDto.getPipeTableTargets();
                    for(int i=0; i<targetList.size(); i++) {
                        CompanyTableColumnInfoCheck companyTableColumnInfoCheck = companyTableColumnInfoRepository.findByCheck(ctName, targetList.get(i));
                        if(companyTableColumnInfoCheck == null) {
                            targetList.set(i, "pass");
                            securityList.add("pass");
                            headerName.add("pass");
                        } else {
                            targetList.set(i, companyTableColumnInfoCheck.getCtciName());
                            securityList.add(companyTableColumnInfoCheck.getCtciSecuriy());
                            String uniqueName = Utils.generateUniqueName(companyTableColumnInfoCheck.getCtciDesignation(), headerName);
                            headerName.add(uniqueName);
                        }
                    }
                }
//                log.info("일부 targetList : "+targetList);
//                log.info("일부 securityList : "+securityList);
//                log.info("일부 headerName : "+headerName);
            }
            else {
                List<CompanyTableColumnInfoCheckList> companyTableColumnInfoCheckLists = companyTableColumnInfoRepository.findByCheckList(ctName);
                for(CompanyTableColumnInfoCheckList companyTableColumnInfoCheckList : companyTableColumnInfoCheckLists) {
                    targetList.add(companyTableColumnInfoCheckList.getCtciName());
                    securityList.add(companyTableColumnInfoCheckList.getCtciSecuriy());
                    String uniqueName = Utils.generateUniqueName(companyTableColumnInfoCheckList.getCtciDesignation(), headerName);
                    headerName.add(uniqueName);
                }

//                log.info("전체 targetList : "+targetList);
//                log.info("전체 securityList : "+securityList);
//                log.info("전체 headerName : "+headerName);
            }

            StringBuilder resultQuery = new StringBuilder();
            StringBuilder selectQuery = new StringBuilder();
            StringBuilder whereQuery = new StringBuilder();

            // 쿼리 기본셋팅
            resultQuery.append("SELECT ");

            selectQuery.append("kokonut_IDX as kokonut_IDX ");

            // 셀렉트 기본셋팅
            selectQuery.append(
                    ", DATE_FORMAT(kokonut_REGISTER_DATE, '%Y-%m-%d %H시') as 회원가입일시, " +
                            "COALESCE(DATE_FORMAT(kokonut_LAST_LOGIN_DATE, '%Y-%m-%d %H시'), '없음') as 마지막로그인일시 ");

            for(int i=0; i<targetList.size(); i++) {
                if(!targetList.get(i).equals("pass") && !securityList.get(i).equals("pass") && !headerName.get(i).equals("pass")) {
                    selectQuery.append(", COALESCE(").append(targetList.get(i)).append(", '없음') as ").append(headerName.get(i)).append(" ");
                }
            }

            whereQuery.append(" WHERE kokonut_IDX IN (").append(piplTargetList).append(")");

            resultQuery.append(selectQuery)
                    .append("FROM ")
                    .append(ctName)
                    .append(whereQuery);

//            log.info("ctName : "+ctName);
//            log.info("resultQuery : "+resultQuery);

            List<Map<String, Object>> privacyInfo = dynamicUserRepositoryCustom.privacyOpenInfoData(String.valueOf(resultQuery));

            AwsKmsResultDto awsKmsResultDto = null;

            // 암호화된 개인정보 복호화하기
            for(Map<String, Object> map : privacyInfo) {
//				log.info("수정전 map : "+map);
                for(int i=0; i<securityList.size(); i++) {
                    if(!targetList.get(i).equals("pass") && !headerName.get(i).equals("pass") && !securityList.get(i).equals("pass")) {
                        if(securityList.get(i).equals("1")) {
//                            log.info("복호화대상 : "+headerName.get(i));

                            Object key = map.get(headerName.get(i));
//                            log.info("key : "+key);
                            if(key != null) {
                                if (!String.valueOf(key).equals("없음")) { // 벨류값이 Null(없음)일 경우 제외

                                    if (awsKmsResultDto == null) {
                                        awsKmsResultDto = companyDataKeyService.findByCompanyDataKey(cpCode); // 암호화 하기 전에 가져올것
                                    }

//                                    log.info("암호화시작");
                                    try {
                                        String decryptValue = Utils.decrypResult(headerName.get(i),
                                                String.valueOf(key), awsKmsResultDto.getSecretKey(), awsKmsResultDto.getIvKey()); // 복호화된 데이터

//                                        log.info("복호화된 데이터 : " + decryptValue);
                                        dchCount++;
                                        map.put(headerName.get(i), decryptValue);
                                    } catch (Exception e) {
                                        log.error("복호화중 에러발생 - 복호화값 '없음'처리");
                                        map.put(headerName.get(i), "없음");
                                    }
                                }
                            }
                        }
                    }
                }
            }

//            for(Map<String, Object> map : privacyInfo) {
//                log.info("수정후 map : "+map);
//            }

//            log.info("privacyInfo : "+privacyInfo);

            // 파일암호 전송
            // 파일암호(숫자6자리) 생성
            SecureRandom secureRandom = new SecureRandom();
            int filePassword = secureRandom.nextInt(900000) + 100000;
            log.info("생성된 파일암호 : "+filePassword);

            // 인증번호 메일전송
            String title = ReqUtils.filter("개인정보제공 파일의 암호가 도착했습니다.");
            String contents = ReqUtils.unFilter("파일암호 : "+filePassword);

            // 템플릿 호출을 위한 데이터 세팅
//            HashMap<String, String> callTemplate = new HashMap<>();
//            callTemplate.put("template", "MailTemplate");
//            callTemplate.put("title", "개인정보제공 파일암호 알림");
//            callTemplate.put("content", contents);
//
//            // 템플릿 TODO 템플릿 디자인 추가되면 수정
//            contents = mailSender.getHTML5(callTemplate);
            String reciverName = "kokonut";

            String mailSenderResult = mailSender.sendKokonutMail(email, reciverName, title, contents);
            if(mailSenderResult != null) {
                // mailSender 성공
                log.info("### 메일전송 성공했습니다. reciver Email : "+ email);

                log.info("파일명 : "+fileName);
                log.info("시트명 : "+sheetName);
                data = excelService.createExcelFile(fileName, sheetName, privacyInfo, String.valueOf(filePassword));

                // 다운로드 성공기록
                ProvisionDownloadHistory provisionDownloadHistory;
                Optional<ProvisionDownloadHistory> provisionDownloadHistoryOptional = provisionDownloadHistoryRepository.findProvisionDownloadHistoryByProCodeAndAdminId(proCode, adminId);
                if(provisionDownloadHistoryOptional.isPresent()) {
                    provisionDownloadHistory = provisionDownloadHistoryOptional.get();
                    provisionDownloadHistoryOptional.get().setPiphCount(provisionDownloadHistoryOptional.get().getPiphCount()+1);
                } else {
                    provisionDownloadHistory = new ProvisionDownloadHistory();
                    provisionDownloadHistory.setProCode(proCode);
                    provisionDownloadHistory.setAdminId(adminId);
                    provisionDownloadHistory.setPiphCount(1);
                    provisionDownloadHistory.setInsert_email(email);
                    provisionDownloadHistory.setInsert_date(LocalDateTime.now());
                }
                provisionDownloadHistoryRepository.save(provisionDownloadHistory);

                // 복호화 횟수 저장
                if(dchCount > 0) {
                    decrypCountHistoryService.decrypCountHistorySave(cpCode, dchCount);
                }

                historyService.updateHistory(activityHistoryId,
                        cpCode+" - "+activityCode.getDesc()+" 시도 이력", downloadReason, 1);

            }else{
                historyService.updateHistory(activityHistoryId,
                        cpCode+" - "+activityCode.getDesc()+" 시도 이력", downloadReason+"- 개인정보제공시 파일암호전송 실패", 0);

                // mailSender 실패
                log.error("### 해당 메일 전송에 실패했습니다. 관리자에게 문의하세요. reciverEmail : "+ email);
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO041.getCode(), ResponseErrorCode.KO041.getDesc()));
            }

        } else {
            log.error("개인정보제공 정보가 존재하지 않습니다.");

            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO112.getCode(), ResponseErrorCode.KO112.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 개인정보제공 상세내용 조회
    public ResponseEntity<Map<String, Object>> provisionDetail(String proCode, JwtFilterDto jwtFilterDto) {
        log.info("provisionDetail 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("proCode : " + proCode);

        return ResponseEntity.ok(res.success(data));
    }

}
