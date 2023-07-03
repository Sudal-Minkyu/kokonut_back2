package com.app.kokonut.provision;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.realcomponent.CommonUtil;
import com.app.kokonut.common.realcomponent.Utils;
import com.app.kokonut.configs.KeyGenerateService;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dtos.ActivityCode;
import com.app.kokonut.provision.dtos.ProvisionListDto;
import com.app.kokonut.provision.dtos.ProvisionSaveDto;
import com.app.kokonut.provision.dtos.ProvisionSearchDto;
import com.app.kokonut.provision.provisiondownloadhistory.ProvisionDownloadHistoryRepository;
import com.app.kokonut.provision.provisiondownloadhistory.dtos.ProvisionDownloadHistoryListDto;
import com.app.kokonut.provision.provisionentry.ProvisionEntry;
import com.app.kokonut.provision.provisionentry.ProvisionEntryRepository;
import com.app.kokonut.provision.provisionentry.dtos.ProvisionEntrySaveDto;
import com.app.kokonut.provision.provisionlist.ProvisionList;
import com.app.kokonut.provision.provisionlist.ProvisionListRepository;
import com.app.kokonut.provision.provisionroster.ProvisionRoster;
import com.app.kokonut.provision.provisionroster.ProvisionRosterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private final HistoryService historyService;

    private final AdminRepository adminRepository;
    private final ProvisionRepository provisionRepository;
    private final ProvisionDownloadHistoryRepository provisionDownloadHistoryRepository;
    private final ProvisionRosterRepository provisionRosterRepository;
    private final ProvisionEntryRepository provisionEntryRepository;
    private final ProvisionListRepository provisionListRepository;

    @Autowired
    public ProvisionService(KeyGenerateService keyGenerateService, HistoryService historyService,
                            AdminRepository adminRepository, ProvisionRepository provisionRepository,
                            ProvisionDownloadHistoryRepository provisionDownloadHistoryRepository, ProvisionRosterRepository provisionRosterRepository,
                            ProvisionEntryRepository provisionEntryRepository, ProvisionListRepository provisionListRepository){
        this.keyGenerateService = keyGenerateService;
        this.historyService = historyService;
        this.adminRepository = adminRepository;
        this.provisionRepository = provisionRepository;
        this.provisionDownloadHistoryRepository = provisionDownloadHistoryRepository;
        this.provisionRosterRepository = provisionRosterRepository;
        this.provisionEntryRepository = provisionEntryRepository;
        this.provisionListRepository = provisionListRepository;
    }

    // 개인정보제공 등록
    @Transactional
    public ResponseEntity<Map<String, Object>> provisionSave(ProvisionSaveDto provisionSaveDto, JwtFilterDto jwtFilterDto) throws IOException {
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
        String ip = CommonUtil.clientIp();
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
        activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, CommonUtil.publicIp(),0, email);

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

            // 제공 할 테이블+컬럼 저장 -> 모든 항목일 경우 저장하지 않음
            if(proTargetType == 1) {
                ProvisionEntry provisionEntry;
                List<ProvisionEntrySaveDto> provisionEntrySaveDtos = provisionSaveDto.getProvisionEntrySaveDtos();
                log.info("provisionEntrySaveDtos : "+provisionEntrySaveDtos);
                for(ProvisionEntrySaveDto provisionEntrySaveDto : provisionEntrySaveDtos) {
                    if(provisionEntrySaveDto.getPipeTableTargets().size() != 0) {
                        provisionEntry = new ProvisionEntry();
                        provisionEntry.setProCode(saveprovision.getProCode());
                        provisionEntry.setInsert_email(email);
                        provisionEntry.setInsert_date(LocalDateTime.now());

                        provisionEntry.setPipeTableName(provisionEntrySaveDto.getPipeTableName());

                        String pipeTableTargets = String.join(",", provisionEntrySaveDto.getPipeTableTargets());
                        log.info("pipeTableTargets : "+pipeTableTargets);
                        provisionEntry.setPipeTableTargets(pipeTableTargets);

                        provisionEntries.add(provisionEntry);
                    }
                }
            }

            // 제공할 개인정보의 idx
            ProvisionList provisionList = new ProvisionList();
            if(proTargetType == 1) {
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
            }

            if(proTargetType == 1 && provisionEntries.size() != 0) {
                provisionEntryRepository.saveAll(provisionEntries);
                provisionListRepository.save(provisionList);
            }
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
                                                             String filterDownload, String filterState, JwtFilterDto jwtFilterDto, Pageable pageable) throws IOException {
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
        provisionSearchDto.setFilterDownload(filterDownload);
        provisionSearchDto.setFilterState(filterState);

        if(!stime.equals("")) {
            List<LocalDateTime> stimeList = Utils.getStimeList(stime);
            provisionSearchDto.setStimeStart(stimeList.get(0));
            provisionSearchDto.setStimeEnd(stimeList.get(1).plusHours(23).plusMinutes(59));
        }

        log.info("provisionSearchDto : "+provisionSearchDto);

        ActivityCode activityCode;
        String ip = CommonUtil.clientIp();
        Long activityHistoryId;

        // 개인정보 제공 리스트조회 코드
        activityCode = ActivityCode.AC_14;

        // 활동이력 저장 -> 비정상 모드
        activityHistoryId = historyService.insertHistory(2, adminId, activityCode,
                cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, CommonUtil.publicIp(), 0, email);

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
        String ip = CommonUtil.clientIp();
        Long activityHistoryId;

        // 개인정보 제공 리스트조회 코드
        activityCode = ActivityCode.AC_47;

        // 활동이력 저장 -> 비정상 모드
        activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, CommonUtil.publicIp(), 0, email);

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

    // 개인정보제공 상세내용 조회
    public ResponseEntity<Map<String, Object>> provisionDetail(String proCode, JwtFilterDto jwtFilterDto) throws IOException {
        log.info("provisionDetail 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("proCode : " + proCode);

        return ResponseEntity.ok(res.success(data));
    }

}
