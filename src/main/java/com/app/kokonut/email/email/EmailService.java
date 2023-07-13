package com.app.kokonut.email.email;

import com.app.kokonut.admin.Admin;
import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.admin.dtos.AdminEmailInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.awskmshistory.dto.AwsKmsResultDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.component.ReqUtils;
import com.app.kokonut.common.realcomponent.AESGCMcrypto;
import com.app.kokonut.common.realcomponent.CommonUtil;
import com.app.kokonut.common.realcomponent.Utils;
import com.app.kokonut.company.companydatakey.CompanyDataKeyService;
import com.app.kokonut.company.companysetting.CompanySettingRepository;
import com.app.kokonut.company.companysetting.dtos.CompanySettingEmailDto;
import com.app.kokonut.company.companytable.CompanyTableRepository;
import com.app.kokonut.configs.MailSender;
import com.app.kokonut.email.email.dtos.EmailDetailDto;
import com.app.kokonut.email.email.dtos.EmailSendDto;
import com.app.kokonut.email.emailsendgroup.EmailGroupRepository;
import com.app.kokonut.email.emailsendgroup.dtos.EmailGroupAdminInfoDto;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dtos.ActivityCode;
import com.app.kokonut.history.extra.decrypcounthistory.DecrypCountHistoryService;
import com.app.kokonut.keydata.KeyDataService;
import com.app.kokonutuser.KokonutUserService;
import com.app.kokonutuser.dtos.use.KokonutUserEmailFieldDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Woody
 * Date : 2023-04-07
 * Remark :
 */
@Slf4j
@Service
public class EmailService {

    @Value("${kokonut.otp.hostUrl}")
    private String hostUrl; // otp_url

    private final HistoryService historyService;
    private final KokonutUserService kokonutUserService;
    private final CompanyDataKeyService companyDataKeyService;

    private final EmailGroupRepository emailGroupRepository;
    private final AdminRepository adminRepository;
    private final EmailRepository emailRepository;
    private final MailSender mailSender;

    private final CompanyTableRepository companyTableRepository;
    private final CompanySettingRepository companySettingRepository;
    private final DecrypCountHistoryService decrypCountHistoryService;

    @Autowired
    public EmailService(KeyDataService keyDataService, HistoryService historyService, KokonutUserService kokonutUserService,
                        CompanyDataKeyService companyDataKeyService, EmailRepository emailRepository, AdminRepository adminRepository,
                        EmailGroupRepository emailGroupRepository, MailSender mailSender, CompanyTableRepository companyTableRepository,
                        CompanySettingRepository companySettingRepository, DecrypCountHistoryService decrypCountHistoryService) {
        this.historyService = historyService;
        this.kokonutUserService = kokonutUserService;
        this.companyDataKeyService = companyDataKeyService;
        this.emailRepository = emailRepository;
        this.adminRepository = adminRepository;
        this.emailGroupRepository = emailGroupRepository;
        this.mailSender = mailSender;
        this.companyTableRepository = companyTableRepository;
        this.companySettingRepository = companySettingRepository;
        this.decrypCountHistoryService = decrypCountHistoryService;
    }

    // 이메일 목록 조회
    public ResponseEntity<Map<String,Object>> emailList(String email, String searchText, String stime, String emailType, Pageable pageable){
        log.info("emailList 호출");

        log.info("email : "+email);
        log.info("searchText : "+searchText);
        log.info("stime : "+stime);
        log.info("emailType : "+emailType);

        AjaxResponse res = new AjaxResponse();
//        Page<EmailListDto> emailListDtos = emailRepository.findByEmailPage(pageable);

        return null;
    }

    /**
     * 이메일 보내기
     * @param email 페이징 처리를 위한 정보
     * @param emailDetailDto 이메일 내용
     */
    @Transactional
    public ResponseEntity<Map<String,Object>> sendEmail2(String email, EmailDetailDto emailDetailDto){
        log.info("### sendEmail 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 접속한 사용자 인덱스
        Admin admin = adminRepository.findByKnEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다. : "+email));
        emailDetailDto.setEmSenderAdminId(admin.getAdminId());

        // 이메일 전송을 위한 전처리 - filter, unfilter
        String title = ReqUtils.filter(emailDetailDto.getEmTitle());
        String originContents = ReqUtils.filter(emailDetailDto.getEmContents()); // ReqUtils.filter 처리 <p> -- > &lt;p&gt;, html 태그를 DB에 저장하기 위해 이스케이프문자로 치환
        String contents = ReqUtils.unFilter(emailDetailDto.getEmContents()); // &lt;br&gt;이메일내용 --> <br>이메일내용, html 화면에 뿌리기 위해 특수문자를 치환
        log.info("### unFilter After content : " + contents);

        // 이메일 전송을 위한 전처리 - 첨부 이미지 경로 처리
        String imgSrcToken = "src=\"";
        int index = contents.indexOf(imgSrcToken);
        if(index > -1){
            StringBuilder sb = new StringBuilder();
            sb.append(contents);
            sb.insert(index + imgSrcToken.length(), hostUrl);
            contents = sb.toString();
        }

        // 이메일 전송을 위한 준비 - reciverType에 따른 adminIdList 구하기
        String receiverType = emailDetailDto.getEmReceiverType();
        String adminIdList = "";

        if("I".equals(receiverType)){
            adminIdList = emailDetailDto.getEmReceiverType().toString();
        }else if(("G").equals(receiverType)){
            Long emailGroupIdx = emailDetailDto.getEgId();
            EmailGroupAdminInfoDto emailGroupAdminInfoDto;
//            emailGroupAdminInfoDto = emailGroupRepository.findEmailGroupAdminInfoByIdx(emailGroupIdx);
//            adminIdList = emailGroupAdminInfoDto.getEgAdminIdList();
        }else{
            log.error("### 받는사람 타입(I:개별,G:그룹)을 알 수 없습니다. :" + receiverType);
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO040.getCode(), ResponseErrorCode.KO040.getDesc()));
        }

        // mailSender 실질적인 이메일 전송 부분
        String[] toks = adminIdList.split(",");
        for(String tok : toks){
            AdminEmailInfoDto adminEmailInfoDto = adminRepository.findByKnEmailInfo(Long.valueOf(tok));
            if(adminEmailInfoDto != null){
                String reciverEmail = adminEmailInfoDto.getKnEmail();
                String reciverName = adminEmailInfoDto.getKnName();

                log.info("### mailSender을 통해 건별 이메일 전송 시작");
                log.info("### reciver idx : "+tok + ", senderEmail : " +email+", reciverEmail : "+ reciverEmail);
                boolean mailSenderResult = mailSender.sendMail(reciverEmail, reciverName, title, contents);
                if(mailSenderResult){
                    // mailSender 성공
                    log.error("### 메일전송 성공했습니다.. reciver admin idx : "+ tok);
                }else{
                    // mailSender 실패
                    log.error("### 해당 메일 전송에 실패했습니다. 관리자에게 문의하세요. reciver admin idx : "+ tok+", reciverEmail : "+ reciverEmail);
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.KO041.getCode(), ResponseErrorCode.KO041.getDesc()));
                }
            }else{
                // TODO 일부가 탈퇴하고 일부는 이메일 정보가 있을때 처리에 대한 고민
                log.error("### 해당 idx에 해당하는 회원 이메일을 찾을 수 없습니다. reciver admin idx : "+ tok);
            }
        }

        // 전송 이력 저장 처리 - originContents로 DB 저장
        log.info("### 이메일 이력 저장 처리");
        Email reciveEmail = new Email();

        emailDetailDto.setEmContents(originContents);
        reciveEmail.setEmReceiverType(emailDetailDto.getEmReceiverType());
        reciveEmail.setEmTitle(emailDetailDto.getEmTitle());
        reciveEmail.setEmContents(emailDetailDto.getEmContents());

        // 조건에 따른 분기 처리
        if("G".equals(emailDetailDto.getEmReceiverType()) && emailDetailDto.getEgId() != null) {
            reciveEmail.setEgId(emailDetailDto.getEgId());
        }
        reciveEmail.setInsert_date(LocalDateTime.now());

        // save or update
        Email sendEmail = emailRepository.save(reciveEmail);

        log.info("### 이메일 이력 저장 처리 완료");

        // TODO 정상적으로 저장된 경우를 확인하는 방법 알아보기. save 처리가 되던 update 처리가 되던 결과적으로 해당 인덱스는 존재함.
        // sendEamil 객체에서 reciverType에 따라 어드민 인덱스를 조회, 해당 인덱스로 어드민 이메일을 확인한 다음 해당 이메일로 받는 내역을 조회한 다음. 해당 건수가 존재하면 받은걸로 친다고하기엔.
        // 하지만 이런 방법으로 할 경우 이전
        if(emailRepository.existsByEmId(sendEmail.getEmId())){
            log.info("### 이메일 이력 저장에 성공했습니다. : "+sendEmail.getEmId());
            return ResponseEntity.ok(res.success(data));
        }else{
            log.error("### 이메일 이력 저장에 실패했습니다. : "+sendEmail.getEmId());
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO041.getCode(), ResponseErrorCode.KO041.getDesc()));
        }

    }

    /**
     * 이메일 상세보기
     * @param idx email의 idx
     */
    public ResponseEntity<Map<String,Object>> sendEmailDetail(Long idx){
        log.info("### sendEmailDetail 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        if(idx == null){
            log.error("### 이메일 호출할 idx가 존재 하지 않습니다. : "+idx);
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO031.getCode(), ResponseErrorCode.KO031.getDesc()));
        } else {
            log.info("### 이메일 상세보기 idx : "+idx);
            // 이메일 인덱스로 이메일 정보 조회
            EmailDetailDto emailDetailDto = emailRepository.findEmailByIdx(idx);
            if(emailDetailDto != null){
                String receiverType = emailDetailDto.getEmReceiverType();
                String adminIdList = "";
                if("I".equals(receiverType)){
                    // 개별 선택으로 발송한 경우
                    adminIdList = emailDetailDto.getEmReceiverAdminIdList().toString();
                }else if("G".equals(receiverType)){
                    // 그룹 선택으로 메일을 발송한 경우
                    Long emailGroupIdx = emailDetailDto.getEgId();
                    // 메일 그룹 조회 쿼리 동작
//                    EmailGroupAdminInfoDto emailGroupAdminInfoDto = emailGroupRepository.findEmailGroupAdminInfoByIdx(emailGroupIdx);
//                    adminIdList = emailGroupAdminInfoDto.getEgAdminIdList();
                }else{
                    log.error("### 받는사람 타입(I:개별,G:그룹)을 알 수 없습니다. :" + receiverType);
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.KO040.getCode(), ResponseErrorCode.KO040.getDesc()));
                }

                // 받는 사람 이메일 문자열 조회
                StringBuilder emailList = new StringBuilder();
                String[] toks = adminIdList.split(",");
                for(int i = 0; i < toks.length; i++) {
                    String tok = toks[i];
                    AdminEmailInfoDto adminEmailInfoDto = adminRepository.findByKnEmailInfo(Long.valueOf(tok));
                    if(adminEmailInfoDto != null){
                        emailList.append(adminEmailInfoDto.getKnEmail());
                        if(i < toks.length - 1) {
                            emailList.append(", ");
                        }
                    }else{ // kokonut_1@kokonut.me, kokonut_2@kokonut.me, 탈퇴한 사용자, kokonut_4@kokonut.me 형태로 이메일 문자열 반환, 추후 변경될 수 있음.
                        emailList.append("탈퇴한 사용자");
                        if(i < toks.length - 1) {
                            emailList.append(", ");
                        }
                    }
                }
                data.put("emailList", emailList); // 받는 사람 이메일 문자열
                data.put("title", emailDetailDto.getEmTitle()); // 제목
                data.put("contents", emailDetailDto.getEmContents()); // 내용
            } else {
                log.error("### 이메일 정보가 존재 하지 않습니다. : "+idx);
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO031.getCode(), ResponseErrorCode.KO031.getDesc()));
            }
            return ResponseEntity.ok(res.success(data));
        }
    }

    // 이메일발송 호출
    public ResponseEntity<Map<String, Object>> sendEmail(EmailSendDto emailSendDto, JwtFilterDto jwtFilterDto) throws Exception {
        log.info("sendEmail 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("emailSendDto : "+emailSendDto);

        List<String> fileNames = new ArrayList<>();
        if(emailSendDto.getMultipartFiles() != null) {
            for (MultipartFile file : emailSendDto.getMultipartFiles()) {
                if (file.isEmpty()) {
                    continue;
                }
                try {
                    byte[] bytes = file.getBytes();
                    Path path = Paths.get(Objects.requireNonNull(file.getOriginalFilename()));
                    Files.write(path, bytes);
                    fileNames.add(file.getOriginalFilename());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("업로드할 파일들 : "+fileNames);

        String email = jwtFilterDto.getEmail();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
        Long adminId = adminCompanyInfoDto.getAdminId();
        String cpCode = adminCompanyInfoDto.getCompanyCode();
        String ctName = cpCode+"_1";

        // 테스트용 이메일리스트 변수
        List<String> testEmail = new ArrayList<>();
        testEmail.add("nG$8c3KNCi!4qb8xQq@k");
        testEmail.add("V79sGR#HaNTICOyuw%MH");
        testEmail.add("W5KGwCG!GgSP5XLk47yD");
        emailSendDto.setEmailSendChoseList(testEmail);

        // 이메일지정 고유코드
        CompanySettingEmailDto companySettingEmailDto = companySettingRepository.findByCompanySettingEmail(cpCode);

        if(companySettingEmailDto.getCsEmailCodeSetting().equals("")) {
            log.error("이메일 항목으로 지정한 값이 없습니다. 환경설정에서 이메일발송할 항목을 선택 후 다시 시도해주시길 바랍니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO102.getCode(),ResponseErrorCode.KO102.getDesc()));
        }
        else {

            AwsKmsResultDto awsKmsResultDto = companyDataKeyService.findByCompanyDataKey(cpCode);
//            log.info("DataKey : "+awsKmsResultDto.getDataKey());
//            log.info("IV : "+awsKmsResultDto.getIvKey());

            List<String> sendEmailList = new ArrayList<>();

            int dchCount = 0; // 복호화 카운팅

            String fieldName = cpCode+"_"+companySettingEmailDto.getCsEmailCodeSetting();
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

            List<KokonutUserEmailFieldDto> kokonutUserEmailFieldDtos = kokonutUserService.emailFieldList(cpCode, companySettingEmailDto.getCsEmailCodeSetting(), emailSendDto.getEmReceiverType(), emailSendDto.getEmailSendChoseList());
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

            log.info("sendEmailList : "+sendEmailList);

            // 이메일발송 코드
            ActivityCode activityCode = ActivityCode.AC_59_3;

            // 활동이력 저장 -> 비정상 모드
            String ip = CommonUtil.clientIp();

//            Long activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
//        cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip,  CommonUtil.publicIp(), 0, email);







//            historyService.updateHistory(activityHistoryId,
//                    cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", 1);


            // 복호화 횟수 저장
            if(dchCount > 0) {
                decrypCountHistoryService.decrypCountHistorySave(cpCode, dchCount);
            }

        }

        return ResponseEntity.ok(res.success(data));
    }


    /**
     * 이메일 발송 대상 목록 조회하기
     * @param pageable 페이징 처리를 위한 정보
     */
    //    public ResponseEntity<Map<String, Object>> emailTargetGroupList(Pageable pageable) {
//        log.info("### emailTargetGroupList 호출");
//        /*
//         * 그룹명 : 미식플랫폼 00 관리자
//         * 설 명 : 미식플랫폼 00의 관리자 그룹
//         * 관리자 Idx : 2, 4, 5 ...
//         * 이메일 : a001@00.oo.com, a002@00.oo.com, a003@00.oo.com
//         *
//         * findEmailGroupDatils()를 조회한다
//         * 해당 결과에서 가져온 관리자 인덱스를 가지고 관리자 이메일을 조회한다.
//         * EmailGroup Entity 클래스를 가지는 List에 각 값을 넣어서 던져준다.
//         */
//
//        AjaxResponse res = new AjaxResponse();
//
//        EmailGroup emailGroup = new EmailGroup(); // TEMP
//        List<EmailGroupListDto> resultDto = emailGroupRepository.findEmailGroupDetails();
//
//        List<EmailGroup> resultList = new ArrayList<>();
//        for(int i = 0; i<resultDto.size(); i++){
//            emailGroup.setEgId(resultDto.get(i).getEgId());
//            emailGroup.setEgName(resultDto.get(i).getEgName());
//            emailGroup.setEgDesc(resultDto.get(i).getEgDesc());
//            emailGroup.setEgAdminIdList(resultDto.get(i).getEgAdminIdList());
//
//            String adminIds = resultDto.get(i).getEgAdminIdList();
//            String adminIdList[] = adminIds.split(",");
//
//            List<String> emailList = new ArrayList<>();
//            for (String adminId : adminIdList) {
//                String adminEmail = adminRepository.findByKnEmailInfo(Long.parseLong(adminId)).getKnEmail();
//                emailList.add(adminEmail); // a001@00.oo.com, a002@00.oo.com, a003@00.oo.com
//            }
//            StringBuilder adminEmailList = new StringBuilder();
//            for(int j = 0; j < emailList.size(); j++){
//                adminEmailList.append(emailList.get(j));
//                if(j == emailList.size()-1){
//                    // 마지막
//                    adminEmailList.append("");
//                }else {
//                    adminEmailList.append(",");
//                }
//            }
//            String stAdminEmailList = adminEmailList.toString();
//            emailGroup.setAdminEmailList(stAdminEmailList);
//            resultList.add(emailGroup);
//            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
//            log.info(">>>> idx: "+resultList.get(i).getEgId());
//            log.info(">>>> name: "+resultList.get(i).getEgName());
//            log.info(">>>> Desc: "+resultList.get(i).getEgDesc());
//            log.info(">>>> adminId: "+resultList.get(i).getEgAdminIdList());
//            log.info(">>>> adminEmail: "+resultList.get(i).getAdminEmailList());
//            log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< ");
//        }
//
//
//        Page resultPage = new PageImpl<>(resultList, pageable, resultList.size());
//        log.info("결과 List size : "+resultList.size());
//
//    return ResponseEntity.ok(res.ResponseEntityPage(resultPage));
//    }







}
