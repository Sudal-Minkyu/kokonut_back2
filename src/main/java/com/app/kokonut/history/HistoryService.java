package com.app.kokonut.history;

import com.app.kokonut.admin.Admin;
import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.ReqUtils;
import com.app.kokonut.common.CommonUtil;
import com.app.kokonut.common.Utils;
import com.app.kokonut.configs.ExcelService;
import com.app.kokonut.configs.GoogleOTP;
import com.app.kokonut.configs.MailSender;
import com.app.kokonut.history.dtos.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Woody
 * Date : 2022-11-03
 * Remark :
 */
@Slf4j
@Service
public class HistoryService {


    private final GoogleOTP googleOTP;
    private final ExcelService excelService;
    private final MailSender mailSender;

    private final AdminRepository adminRepository;
    private final HistoryRepository historyRepository;

    @Autowired
    public HistoryService(GoogleOTP googleOTP, ExcelService excelService, MailSender mailSender,
                          AdminRepository adminRepository, HistoryRepository historyRepository) {
        this.googleOTP = googleOTP;
        this.excelService = excelService;
        this.mailSender = mailSender;
        this.adminRepository = adminRepository;
        this.historyRepository = historyRepository;
    }

    // 관리자 활동이력 리스트 ahType => "2"
    @Transactional
    public ResponseEntity<Map<String,Object>> activityList(String email, String searchText, String stime, String filterRole, String actvityType, Pageable pageable) {
        log.info("actvityList 호출");

        log.info("email : "+email);
        log.info("searchText : "+searchText);
        log.info("stime : "+stime);
        log.info("filterRole : "+filterRole);
        log.info("actvityType : "+actvityType);

        AjaxResponse res = new AjaxResponse();

        // 접속한 사용자 인덱스
        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
        long adminId = adminCompanyInfoDto.getAdminId();
        long companyId = adminCompanyInfoDto.getCompanyId();
        String cpCode = adminCompanyInfoDto.getCompanyCode();

        HistorySearchDto historySearchDto = new HistorySearchDto();
        historySearchDto.setCompanyId(companyId);
        historySearchDto.setSearchText(searchText);
        historySearchDto.setFilterRole(filterRole);
        if(!actvityType.equals("")) {
            List<ActivityCode> activityCodeList = new ArrayList<>();
            String[] employeeNamesSplit = actvityType.split(",");
            ArrayList<String> actvityTypeList = new ArrayList<>(Arrays.asList(employeeNamesSplit));

            for(String actvityCode : actvityTypeList) {
                activityCodeList.add(ActivityCode.valueOf(actvityCode));
            }
            historySearchDto.setActivityCodeList(activityCodeList);
        }

        if(!stime.equals("")) {
            List<LocalDateTime> stimeList = Utils.getStimeList(stime);
            historySearchDto.setStimeStart(stimeList.get(0));
            historySearchDto.setStimeEnd(stimeList.get(1).plusHours(23).plusMinutes(59));
        }
        else {
            log.info("활동날짜 범위를 지정해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO077.getCode(), ResponseErrorCode.KO077.getDesc()));
        }

        // 활동이력 조회 코드
        ActivityCode activityCode = ActivityCode.AC_07_1;
        String ip = CommonUtil.publicIp();
        Long activityHistoryId;

        // 활동이력 저장 -> 비정상 모드
        activityHistoryId = insertHistory(4, adminId, activityCode,
                cpCode+" - ", "", ip, 0, email);

        Page<HistoryListDto> historyListDtos = historyRepository.findByHistoryPage(historySearchDto, pageable);

        updateHistory(activityHistoryId, null, "", 1);
        return ResponseEntity.ok(res.ResponseEntityPage(historyListDtos));
    }

    // 관리자 활동이력 엑셀다운로드 ahType => "2"
    @Transactional
    public ResponseEntity<Map<String,Object>> activityDownloadExcel(String email, String searchText, String stime, String actvityType, String otpValue, String downloadReason) throws IOException {
        log.info("activityDownloadExcel 호출");

        log.info("email : "+email);
        log.info("searchText : "+searchText);
        log.info("stime : "+stime);
        log.info("actvityType : "+actvityType);
        log.info("otpValue : "+otpValue);
        log.info("downloadReason : "+downloadReason);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data;

        if(otpValue == null || otpValue.equals("")) {
            log.error("구글 OTP 값이 존재하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO010.getCode(),ResponseErrorCode.KO010.getDesc()));
        }

        // 접속한 사용자 인덱스
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

        HistorySearchDto historySearchDto = new HistorySearchDto();
        historySearchDto.setCompanyId(adminCompanyInfoDto.getCompanyId());
        historySearchDto.setSearchText(searchText);

        if(!actvityType.equals("")) {
            List<ActivityCode> activityCodeList = new ArrayList<>();
            String[] employeeNamesSplit = actvityType.split(",");
            ArrayList<String> actvityTypeList = new ArrayList<>(Arrays.asList(employeeNamesSplit));

            for(String actvityCode : actvityTypeList) {
                activityCodeList.add(ActivityCode.valueOf(actvityCode));
            }
            historySearchDto.setActivityCodeList(activityCodeList);
        }

        if(!stime.equals("")) {
            List<LocalDateTime> stimeList = Utils.getStimeList(stime);
            historySearchDto.setStimeStart(stimeList.get(0));
            historySearchDto.setStimeEnd(stimeList.get(1).plusHours(23).plusMinutes(59));
        }
        else {
            log.info("활동날짜 범위를 지정해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO077.getCode(), ResponseErrorCode.KO077.getDesc()));
        }

        List<HistoryExcelDownloadListDto> historyExcelDownloadListDtos = historyRepository.findByHistoryList(historySearchDto);
        if(historyExcelDownloadListDtos.isEmpty()) {
            log.info("활동이력 데이터가 존재하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO115.getCode(), ResponseErrorCode.KO115.getDesc()));
        }

        List<Map<String, Object>> historyDownloadDataList = new ArrayList<>();
        Map<String, Object> historyDownloadData;
        for (HistoryExcelDownloadListDto historyExcelDownloadListDto : historyExcelDownloadListDtos) {
            historyDownloadData = new HashMap<>();

            historyDownloadData.put("관리자명", historyExcelDownloadListDto.getKnName());
            historyDownloadData.put("관리자이메일", historyExcelDownloadListDto.getKnEmail());
            historyDownloadData.put("등급", historyExcelDownloadListDto.getKnRoleDesc());
            historyDownloadData.put("활동내역", historyExcelDownloadListDto.getActivityCode());
            historyDownloadData.put("활동상세내역", historyExcelDownloadListDto.getAhActivityDetail());
            historyDownloadData.put("사유", historyExcelDownloadListDto.getAhReason());
            historyDownloadData.put("활동일시", historyExcelDownloadListDto.getInsert_date());
            historyDownloadData.put("접속IP", historyExcelDownloadListDto.getAhIpAddr());
            historyDownloadData.put("상태", historyExcelDownloadListDto.getAhState());

            historyDownloadDataList.add(historyDownloadData);
        }

        // 활동이력 다운로드 코드
        ActivityCode activityCode = ActivityCode.AC_07_2;
        String ip = CommonUtil.publicIp();
        Long activityHistoryId;

        // 활동이력 저장 -> 비정상 모드
        activityHistoryId = insertHistory(2, adminId, activityCode,
                cpCode+" - "+downloadReason, downloadReason, ip, 0, email);

        // 파일암호 전송
        // 파일암호(숫자6자리) 생성
        SecureRandom secureRandom = new SecureRandom();
        int filePassword = secureRandom.nextInt(900000) + 100000;
        log.info("생성된 파일암호 : "+filePassword);

        // 인증번호 메일전송
        String title = ReqUtils.filter("활동이력 파일의 암호가 도착했습니다.");
        String contents = ReqUtils.unFilter("파일암호 : "+filePassword);

        // 템플릿 호출을 위한 데이터 세팅
        HashMap<String, String> callTemplate = new HashMap<>();
//        callTemplate.put("template", "KokonutMailTemplate");
        callTemplate.put("title", "활동이력 파일암호 알림");
        callTemplate.put("content", contents);

        // 템플릿 TODO 템플릿 디자인 추가되면 수정
        contents = mailSender.getHTML6(callTemplate);
        String reciverName = "kokonut";

        String mailSenderResult = mailSender.sendKokonutMail(email, reciverName, title, contents);
        if(mailSenderResult != null) {
            // mailSender 성공
            log.info("### 메일전송 성공했습니다. reciver Email : "+ email);

            LocalDate now = LocalDate.now();
            String fileName = now+"_관리자활동이력";
            String sheetName = "관리자활동이력";

            log.info("파일명 : "+fileName);
            log.info("시트명 : "+sheetName);
            data = excelService.createExcelFile(fileName, sheetName, historyDownloadDataList, String.valueOf(filePassword));

            updateHistory(activityHistoryId, null, downloadReason, 1);

        }else{
            updateHistory(activityHistoryId, null, downloadReason+"- 활동이력다운로드 파일암호전송 실패", 1);

            // mailSender 실패
            log.error("### 해당 메일 전송에 실패했습니다. 관리자에게 문의하세요. reciverEmail : "+ email);
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO041.getCode(), ResponseErrorCode.KO041.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 관리자 처리이력 리스트 ahType => "1"
    public ResponseEntity<Map<String,Object>> findByProcessHistoryList(String email, String searchText, String stime, String actvityType, Pageable pageable) {
        log.info("findByHistoryList 호출");

        log.info("email : "+email);
        log.info("searchText : "+searchText);
        log.info("stime : "+stime);
        log.info("actvityType : "+actvityType);

        AjaxResponse res = new AjaxResponse();

        // 접속한 사용자 인덱스
        Admin admin = adminRepository.findByKnEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다. : "+email));

        HistorySearchDto HistorySearchDto = new HistorySearchDto();
        HistorySearchDto.setCompanyId(admin.getCompanyId());
        HistorySearchDto.setSearchText(searchText);

        if(!actvityType.equals("")) {
            List<ActivityCode> activityCodeList = new ArrayList<>();
            String[] employeeNamesSplit = actvityType.split(",");
            ArrayList<String> actvityTypeList = new ArrayList<>(Arrays.asList(employeeNamesSplit));

            for(String actvityCode : actvityTypeList) {
                activityCodeList.add(ActivityCode.valueOf(actvityCode));
            }
            HistorySearchDto.setActivityCodeList(activityCodeList);
        }

        if(!stime.equals("")) {
            List<LocalDateTime> stimeList = Utils.getStimeList(stime);
            HistorySearchDto.setStimeStart(stimeList.get(0));
            HistorySearchDto.setStimeEnd(stimeList.get(1).plusHours(23).plusMinutes(59));
        }
        else {
            log.info("활동날짜 범위를 지정해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO077.getCode(), ResponseErrorCode.KO077.getDesc()));
        }

        Page<HistoryListDto> historyListDtos = historyRepository.findByHistoryPage(HistorySearchDto, pageable);
        return ResponseEntity.ok(res.ResponseEntityPage(historyListDtos));
    }

    // 활동이력 인서트
    @Transactional
    public Long insertHistory(int ahType, Long adminId, ActivityCode activityCode,
                                      String ahActivityDetail, String ahReason, String ahPublicIpAddr, int ahState, String email) {

        History History = new History();
        History.setAhType(ahType);
        History.setAdminId(adminId);
        History.setActivityCode(activityCode);
        History.setAhActivityDetail(ahActivityDetail);
        History.setAhReason(ahReason);
        History.setAhPublicIpAddr(ahPublicIpAddr);
        History.setAhState(ahState);
        History.setInsert_email(email);
        History.setInsert_date(LocalDateTime.now());

        History = historyRepository.save(History);

        return History.getAhId();
    }

    // 활동이력 수정
    @Transactional
    public void updateHistory(Long ahId, String activityDetail, String ahReason, int ahState) {
        History history = historyRepository.findById(ahId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 'activityCode' 입니다."));

        if (activityDetail != null) {
            history.setAhActivityDetail(activityDetail);
        }
        history.setAhReason(ahReason);
        history.setAhState(ahState);

        historyRepository.save(history);
    }

    // 활동이력 삭제
    public void deleteHistoryByIdx(Long idx) {
        History History = historyRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 'History' 입니다."));

        historyRepository.delete(History);
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> activityUpdate(long ahId) {
        log.info("activityUpdate 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        History history = historyRepository.findById(ahId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 'activityCode' 입니다."));

        history.setAhState(1);

        historyRepository.save(history);

        return ResponseEntity.ok(res.success(data));
    }

}
