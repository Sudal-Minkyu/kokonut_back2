package com.app.kokonut.auth;

import com.app.kokonut.admin.Admin;
import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.AdminService;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.admin.dtos.AdminCompanySettingDto;
import com.app.kokonut.admin.enums.AuthorityRole;
import com.app.kokonut.alimtalk.AlimtalkSendService;
import com.app.kokonut.auth.dtos.*;
import com.app.kokonut.auth.jwt.been.JwtTokenProvider;
import com.app.kokonut.auth.jwt.dto.AuthRequestDto;
import com.app.kokonut.auth.jwt.dto.AuthResponseDto;
import com.app.kokonut.auth.jwt.dto.GoogleOtpGenerateDto;
import com.app.kokonut.auth.jwt.dto.RedisDao;
import com.app.kokonut.awskmshistory.AwsKmsHistoryService;
import com.app.kokonut.awskmshistory.dto.AwsKmsResultDto;
import com.app.kokonut.common.*;
import com.app.kokonut.company.company.Company;
import com.app.kokonut.company.company.CompanyRepository;
import com.app.kokonut.company.companydatakey.CompanyDataKey;
import com.app.kokonut.company.companydatakey.CompanyDataKeyRepository;
import com.app.kokonut.company.companysetting.CompanySetting;
import com.app.kokonut.company.companysetting.CompanySettingRepository;
import com.app.kokonut.company.companysetting.dtos.CompanySettingCheckDto;
import com.app.kokonut.company.companysettingaccessip.CompanySettingAccessIPRepository;
import com.app.kokonut.company.companytable.CompanyTable;
import com.app.kokonut.company.companytable.CompanyTableRepository;
import com.app.kokonut.company.companytablecolumninfo.CompanyTableColumnInfo;
import com.app.kokonut.company.companytablecolumninfo.CompanyTableColumnInfoRepository;
import com.app.kokonut.configs.GoogleOTP;
import com.app.kokonut.configs.KeyGenerateService;
import com.app.kokonut.configs.MailSender;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dtos.ActivityCode;
import com.app.kokonut.history.extra.decrypcounthistory.DecrypCountHistoryService;
import com.app.kokonutuser.KokonutUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Woody
 * Date : 2022-12-09
 * Time :
 * Remark : AuthService(인증서비스), 기존의 LoginService와 동일
 */
@Slf4j
@Service
public class AuthService {


    private final AdminService adminService;
    private final HistoryService historyService;
    private final KokonutUserService kokonutUserService;
    private final DecrypCountHistoryService decrypCountHistoryService;
    private final AlimtalkSendService alimtalkSendService;

    private final AwsKmsUtil awsKmsUtil;
    private final WhoisUtil whoisUtil;
    private final KeyGenerateService keyGenerateService;
    private final AdminRepository adminRepository;
    private final AwsKmsHistoryService awsKmsHistoryService;

    private final CompanyRepository companyRepository;
    private final CompanyDataKeyRepository companyDataKeyRepository;
    private final CompanyTableRepository companyTableRepository;
    private final CompanyTableColumnInfoRepository companyTableColumnInfoRepository;
    private final CompanySettingRepository companySettingRepository;
    private final CompanySettingAccessIPRepository companySettingAccessIPRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final RedisDao redisDao;

    private final GoogleOTP googleOTP;

    private final MailSender mailSender;

    @Autowired
    public AuthService(AdminService adminService, HistoryService historyService,
                       KokonutUserService kokonutUserService, DecrypCountHistoryService decrypCountHistoryService,
                       AlimtalkSendService alimtalkSendService, AdminRepository adminRepository,
                       AwsKmsUtil awsKmsUtil, WhoisUtil whoisUtil, KeyGenerateService keyGenerateService,
                       AwsKmsHistoryService awsKmsHistoryService, CompanyRepository companyRepository,
                       CompanyDataKeyRepository companyDataKeyRepository, CompanyTableRepository companyTableRepository,
                       CompanyTableColumnInfoRepository companyTableColumnInfoRepository,
                       CompanySettingRepository companySettingRepository, CompanySettingAccessIPRepository companySettingAccessIPRepository,
                       PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider,
                       AuthenticationManagerBuilder authenticationManagerBuilder,
                       RedisDao redisDao, GoogleOTP googleOTP, MailSender mailSender) {
        this.adminService = adminService;
        this.historyService = historyService;
        this.kokonutUserService = kokonutUserService;
        this.decrypCountHistoryService = decrypCountHistoryService;
        this.alimtalkSendService = alimtalkSendService;
        this.adminRepository = adminRepository;
        this.awsKmsUtil = awsKmsUtil;
        this.whoisUtil = whoisUtil;
        this.keyGenerateService = keyGenerateService;
        this.awsKmsHistoryService = awsKmsHistoryService;
        this.companyRepository = companyRepository;
        this.companyDataKeyRepository = companyDataKeyRepository;
        this.companyTableRepository = companyTableRepository;
        this.companyTableColumnInfoRepository = companyTableColumnInfoRepository;
        this.companySettingRepository = companySettingRepository;
        this.companySettingAccessIPRepository = companySettingAccessIPRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.redisDao = redisDao;
        this.googleOTP = googleOTP;
//        this.AWSURL = keyDataService.findByKeyValue("aws_s3_url");
        this.mailSender = mailSender;
    }

    // 이메일 가입존재 여부
    public ResponseEntity<Map<String, Object>> checkKnEmail(String knEmail) {
        log.info("checkKnEmail 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        if(knEmail.isEmpty()){
            log.info("이메일을 입력해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO079.getCode(), ResponseErrorCode.KO079.getDesc()));
        }

        boolean result = adminRepository.existsByKnEmail(knEmail);
        if(result) {
            AdminCompanySettingDto adminCompanySettingDto = adminRepository.findByAdminCompanySetting(knEmail);
            int knPwdErrorCount = adminCompanySettingDto.getKnPwdErrorCount();
            String roleCode = adminCompanySettingDto.getKnRoleCode();

            int csPasswordErrorCountSetting = Integer.parseInt(adminCompanySettingDto.getCsPasswordErrorCountSetting());
            if(csPasswordErrorCountSetting <= knPwdErrorCount && !roleCode.equals("ROLE_SYSTEM")) {
                log.error("로그인 오류 횟수제한 이메일 : "+knEmail);
                // -> 로그인불가처리 - 관리자가 비밀번호 재설정을 눌러줄 방법밖에 없음(왕관관리자는 코코넛에게 문의)
                if(!roleCode.equals("ROLE_MASTER") && !roleCode.equals("ROLE_ADMIN")) {
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.KO095.getCode(),"비밀번호를 "+knPwdErrorCount+"회 틀리셨습니다. 관리자에게 비밀번호변경을 요청 바랍니다."));
//                    return ResponseEntity.ok(res.fail(ResponseErrorCode.KO096.getCode(),"비밀번호를 "+knPwdErrorCount+"회 틀리셨습니다. contact@kokonut.me로 문의 바랍니다."));
                }
//                else {
//                    return ResponseEntity.ok(res.fail(ResponseErrorCode.KO095.getCode(),"비밀번호를 "+knPwdErrorCount+"회 틀리셨습니다. 관리자에게 비밀번호변경을 요청 바랍니다."));
//                }
            }
        }

        data.put("result", result);
        return ResponseEntity.ok(res.success(data));
    }

    // 이메일 중복확인
    public ResponseEntity<Map<String, Object>> existsKnEmail(String knEmail) {
        log.info("existsKnEmail 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        if(knEmail.isEmpty()){
            log.info("이메일을 입력해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO079.getCode(), ResponseErrorCode.KO079.getDesc()));
        }

//        log.info("knEmail : "+knEmail);
        if (adminRepository.existsByKnEmail(knEmail)) {
            log.info("이미 가입되어 있는 이메일입니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO005.getCode(), ResponseErrorCode.KO005.getDesc()));
        }
        return ResponseEntity.ok(res.success(data));
    }

    // 이메일 인증번호 보내기(6자리 번호 형식, 유효기간 3분)
    public ResponseEntity<Map<String, Object>> numberSendKnEmail(String knEmail) throws IOException {
        log.info("numberSendKnEmail 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 인증번호(숫자6자리) 생성
        SecureRandom secureRandom = new SecureRandom();
        int ctNumber = secureRandom.nextInt(900000) + 100000;
        log.info("생성된 인증번호 : "+ctNumber);

        // 인증번호 메일전송
        // 이메일 전송을 위한 전처리 - filter, unfilter
        String title = ReqUtils.filter("코코넛 이메일 인증번호가 도착했습니다.");
        String contents = ReqUtils.unFilter("인증번호 : "+ctNumber);

        // 템플릿 호출을 위한 데이터 세팅
        HashMap<String, String> callTemplate = new HashMap<>();
//        callTemplate.put("template", "MailTemplate");
        callTemplate.put("title", "인증번호 알림");
        callTemplate.put("content", contents);

        // 템플릿 TODO 템플릿 디자인 추가되면 수정
        contents = mailSender.getHTML6(callTemplate);
        String reciverEmail = knEmail;
        String reciverName = "kokonut";

        String mailSenderResult = mailSender.sendKokonutMail(reciverEmail, reciverName, title, contents);
        if(mailSenderResult != null) {
            // mailSender 성공
            log.info("### 메일전송 성공했습니다. reciver Email : "+ knEmail);
        }else{
            // mailSender 실패
            log.error("### 해당 메일 전송에 실패했습니다. 관리자에게 문의하세요. reciverEmail : "+ knEmail);
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO041.getCode(), ResponseErrorCode.KO041.getDesc()));
        }

        // 인증번호 레디스에 담기
        redisDao.setValues("CT: " + knEmail, String.valueOf(ctNumber), Duration.ofMillis(180000)); // 제한시간 3분
        log.info("레디스에 인증번호 저장성공");

        return ResponseEntity.ok(res.success(data));
    }

    // 이메일 인증번호 검증
    public ResponseEntity<Map<String, Object>> numberCheckKnEmail(String knEmail, String ctNumber) {
        log.info("numberCheckKnEmail 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String redisCtNumber = redisDao.getValues("CT: "+knEmail);
//        log.info("redisCtNumber : "+redisCtNumber);

        if(redisCtNumber != null) {
            if(redisCtNumber.equals(ctNumber)) {
                redisDao.deleteValues("CT: " + knEmail);
                log.info("redis 인증번호 체크완료");
            } else {
                log.error("입력하신 인증번호가 일치하지 않습니다. 다시 입력해주세요.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO076.getCode(), ResponseErrorCode.KO076.getDesc()));
            }
        } else {
            log.error("제한시간 내에 입력하시지 않았습니다. 다시 인증번호를 받아주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO075.getCode(), ResponseErrorCode.KO075.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 이메일찾기 기능
    public ResponseEntity<Map<String, Object>> findKnEmail(String keyEmail) {
        log.info("findKnEmail 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("keyEmail : "+keyEmail);
        // 레디스의 keyEmail의 값과 동일한 이메일 가져오기
        String redisKnEmail = redisDao.getValues("KE: "+keyEmail);
        if(redisKnEmail != null) {
            log.info("redisKnEmail : "+redisKnEmail);
            // 이메일 *** 처리하기
            String[] array = redisKnEmail.split("@");
            String firstEmail = array[0];
            String secondEmail = array[1];

            // 앞에고 firstEmail
//            log.info("firstEmail : "+firstEmail);
            int firstEmailLen = firstEmail.length();
            int firstEmailLenVal = (firstEmailLen*2)/3;

            // 뒤에고 secondEmail
//            log.info("secondEmail : "+secondEmail);
            int secondEmailLen = secondEmail.length();
            int secondEmailLenVal = (secondEmailLen*2)/3;
            StringBuilder secondResult = new StringBuilder(secondEmail.substring(secondEmailLenVal));
            for(int i=0; i<=secondEmailLen-secondEmailLenVal; i++) {
                secondResult = new StringBuilder("*"+secondResult);
            }

            redisKnEmail = firstEmail.substring(0, firstEmailLenVal) + "*".repeat(Math.max(0, firstEmailLen - firstEmailLenVal + 1)) + "@" + secondResult;

            data.put("knEmail", redisKnEmail);

            // 레디스에서 삭제처리
//            redisDao.deleteValues("KE: " + keyEmail);
        } else {
            log.error("조회한 데이터가 없습니다."); // 이쪽으로 들어올 수가 없음
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO003.getCode(), ResponseErrorCode.KO003.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 이메일로 임시비밀번호 보내는 기능
    @Transactional
    public ResponseEntity<Map<String, Object>> passwordSendKnEmail(String knEmail) throws IOException {
        log.info("passwordSendKnEmail 호출");

        log.info("비밀번호찾는 이메일 : "+knEmail);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 접속정보에서 필요한 정보 가져오기.
        Admin admin = adminRepository.findByKnEmail(knEmail)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다. : " + knEmail));

        String tempPassword = Utils.getRamdomStr(10);
        log.info("임시비밀번호 : "+tempPassword);

        admin.setKnPassword(passwordEncoder.encode(tempPassword));

        // 인증번호 메일전송
        // 이메일 전송을 위한 전처리 - filter, unfilter
        String title = ReqUtils.filter("코코넛 이메일 임시비밀번호가 도착했습니다.");
        String contents = ReqUtils.unFilter("임시비밀번호 : "+tempPassword);

//        // 템플릿 호출을 위한 데이터 세팅
        HashMap<String, String> callTemplate = new HashMap<>();
//        callTemplate.put("template", "KokonutMailTemplate");
        callTemplate.put("title", "인증번호 알림");
        callTemplate.put("content", contents);

        // 템플릿 TODO 템플릿 디자인 추가되면 수정
        contents = mailSender.getHTML6(callTemplate);
        String reciverName = "kokonut";

        String mailSenderResult = mailSender.sendKokonutMail(knEmail, reciverName, title, contents);
        log.info("mailSenderResult : "+ mailSenderResult);

        if(mailSenderResult != null){
            // mailSender 성공
            log.info("### 메일전송 성공했습니다. reciver Email : "+ knEmail);
            adminRepository.save(admin);

            data.put("tempPassword", tempPassword);
        }else{
            // mailSender 실패
            log.error("### 해당 메일 전송에 실패했습니다. 관리자에게 문의하세요. reciverEmail : "+ knEmail);
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO041.getCode(), ResponseErrorCode.KO041.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 비밀번호 찾기(사용할 비밀번호로 변경하는) 기능
    @Transactional
    public ResponseEntity<Map<String, Object>> passwordUpdate(AdminPasswordChangeDto adminPasswordChangeDto, HttpServletRequest request, HttpServletResponse response) {
        log.info("passwordUpdate 호출!");

//        log.info("adminPasswordChangeDto : "+adminPasswordChangeDto);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 본인인증 거치는 함수호출
        AuthPhoneCheckDto authPhoneCheckDto = Utils.authPhoneCheck(request);

        log.info("비밀번호 찾기 시작");
        Optional<Admin> optionalAdmin = adminRepository.findByKnEmail(adminPasswordChangeDto.getKnEmail());
        if (optionalAdmin.isEmpty()) {
            log.error("존재하지 않은 유저");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(),"해당 유저가 "+ResponseErrorCode.KO004.getDesc()));
        } else {
            String knName = optionalAdmin.get().getKnName();
            String knPhoneNumber = optionalAdmin.get().getKnPhoneNumber();

            // 본인인증 체크
            if (!knPhoneNumber.equals(authPhoneCheckDto.getJoinPhone()) || !knName.equals(authPhoneCheckDto.getJoinName())) {
                log.error("본인인증된 명의 및 휴대전화번호가 아닙니다. 본인인증을 다시해주세요.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO033.getCode(), ResponseErrorCode.KO033.getDesc()));
            }else {
                // 인증 쿠키제거
                Utils.cookieDelete("joinName", response);
                Utils.cookieDelete("joinPhone", response);
            }

            // 임시비밀번호 일치한지 체크
            if (!passwordEncoder.matches(adminPasswordChangeDto.getTempPwd(), optionalAdmin.get().getKnPassword())){
                log.error("보내드린 임시 비밀번호가 일치하지 않습니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO078.getCode(), ResponseErrorCode.KO078.getDesc()));
            }

            // 비밀번호 일치한지 체크
            if (!adminPasswordChangeDto.getKnPassword().equals(adminPasswordChangeDto.getKnPasswordConfirm())) {
                log.error("입력하신 비밀번호가 서로 일치하지 않습니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO013.getCode(), ResponseErrorCode.KO013.getDesc()));
            }

            optionalAdmin.get().setKnPassword(passwordEncoder.encode(adminPasswordChangeDto.getKnPassword()));
            optionalAdmin.get().setKnPwdChangeDate(LocalDateTime.now());
            optionalAdmin.get().setKnPwdErrorCount(0);
            optionalAdmin.get().setModify_email(adminPasswordChangeDto.getKnEmail());
            optionalAdmin.get().setModify_date(LocalDateTime.now());
            adminRepository.save(optionalAdmin.get());
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 코코넛 회원가입 기능
    @Transactional
    public ResponseEntity<Map<String, Object>> kokonutSignUp(AuthRequestDto.KokonutSignUp kokonutSignUp, HttpServletRequest request, HttpServletResponse response) {
        log.info("kokonutSignUp 호출!");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 본인인증 거치는 함수호출
        AuthPhoneCheckDto authPhoneCheckDto = Utils.authPhoneCheck(request);

        if (adminRepository.existsByKnEmail(kokonutSignUp.getKnEmail())) {
            log.error("이미 회원가입된 이메일입니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO005.getCode(), ResponseErrorCode.KO005.getDesc()));
        }

        if (!kokonutSignUp.getKnEmailCheck()) {
            log.error("이메일인증을 해주시길 바랍니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO005.getCode(), ResponseErrorCode.KO005.getDesc()));
        }

        // 비밀번호 일치한지 체크
        if (!kokonutSignUp.getKnPassword().equals(kokonutSignUp.getKnPasswordConfirm())) {
            log.error("입력하신 비밀번호가 서로 일치하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO013.getCode(), ResponseErrorCode.KO013.getDesc()));
        }

        // 본인인증 체크
        if (!kokonutSignUp.getKnPhoneNumber().equals(authPhoneCheckDto.getJoinPhone()) || !kokonutSignUp.getKnName().equals(authPhoneCheckDto.getJoinName())) {
            log.error("본인인증된 명의 및 휴대전화번호가 아닙니다. 본인인증을 다시해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO033.getCode(), ResponseErrorCode.KO033.getDesc()));
        }else {
            // 인증 쿠키제거
            Utils.cookieDelete("joinName", response);
            Utils.cookieDelete("joinPhone", response);
        }

        log.info("회원가입 시작");

        // 저장할 KMS 암호화키 생성
        String dataKey;
//        if(!signUp.getKnEmail().equals("kokonut@kokonut.me")) { // 테스트일 경우 패스
        AwsKmsResultDto awsKmsResultDto = awsKmsUtil.dataKeyEncrypt();
        if (awsKmsResultDto.getResult().equals("success")) {
            dataKey = awsKmsResultDto.getDataKey();
        } else {
            // 리턴처리를 어떻게해야 할지 내용 정하기 - 2022/12/22 to.woody
            log.error("암호화 키 생성 실패");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO036.getCode(), ResponseErrorCode.KO036.getDesc()));
        }
        //    }

        // 소속 저장
        String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        String cpCode = keyGenerateService.keyGenerate("kn_company", "kokonut"+nowDate, "KokonutSystem");

        Company company = new Company();
        // 회사 고유코드
        company.setCpCode(cpCode);
        company.setCpName(kokonutSignUp.getCpName());
        company.setCpTableCount(1);
        company.setCpSubscribe("0");
        company.setInsert_email(kokonutSignUp.getKnEmail());
        company.setInsert_date(LocalDateTime.now());

        // 회사 암호화키(DataKey) 저장
        CompanyDataKey companyDataKey = new CompanyDataKey();
        companyDataKey.setCpCode(company.getCpCode());
        companyDataKey.setDataKey(dataKey);
        companyDataKey.setIvKey(Base64.getEncoder().encodeToString(AESGCMcrypto.generateIV()));

        String ctName = cpCode+"_1";

        // 회사 유저 기본테이블 저장
        CompanyTable companyTable = new CompanyTable();
        companyTable.setCpCode(cpCode);
        companyTable.setCtName(ctName);
        companyTable.setCtTableCount("1");
        companyTable.setCtDesignation("기본");
        companyTable.setCtAddColumnCount(1);
        companyTable.setCtNameStatus("");
        companyTable.setCtPhoneStatus("");
        companyTable.setCtBirthStatus("");
        companyTable.setCtGenderStatus("");
        companyTable.setCtEmailStatus("");
        companyTable.setCtAddColumnSecurityCount(0);
        companyTable.setCtAddColumnUniqueCount(0);
        companyTable.setCtAddColumnSensitiveCount(0);
        companyTable.setInsert_email(kokonutSignUp.getKnEmail());
        companyTable.setInsert_date(LocalDateTime.now());

        // 계정 저장
        Admin admin = new Admin();
        admin.setKnName(kokonutSignUp.getKnName());
        admin.setKnPhoneNumber(kokonutSignUp.getKnPhoneNumber());
        admin.setKnEmail(kokonutSignUp.getKnEmail());
        admin.setKnPassword(passwordEncoder.encode(kokonutSignUp.getKnPassword()));
        admin.setCompanyId(companyRepository.save(company).getCompanyId());
        admin.setMasterId(0L); // 사업자는 0, 관리자가 등록한건 관리자IDX ?
        admin.setKnUserType(1);
        admin.setKnRegType(1);
        admin.setKnRoleCode(AuthorityRole.ROLE_MASTER);
        admin.setKnIsEmailAuth("Y");
        admin.setInsert_email(kokonutSignUp.getKnEmail());
        admin.setInsert_date(LocalDateTime.now());
        Admin saveAdmin = adminRepository.save(admin);

        companyDataKeyRepository.save(companyDataKey);
        companyTableRepository.save(companyTable);

        boolean result = kokonutUserService.createTableKokonutUser(ctName, 0);
        log.info("result : "+result);
        if(result) {
            // 기본컬럼 아이디 저장(1_id)
            CompanyTableColumnInfo companyTableColumnInfo = new CompanyTableColumnInfo();
            companyTableColumnInfo.setCtName(cpCode+"_1");
            companyTableColumnInfo.setCtciName("ID_1_id");
            companyTableColumnInfo.setCtciCode("1_id");
            companyTableColumnInfo.setCtciDesignation("아이디");
            companyTableColumnInfo.setCtciSecuriy("0");
            companyTableColumnInfo.setInsert_email(kokonutSignUp.getKnEmail());
            companyTableColumnInfo.setInsert_date(LocalDateTime.now());
            companyTableColumnInfoRepository.save(companyTableColumnInfo);

            // 서비스설정 기본값 저장
            CompanySetting companySetting = new CompanySetting();
            companySetting.setCpCode(cpCode);
            companySetting.setCsOverseasBlockSetting("0");
            companySetting.setCsAccessSetting("0");
            companySetting.setCsPasswordChangeSetting("12");
            companySetting.setCsPasswordErrorCountSetting("5");
            companySetting.setCsAutoLogoutSetting("30");
            companySetting.setCsLongDisconnectionSetting("0");
            companySetting.setInsert_email(kokonutSignUp.getKnEmail());
            companySetting.setInsert_date(LocalDateTime.now());
            companySettingRepository.save(companySetting);

            // 호출 카운팅 올리기
            awsKmsHistoryService.awskmsHistoryCount(cpCode);

//            String randomStr = Utils.getAlphabetStr(5);
//            String app_user_id = "app_user_id_"+randomStr; // app_user_id 변수

//            List<JSONObject> bottonJsonList = new ArrayList<>();
//            JSONObject bottonJson = new JSONObject();
//            bottonJson.put("name", "온보딩 신청하기");
//            bottonJson.put("type", "WL");
//            bottonJson.put("url_pc", "https://kokonut.me/#/landing");
//            bottonJson.put("url_mobile", "https://kokonut.me/#/landing");
//            bottonJsonList.add(bottonJson);

//            // 알림톡전송
//            alimtalkSendService.kokonutAlimtalkSend("f1d0081b74b59cedc2648ea2da6fa6788e26c181", "kokonut_template_02", "[안내]\n" +
//                    saveAdmin.getKnName()+" 님, 코코넛에 오신 것을 환영합니다!\n" +
//                    "\n" +
//                    "코코넛은 개인정보보호법을 제대로 준수할 수 있도록\n" +
//                    "1) 수집한 개인정보를 법에 맞게 제대로 관리하는 기능\n" +
//                    "2) 수집한 개인정보를 안전하게 보호하는 기술적인 보안\n" +
//                    "을 모두 제공하는 구독형 서비스입니다.\n" +
//                    "\n" +
//                    "처음이라 어렵다면?! 코코넛 온보딩을 진행하고 있습니다!\n" +
//                    "차근차근 도와드릴게요!", saveAdmin.getKnPhoneNumber(), app_user_id, "1", bottonJsonList);

            // 슬랙 메세지전송
            SlackUtil.registerAlarmSend(saveAdmin.getKnName()+"님이 '"+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))+"' 에 회원가입 하셨습니다.");
        }

        log.info("사업자 정보 저장 saveAdmin : "+saveAdmin.getAdminId());

        return ResponseEntity.ok(res.success(data));
    }

    // 로그인, 구글OTP 확인후 -> JWT 발급기능
    @Transactional
    public ResponseEntity<Map<String,Object>> authToken(AuthRequestDto.Login login,
                                                        HttpServletRequest request, HttpServletResponse response) {
        log.info("authToken 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 이메일이 test@kokonut.me 체크하는 로직추가
        String knEmail = login.getKnEmail();

        if (knEmail.equals("test@kokonut.me")) {
            log.info("체험하기 모드 : 체험하기 로그인");

            data.put("jwtToken", "experience_jwt");
            return ResponseEntity.ok(res.success(data));
        }
        else {
            if(login.getOtpValue() == null || login.getOtpValue().equals("")) {
                log.error("구글 OTP 값이 존재하지 않습니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO010.getCode(),ResponseErrorCode.KO010.getDesc()));
            }

            int historyState = 0;
            Optional<Admin> optionalAdmin = adminRepository.findByKnEmail(knEmail);

            if (optionalAdmin.isEmpty()) {
                log.error("아이디 또는 비밀번호가 일치하지 않습니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO016.getCode(),ResponseErrorCode.KO016.getDesc()));
            }
            else {
                try {

                    // 로그인 코드
                    String publicIp = CommonUtil.publicIp();
                    ActivityCode activityCode = ActivityCode.AC_01;

                    AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(knEmail);
                    Long adminId = adminCompanyInfoDto.getAdminId();
                    String companyCode = adminCompanyInfoDto.getCompanyCode();
                    String roleCode = optionalAdmin.get().getKnRoleCode().getCode();

                    // 활동이력 저장 -> 로그인실패 - 비정상 모드
                    Long activityHistoryId = historyService.insertHistory(2, adminId, activityCode,
                            companyCode+" - "+activityCode.getDesc()+" 시도 이력", "로그인 실패 비밀번호 일치하지 않음", publicIp, 0, knEmail);

                    // Login ID/PW 를 기반으로 Authentication 객체 생성 -> 아아디 / 비번 검증
                    // 이때 authentication은 인증 여부를 확인하는 authenticated 값이 false
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            login.getKnEmail(), Utils.decryptData(login.getKnPassword(), request.getHeader("keyBufferSto"), request.getHeader("ivSto")));


                    if(optionalAdmin.get().getKnOtpKey() == null){ // 시스템관리자 일 경우 제외
                        log.error("등록된 OTP가 존재하지 않습니다. 구글 OTP 인증을 등록해주세요.");
                        return ResponseEntity.ok(res.fail(ResponseErrorCode.KO011.getCode(),ResponseErrorCode.KO011.getDesc()));
                    }
                    else {
                        // 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
                        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
                        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

                        // OTP 검증 절차
                        boolean auth;

                        if (knEmail.equals("system@kokonut.me")) {
                            auth = true;
                        } else {
                            auth = googleOTP.checkCode(login.getOtpValue(), optionalAdmin.get().getKnOtpKey());
                        }
                        log.info("auth : " + auth);

                        if (!auth) {
                            log.error("입력된 구글 OTP 값이 일치하지 않습니다. 다시 확인해주세요.");
                            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO012.getCode(), ResponseErrorCode.KO012.getDesc()));
                        } else {
                            log.info("OTP인증완료 -> JWT토큰 발급");

                            int knPwdErrorCount = optionalAdmin.get().getKnPwdErrorCount(); // 비밀번호 오류횟수

                            // 활동이력 저장 -> 로그인정상 - 비정상 모드
                            historyService.updateHistory(activityHistoryId,
                                    companyCode+" - "+activityCode.getDesc()+" 시도 이력", "로그인 성공", 0);

                            // 비밀번호 오휴 횟수 제한 가져오기
                            // 설정해둔 횟수와 같거나 크면 로그인 제한됨 -> 비밀번호 찾기 미제공 -> 왕관 최고관리자일경우
                            CompanySettingCheckDto companySettingCheckDto = companySettingRepository.findByCompanySettingCheck(companyCode);
                            if(companySettingCheckDto.getCsOverseasBlockSetting().equals("1")) {
                                // 로그인사람이 해외인지 체크
                                log.info("서비스 로그인 위치가 해외인지 체크");

                                // 오픈API 라이브러리를 통해 체킹하기
                                String countryCode = whoisUtil.whoisAPI(publicIp);
                                log.info("countryCode : "+countryCode);

                                if(countryCode != null) {
                                    log.info("로그인 국가코드 : "+countryCode);
                                    if(!countryCode.equals("KR")) {
                                        log.info("해외로그인 차단서비스 활성화");
                                        historyState = 1;
                                        historyService.updateHistory(activityHistoryId,
                                                 companyCode+" - "+activityCode.getDesc()+" 시도 이력", "해외로그인 차단서비스 활성화 -> 로그인 시도 해당 국가코드 : "+countryCode, 0);
                                        data.put("blockAbroad", activityHistoryId);
                                        data.put("knName", optionalAdmin.get().getKnName());
                                        data.put("knPhoneNumber", optionalAdmin.get().getKnPhoneNumber());
                                        data.put("blockAbroadMsg", "해외로그인이 차단되어 본인인증해주시길 바랍니다.");
                                    }
                                } else {
                                    log.error("whois library 호출오류");
                                    historyService.insertHistory(4, adminId, activityCode,
                                            companyCode+" - "+activityCode.getDesc()+" 시도 이력", "whois library 호출오류", publicIp, 0, knEmail);
                                }
                            }

                            
                            if(companySettingCheckDto.getCsAccessSetting().equals("1")) {
                                log.info("접속 허용IP 체크");
                                boolean accessIpCheckResult = companySettingAccessIPRepository.existsCompanySettingAccessIPByCsIdAndCsipIp(companySettingCheckDto.getCsId(), publicIp);
                                log.info("accessIpCheckResult : "+accessIpCheckResult);
                                if(!accessIpCheckResult) {
                                    log.error("허용되지 않은 IP 차단");

                                    historyService.updateHistory(activityHistoryId,
                                            companyCode+" - "+activityCode.getDesc()+" 시도 이력", "접속 허용되지 않은 IP에서 로그인 시도하여 실패", 0);

                                    return ResponseEntity.ok(res.fail(ResponseErrorCode.KO094.getCode(),ResponseErrorCode.KO094.getDesc()));
                                }
                            }

                            int csPasswordErrorCountSetting = Integer.parseInt(companySettingCheckDto.getCsPasswordErrorCountSetting());
//                            log.info("csPasswordErrorCountSetting : "+csPasswordErrorCountSetting);
//                            log.info("knPwdErrorCount : "+knPwdErrorCount);
                            if(csPasswordErrorCountSetting <= knPwdErrorCount && !roleCode.equals("ROLE_SYSTEM")) {
                                log.error("로그인 오류 횟수제한 이메일 : "+knEmail);
                                // -> 로그인불가처리 - 관리자가 비밀번호 재설정을 눌러줄 방법밖에 없음(왕관관리자는 코코넛에게 문의)
                                historyService.updateHistory(activityHistoryId,
                                        companyCode+" - "+activityCode.getDesc()+" 시도 이력", "로그인 오류횟수 초과로 인한 로그인실패", 0);

                                if(roleCode.equals("ROLE_MASTER") || roleCode.equals("ROLE_ADMIN")) {
//                                    return ResponseEntity.ok(res.fail(ResponseErrorCode.KO096.getCode(),"비밀번호를 "+knPwdErrorCount+"회 틀리셨습니다. contact@kokonut.me로 문의바랍니다."+"(최대횟수 : "+csPasswordErrorCountSetting+"회)"));
                                    return ResponseEntity.ok(res.fail(ResponseErrorCode.KO096.getCode(),"비밀번호를 "+knPwdErrorCount+"회 틀리셨습니다. 비밀번호 찾기를 진행해주시길 바랍니다."));
                                } else {
                                    return ResponseEntity.ok(res.fail(ResponseErrorCode.KO095.getCode(),"비밀번호를 "+knPwdErrorCount+"회 틀리셨습니다. 최고관리자에게 비밀번호변경을 요청 바랍니다."));
                                }
                            }

                            // 인증 정보를 기반으로 JWT 토큰 생성
                            AuthResponseDto.TokenInfo jwtToken = jwtTokenProvider.generateToken(authentication);

                            data.put("jwtToken", jwtToken.getAccessToken());

                            // 쿠키저장함수 호출
                            if(response != null) {
                                Utils.cookieSave("refreshToken", jwtToken.getRefreshToken(), 604800, response);
                            }

                            // RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
                            redisDao.setValues("RT: " + authentication.getName(), jwtToken.getRefreshToken(), Duration.ofMillis(jwtToken.getRefreshTokenExpirationTime()));

                            // 비밀번호 틀린횟수 초기화
                            if(knPwdErrorCount != 0) {
                                optionalAdmin.get().setKnPwdErrorCount(0);
                            }

                            // 마지막 로그인 시간기록
                            optionalAdmin.get().setKnLastLoginDate(LocalDateTime.now());
                            // 최근 접속 IP
                            optionalAdmin.get().setKnIpAddr(publicIp);
                            adminRepository.save(optionalAdmin.get());

                            if(historyState == 0) {
                                historyService.updateHistory(activityHistoryId,
                                        companyCode+" - "+activityCode.getDesc()+" 시도 이력", "로그인 성공", 1);
                            }

                            return ResponseEntity.ok(res.success(data));
                        }
                    }
                }
                catch (Exception e) {
                    log.error("아이디 또는 비밀번호가 일치하지 않습니다.");

                    // 비밀번호가 틀렸기때문에 비밀번호 오류횟수 카운팅
                    optionalAdmin.get().setKnPwdErrorCount(optionalAdmin.get().getKnPwdErrorCount()+1);
                    adminRepository.save(optionalAdmin.get());

                    return ResponseEntity.ok(res.fail(ResponseErrorCode.KO016.getCode(),ResponseErrorCode.KO016.getDesc()));
                }
            }
        }
    }

    // JWT 토큰 새로고침 기능
    public ResponseEntity<Map<String,Object>> reissue(AuthRequestDto.Reissue reissue) {
        log.info("reissue 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // Access Token 에서 User knEmail 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());

        // Redis 에서 User knEmail 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
        String refreshToken = redisDao.getValues("RT: "+authentication.getName());

        // Refresh Token 검증
        if (jwtTokenProvider.validateToken(refreshToken) == 400) {
            log.error("유효하지 않은 토큰정보임");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO007.getCode(),ResponseErrorCode.KO007.getDesc()));
        }

        // 로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
        if(ObjectUtils.isEmpty(refreshToken)) {
            log.error("로그아웃된 토큰임");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO006.getCode(),ResponseErrorCode.KO006.getDesc()));
        }

//        if(!refreshToken.equals(reissue.getRefreshToken())) {
//            log.error("Redis토큰과 받은 리플레쉬 토큰이 맞지않음");
//            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO008.getCode(),ResponseErrorCode.KO008.getDesc()));
//        }

        // Redis 에서 해당 User knEmail 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
//        if (redisDao.getValues("RT:" + authentication.getName()) != null) {
        // Refresh Token 삭제
//            redisDao.deleteValues("RT:" + authentication.getName());
//        }

        // 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenProvider.getExpiration(reissue.getAccessToken());
        redisDao.setValues(reissue.getAccessToken(), "logout", Duration.ofMillis(expiration));

        // 새로운 토큰 생성
        AuthResponseDto.TokenInfo jwtToken = jwtTokenProvider.generateToken(authentication);

        // RefreshToken Redis 업데이트
//        redisDao.setValues("RT: "+authentication.getName(), jwtToken.getRefreshToken(), Duration.ofMillis(jwtToken.getRefreshTokenExpirationTime()));

        data.put("jwtToken", jwtToken.getAccessToken());

        return ResponseEntity.ok(res.success(data));
    }

    // 로그아웃 기능
    public ResponseEntity<Map<String,Object>> logout(AuthRequestDto.Logout logout, HttpServletRequest request, HttpServletResponse response) {
        log.info("logout 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String accessToken = logout.getAccessToken();

        // Access Token 검증
        if (jwtTokenProvider.validateToken(accessToken) == 200) {
//            log.error("유효하지 않은 토큰정보임");
//            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO006.getCode(),ResponseErrorCode.KO006.getDesc()));
//        } else {
            // Access Token 에서 User knEmail 을 가져옵니다.
//            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

            // Redis 에서 해당 User knEmail 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
//            if (redisDao.getValues("RT: "+authentication.getName()) != null) {
            // Refresh Token 삭제
//                redisDao.deleteValues("RT: "+authentication.getName());
//            }

            // 해당 Access Token 유효시간 가지고 와서 BlackList로 저장하기
            Long expiration = jwtTokenProvider.getExpiration(accessToken);
            redisDao.setValues(accessToken, "logout", Duration.ofMillis(expiration));
        }

        // 쿠키리셋처리
        Utils.cookieLogout(request, response);

        return ResponseEntity.ok(res.success(data));
    }

    // 구글 QR코드 생성(1번)
    public ResponseEntity<Map<String, Object>> otpQRcode(String knEmail) {
        log.info("otpQRcode 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("knEmail : "+knEmail);
//        String userknEmail = SecurityUtil.getCurrentUserknEmail();
//        if(userknEmail.equals("anonymousUser")){
//            log.error("사용하실 수 없는 토큰정보 입니다. 다시 로그인 해주세요.");
//            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO009.getCode(),ResponseErrorCode.KO009.getDesc()));
//        } else {

        Optional<Admin> optionalAdmin = adminRepository.findByKnEmail(knEmail);

        if (optionalAdmin.isEmpty()) {
            log.error("해당 유저가 존재하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(),"해당 유저가 "+ResponseErrorCode.KO004.getDesc()));
        }
        else {
            log.info("구글 QR코드 보낼 이메일 : " + knEmail);

            // 서프의 Map관련 이슈
//            HashMap<String, String> map = googleOTP.generate(userknEmail);
            GoogleOtpGenerateDto googleOtpGenerateDto = googleOTP.generate(knEmail);
//            String otpKey = map.get("encodedKey");
            String url = googleOtpGenerateDto.getUrl().replace("200x200", "160x160");

//            url = url.replace("200x200", "160x160");

            data.put("CSRF_TOKEN", UUID.randomUUID().toString());
            data.put("otpKey", googleOtpGenerateDto.getOtpKey());
            data.put("url", url);

            return ResponseEntity.ok(res.success(data));
        }
    }

    // 구글 OTP 값 확인(2번)
    public ResponseEntity<Map<String,Object>> checkOTP(AdminGoogleOTPDto.GoogleOtpCertification googleOtpCertification) {
        log.info("checkOTP 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        Optional<Admin> optionalAdmin = adminRepository.findByKnEmail(googleOtpCertification.getKnEmail());

        if (optionalAdmin.isEmpty()) {
            log.error("해당 유저가 존재하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(),"해당 유저가 "+ResponseErrorCode.KO004.getDesc()));
        }
        else {
            Pattern pattern = Pattern.compile("[+-]?\\d+");
            if(!pattern.matcher(googleOtpCertification.getOtpValue()).matches()) {
                log.error("OTP는 숫자 형태로 입력하세요.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO015.getCode(),ResponseErrorCode.KO015.getDesc()));
            }

            //현재암호비교
            if (!passwordEncoder.matches(googleOtpCertification.getKnPassword(), optionalAdmin.get().getKnPassword())){
                log.error("입력한 비밀번호가 일치하지 않습니다.");
                adminService.adminErrorPwd(optionalAdmin.get());
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO013.getCode(), ResponseErrorCode.KO013.getDesc()));
            }

            boolean auth;
            auth = googleOTP.checkCode(googleOtpCertification.getOtpValue(), googleOtpCertification.getKnOtpKey());

            AriaUtil aria = new AriaUtil();
            String encValue = aria.Encrypt("authOtpKeyKokonut!!");

            if(!auth){
                log.error("입력된 구글 OTP 값이 일치하지 않습니다. 다시 확인해주세요.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO012.getCode(),ResponseErrorCode.KO012.getDesc()));
            }
            else {
                data.put("authOtpKey", encValue);
                return ResponseEntity.ok(res.success(data));
            }
        }
    }

    // 구글 OTPKey 등록(3번)
    @Transactional
    public ResponseEntity<Map<String, Object>> saveOTP(AdminGoogleOTPDto.GoogleOtpSave googleOtpSave, HttpServletRequest request, HttpServletResponse response) {
        log.info("saveOTP 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        Optional<Admin> optionalAdmin = adminRepository.findByKnEmail(googleOtpSave.getKnEmail());

        if (optionalAdmin.isEmpty()) {
            log.error("해당 유저가 존재하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(),"해당 유저가 "+ResponseErrorCode.KO004.getDesc()));
        }
        else {

            // 본인인증 거치는 함수호출
            AuthPhoneCheckDto authPhoneCheckDto = Utils.authPhoneCheck(request);

            String knName = optionalAdmin.get().getKnName();
            String knPhoneNumber = optionalAdmin.get().getKnPhoneNumber();

            // 본인인증 체크
            if (!knPhoneNumber.equals(authPhoneCheckDto.getJoinPhone()) || !knName.equals(authPhoneCheckDto.getJoinName())) {
                log.error("본인인증된 명의 및 휴대전화번호가 아닙니다. 본인인증을 다시해주세요.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO033.getCode(), ResponseErrorCode.KO033.getDesc()));
            }else {
                // 인증 쿠키제거
                Utils.cookieDelete("joinName", response);
                Utils.cookieDelete("joinPhone", response);
            }

            log.info("OTP Key 등록 할 이메일 : " + optionalAdmin.get().getKnEmail());

            String authOtpKey = googleOtpSave.getAuthOtpKey();
            AriaUtil aria = new AriaUtil();
            String decValue = aria.Decrypt(authOtpKey);
            log.info("decValue : " + decValue);

            if (!decValue.equals("authOtpKeyKokonut!!")) { // 본인인증 이후 나오는 값
                log.error("인증되지 않은 절차로 진행되었습니다. 올바른 방법으로 진행해주세요.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO014.getCode(), ResponseErrorCode.KO014.getDesc()));
            }

            optionalAdmin.get().setModify_email(optionalAdmin.get().getKnEmail());
            optionalAdmin.get().setModify_date(LocalDateTime.now());

            optionalAdmin.get().setKnOtpKey(googleOtpSave.getKnOtpKey());
            optionalAdmin.get().setKnIsLoginAuth("Y");
            adminRepository.save(optionalAdmin.get());

            // 변경이후 시스템관리자에게 메일보내기 시작 추가하기 12/05 Woody

            return ResponseEntity.ok(res.success(data));
        }
    }

    // 관리자 등록하기전 키 검증
    @Transactional
    public ResponseEntity<Map<String, Object>> createCheck(AdminCreateDto adminCreateDto) throws Exception {
        log.info("createCheck 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

//        log.info("ev : "+adminCreateDto.getEvKoData());
//        log.info("kv : "+adminCreateDto.getKvKoData());
//        log.info("iv : "+adminCreateDto.getIvKoData());

        long ev = Long.parseLong(adminCreateDto.getEvKoData());
        String kv = adminCreateDto.getKvKoData();
        String iv = adminCreateDto.getIvKoData();

        CompanyEncryptDto companyEncryptDto = companyRepository.findByDataKey(ev);
        String cpCode = companyEncryptDto.getCpCode();

        AwsKmsResultDto awsKmsResultDto = awsKmsUtil.dataKeyDecrypt(companyEncryptDto.getDataKey());
        String email = AESGCMcrypto.decrypt(kv,awsKmsResultDto.getSecretKey(), iv);
//        log.info("email : "+email);

        // 검증후 삭제처리
        String redisEvCode = redisDao.getValues("EV: "+email);
//        log.info("redisEvCode : "+redisEvCode);

        if(redisEvCode != null) {
            if(redisEvCode.equals(kv)) {
//                redisDao.deleteValues("EV: " + email); // 데이터를 삭제해줘야할지 1일동안 놔둘지 고민임
                Optional<Admin> optionalAdmin = adminRepository.findByKnEmail(email);
                if (optionalAdmin.isEmpty()) {
                    log.error("존재하지 않은 유저");
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(),"해당 유저가 "+ResponseErrorCode.KO004.getDesc()));
                } else {
                    String pagetext = "";
                    if(adminCreateDto.getSendType().equals("1")) {
                        if(optionalAdmin.get().getKnIsEmailAuth().equals("Y")) {
                            log.error("이미 가입된 관리자 입니다.");
                            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO086.getCode(), ResponseErrorCode.KO086.getDesc()));
                        } else {
                            pagetext = "관리자 등록";
                        }
                    } else {
                        pagetext = "비밀번호 변경";
                    }

                    data.put("pagetext", pagetext);
                    data.put("userEmail", email);

                    // AWS 호출 카운팅
                    awsKmsHistoryService.awskmsHistoryCount(cpCode);

                    // 복호화 횟수 저장
                    decrypCountHistoryService.decrypCountHistorySave(cpCode, 1);

                    log.info("검증완료");
                }
            } else {
                log.error("이메일 인증이 맞지 않습니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO084.getCode(), ResponseErrorCode.KO084.getDesc()));
            }
        } else {
            if(adminCreateDto.getSendType().equals("1")) {
                log.error("관리자 등록 인증 - 만료된 페이지 입니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO085_1.getCode(), ResponseErrorCode.KO085_1.getDesc()));
            } else {
                log.error("비밀번호변경 인증 - 만료된 페이지 입니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO085_2.getCode(), ResponseErrorCode.KO085_2.getDesc()));
            }
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 관리자 등록 최종
    public ResponseEntity<Map<String, Object>> createUser(AuthRequestDto.KokonutCreateUser kokonutCreateUser, HttpServletRequest request, HttpServletResponse response) {
        log.info("createUser 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 본인인증 거치는 함수호출
        AuthPhoneCheckDto authPhoneCheckDto = Utils.authPhoneCheck(request);
        // 본인인증 체크
        if (!kokonutCreateUser.getKnPhoneNumber().equals(authPhoneCheckDto.getJoinPhone()) || !kokonutCreateUser.getKnName().equals(authPhoneCheckDto.getJoinName())) {
            log.error("본인인증된 명의 및 휴대전화번호가 아닙니다. 본인인증을 다시해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO033.getCode(), ResponseErrorCode.KO033.getDesc()));
        }else {
            // 인증 쿠키제거
            Utils.cookieDelete("joinName", response);
            Utils.cookieDelete("joinPhone", response);
        }

        // 비밀번호 일치한지 체크
        if (!kokonutCreateUser.getKnPassword().equals(kokonutCreateUser.getKnPasswordConfirm())) {
            log.error("입력하신 비밀번호가 서로 일치하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO013.getCode(), ResponseErrorCode.KO013.getDesc()));
        }

        log.info("관리자등록 시작");
        log.info("받아온 값 kokonutCreateUser : " + kokonutCreateUser);
        Optional<Admin> optionalAdmin = adminRepository.findByKnEmail(kokonutCreateUser.getUserEmail());
        if (optionalAdmin.isEmpty()) {
            log.error("존재하지 않은 유저");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(),"해당 유저가 "+ResponseErrorCode.KO004.getDesc()));
        } else {
            log.info("관리자 최종등록 완료");

            String knName = optionalAdmin.get().getKnName();
            String knLoginAuth = optionalAdmin.get().getKnIsLoginAuth();

            if(knLoginAuth.equals("N") && knName.equals("미가입")) {
                optionalAdmin.get().setKnName(kokonutCreateUser.getKnName());
                optionalAdmin.get().setKnPhoneNumber(kokonutCreateUser.getKnPhoneNumber());
                optionalAdmin.get().setKnPassword(passwordEncoder.encode(kokonutCreateUser.getKnPassword()));
                optionalAdmin.get().setKnIsEmailAuth("Y");
                optionalAdmin.get().setKnEmailAuthCode(null);
                optionalAdmin.get().setModify_email(kokonutCreateUser.getUserEmail());
                optionalAdmin.get().setModify_date(LocalDateTime.now());

                adminRepository.save(optionalAdmin.get());

                // 레디스 데이터 제거
                redisDao.deleteValues("EV: " + kokonutCreateUser.getUserEmail());
            }
            else {
                log.error("이미 인증된 관리자 입니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO117.getCode(), ResponseErrorCode.KO117.getDesc()));
            }
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 관리자 비밀번호 변경
    @Transactional
    public ResponseEntity<Map<String, Object>> passwordChange(AdminPwdChagneMailDto adminPwdChagneMailDto, HttpServletRequest request, HttpServletResponse response) {
        log.info("passwordChange 호출!");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 본인인증 거치는 함수호출
        AuthPhoneCheckDto authPhoneCheckDto = Utils.authPhoneCheck(request);

        // 비밀번호 일치한지 체크
        if (!adminPwdChagneMailDto.getKnPassword().equals(adminPwdChagneMailDto.getKnPasswordConfirm())) {
            log.error("입력하신 비밀번호가 서로 일치하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO013.getCode(), ResponseErrorCode.KO013.getDesc()));
        }

        log.info("관리자 비밀번호변경 시작");
        Optional<Admin> optionalAdmin = adminRepository.findByKnEmail(adminPwdChagneMailDto.getUserEmail());
        if (optionalAdmin.isEmpty()) {
            log.error("존재하지 않은 유저");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(),"해당 유저가 "+ResponseErrorCode.KO004.getDesc()));
        } else {

            String knName = optionalAdmin.get().getKnName();
            String knPhoneNumber = optionalAdmin.get().getKnPhoneNumber();

            // 본인인증 체크
            if (!knPhoneNumber.equals(authPhoneCheckDto.getJoinPhone()) || !knName.equals(authPhoneCheckDto.getJoinName())) {
                log.error("본인인증된 명의 및 휴대전화번호가 아닙니다. 본인인증을 다시해주세요.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO033.getCode(), ResponseErrorCode.KO033.getDesc()));
            }else {
                // 인증 쿠키제거
                Utils.cookieDelete("joinName", response);
                Utils.cookieDelete("joinPhone", response);
            }

            log.info("관리자 비밀번호변경 완료");

            optionalAdmin.get().setKnPassword(passwordEncoder.encode(adminPwdChagneMailDto.getKnPassword()));
            optionalAdmin.get().setKnPwdErrorCount(0);
            optionalAdmin.get().setModify_email(adminPwdChagneMailDto.getUserEmail());
            optionalAdmin.get().setModify_date(LocalDateTime.now());

            adminRepository.save(optionalAdmin.get());

            // 레디스 데이터 제거
            redisDao.deleteValues("EV: " + adminPwdChagneMailDto.getUserEmail());
        }

        return ResponseEntity.ok(res.success(data));
    }


}
