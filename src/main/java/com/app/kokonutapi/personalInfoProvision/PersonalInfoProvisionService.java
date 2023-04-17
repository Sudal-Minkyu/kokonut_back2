package com.app.kokonutapi.personalInfoProvision;

import com.app.kokonut.common.realcomponent.AriaUtil;
import com.app.kokonut.configs.MailSender;
import com.app.kokonutapi.personalInfoProvision.dtos.PersonalInfoProvisionSetDto;
import com.app.kokonutapi.personalInfoProvision.dtos.PersonalInfoProvisionSaveDto;
import org.modelmapper.ModelMapper;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.configs.KeyGenerateService;
import com.app.kokonutapi.personalInfoProvision.dtos.PersonalInfoProvisionListDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Woody
 * Date : 2023-01-17
 * Remark :
 */
@Slf4j
@Service
public class PersonalInfoProvisionService {

    private final AriaUtil ariaUtil;
    private final ModelMapper modelMapper;
    private final MailSender mailSender;
    private final KeyGenerateService keyGenerateService;

    private final HistoryService historyService;

    private final AdminRepository adminRepository;
    private final PersonalInfoProvisionRepository personalInfoProvisionRepository;

    @Autowired
    public PersonalInfoProvisionService(AriaUtil ariaUtil, ModelMapper modelMapper, MailSender mailSender,
                                        KeyGenerateService keyGenerateService, HistoryService historyService,
                                        AdminRepository adminRepository, PersonalInfoProvisionRepository personalInfoProvisionRepository){
        this.ariaUtil = ariaUtil;
        this.modelMapper = modelMapper;
        this.mailSender = mailSender;
        this.keyGenerateService = keyGenerateService;
        this.historyService = historyService;
        this.adminRepository = adminRepository;
        this.personalInfoProvisionRepository = personalInfoProvisionRepository;
    }


    // 정보제공 등록
    @Transactional
    public ResponseEntity<Map<String, Object>> privisionSave(PersonalInfoProvisionSaveDto personalInfoProvisionSaveDto, String email) {
        log.info("privisionSave 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("personalInfoProvisionSaveDto : " + personalInfoProvisionSaveDto);
        log.info("email : " + email);

//        // 해당 이메일을 통해 회사 IDX 조회
//        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
//
//        long adminId;
//        long companyId;
//        String companyCode;
//
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
//        // 고유넘버
//        String piNumber = keyGenerateService.keyGenerate("personal_info_provision", nowDate+companyCode, String.valueOf(adminId));
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

    // 정보제공을 받는 담당자에게 이메일 전송
    public Boolean sendEmailToRecipient(String number, String email, LocalDateTime startDate, LocalDateTime expDate, Integer period) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startDateStr = sdf.format(startDate);
            String expDateStr = sdf.format(expDate);

            String title = "[Kokonut] 정보제공 담당자 배정 안내";
            String contents;

            String url = "/mail/notice-in-charge-of-personal-info" +
                    "?number=" + ariaUtil.Encrypt(number) +
                    "&startDate=" + ariaUtil.Encrypt(startDateStr) +
                    "&expDate=" + ariaUtil.Encrypt(expDateStr) +
                    "&period=" + period +
                    "&unknownUser=true";

//            if(adminRepository.selectAdminByEmail(paramMap) == null) { // -> 조회후 회원가입되지 않은
//                title = "[Kokonut] 정보제공 담당자 배정 안내";
//                url.append("&unknownUser=true");
//            }
//            else {
//                title = "[Kokonut] 정보제공 담당자 배정 안내";
//                url.append("&unknownUser=false");
//            }

            contents = mailSender.getHTML2(url);
            mailSender.sendMail(email, null, title, contents);

            return true;
        } catch (IOException e) {
            log.error("메일발송 실패");
            log.error("e : "+e.getMessage());
            return false;
        }
    }

    // 정보제공 목록 조회
    public ResponseEntity<Map<String,Object>> personalInfoProvisionList(PersonalInfoProvisionSetDto personalInfoProvisionSetDto) {

        log.info("personalInfoProvisionList 호출");

        log.info("@RequestBody personalInfoProvisionMapperDto : "+personalInfoProvisionSetDto);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<PersonalInfoProvisionListDto> personalInfoProvisionListDtos =
                personalInfoProvisionRepository.findByProvisionList(personalInfoProvisionSetDto.getPersonalInfoProvisionMapperDto());
        Integer total = personalInfoProvisionListDtos.size();

        log.info("personalInfoProvisionListDtos : "+personalInfoProvisionListDtos);
        log.info("total : "+total);

        data.put("personalInfoProvisionListDtos",personalInfoProvisionListDtos);
        data.put("total",total);

        return ResponseEntity.ok(res.success(data));
    }

    public ResponseEntity<Map<String, Object>> provisionListForAgree(PersonalInfoProvisionSetDto personalInfoProvisionSetDto) {
        log.info("provisionListForAgree 호출");

        log.info("@RequestBody personalInfoProvisionMapperDto : "+personalInfoProvisionSetDto);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        try{

//            HashMap<String, Object> searchMap = null;
//            if(paramMap.containsKey("searchData")){
//                searchMap = (HashMap<String, Object>) paramMap.get("searchData");
//                paramMap.putAll(searchMap);
//            }
//
//            paramMap.put("companyId", authUser.getUser().getCompanyId());
//            personalInfoProvisionSetDto.getPersonalInfoProvisionMapperDto().setCompanyId(13L);

//            if(!paramMap.containsKey("state")) {
//                throw new Exception("not found state");
//            }
//
//            final Integer STATE = Integer.parseInt(paramMap.get("state").toString());
            final Integer STATE = personalInfoProvisionSetDto.getPersonalInfoProvisionMapperDto().getState();

            // 수집 필수 - 동의여부 별도 수집 조건 추가
            personalInfoProvisionSetDto.getPersonalInfoProvisionMapperDto().setAgreeYn("Y");
            personalInfoProvisionSetDto.getPersonalInfoProvisionMapperDto().setAgreeType(2);
//            paramMap.put("agreeYn", 'Y');
//            paramMap.put("agreeType", 2);

//            rows = personalInfoProvisionService.getProvisionList(paramMap);
//            total = rows.size();

            List<PersonalInfoProvisionListDto> personalInfoProvisionListDtos =
                    personalInfoProvisionRepository.findByProvisionList(personalInfoProvisionSetDto.getPersonalInfoProvisionMapperDto());
            int total = personalInfoProvisionListDtos.size();

            /*
             * 상태
             * 1: 수집중: 정보제공일 이전
             * 2: 수집완료: 정보제공일 부터 정보제공 만료일 까지
             * 3: 기간만료: 정보제공 만료일 이후
             */

            // 상태값 계산
            Date today = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//            for(int i = rows.size()-1; i >= 0; i--) {
//                HashMap<String, Object> row = rows.get(i);
//
//                Date startDate = sdf.parse(row.get("START_DATE").toString());
//                Date expDate = sdf.parse(row.get("EXP_DATE").toString());
//
//                int rowState = 0;
//
//                if(today.compareTo(startDate) < 0) {
//                    rowState = 1; // 수집중
//                }
//                else if(today.compareTo(startDate) >= 0 && today.compareTo(expDate) <= 0){
//                    rowState = 2; // 수집완료
//                }
//                else {
//                    rowState = 3; // 기간만료
//                }
//
//                if(STATE == 0 || (STATE != 0 && STATE == rowState)) {
//                    row.put("STATE", rowState);
//                }
//                else {
//                    rows.remove(i);
//                    total--;
//                }
//            }
            for(int i = total-1; i >= 0; i--) {
                PersonalInfoProvisionListDto row = personalInfoProvisionListDtos.get(i);

                Date startDate = sdf.parse(row.getStartDate().toString());
                Date expDate = sdf.parse(row.getExpDate().toString());

                int rowState = 0;

                if(today.compareTo(startDate) < 0) {
                    rowState = 1; // 수집중
                }
                else if(today.compareTo(startDate) >= 0 && today.compareTo(expDate) <= 0){
                    rowState = 2; // 수집완료
                }
                else {
                    rowState = 3; // 기간만료
                }

                if(STATE == 0 || STATE == rowState) {
                    row.setState(rowState);
                }
                else {
                    personalInfoProvisionListDtos.remove(i);
                    total--;
                }
            }

            data.put("personalInfoProvisionListDtos",personalInfoProvisionListDtos);
            data.put("total",total);

//            dataTables = new DataTables(paramMap, rows, total);

        }catch (Exception e) {
//            logger.error(e.getMessage());
            log.error("e : "+e);
        }

        return ResponseEntity.ok(res.success(data));
    }




}
