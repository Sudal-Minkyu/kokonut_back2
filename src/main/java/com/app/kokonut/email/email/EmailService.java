package com.app.kokonut.email.email;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.awskmshistory.dto.AwsKmsResultDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.AESGCMcrypto;
import com.app.kokonut.common.CommonUtil;
import com.app.kokonut.common.ReqUtils;
import com.app.kokonut.common.Utils;
import com.app.kokonut.company.companydatakey.CompanyDataKeyService;
import com.app.kokonut.company.companysetting.CompanySettingRepository;
import com.app.kokonut.company.companysetting.dtos.CompanySettingEmailDto;
import com.app.kokonut.configs.MailSender;
import com.app.kokonut.email.email.dtos.EmailListDto;
import com.app.kokonut.email.email.dtos.EmailSearchDto;
import com.app.kokonut.email.email.dtos.EmailSendDto;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dtos.ActivityCode;
import com.app.kokonut.history.extra.decrypcounthistory.DecrypCountHistoryService;
import com.app.kokonutuser.KokonutUserService;
import com.app.kokonutuser.dtos.use.KokonutUserEmailFieldDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Woody
 * Date : 2023-04-07
 * Remark :
 */
@Slf4j
@Service
public class EmailService {

    private final HistoryService historyService;
    private final KokonutUserService kokonutUserService;
    private final CompanyDataKeyService companyDataKeyService;

    private final AdminRepository adminRepository;
    private final EmailRepository emailRepository;
    private final MailSender mailSender;

    private final CompanySettingRepository companySettingRepository;
    private final DecrypCountHistoryService decrypCountHistoryService;

    @Autowired
    public EmailService(HistoryService historyService, KokonutUserService kokonutUserService,
                        CompanyDataKeyService companyDataKeyService, EmailRepository emailRepository, AdminRepository adminRepository,
                        MailSender mailSender, CompanySettingRepository companySettingRepository, DecrypCountHistoryService decrypCountHistoryService) {
        this.historyService = historyService;
        this.kokonutUserService = kokonutUserService;
        this.companyDataKeyService = companyDataKeyService;
        this.emailRepository = emailRepository;
        this.adminRepository = adminRepository;
        this.mailSender = mailSender;
        this.companySettingRepository = companySettingRepository;
        this.decrypCountHistoryService = decrypCountHistoryService;
    }

    // 이메일 목록 조회
    public ResponseEntity<Map<String,Object>> emailList(JwtFilterDto jwtFilterDto, String searchText, String stime, String emPurpose, Pageable pageable) throws IOException {
        log.info("emailList 호출");

        log.info("email : "+jwtFilterDto.getEmail());
        log.info("searchText : "+searchText);
        log.info("stime : "+stime);

        String email = jwtFilterDto.getEmail();
        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

        long adminId = adminCompanyInfoDto.getAdminId();
        String cpCode = adminCompanyInfoDto.getCompanyCode();

        AjaxResponse res = new AjaxResponse();

        EmailSearchDto emailSearchDto = new EmailSearchDto();
        emailSearchDto.setCpCode(cpCode);
        emailSearchDto.setSearchText(searchText);
        emailSearchDto.setEmPurpose(emPurpose);

        if(!stime.equals("")) {
            List<LocalDateTime> stimeList = Utils.getStimeList(stime);
            emailSearchDto.setStimeStart(stimeList.get(0));
            emailSearchDto.setStimeEnd(stimeList.get(1).plusHours(23).plusMinutes(59));
        }

        log.info("emailSearchDto : "+emailSearchDto);

        ActivityCode activityCode;
        String ip = CommonUtil.publicIp();
        Long activityHistoryId;

        // 이메일 발송목록 조회 코드
        activityCode = ActivityCode.AC_59_4;

        // 활동이력 저장 -> 비정상 모드
        activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                cpCode+" - ", "", ip, 0, email);

        Page<EmailListDto> emailListDtos = emailRepository.findByEmailPage(emailSearchDto, pageable);

        historyService.updateHistory(activityHistoryId,
                null, "", 1);
        return ResponseEntity.ok(res.ResponseEntityPage(emailListDtos));
    }

    // 이메일 발송 호출
    public ResponseEntity<Map<String, Object>> sendEmailService(EmailSendDto emailSendDto, JwtFilterDto jwtFilterDto) throws Exception {
        log.info("sendEmailService 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

//        log.info("emailSendDto : "+emailSendDto);

        if(!Utils.isValidEmail(emailSendDto.getEmEmailSend())) {
            log.error("이메일주소 형식과 맞지 않습니다. 발송자 이메일을 확인해주시길 바랍니다. 입력 이메일 : "+emailSendDto.getEmEmailSend());
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO104.getCode(),ResponseErrorCode.KO104.getDesc() +" " +
                    "발송자 이메일을 확인해주시길 바랍니다. 입력 이메일 : "+emailSendDto.getEmEmailSend()));
        }

        Long reservationTime = null;
        if(emailSendDto.getEmType().equals("2")) {
            if(emailSendDto.getEmReservationDate() == null) {
                log.error("예약발송일 경우 발송할 시간이 필요합니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO105.getCode(),ResponseErrorCode.KO105.getDesc()));
            } else {
                reservationTime = emailSendDto.getEmReservationDate();
                log.info("예약발송 시간 : "+reservationTime);
            }
        }

        List<MultipartFile> multipartFiles = null;

        // 첨부파일 용량이 20MB가 넘는지 체크
        long totalSize = 0; // 총 파일 크기를 위한 변수
        final long TEN_MB = 10 * 1024 * 1024; // 10MB를 바이트 단위로 변환
        final long TWENTY_MB = 20 * 1024 * 1024; // 20MB를 바이트 단위로 변환
        if(emailSendDto.getMultipartFiles() != null && emailSendDto.getMultipartFiles().size() != 0) {
            multipartFiles = emailSendDto.getMultipartFiles();
            for (MultipartFile multipartFile : emailSendDto.getMultipartFiles()) {
                if (multipartFile.isEmpty()) {
                    continue;
                }
                try {
                    byte[] bytes = multipartFile.getBytes();
                    if(bytes.length > TEN_MB) {
                        log.error("하나의 파일에 용량이 10MB가 넘습니다. 10MB가 넘지 않도록 해주시길 바랍니다.");
                        return ResponseEntity.ok(res.fail(ResponseErrorCode.KO109.getCode(),ResponseErrorCode.KO109.getDesc()));
                    } else {
                        totalSize += bytes.length; // 파일 크기를 더함

                        // 총 파일 크기가 20MB를 초과하면 예외를 발생시킴
                        if (totalSize > TWENTY_MB) {
                            log.error("첨부파일 용량이 20MB가 넘습니다. 20MB가 넘지 않도록 해주시길 바랍니다.");
                            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO106.getCode(),ResponseErrorCode.KO106.getDesc()));
                        }
                    }
                } catch (IOException e) {
                    log.error("예외처리 : "+ e);
                    log.error("예외메세지 : "+ e.getMessage());
                }
            }
        }

        int emSendAllCount; // 발송건수

        String email = jwtFilterDto.getEmail();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
        Long adminId = adminCompanyInfoDto.getAdminId();
        String cpCode = adminCompanyInfoDto.getCompanyCode();
        String ctName = cpCode+"_1";

        // 테스트용 이메일리스트 변수
//        List<String> testEmail = new ArrayList<>(); //  emailSendDto.getEmailSendChoseList()
//        testEmail.add("13GSs9SfZGe#uT!ANOxy");
//        testEmail.add("I!@9RTP!!Qyay1ja9cRF");
//        emailSendDto.setEmailSendChoseList(testEmail);

        // 이메일지정 고유코드
        CompanySettingEmailDto companySettingEmailDto = companySettingRepository.findByCompanySettingEmail(cpCode);

        if(companySettingEmailDto.getCsEmailCodeSetting().equals("")) {
            log.error("이메일 항목으로 지정한 값이 없습니다. 환경설정에서 이메일발송할 항목을 선택 후 다시 시도해주시길 바랍니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO102.getCode(),ResponseErrorCode.KO102.getDesc()));
        }
        else {

            AwsKmsResultDto awsKmsResultDto = companyDataKeyService.findByCompanyDataKey(cpCode);

            String toCompanyName = companySettingEmailDto.getCpName();

            List<String> sendEmailList = new ArrayList<>();

            int dchCount = 0; // 복호화 카운팅

            String fieldName;
            if(!companySettingEmailDto.getCsEmailCodeSetting().equals("1_id")) {
                fieldName = cpCode+"_"+companySettingEmailDto.getCsEmailCodeSetting();
            } else {
                fieldName = "ID_"+companySettingEmailDto.getCsEmailCodeSetting();
            }
            log.info("이메일지정 필드명 : "+fieldName);

            // 해당 필드의 코멘트 조회
            String comment = kokonutUserService.getColumnComment(ctName, fieldName);
            log.info("comment : "+comment);

            int encType; // 암호화실행 여부
            int secType; // 암호화, 비암호화여부 : "0" 암호화, "1" 비암호화
            String[] commentText = comment.split(",");
            String commentCheck = commentText[3];
            String commentSecurity = commentText[1];
            log.info("commentCheck : "+commentCheck);

            if(commentCheck.equals("기본항목")) {
                encType = 0;
                secType = 1;
            }
            else if (commentCheck.equals("전자상거래법") && commentText[0].equals("이메일주소")) {
                encType = 1;
                secType = 0;
            }
            else if(commentCheck.equals("추가항목")){
                encType = 2;
                if (commentSecurity.equals("암호화")) {
                    secType = 0;
                } else {
                    secType = 1;
                }
            }
            else {
                // 이메일로 사용할수없는 필드임
                log.error("이메일 항목으로 지정한 값이 없습니다. 환경설정에서 이메일발송할 항목을 선택 후 다시 시도해주시길 바랍니다. 현재 지정된 항목 : "+commentCheck);
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO103.getCode(),ResponseErrorCode.KO103.getDesc() +"현재 지정된 항목 : "+commentCheck));
            }

            List<KokonutUserEmailFieldDto> kokonutUserEmailFieldDtos = kokonutUserService.emailFieldList(ctName, fieldName,
                    emailSendDto.getEmReceiverType(), emailSendDto.getEmailSendChoseList());
            emSendAllCount = kokonutUserEmailFieldDtos.size();
//            log.info("kokonutUserEmailFieldDtos : "+kokonutUserEmailFieldDtos);

            for (KokonutUserEmailFieldDto kokonutUserEmailFieldDto : kokonutUserEmailFieldDtos) {
//                log.info("emailField : "+kokonutUserEmailFieldDto.getEmailField());

                String decrypEmail; // 복호화한 이메일

                // 기본항목 아이디로 지정했을 경우
                if(encType == 0) {
                    decrypEmail = (String) kokonutUserEmailFieldDto.getEmailField();
                    if(Utils.isValidEmail(decrypEmail)) {
                        sendEmailList.add(decrypEmail);
                    }
                }

                // 전자상거레법의 이메일주소로 지정했을 경우
                else if(encType == 1) {
                    String[] value = String.valueOf(kokonutUserEmailFieldDto.getEmailField()).split("\\|\\|__\\|\\|");
//                    log.info("value : "+ Arrays.toString(value));
                    decrypEmail = AESGCMcrypto.decrypt(value[0], awsKmsResultDto.getSecretKey(), awsKmsResultDto.getIvKey())+value[1];
                    dchCount++;
                    if(Utils.isValidEmail(decrypEmail)) {
                        sendEmailList.add(decrypEmail);
                    }
                }

                // 추가항목으로 지정했을 경우
                else {
                    if(secType == 0) {
                        // 복호화작업
                        decrypEmail = AESGCMcrypto.decrypt((String) kokonutUserEmailFieldDto.getEmailField(), awsKmsResultDto.getSecretKey(), awsKmsResultDto.getIvKey());
                        dchCount++;
                    } else {
                        decrypEmail = (String) kokonutUserEmailFieldDto.getEmailField();
                    }

                    if(Utils.isValidEmail(decrypEmail)) {
                        sendEmailList.add(decrypEmail);
                    }
                }
            }

            // 이메일발송 코드
            ActivityCode activityCode = ActivityCode.AC_59_3;

            // 활동이력 저장 -> 비정상 모드
            String ip = CommonUtil.publicIp();

            Long activityHistoryId = historyService.insertHistory(2, adminId, activityCode,
                    cpCode+" - ", "", ip, 0, email);

            log.info("sendEmailList : "+sendEmailList);

            // 이메일 전송작업시작
            String emTitle = ReqUtils.filter(emailSendDto.getEmTitle());
            log.info("emTitle : "+emTitle);

            // 이메일DB에 저장할 내용
            String emContents = ReqUtils.filter(emailSendDto.getEmContents()); // ReqUtils.filter 처리 <p> -- > &lt;p&gt;, html 태그를 DB에 저장하기 위해 이스케이프문자로 치환
            log.info("emContents : "+emContents);

            String contents = ReqUtils.unFilter(emailSendDto.getEmContents()); // &lt;br&gt;이메일내용 --> <br>이메일내용, html 화면에 뿌리기 위해 특수문자를 치환
            log.info("contents : "+contents);

//            HashMap<String, String> callTemplate = new HashMap<>();
//            callTemplate.put("template", "MailTemplate"); // 고객용 템플릿 MailTemplateOld, 코코넛용 템플릿 MailTemplate
//            callTemplate.put("title", emTitle);
//            callTemplate.put("content", contents);
//
//            contents = mailSender.getHTML5(callTemplate);

//            List<String> testSendEmail = new ArrayList<>(); // sendEmailList
//            testSendEmail.add("brian20@nate.com");
//            testSendEmail.add("gkstls2006@naver.com");

            String emailSendResult = null;
            if(!sendEmailList.isEmpty()) {

                emailSendResult = mailSender.newSendMail(emailSendDto.getEmEmailSend(), toCompanyName, sendEmailList, emTitle, contents, reservationTime, multipartFiles);
//                emailSendResult = mailSender.newSendMail(emailSendDto.getEmEmailSend(), toCompanyName, testSendEmail, emTitle, contentsTemplate, reservationTime, multipartFiles);

            } else {
                historyService.updateHistory(activityHistoryId,
                        cpCode+" - "+"발송할 이메일이 존재하지 않습니다.", "발송할 이메일이 존재하지 않습니다.", 0);
            }

            if(emailSendResult != null) {
                Email saveEmail = new Email();
                saveEmail.setCpCode(cpCode);
                saveEmail.setEmTitle(emTitle);
                saveEmail.setEmContents(emContents);
                saveEmail.setEmType(emailSendDto.getEmType());

                if(emailSendDto.getEmType().equals("2")) {
                    if(reservationTime != null) {
                        Instant instant = Instant.ofEpochMilli(reservationTime);
                        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                        log.info("예약발송 날짜 : "+localDateTime);

                        saveEmail.setEmReservationDate(localDateTime);
                    }
                    saveEmail.setEmState("2");
                }
                else {
                    saveEmail.setEmState("1");
                }

                saveEmail.setEmPurpose(emailSendDto.getEmPurpose());
                if(emailSendDto.getEmPurpose().equals("3")) {
                    saveEmail.setEmEtc(emailSendDto.getEmEtc());
                }
                saveEmail.setEmReceiverType(emailSendDto.getEmReceiverType());
                saveEmail.setEmEmailSend(emailSendDto.getEmEmailSend());

                saveEmail.setEmRequestId(emailSendResult);
                saveEmail.setEmSendAllCount(emSendAllCount);

                saveEmail.setEmYyyymm(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM")));
                saveEmail.setInsert_email(email);
                saveEmail.setInsert_date(LocalDateTime.now());

                emailRepository.save(saveEmail);

                historyService.updateHistory(activityHistoryId,
                        null, "", 1);
            }

            // 복호화 횟수 저장
            if(dchCount > 0) {
                decrypCountHistoryService.decrypCountHistorySave(cpCode, dchCount);
            }

        }

        return ResponseEntity.ok(res.success(data));
    }

    // 이메일 발송 예약 취소
    public ResponseEntity<Map<String, Object>> emailReservedCancel(Long emId, JwtFilterDto jwtFilterDto) {
        log.info("emailReservedCancel 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
        Long adminId = adminCompanyInfoDto.getAdminId();
        String cpCode = adminCompanyInfoDto.getCompanyCode();

        // 이메일 예약발송 취소 코드
        ActivityCode activityCode = ActivityCode.AC_59_5;

        // 활동이력 저장 -> 비정상 모드
        String ip = CommonUtil.publicIp();

        Long activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                cpCode+" - ", "", ip,  0, email);

        Optional<Email> optionalEmail = emailRepository.findById(emId);
        if(optionalEmail.isPresent()) {

            if(optionalEmail.get().getEmState().equals("2")) {
                optionalEmail.get().setEmState("6");
                optionalEmail.get().setModify_email(email);
                optionalEmail.get().setModify_date(LocalDateTime.now());
                emailRepository.save(optionalEmail.get());

                historyService.updateHistory(activityHistoryId,
                        null, "", 1);
            } else {
                historyService.updateHistory(activityHistoryId,
                        cpCode+" - "+"이미 발송된 이메일", "이미 발송된 이메일입니다.", 0);
                log.error("이미 발송된 이메일이므로 취소할 수 없습니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO108.getCode(),ResponseErrorCode.KO108.getDesc()));
            }

        } else {
            historyService.updateHistory(activityHistoryId,
                    cpCode+" - "+"예약발송 취소할 정보가 존재하지 않음", "예약발송 취소할 정보가 존재하지 않습니다.", 0);
            log.error("예약발송 취소할 정보가 존재하지 않습니다. 새로고침 이후 다시 시도해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO107.getCode(),ResponseErrorCode.KO107.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 익월 사용금액 가져오기 -> 한 건당 0.5원
    public int emailSendMonthPrice(String cpCode, String yyyymm) {
        int result = emailRepository.findByMonthSendPrice(cpCode, yyyymm);
        log.info("총 발송건수 : "+result);
        if(result != 0) {
            return (int) Math.round(result*0.5);
        } else {
            return 0;
        }
    }



}
