package com.app.kokonut.totalDBDownload;

import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dto.ActivityCode;
import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.admin.dtos.AdminOtpKeyDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.realcomponent.CommonUtil;
import com.app.kokonut.common.realcomponent.Utils;
import com.app.kokonut.configs.GoogleOTP;
import com.app.kokonut.totalDBDownload.dtos.TotalDbDownloadListDto;
import com.app.kokonut.totalDBDownload.dtos.TotalDbDownloadSearchDto;
import com.app.kokonut.totalDBDownloadHistory.TotalDbDownloadHistory;
import com.app.kokonut.totalDBDownloadHistory.TotalDbDownloadHistoryRepository;
import com.app.kokonutdormant.KokonutDormantService;
import com.app.kokonutuser.KokonutUserService;
import com.app.kokonutuser.dtos.KokonutUserFieldDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Woody
 * Date : 2023-01-13
 * Time :
 * Remark : TotalDbDownload Service
 */
@Slf4j
@Service
public class TotalDbDownloadService {

    private final HistoryService historyService;

    private final GoogleOTP googleOTP;
    private final AdminRepository adminRepository;
    private final KokonutUserService kokonutUserService;
    private final KokonutDormantService kokonutDormantService;

    private final TotalDbDownloadRepository totalDbDownloadRepository;
    private final TotalDbDownloadHistoryRepository totalDbDownloadHistoryRepository;

    @Autowired
    public TotalDbDownloadService(GoogleOTP googleOTP, AdminRepository adminRepository,
                                  HistoryService historyService, KokonutDormantService kokonutDormantService,
                                  KokonutUserService kokonutUserService,
                                  TotalDbDownloadRepository totalDbDownloadRepository, TotalDbDownloadHistoryRepository totalDbDownloadHistoryRepository){
        this.googleOTP = googleOTP;
        this.adminRepository = adminRepository;
        this.kokonutUserService = kokonutUserService;
        this.kokonutDormantService = kokonutDormantService;
        this.historyService = historyService;
        this.totalDbDownloadRepository = totalDbDownloadRepository;
        this.totalDbDownloadHistoryRepository = totalDbDownloadHistoryRepository;
    }

    // 개인정보 DB 데이터 전체 다운로드 요청
    @Transactional
    public ResponseEntity<Map<String, Object>> userDbDataDownloadApply(String otpValue, String reason, String email) {
        log.info("userDbDataDownloadApply 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("otpValue : "+ otpValue);
        log.info("reason : "+ reason);
        log.info("email : "+email);

        // 해당 이메일을 통해 회사 tdId 조회
        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

        Long adminId;
        Long companyId;
        String companyCode;

        if(adminCompanyInfoDto == null) {
            log.error("이메일 정보가 존재하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(), "해당 이메일의 정보가 "+ResponseErrorCode.KO004.getDesc()));
        }
        else {
            adminId = adminCompanyInfoDto.getAdminId(); // modifiertdId
            companyId = adminCompanyInfoDto.getCompanyId(); // companyId
            companyCode = adminCompanyInfoDto.getCompanyCode(); // tableName
        }

        AdminOtpKeyDto adminOtpKeyDto = adminRepository.findByOtpKey(email);
        if(adminOtpKeyDto != null) {
            // OTP 검증 절차
            boolean auth = googleOTP.checkCode(otpValue, adminOtpKeyDto.getKnOtpKey());
//			log.info("auth : " + auth);

            if (!auth) {
                log.error("입력된 구글 OTP 값이 일치하지 않습니다. 확인해주세요.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO012.getCode(), ResponseErrorCode.KO012.getDesc()));
            }
        } else {
            log.error("등록된 OTP가 존재하지 않습니다. 구글 OTP 2단계 인증을 등록해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO011.getCode(), ResponseErrorCode.KO011.getDesc()));
        }

        // 회원 DB데이터 다운로드 요청 코드
        ActivityCode activityCode = ActivityCode.AC_22;
        // 활동이력 저장 -> 비정상 모드
        String ip = CommonUtil.clientIp();
        Long activityHistoryId = historyService.insertHistory(2, adminId, activityCode, companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, 0, email);

        // 회원 DB데이터 다운로드 요청건 insert
        TotalDbDownload totalDbDownload = new TotalDbDownload();
        totalDbDownload.setAdminId(adminId);
        totalDbDownload.setTdReason(reason);
        totalDbDownload.setTdState(1);
        totalDbDownload.setTdApplyDate(LocalDateTime.now());
        totalDbDownload.setInsert_date(LocalDateTime.now());

        try{
            totalDbDownloadRepository.save(totalDbDownload);

            log.info("회원 DB 데이터 다운로드 요청 완료");
            historyService.updateHistory(activityHistoryId,
                    companyCode+" - "+activityCode.getDesc()+" 완료 이력", "", 1);
        } catch (Exception e){
            log.error("e : "+e.getMessage());
            log.error("회원 DB 데이터 다운로드 요청 실패");
            historyService.updateHistory(activityHistoryId,
                    companyCode+" - "+activityCode.getDesc()+" 실패 이력", "필드 삭제 조건에 부합하지 않습니다.", 1);
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO068.getCode(), ResponseErrorCode.KO068.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 개인정보 DB 데이터 다운로드 요청건 리스트
    public ResponseEntity<Map<String, Object>> userDbDataDownloadList(TotalDbDownloadSearchDto totalDbDownloadSearchDto, String email, Pageable pageable) {
        log.info("userDbDataDownloadList 호출");

        AjaxResponse res = new AjaxResponse();

        log.info("totalDbDownloadSearchDto : "+ totalDbDownloadSearchDto);
        log.info("email : "+email);

        // 해당 이메일을 통해 회사 tdId 조회
        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

        String companyCode;

        if(adminCompanyInfoDto == null) {
            log.error("이메일 정보가 존재하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(), "해당 이메일의 정보가 "+ResponseErrorCode.KO004.getDesc()));
        }
        else {
            companyCode = adminCompanyInfoDto.getCompanyCode(); // tableName
        }

        Page<TotalDbDownloadListDto> totalDbDownloadListDtoList = totalDbDownloadRepository.findByTotalDbDownloadList(totalDbDownloadSearchDto, companyCode, pageable);

        return ResponseEntity.ok(res.ResponseEntityPage(totalDbDownloadListDtoList));
    }

    // 개인정보 DB 데이터 다운로드 시작
    @Transactional
    public void userDbDataDownloadStart(Long tdId, String email, HttpServletRequest request, HttpServletResponse response) {
        log.info("userDbDataDownloadStart 호출");

        log.info("tdId : "+ tdId);
        log.info("email : "+email);

        // 해당 이메일을 통해 회사 tdId 조회
        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

        Long adminId;
        Long companyId;
        String companyCode;

        if(adminCompanyInfoDto == null) {
            log.error("이메일 정보가 존재하지 않습니다.");
            return;
        }
        else {
            adminId = adminCompanyInfoDto.getAdminId(); // modifiertdId
            companyId = adminCompanyInfoDto.getCompanyId(); // companyId
            companyCode = adminCompanyInfoDto.getCompanyCode(); // tableName
        }

        try{

            // 개인정보 DB 데이터 다운로드 시작 코드
            ActivityCode activityCode = ActivityCode.AC_23;
            // 활동이력 저장 -> 비정상 모드
            String ip = CommonUtil.clientIp();
            Long activityHistoryId = historyService.insertHistory(3, adminId, activityCode,
                    companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, 0, email);

            Optional<TotalDbDownload> optionalTotalDbDownload = totalDbDownloadRepository.findById(tdId);
            if(optionalTotalDbDownload.isPresent()) {

                if(optionalTotalDbDownload.get().getTdLimit() == 0) {
                    log.error("다운로드 횟수가 초과됬습니다. 재요청해주시길 바랍니다.");
                    return;
                }

                List<String> headerList = new ArrayList<>();
                List<String> headerKeyList = new ArrayList<>();

                request.setCharacterEncoding("UTF-8");
                response.setContentType("text/html; charset=UTF-8");

//                String DECRYPTED_KEY = companyService.selectCompanyEncryptKey(companyId); // 복호화용 키 일단 보류

                List<KokonutUserFieldDto> columns = kokonutUserService.getUserSendDataColumns(companyCode);

                List<Map<String, Object>> kokonutUserData = kokonutUserService.selectUserList(companyCode);
                List<Map<String, Object>> kokonutDormantData = kokonutDormantService.selectDormantList(companyCode);

                for (KokonutUserFieldDto kokonutUserFieldDto : columns) {
                    String Field = kokonutUserFieldDto.getField();
                    String Comment = kokonutUserFieldDto.getComment();

                    // Field 옵션 명 및 암호화 정형화
                    String FieldOptionName = Comment;

                    if(!FieldOptionName.contains("수정가능")) {
                        if(Comment.contains("(")) {
                            String[] FieldOptionNameList = Comment.split("\\(");
                            FieldOptionName = FieldOptionNameList[0];
                        }

                        // 보낼필요 없는 데이터
                        if("비밀번호".equals(FieldOptionName)) {
                            continue;
                        }
                    } else {
                        if (Comment.contains("(")) {
                            String[] FieldOptionNameList = Comment.split("\\(");
                            FieldOptionName = FieldOptionNameList[0];
                        }
                    }

                    headerList.add(FieldOptionName);
                    headerKeyList.add(Field);

                    // 각 데이터의 대한 복호화가 필요한 데이터일 경우 어떻게 처리할건지 방안세우기 to. woody
//                    for (int j = 0; j < dataList.size(); j++) {
//                        HashMap<String, Object> map = dataList.get(j);
//
//                        // 내용이 비였으면 "" 값 입력
//                        if(!map.containsKey(Field)) {
//                            map.put(Field, "");
//                        }
//                    }
//                    if("성별".equals(FieldOptionName)) {
//                        FieldOptionName = "성별(0:남 / 1:여)";
//                    }
//                    if("개인정보 동의".equals(FieldOptionName)) {
//                        FieldOptionName = "개인정보 동의(N:동의하지않음 / Y:동의)";
//                    }
//                    // 휴대폰번호 암호화 분기처리된 값 처리를 위함
//                    if(Comment.contains("암호화") && Field.equals("Mobile_Number")) {
//                        isDecryptMobileNumber = true;
//                    }
                }

                String state = "현재상태";
                headerList.add(state);
                headerKeyList.add(state);

                // 삭제 DB는 일단 제외 woody
//                for (int i = 0; i < removeList.size(); i++) {
//                    HashMap<String, Object> removeMap = removeList.get(i);
//                    dataList.add(removeMap);
//                }

                String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                String exportFileName = companyCode + "_개인정보 DB 데이터_" + nowDate + ".csv";

                // 파일명 인코딩
                String browser;
                String userAgent = request.getHeader("User-Agent");

                if (userAgent.contains("MSIE") || userAgent.contains("Trident"))   {
                    browser = "MSIE";
                } else if (userAgent.contains("Opera") || userAgent.contains("OPR")) {
                    browser = "Opera";
                } else if (userAgent.contains("Safari")) {
                    if(userAgent.contains("Chrome")){
                        browser = "Chrome";
                    }else{
                        browser = "Safari";
                    }
                } else {
                    browser = "Firefox";
                }

                if (browser.equals("MSIE")) {
                    exportFileName = new String(exportFileName.getBytes("euc-kr"), StandardCharsets.ISO_8859_1);
                } else if (browser.equals("Firefox") || browser.equals("Safari") || browser.equals("Opera")) {
                    exportFileName =  '"' + new String(exportFileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) + '"';
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < exportFileName.length(); i++)
                    {
                        char c = exportFileName.charAt(i);
                        if (c > '~') {
                            sb.append(URLEncoder.encode("" + c, StandardCharsets.UTF_8));
                        } else {
                            sb.append(c);
                        }
                    }
                    exportFileName = sb.toString();
                }

                response.setHeader("Content-Disposition", "attachment; filename=\""+exportFileName+"\";");
                response.setContentType( "application/octet-stream; UTF-8" );
                response.setHeader("Content-Type", "application/octet-stream");
                response.setHeader("Content-Transfer-Encoding", "binary;");
                response.setHeader("Pragma", "no-cache;");
                response.setHeader("Expires", "-1;");

                Cookie cookie = new Cookie("fileDownload", URLEncoder.encode("true", StandardCharsets.UTF_8));
                cookie.setMaxAge(10);
                response.addCookie(cookie);

                PrintWriter printWriter = response.getWriter();
                printWriter.write("\ufeff");
                CSVPrinter csvPrinter = new CSVPrinter(printWriter, CSVFormat.EXCEL);

                // 헤더
                Object[] csvHeader = new String[headerList.size()];
                for (int i = 0; i < headerList.size(); i++) {
                    csvHeader[i] = headerList.get(i);
                }
                csvPrinter.printRecord(csvHeader);
                printWriter.flush();

                // 개인정보 테이블 데이터 csv파일에 삽입
                if(kokonutUserData != null) {
                    log.info("개인정보데이터 엑셀로 전송");
                    for (Map<String, Object> userData : kokonutUserData) {
                        // 엑셀파일 헤더에 값 넣기
                        Object[] csvBody = new String[headerKeyList.size()];

                        // 해당 헤더의 대한 데이터 넣기
                        for (int j = 0; j < headerKeyList.size(); j++) {
                            String Field = headerKeyList.get(j);
                            if(Field.equals(state)) {
                                csvBody[j] = "이용중";
                            } else {
                                if (userData.get(Field) != null) {
                                    // 각 데이터 복호화가 필요할시 사용
//                                  String decryptedValue = AesCrypto.decrypt(new String(map.get(i).get(Field).toString().getBytes()), DECRYPTED_KEY);
                                    csvBody[j] = Utils.weekPointForExcel(userData.get(Field).toString() + "\t");
                                } else {
                                    csvBody[j] = "";
                                }
                            }
                        }
                        csvPrinter.printRecord(csvBody);
                        printWriter.flush();
                    }
                }

                // 휴면 테이블 데이터 csv파일에 삽입
                if(kokonutDormantData != null) {
                    log.info("휴면데이터 엑셀로 전송");
                    for (Map<String, Object> dormantData : kokonutDormantData) {
                        // 엑셀파일 헤더에 값 넣기
                        Object[] csvBody = new String[headerKeyList.size()];

                        // 해당 헤더의 대한 데이터 넣기
                        for (int j = 0; j < headerKeyList.size(); j++) {
                            String Field = headerKeyList.get(j);
                            if(Field.equals(state)) {
                                csvBody[j] = "휴면중";
                            } else {
                                if (dormantData.get(Field) != null) {
//                                  String decryptedValue = AesCrypto.decrypt(new String(map.get(i).get(Field).toString().getBytes()), DECRYPTED_KEY);
                                    csvBody[j] = Utils.weekPointForExcel(dormantData.get(Field).toString() + "\t");
                                } else {
                                    csvBody[j] = "";
                                }
                            }
                        }
                        csvPrinter.printRecord(csvBody);
                        printWriter.flush();
                    }
                }
                csvPrinter.close();

                // 다운로드 요청 완료시 횟수 차감
                optionalTotalDbDownload.get().setTdLimit(optionalTotalDbDownload.get().getTdLimit()-1);
                TotalDbDownload saveTotalDbDownload = totalDbDownloadRepository.save(optionalTotalDbDownload.get());

                // 다운로드 로그 테이블에 기록
                TotalDbDownloadHistory totalDbDownloadHistory = new TotalDbDownloadHistory();
                totalDbDownloadHistory.setTdId(saveTotalDbDownload.getTdId());
                totalDbDownloadHistory.setTdhFileName(companyCode + "_개인정보 DB 데이터_" + nowDate + ".csv");
                totalDbDownloadHistory.setTdhReason("개인정보 DB 데이터 다운로드");
                totalDbDownloadHistory.setInsert_date(LocalDateTime.now());
                totalDbDownloadHistoryRepository.save(totalDbDownloadHistory);

                // 활동 완료 업데이트
                historyService.updateHistory(activityHistoryId,
                        companyCode+" - "+activityCode.getDesc()+" 완료 이력", "", 1);

            } else {
                log.error("개인정보 DB 데이터 다운로드 요청 데이터가 존재하지 않습니다.");
                historyService.updateHistory(activityHistoryId,
                        companyCode+" - "+activityCode.getDesc()+" 실패 이력", "조회한 데이터가 없습니다.", 1);
            }

        } catch (Exception e){
            log.error("e : "+e.getMessage());
            log.error("조회한 데이터가 없습니다.");
        }
    }

}
