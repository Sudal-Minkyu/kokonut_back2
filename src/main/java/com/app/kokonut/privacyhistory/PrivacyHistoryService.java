package com.app.kokonut.privacyhistory;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.ReqUtils;
import com.app.kokonut.common.CommonUtil;
import com.app.kokonut.common.Utils;
import com.app.kokonut.configs.ExcelService;
import com.app.kokonut.configs.GoogleOTP;
import com.app.kokonut.configs.MailSender;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dtos.ActivityCode;
import com.app.kokonut.privacyhistory.dtos.PrivacyHistoryCode;
import com.app.kokonut.privacyhistory.dtos.PrivacyHistoryExcelDownloadListDto;
import com.app.kokonut.privacyhistory.dtos.PrivacyHistoryListDto;
import com.app.kokonut.privacyhistory.dtos.PrivacyHistorySearchDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Woody
 * Date : 2023-05-15
 * Remark :
 */
@Slf4j
@Service
public class PrivacyHistoryService {

    private final GoogleOTP googleOTP;
    private final MailSender mailSender;

    private final ExcelService excelService;
    private final HistoryService historyService;

    private final AdminRepository adminRepository;
    private final PrivacyHistoryRepository privacyHistoryRepository;

    @Autowired
    public PrivacyHistoryService(GoogleOTP googleOTP, MailSender mailSender,
                                 ExcelService excelService, HistoryService historyService, AdminRepository adminRepository, PrivacyHistoryRepository privacyHistoryRepository) {
        this.googleOTP = googleOTP;
        this.mailSender = mailSender;
        this.excelService = excelService;
        this.historyService = historyService;
        this.adminRepository = adminRepository;
        this.privacyHistoryRepository = privacyHistoryRepository;
    }

    public void privacyHistoryInsert(Long adminId, PrivacyHistoryCode privacyHistoryCode,Integer kphType, String kphReason, String kphIpAddr, String email) {
        log.info("privacyHistoryInsert 개인정보 처리이력 저장  호출");
        PrivacyHistory privacyHistory = new PrivacyHistory();
        privacyHistory.setAdminId(adminId);
        privacyHistory.setPrivacyHistoryCode(privacyHistoryCode);
        privacyHistory.setKphType(kphType);
        privacyHistory.setKphReason(kphReason);
        privacyHistory.setKphIpAddr(kphIpAddr);
        privacyHistory.setInsert_email(email);
        privacyHistory.setInsert_date(LocalDateTime.now());

        privacyHistoryRepository.save(privacyHistory);

    }

    public ResponseEntity<Map<String, Object>> privacyHistoryList(String searchText, String stime, String filterRole, String filterState,
                                                                  JwtFilterDto jwtFilterDto, Pageable pageable) {
        log.info("privacyHistoryList 호출");

        log.info("searchText : "+searchText);
        log.info("stime : "+stime);
        log.info("filterRole : "+filterRole);
        log.info("filterState : "+filterState);

        AjaxResponse res = new AjaxResponse();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());

        PrivacyHistorySearchDto privacyHistorySearchDto = new PrivacyHistorySearchDto();
        privacyHistorySearchDto.setCompanyId(adminCompanyInfoDto.getCompanyId());
        privacyHistorySearchDto.setSearchText(searchText);
        privacyHistorySearchDto.setFilterRole(filterRole);
        privacyHistorySearchDto.setFilterState(filterState);

        if(!stime.equals("")) {
            List<LocalDateTime> stimeList = Utils.getStimeList(stime);
            privacyHistorySearchDto.setStimeStart(stimeList.get(0));
            privacyHistorySearchDto.setStimeEnd(stimeList.get(1).plusHours(23).plusMinutes(59));
        }
        else {
            log.info("활동날짜 범위를 지정해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO077.getCode(), ResponseErrorCode.KO077.getDesc()));
        }

        Page<PrivacyHistoryListDto> privacyHistoryListDtos = privacyHistoryRepository.findByPrivacyHistoryPage(privacyHistorySearchDto, pageable);

        if(privacyHistoryListDtos.getTotalPages() == 0) {
            log.info("조회된 데이터가 없습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO003.getCode(), ResponseErrorCode.KO003.getDesc()));
        }

        return ResponseEntity.ok(res.ResponseEntityPage(privacyHistoryListDtos));
    }

    // 개인정보 처리 활동이력 엑셀다운로드
    public ResponseEntity<Map<String, Object>> privacyHistoryDownloadExcel(String searchText, String stime, String filterRole, String filterState, String otpValue, String downloadReason, JwtFilterDto jwtFilterDto) throws IOException {
        log.info("privacyHistoryDownloadExcel 호출");

        log.info("searchText : "+searchText);
        log.info("stime : "+stime);
        log.info("filterRole : "+filterRole);
        log.info("filterState : "+filterState);
        log.info("otpValue : "+otpValue);
        log.info("downloadReason : "+downloadReason);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data;

        String email = jwtFilterDto.getEmail();

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

        PrivacyHistorySearchDto privacyHistorySearchDto = new PrivacyHistorySearchDto();
        privacyHistorySearchDto.setCompanyId(adminCompanyInfoDto.getCompanyId());
        privacyHistorySearchDto.setSearchText(searchText);
        privacyHistorySearchDto.setFilterRole(filterRole);
        privacyHistorySearchDto.setFilterState(filterState);

        if(!stime.equals("")) {
            List<LocalDateTime> stimeList = Utils.getStimeList(stime);
            privacyHistorySearchDto.setStimeStart(stimeList.get(0));
            privacyHistorySearchDto.setStimeEnd(stimeList.get(1).plusHours(23).plusMinutes(59));
        }
        else {
            log.info("활동날짜 범위를 지정해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO077.getCode(), ResponseErrorCode.KO077.getDesc()));
        }

        List<PrivacyHistoryExcelDownloadListDto> privacyHistoryExcelDownloadListDtos = privacyHistoryRepository.findByPrivacyHistoryList(privacyHistorySearchDto);

        if(privacyHistoryExcelDownloadListDtos.isEmpty()) {
            log.info("처리이력 데이터가 존재하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO115.getCode(), ResponseErrorCode.KO115.getDesc()));
        }

        List<Map<String, Object>> privacyHistoryExcelDownloadList = new ArrayList<>();
        Map<String, Object> privacyHistoryExcelDownloadData;
        for (PrivacyHistoryExcelDownloadListDto privacyHistoryExcelDownloadListDto : privacyHistoryExcelDownloadListDtos) {
            privacyHistoryExcelDownloadData = new HashMap<>();

            privacyHistoryExcelDownloadData.put("관리자", privacyHistoryExcelDownloadListDto.getKnName());
            privacyHistoryExcelDownloadData.put("관리자이메일", privacyHistoryExcelDownloadListDto.getKnEmail());
            privacyHistoryExcelDownloadData.put("관리자 등급", privacyHistoryExcelDownloadListDto.getKnRoleDesc());
            privacyHistoryExcelDownloadData.put("처리내역", privacyHistoryExcelDownloadListDto.getPrivacyHistoryCode());
            privacyHistoryExcelDownloadData.put("처리내용", privacyHistoryExcelDownloadListDto.getKphReason());
            privacyHistoryExcelDownloadData.put("활동일시", privacyHistoryExcelDownloadListDto.getInsert_date());
            privacyHistoryExcelDownloadData.put("접속IP", privacyHistoryExcelDownloadListDto.getKphIpAddr());

            privacyHistoryExcelDownloadList.add(privacyHistoryExcelDownloadData);
        }

        // 처리이력 다운로드 코드
        ActivityCode activityCode = ActivityCode.AC_10;
        String ip = CommonUtil.publicIp();
        Long activityHistoryId;

        // 활동이력 저장 -> 비정상 모드
        activityHistoryId = historyService.insertHistory(2, adminId, activityCode,
                cpCode+" - "+activityCode.getDesc()+" 시도 이력", downloadReason, ip, 0, email);

        // 파일암호 전송
        // 파일암호(숫자6자리) 생성
        SecureRandom secureRandom = new SecureRandom();
        int filePassword = secureRandom.nextInt(900000) + 100000;
        log.info("생성된 파일암호 : "+filePassword);

        // 인증번호 메일전송
        String title = ReqUtils.filter("처리이력 파일의 암호가 도착했습니다.");
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
            String fileName = now+"_처리이력";
            String sheetName = "관리자처리이력";

            log.info("파일명 : "+fileName);
            log.info("시트명 : "+sheetName);
            data = excelService.createExcelFile(fileName, sheetName, privacyHistoryExcelDownloadList, String.valueOf(filePassword));

            historyService.updateHistory(activityHistoryId, cpCode+" - "+activityCode.getDesc()+" 시도 이력", downloadReason, 1);

        }else{
            historyService.updateHistory(activityHistoryId, cpCode+" - "+activityCode.getDesc()+" 시도 이력", downloadReason+"- 처리이력다운로드 파일암호전송 실패", 1);

            // mailSender 실패
            log.error("### 해당 메일 전송에 실패했습니다. 관리자에게 문의하세요. reciverEmail : "+ email);
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO041.getCode(), ResponseErrorCode.KO041.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

}
