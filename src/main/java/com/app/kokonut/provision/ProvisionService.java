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
import com.app.kokonut.history.dto.ActivityCode;
import com.app.kokonut.provision.dtos.ProvisionListDto;
import com.app.kokonut.provision.dtos.ProvisionSaveDto;
import com.app.kokonut.provision.dtos.ProvisionSearchDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    public ProvisionService(KeyGenerateService keyGenerateService, HistoryService historyService,
                            AdminRepository adminRepository, ProvisionRepository provisionRepository){
        this.keyGenerateService = keyGenerateService;
        this.historyService = historyService;
        this.adminRepository = adminRepository;
        this.provisionRepository = provisionRepository;
    }

    // 개인정보제공 등록
    @Transactional
    public ResponseEntity<Map<String, Object>> provisionSave(ProvisionSaveDto provisionSaveDto, JwtFilterDto jwtFilterDto) {
        log.info("provisionSave 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("provisionSaveDto : " + provisionSaveDto);

        String email = jwtFilterDto.getEmail();
        log.info("email : " + email);

        // 해당 이메일을 통해 회사 IDX 조회
        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

        long adminId;
        long companyId;
        String companyCode;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime proStartDate = LocalDateTime.parse(provisionSaveDto.getProStartDate()+" 00:00:00.000", formatter);
        LocalDateTime proExpDate = LocalDateTime.parse(provisionSaveDto.getProExpDate()+" 00:00:00.000", formatter);
        log.info("proStartDate : " + proStartDate);
        log.info("proExpDate : " + proExpDate);

//        if (adminCompanyInfoDto == null) {
//            log.error("이메일 정보가 존재하지 않습니다.");
//            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(), "해당 이메일의 정보가 " + ResponseErrorCode.KO004.getDesc()));
//        } else {
//            adminId = adminCompanyInfoDto.getAdminId();
//            companyId = adminCompanyInfoDto.getCompanyId();
//            companyCode = adminCompanyInfoDto.getCompanyCode();
//        }
//
//        if(personalInfoProvisionSaveDto.getPiRecipientType() == 2 && personalInfoProvisionSaveDto.getPiAgreeType() == 'N') {
//            log.error("제3자 제공일 경우 정보제공 동의여부를 체크해주세요.");
//            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO000.getCode(), ResponseErrorCode.KO000.getDesc()));
//        }
//
//        // 정보제공 등록 코드
//        ActivityCode activityCode = ActivityCode.AC_21;
//        // 활동이력 저장 -> 비정상 모드
//        String ip = CommonUtil.clientIp();
//        Long activityHistoryId = activityHistoryService.insertHistory(4, adminId, activityCode, companyCode + " - " + activityCode.getDesc() + " 시도 이력", "", ip, 0, email);
//

//        String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // 고유넘버
//        String proCode = keyGenerateService.keyGenerate("kn_personal_info_provision", nowDate+"companyCode", "KokonutSystem");

//
//        PersonalInfoProvision personalInfoProvision = modelMapper.map(personalInfoProvisionSaveDto,PersonalInfoProvision.class);
//        personalInfoProvision.setAdminId(adminId);
//        personalInfoProvision.setPiNumber(piNumber);
//        personalInfoProvision.setInsert_email(email);
//        personalInfoProvision.setInsert_date(LocalDateTime.now());
//
//        try {
//            personalInfoProvisionRepository.save(personalInfoProvision);
//            log.error("정보제공 등록 성공");
//            activityHistoryService.updateHistory(activityHistoryId,
//                    companyCode + " - " + activityCode.getDesc() + " 완료 이력", "", 1);
//        } catch (Exception e){
//            log.error("정보제공 등록 실패");
//            activityHistoryService.updateHistory(activityHistoryId,
//                    companyCode + " - " + activityCode.getDesc() + " 실패 이력", "필드 삭제 조건에 부합하지 않습니다.", 1);
//            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO073.getCode(), ResponseErrorCode.KO073.getDesc()));
//        }
//
//        String memo = "밑에 메모 별도수집대상?";
////
////        // 별도수집인 경우 별도수집 대상 목록을 미리 저장한다.
////        if(data.getRecipientType() == 2 && (data.getType() == 1 || data.getType() == 2) && data.getAgreeYn() == 'Y' && data.getAgreeType() == 2) {
////            String tableName = dynamicUserService.SelectTableName(data.getCompanyId());
////            List<Map<String, Object>> userList = dynamicUserService.SelectUserListByTableName(tableName);
////
////            if(data.getTargetStatus().equals("ALL")) {
////                List<String> idList = new ArrayList<String>();
////                for(Map<String, Object> userInfo : userList) {
////                    String id = userInfo.get("ID").toString();
////                    idList.add(id);
////                }
////
////                personalInfoService.saveTempProvisionAgree(number, idList);
////            } else {
////
////                List<String> idList = new ArrayList<String>();
////
////                String[] userIdxList = data.getTargets().split(",");
////                for(String userIdx : userIdxList) {
////                    for(int i = userList.size()-1; i >= 0; i--) {
////                        Map<String, Object> userInfo = userList.get(i);
////                        if(userInfo.get("IDX").toString().equals(userIdx)) {
////                            idList.add(userInfo.get("ID").toString());
////                            userList.remove(i);
////                            break;
////                        }
////                    }
////                }
////
////                personalInfoService.saveTempProvisionAgree(number, idList);
////            }
////        }
//
//        Integer period = Math.toIntExact(ChronoUnit.DAYS.between(personalInfoProvisionSaveDto.getPiStartDate(), personalInfoProvisionSaveDto.getPiExpDate()));
//        log.info("시작과 종료일까지 "+period+"일 차이");
//
//        // 담당자가 내부직원인지 검증
//        // 로직확인후 작업할것
//
//        // 받는사람에게 이메일 전송
//        boolean mailResult = sendEmailToRecipient(piNumber, personalInfoProvisionSaveDto.getPiRecipientEmail(),
//                personalInfoProvisionSaveDto.getPiStartDate(), personalInfoProvisionSaveDto.getPiExpDate(), period);
//        if(mailResult) {
//            log.info("메일전송 성공");
//        } else {
//            log.info("메일전송 실패");
//        }

        return ResponseEntity.ok(res.success(data));
    }

    // 개인정보제공 리스트 조회
    public ResponseEntity<Map<String, Object>> provisionList(String searchText, String stime,
                                                             String filterDownload, String filterState, JwtFilterDto jwtFilterDto, Pageable pageable) {
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



}
