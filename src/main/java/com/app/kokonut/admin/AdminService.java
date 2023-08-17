package com.app.kokonut.admin;

import com.app.kokonut.admin.dtos.*;
import com.app.kokonut.admin.enums.AuthorityRole;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.auth.jwt.dto.RedisDao;
import com.app.kokonut.awskmshistory.dto.AwsKmsResultDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.component.ReqUtils;
import com.app.kokonut.common.realcomponent.AESGCMcrypto;
import com.app.kokonut.common.realcomponent.CommonUtil;
import com.app.kokonut.common.realcomponent.Utils;
import com.app.kokonut.company.company.Company;
import com.app.kokonut.company.company.CompanyRepository;
import com.app.kokonut.company.companydatakey.CompanyDataKeyService;
import com.app.kokonut.configs.MailSender;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dtos.ActivityCode;
import com.app.kokonut.history.extra.encrypcounthistory.EncrypCountHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author Woody
 * Date : 2022-12-01
 * Time :
 * Remark : AdminService + 인증서비스
 */
@Slf4j
@Service
public class AdminService {

    @Value("${kokonut.front.server.domain}")
    public String frontServerDomainIp;

    private final AdminRepository adminRepository;
    private final CompanyRepository companyRepository;
    private final CompanyDataKeyService companyDataKeyService;
    private final HistoryService historyService;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;
    private final EncrypCountHistoryService encrypCountHistoryService;

    private final RedisDao redisDao;

    @Autowired
    public AdminService(AdminRepository adminRepository, CompanyRepository companyRepository, CompanyDataKeyService companyDataKeyService,
                        HistoryService historyService, PasswordEncoder passwordEncoder, MailSender mailSender, EncrypCountHistoryService encrypCountHistoryService, RedisDao redisDao) {
        this.adminRepository = adminRepository;
        this.companyRepository = companyRepository;
        this.companyDataKeyService = companyDataKeyService;
        this.historyService = historyService;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.encrypCountHistoryService = encrypCountHistoryService;
        this.redisDao = redisDao;
    }

    // 마이페이지(내정보) 데이터 호출
    public ResponseEntity<Map<String, Object>> myInfo(JwtFilterDto jwtFilterDto) {
        log.info("myInfo 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();

        log.info("마이페이지 조회 이메일 : "+email);
        AdminMyInfoDto adminMyInfoDto = adminRepository.findByAdminMyInfo(email);
        log.info("adminMyInfoDto : "+adminMyInfoDto);
        if(adminMyInfoDto != null) {
            data.put("knEmail",adminMyInfoDto.getKnEmail());
            data.put("knName",adminMyInfoDto.getKnName());
            data.put("knPhoneNumber",adminMyInfoDto.getKnPhoneNumber());
            data.put("cpName",adminMyInfoDto.getCpName());
            data.put("knDepartment",adminMyInfoDto.getKnDepartment());
        } else{
            log.error("사용하실 수 없는 토큰정보 입니다. 다시 로그인 해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO009.getCode(),ResponseErrorCode.KO009.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 휴대전화번호 변경
    @Transactional
    public ResponseEntity<Map<String, Object>> phoneChange(String knName, String knPhoneNumber, JwtFilterDto jwtFilterDto) {
        log.info("phoneChange 호출!");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
        Long adminId = adminCompanyInfoDto.getAdminId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();

        // 휴대전화변경 코드
        ActivityCode activityCode = ActivityCode.AC_35;
        // 활동이력 저장 -> 비정상 모드
        String ip = CommonUtil.publicIp();

        log.info("휴대전화번호 변경 이메일 : "+email);

        Optional<Admin> optionalAdmin = adminRepository.findByKnEmail(email);
        if(optionalAdmin.isPresent()) {
            Long activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, 0, jwtFilterDto.getEmail());

            optionalAdmin.get().setKnName(knName);
            optionalAdmin.get().setKnPhoneNumber(knPhoneNumber);
            optionalAdmin.get().setModify_email(email);
            optionalAdmin.get().setModify_date(LocalDateTime.now());
            adminRepository.save(optionalAdmin.get());

            historyService.updateHistory(activityHistoryId,
                    companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", 1);
        } else{
            log.error("해당 유저가 존재하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(),"해당 유저가 "+ResponseErrorCode.KO004.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 소속명 변경
    @Transactional
    public ResponseEntity<Map<String, Object>> cpChange(String cpContent, String knPassword, Integer state, JwtFilterDto jwtFilterDto) {
        log.info("cpChange 호출");

        log.info("변경내용 : "+cpContent);
        log.info("state : "+state);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
        Long adminId = adminCompanyInfoDto.getAdminId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();

        // 활동 코드
        ActivityCode activityCode;
        if(state == 1) {
            // 소속명 변경
            activityCode = ActivityCode.AC_36;
        } else {
            // 부서 변경/등록
            activityCode = ActivityCode.AC_37;
        }

        String ip = CommonUtil.publicIp();

        Optional<Admin> optionalAdmin = adminRepository.findByKnEmail(email);
        if(optionalAdmin.isPresent()) {

            // 비밀번호 검증
            if (!passwordEncoder.matches(knPassword, optionalAdmin.get().getKnPassword())){
                log.error("입력하신 비밀번호가 일치하지 않습니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO013.getCode(), ResponseErrorCode.KO013.getDesc()));
            }

            // 활동이력 저장 -> 비정상 모드
            Long activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, 0, jwtFilterDto.getEmail());

            if(state == 1) {
                // 소속명 변경
                Optional<Company> optionalCompany = companyRepository.findById(optionalAdmin.get().getCompanyId());
                if(optionalCompany.isPresent()) {
                    optionalCompany.get().setCpName(cpContent);
                    optionalCompany.get().setModify_email(email);
                    optionalCompany.get().setModify_date(LocalDateTime.now());
                    companyRepository.save(optionalCompany.get());
                } else {
                    log.error("회사 정보가 존재하지 않습니다.");
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(), "회사 정보가 " + ResponseErrorCode.KO004.getDesc()));
                }
            } else {
                optionalAdmin.get().setKnDepartment(cpContent);
                optionalAdmin.get().setModify_email(email);
                optionalAdmin.get().setModify_date(LocalDateTime.now());
                adminRepository.save(optionalAdmin.get());
            }

            historyService.updateHistory(activityHistoryId,
                    companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", 1);
        } else{
            log.error("해당 유저가 존재하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(),"해당 유저가 "+ResponseErrorCode.KO004.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 비밀번호 변경
    @Transactional
    public ResponseEntity<Map<String, Object>> pwdChange(String oldknPassword, String newknPassword, String newknPasswordCheck, JwtFilterDto jwtFilterDto) {
        log.info("pwdChange 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
        Long adminId = adminCompanyInfoDto.getAdminId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();

        // 활동 코드
        ActivityCode activityCode = ActivityCode.AC_38;
        String ip = CommonUtil.publicIp();

        Optional<Admin> optionalAdmin = adminRepository.findByKnEmail(email);
        if(optionalAdmin.isPresent()) {

            // 비밀번호 검증
            if (!passwordEncoder.matches(oldknPassword, optionalAdmin.get().getKnPassword())){
                log.error("입력하신 비밀번호가 일치하지 않습니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO013.getCode(), ResponseErrorCode.KO013.getDesc()));
            }

            // 비밀번호 확인비교
            if (!newknPassword.equals(newknPasswordCheck)){
                log.error("새로운 비밀번호가 일치하지 않습니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO083.getCode(), ResponseErrorCode.KO083.getDesc()));
            }

            // 활동이력 저장 -> 비정상 모드
            Long activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip,0, jwtFilterDto.getEmail());

            optionalAdmin.get().setKnPassword(passwordEncoder.encode(newknPassword));
            optionalAdmin.get().setKnPwdChangeDate(LocalDateTime.now());
            optionalAdmin.get().setKnPwdErrorCount(0);
            optionalAdmin.get().setModify_email(email);
            optionalAdmin.get().setModify_date(LocalDateTime.now());
            adminRepository.save(optionalAdmin.get());

            historyService.updateHistory(activityHistoryId,
                    companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", 1);
        } else{
            log.error("해당 유저가 존재하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(),"해당 유저가 "+ResponseErrorCode.KO004.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }



    // 유저의 권한 및 jwt 토큰확인
    public ResponseEntity<Map<String,Object>> authorityCheck(JwtFilterDto jwtFilterDto) {
        log.info("authorityCheck 호출");

//        log.info("jwtFilterDto : "+jwtFilterDto);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();
        if(email.equals("anonymousUser")){
            log.error("사용하실 수 없는 토큰정보 입니다. 다시 로그인 해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO009.getCode(),ResponseErrorCode.KO009.getDesc()));
        } else{
//            log.info("해당 유저의 이메일 : "+email);

            AdminInfoDto adminInfoDto = adminRepository.findByAdminInfo(email);

//            log.info("해당 유저의 권한 : "+jwtFilterDto.getRole().getDesc());
            data.put("knName",adminInfoDto.getKnName());
            data.put("cpName",adminInfoDto.getCpName());
            data.put("knEmail",email);
            data.put("role",jwtFilterDto.getRole().getCode());

            data.put("knPhoneNumber",adminInfoDto.getKnPhoneNumber());

            LocalDate nowDate = LocalDate.now();
            // log.info("nowDate : "+nowDate);
            LocalDate electronicDate = adminInfoDto.getCpElectronicDate();
            // log.info("electronicDate : "+electronicDate);

            if(adminInfoDto.getCpElectronic() == 2) {
                if(electronicDate.isBefore(nowDate)) {
                    // log.info("2이고 1년이 지났음");
                    data.put("electronic", 0);
                } else {
                    // log.info("2이고 1년이 지나지 않았음");
                    data.put("electronic", adminInfoDto.getCpElectronic());
                }
            } else {
                data.put("electronic",adminInfoDto.getCpElectronic());
            }

            data.put("csAutoLogoutSetting",adminInfoDto.getCsAutoLogoutSetting());

            LocalDateTime compareDate = adminInfoDto.getCompareDate(); // 비밀번호 변경날짜 : 만약 null일 경우 insert_date 값을 가져온다.
            long monthsBetween = ChronoUnit.MONTHS.between(compareDate, LocalDateTime.now());
//            log.info("monthsBetween : "+monthsBetween);  // 변경날짜와 현재날짜의 월수 차이
            if(adminInfoDto.getCsPasswordChangeSetting() <= monthsBetween) {
                data.put("csPasswordChangeState","2");
            } else {
                data.put("csPasswordChangeState","1");
            }

            data.put("emailSendSettingState",adminInfoDto.getEmailSendSettingState()); // "1"이면 이메일발송 셋팅함, "0" 이면 이메일발송 셋팅하지않음

            data.put("paymentBillingCheck",adminInfoDto.getBillingCheck()); // "1"이면 등록됨, "0" 이면 등록되지않음(팝업창 안내), "2"면 구독해지
            if(adminInfoDto.getBillingCheck().equals("2")) {
                // 구독해지인 상태일 경우 구독해지를 취소하고 계속이어나갈지의 대한 여부
                LocalDateTime cpSubscribeDate = adminInfoDto.getCpSubscribeDate();
                if(LocalDateTime.now().getMonth() == cpSubscribeDate.getMonth()) {
                    data.put("paymentDeleteCancel","1"); // 구독해지했지만 이어서 할 경우 구독상태를 "1"로 수정하고 구독취소날짜를 NULL 값으로 수정하는 API 호출
                } else {
                    data.put("paymentDeleteCancel","0"); // 구독해지했지만 이어서 할수없고 다시 카드를 재등록해야되는 상태
                }
            }

        }

        return ResponseEntity.ok(res.success(data));
    }

    // 비밀번호 틀릴시 에러횟수 카운팅하는 함수
    public void adminErrorPwd(Admin admin) {
        log.info("비밀번호 틀릴시 에러횟수 카운팅하는 함수 호출");

        admin.setKnPwdErrorCount(admin.getKnPwdErrorCount()+1);
        admin.setModify_email(admin.getKnEmail());
        admin.setModify_date(LocalDateTime.now());
        adminRepository.save(admin);
    }

    // 관리자 목록 리스트 호출
    public ResponseEntity<Map<String, Object>> list(String searchText, String filterRole, String filterState, JwtFilterDto jwtFilterDto, Pageable pageable) {
        log.info("list 호출");

        log.info("searchText : "+searchText);
        log.info("filterRole : "+filterRole);
        log.info("filterState : "+filterState);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();
        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
        Long companyId = adminCompanyInfoDto.getCompanyId();

        Integer knState;
        if(filterState.equals("")) {
            knState = null;
        } else {
            knState = Integer.parseInt(filterState);
        }

        List<AdminListDto> adminListDtoList = new ArrayList<>();
        AdminListDto adminListDto;

        Page<AdminListSubDto> adminListDtos = adminRepository.findByAdminList(searchText, filterRole, knState, companyId, email, pageable);
        if(adminListDtos.getTotalPages() == 0) {
            log.info("조회된 데이터가 없습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO003.getCode(), ResponseErrorCode.KO003.getDesc()));
        } else {

            for(int i=0; i<adminListDtos.getNumberOfElements(); i++) {
                adminListDto = new AdminListDto();

                adminListDto.setKnName(adminListDtos.getContent().get(i).getKnName());
                adminListDto.setKnEmail(adminListDtos.getContent().get(i).getKnEmail());
                adminListDto.setKnState(adminListDtos.getContent().get(i).getKnState());
                adminListDto.setKnRoleDesc(adminListDtos.getContent().get(i).getKnRoleDesc());
                adminListDto.setKnRoleCode(adminListDtos.getContent().get(i).getKnRoleCode());

                adminListDto.setKnLastLoginDate(adminListDtos.getContent().get(i).getKnLastLoginDate());
                adminListDto.setKnIpAddr(adminListDtos.getContent().get(i).getKnIpAddr());

                adminListDto.setKnIsEmailAuth(adminListDtos.getContent().get(i).getKnIsEmailAuth());
                adminListDto.setInsertName(adminListDtos.getContent().get(i).getInsertName());
                adminListDto.setInsert_date(adminListDtos.getContent().get(i).getInsert_date());
                adminListDtoList.add(adminListDto);
            }

            data.put("datalist",adminListDtoList);
            data.put("total_rows",adminListDtos.getTotalElements());
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 관리자 등록
    @Transactional
    public ResponseEntity<Map<String, Object>> create(String userEmail, String choseRole, JwtFilterDto jwtFilterDto) throws Exception {
        log.info("create 호출");

        log.info("userEmail : "+userEmail);
        log.info("choseRole : "+choseRole);
        log.info("jwtFilterDto : "+jwtFilterDto);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();

        String roleCode;
        if(choseRole.equals("최고관리자")) {
            roleCode = "ROLE_ADMIN";
        } else if(choseRole.equals("관리자")) {
            roleCode = "ROLE_USER";
        } else if(choseRole.equals("게스트")) {
            roleCode = "ROLE_GUEST";
        } else {
            roleCode = "";
        }
        log.info("roleCode : "+AuthorityRole.valueOf(roleCode));

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
        Long adminId = adminCompanyInfoDto.getAdminId();
        Long companyId = adminCompanyInfoDto.getCompanyId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();

        // 활동 코드
        ActivityCode activityCode = ActivityCode.AC_04;
        String ip = CommonUtil.publicIp();

        if(jwtFilterDto.getRole().getCode().equals("ROLE_SYSTEM") || jwtFilterDto.getRole().getCode().equals("ROLE_MASTER") || jwtFilterDto.getRole().getCode().equals("ROLE_ADMIN")) {
            log.info("관리자 등록 시작");

            // 관리자추가 저장 -> 비정상 모드
            Long activityHistoryId = historyService.insertHistory(2, adminId, activityCode,
                    companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, 0, jwtFilterDto.getEmail());

            AwsKmsResultDto awsKmsResultDto = companyDataKeyService.findByCompanyDataKey(companyCode);
            byte[] ivBytes = AESGCMcrypto.generateIV();

            // 이메일인증코드
            String knEmailAuthCode = AESGCMcrypto.encrypt(userEmail.getBytes(StandardCharsets.UTF_8), awsKmsResultDto.getSecretKey(), ivBytes);

            String ivKo = Base64.getEncoder().encodeToString(ivBytes);

            // 관리자 등록메일 보내기
            String title = "관리자등록 인증 알림";
            // TODO : 답변 내용을 HTML 태그를 붙여서 메일로 전송해준다. 화면단과 개발할 때 추가 개발해야함.
            String contents = "관리자등록 요청 되었습니다. <br>해당 링크를 통해 가입을 이어서 해주시길 바랍니다.<br>링크 : "+
            "<a href=\""+frontServerDomainIp+"/#/create?" +
                    "send=1&" +
                    "evKo="+ companyId +"&" +
                    "ivKo="+ivKo +"&" +
                    "kvKo="+knEmailAuthCode+"\" target=\"_blank\">"+
                    "이어서 가입하기"+
                    "</a>";
            log.info("toEmail" + userEmail + ", toName" + "코코넛");

            title = ReqUtils.filter("관리자 등록 재인증 알림");
            contents = ReqUtils.unFilter(contents);

            // 템플릿 호출을 위한 데이터 세팅
            HashMap<String, String> callTemplate = new HashMap<>();
            callTemplate.put("template", "MailTemplate");
            callTemplate.put("title", title);
            callTemplate.put("content", contents);

            // 템플릿 TODO 템플릿 디자인 추가되면 수정
            contents = mailSender.getHTML6(callTemplate);

            String reciverName = "kokonut";
            String mailSenderResult = mailSender.sendKokonutMail(userEmail, reciverName, title, contents);
            if(mailSenderResult != null) {
                log.info("### 메일전송 성공했습니다. reciver Email : "+ userEmail);

                // 인증번호 레디스에 담기
                // -> 레디스서버에 24시간동안 보관
                redisDao.setValues("EV: " + userEmail, knEmailAuthCode, Duration.ofMillis((long)1000*60*60*24)); // 제한시간 24시간
//                redisDao.setValues("EV: " + userEmail, knEmailAuthCode, Duration.ofMillis((long)1000*60)); // 제한시간 1분

                Admin admin = new Admin();
                admin.setKnEmail(userEmail);
                admin.setKnName("미가입");
                admin.setKnPassword(passwordEncoder.encode(Utils.getRamdomStr(10))); // 난수로 비밀번호설정 -> 아무도 알수없음.
                admin.setKnPwdErrorCount(0);
                admin.setKnRegType(0);
                admin.setCompanyId(companyId);
                admin.setMasterId(companyId);
                admin.setKnUserType(2);
                admin.setKnIsEmailAuth("N");
                admin.setKnEmailAuthCode(knEmailAuthCode);
                admin.setKnState(1);
                admin.setKnRoleCode(AuthorityRole.valueOf(roleCode));
                admin.setInsert_email(email);
                admin.setInsert_date(LocalDateTime.now());
                adminRepository.save(admin);

                // 암호화 횟수 저장
                encrypCountHistoryService.encrypCountHistorySave(companyCode, 1);

                historyService.updateHistory(activityHistoryId,
                        companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", 1);
            }else{
                log.error("### 해당 메일 전송에 실패했습니다. 관리자에게 문의하세요. reciverEmail : "+ userEmail);
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO041.getCode(), ResponseErrorCode.KO041.getDesc()));
            }

        } else{
            log.error("접근 권한이 없습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO001.getCode(),ResponseErrorCode.KO001.getDesc()));
        }


        return ResponseEntity.ok(res.success(data));
    }

    // 관리자등록 인증메일 재전송
    @Transactional
    public ResponseEntity<Map<String, Object>> createMailAgain(String userEmail, JwtFilterDto jwtFilterDto) throws Exception {
        log.info("createMailAgain 호출");

        log.info("userEmail : "+userEmail);
        log.info("jwtFilterDto : "+jwtFilterDto);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
        Long adminId = adminCompanyInfoDto.getAdminId();
        Long companyId = adminCompanyInfoDto.getCompanyId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();

        // 활동 코드
        ActivityCode activityCode = ActivityCode.AC_05;
        String ip = CommonUtil.publicIp();

        if(jwtFilterDto.getRole().getCode().equals("ROLE_SYSTEM") || jwtFilterDto.getRole().getCode().equals("ROLE_MASTER") || jwtFilterDto.getRole().getCode().equals("ROLE_ADMIN")) {
            log.info("관리자등록 재인증 시작");

            // 관리자추가 저장 -> 비정상 모드
            Long activityHistoryId = historyService.insertHistory(2, adminId, activityCode,
                    companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, 0, email);

            AwsKmsResultDto awsKmsResultDto = companyDataKeyService.findByCompanyDataKey(companyCode);
            byte[] ivBytes = AESGCMcrypto.generateIV();

            // 이메일인증코드
            String knEmailAuthCode = AESGCMcrypto.encrypt(userEmail.getBytes(StandardCharsets.UTF_8), awsKmsResultDto.getSecretKey(), ivBytes);

            String ivKo = Base64.getEncoder().encodeToString(ivBytes);

            // 관리자 등록메일 보내기
            String title = "관리자등록 재인증 알림";
            // TODO : 답변 내용을 HTML 태그를 붙여서 메일로 전송해준다. 화면단과 개발할 때 추가 개발해야함.
            String contents = "관리자등록 재요청 되었습니다. <br>해당 링크를 통해 가입을 이어서 해주시길 바랍니다.<br>링크 : "+
                    "<a href=\""+frontServerDomainIp+"/#/create?" +
                    "send=1&" +
                    "evKo="+ companyId +"&" +
                    "ivKo="+ivKo +"&" +
                    "kvKo="+knEmailAuthCode+"\" target=\"_blank\">"+
                    "이어서 가입하기"+
                    "</a>";
            log.info("toEmail" + userEmail + ", toName" + "코코넛");

            title = ReqUtils.filter("관리자 등록 재인증 알림");
            contents = ReqUtils.unFilter(contents);

            // 템플릿 호출을 위한 데이터 세팅
            HashMap<String, String> callTemplate = new HashMap<>();
            callTemplate.put("title", title);
            callTemplate.put("content", contents);

            // 템플릿 TODO 템플릿 디자인 추가되면 수정
            contents = mailSender.getHTML6(callTemplate);

            String reciverName = "kokonut";

            String mailSenderResult = mailSender.sendKokonutMail(userEmail, reciverName, title, contents);
            if(mailSenderResult != null) {
                log.info("### 메일전송 성공했습니다. reciver Email : "+ userEmail);

                // 인증번호 레디스에 담기
                // -> 레디스서버에 24시간동안 보관
                redisDao.setValues("EV: " + userEmail, knEmailAuthCode, Duration.ofMillis((long)1000*60*60*24)); // 제한시간 24시간
//                redisDao.setValues("EV: " + userEmail, knEmailAuthCode, Duration.ofMillis((long)1000*60)); // 제한시간 1분

                // 암호화 횟수 저장
                encrypCountHistoryService.encrypCountHistorySave(companyCode, 1);

                historyService.updateHistory(activityHistoryId,
                        companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", 1);
            }else{
                log.error("### 해당 메일 전송에 실패했습니다. 관리자에게 문의하세요. reciverEmail : "+ userEmail);
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO041.getCode(), ResponseErrorCode.KO041.getDesc()));
            }

        } else{
            log.error("접근 권한이 없습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO001.getCode(),ResponseErrorCode.KO001.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 내부제공, 외부제공 관리자목록 리스트 호출
    public ResponseEntity<Map<String, Object>> offerAdminList(String type, JwtFilterDto jwtFilterDto) {
        log.info("offerAdminList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();
        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
        Long companyId = adminCompanyInfoDto.getCompanyId();

        // type => "0" 내부제공, "1" 외부제공,
        List<AdminOfferListDto> adminOfferListDtos = adminRepository.findByAdminOfferList(companyId, type, email);
        data.put("offerList",adminOfferListDtos);

        return ResponseEntity.ok(res.success(data));
    }

    // 비밀번호변경 메일전송(24시간후 만료)
    public ResponseEntity<Map<String, Object>> passwordChangeMail(String userEmail, JwtFilterDto jwtFilterDto) throws Exception {
        log.info("passwordChangeMail 호출");

        log.info("userEmail : "+userEmail);
        log.info("jwtFilterDto : "+jwtFilterDto);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
        Long adminId = adminCompanyInfoDto.getAdminId();
        Long companyId = adminCompanyInfoDto.getCompanyId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();

        // 활동 코드
        ActivityCode activityCode = ActivityCode.AC_05;
        String ip = CommonUtil.publicIp();

        if(jwtFilterDto.getRole().getCode().equals("ROLE_SYSTEM") || jwtFilterDto.getRole().getCode().equals("ROLE_MASTER") || jwtFilterDto.getRole().getCode().equals("ROLE_ADMIN")) {
            log.info("비밀번호변경 시작");

            // 관리자추가 저장 -> 비정상 모드
            Long activityHistoryId = historyService.insertHistory(2, adminId, activityCode,
                    companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, 0, email);

            AwsKmsResultDto awsKmsResultDto = companyDataKeyService.findByCompanyDataKey(companyCode);
            byte[] ivBytes = AESGCMcrypto.generateIV();

            // 이메일인증코드
            String knEmailAuthCode = AESGCMcrypto.encrypt(userEmail.getBytes(StandardCharsets.UTF_8), awsKmsResultDto.getSecretKey(), ivBytes);

            String ivKo = Base64.getEncoder().encodeToString(ivBytes);

            // 관리자 등록메일 보내기
            // TODO : 답변 내용을 HTML 태그를 붙여서 메일로 전송해준다. 화면단과 개발할 때 추가 개발해야함.
            String title = "비밀번호 변경 알림";
            String contents = "비밀번호 변경 요청 되었습니다. <br>해당 링크를 통해 변경을 이어서 해주시길 바랍니다.<br>링크 : "+
                    "<a href=\""+frontServerDomainIp+"/#/create?" +
                    "send=2&" +
                    "evKo="+ companyId +"&" +
                    "ivKo="+ivKo +"&" +
                    "kvKo="+knEmailAuthCode+"\" target=\"_blank\">"+
                    "비밀번호 변경하기"+
                    "</a>";

            log.info("toEmail" + userEmail + ", toName" + "코코넛");

            contents = ReqUtils.unFilter(contents);

//            // 템플릿 호출을 위한 데이터 세팅
            HashMap<String, String> callTemplate = new HashMap<>();
//            callTemplate.put("template", "MailTemplate");
            callTemplate.put("title", title);
            callTemplate.put("content", contents);

            // 템플릿 TODO 템플릿 디자인 추가되면 수정
            contents = mailSender.getHTML6(callTemplate);

            String reciverName = "kokonut";

            String mailSenderResult = mailSender.sendKokonutMail(userEmail, reciverName, title, contents);
            if(mailSenderResult != null) {
                log.info("### 메일전송 성공했습니다. reciver Email : "+ userEmail);

                // 인증번호 레디스에 담기
                // -> 레디스서버에 24시간동안 보관
                redisDao.setValues("EV: " + userEmail, knEmailAuthCode, Duration.ofMillis((long)1000*60*60*24)); // 제한시간 24시간
//                redisDao.setValues("EV: " + userEmail, knEmailAuthCode, Duration.ofMillis((long)1000*60)); // 제한시간 1분

                // 암호화 횟수 저장
                encrypCountHistoryService.encrypCountHistorySave(companyCode, 1);

                historyService.updateHistory(activityHistoryId,
                        companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", 1);
            }else{
                log.error("### 해당 메일 전송에 실패했습니다. 관리자에게 문의하세요. reciverEmail : "+ userEmail);
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO041.getCode(), ResponseErrorCode.KO041.getDesc()));
            }

        } else{
            log.error("접근 권한이 없습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO001.getCode(),ResponseErrorCode.KO001.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }
}
