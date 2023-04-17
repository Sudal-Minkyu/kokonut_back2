package com.app.kokonut.auth;

import com.app.kokonut.admin.Admin;
import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.AdminService;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.admin.enums.AuthorityRole;
import com.app.kokonut.auth.dtos.AdminCreateDto;
import com.app.kokonut.auth.dtos.AdminGoogleOTPDto;
import com.app.kokonut.auth.dtos.AdminPasswordChangeDto;
import com.app.kokonut.auth.dtos.CompanyEncryptDto;
import com.app.kokonut.auth.jwt.been.JwtTokenProvider;
import com.app.kokonut.auth.jwt.dto.AuthRequestDto;
import com.app.kokonut.auth.jwt.dto.AuthResponseDto;
import com.app.kokonut.auth.jwt.dto.GoogleOtpGenerateDto;
import com.app.kokonut.auth.jwt.dto.RedisDao;
import com.app.kokonut.awsKmsHistory.AwsKmsHistory;
import com.app.kokonut.awsKmsHistory.AwsKmsHistoryRepository;
import com.app.kokonut.awsKmsHistory.dto.AwsKmsResultDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.component.*;
import com.app.kokonut.common.realcomponent.*;
import com.app.kokonut.company.company.Company;
import com.app.kokonut.company.company.CompanyRepository;
import com.app.kokonut.company.companyFile.CompanyFile;
import com.app.kokonut.company.companyFile.CompanyFileRepository;
import com.app.kokonut.company.companydatakey.CompanyDataKey;
import com.app.kokonut.company.companydatakey.CompanyDataKeyRepository;
import com.app.kokonut.company.companytable.CompanyTable;
import com.app.kokonut.company.companytable.CompanyTableRepository;
import com.app.kokonut.configs.GoogleOTP;
import com.app.kokonut.configs.KeyGenerateService;
import com.app.kokonut.configs.MailSender;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dto.ActivityCode;
import com.app.kokonut.keydata.KeyDataService;
import com.app.kokonutuser.KokonutUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
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

    @Value("${kokonut.aws.s3.businessS3Folder}")
    private String businessS3Folder;

    @Value("${kokonut.aws.s3.url}")
    private String AWSURL;

    private final AdminService adminService;
    private final HistoryService historyService;
    private final KokonutUserService kokonutUserService;

    private final AwsS3Util awsS3Util;
    private final AwsKmsUtil awsKmsUtil;

    private final KeyGenerateService keyGenerateService;
    private final AdminRepository adminRepository;

    private final CompanyRepository companyRepository;
    private final CompanyDataKeyRepository companyDataKeyRepository;
    private final CompanyTableRepository companyTableRepository;
    private final CompanyFileRepository companyFileRepository;

    private final AwsKmsHistoryRepository awsKmsHistoryRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final RedisDao redisDao;

    private final GoogleOTP googleOTP;

    private final MailSender mailSender;

    @Autowired
    public AuthService(AdminService adminService, HistoryService historyService,
                       KeyDataService keyDataService, KokonutUserService kokonutUserService, AwsS3Util awsS3Util, AdminRepository adminRepository,
                       AwsKmsUtil awsKmsUtil, KeyGenerateService keyGenerateService, CompanyRepository companyRepository,
                       CompanyDataKeyRepository companyDataKeyRepository, CompanyTableRepository companyTableRepository, CompanyFileRepository companyFileRepository,
                       AwsKmsHistoryRepository awsKmsHistoryRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider,
                       AuthenticationManagerBuilder authenticationManagerBuilder,
                       RedisDao redisDao, GoogleOTP googleOTP, MailSender mailSender) {
        this.adminService = adminService;
        this.historyService = historyService;
        this.kokonutUserService = kokonutUserService;
        this.awsS3Util = awsS3Util;
        this.adminRepository = adminRepository;
        this.awsKmsUtil = awsKmsUtil;
        this.keyGenerateService = keyGenerateService;
        this.companyRepository = companyRepository;
        this.companyDataKeyRepository = companyDataKeyRepository;
        this.companyTableRepository = companyTableRepository;
        this.companyFileRepository = companyFileRepository;
        this.awsKmsHistoryRepository = awsKmsHistoryRepository;
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

        data.put("result", adminRepository.existsByKnEmail(knEmail));
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
        String ctNumber = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        log.info("생성된 인증번호 : "+ctNumber);

        // 인증번호 메일전송
        // 이메일 전송을 위한 전처리 - filter, unfilter
        String title = ReqUtils.filter("코코넛 이메일 인증번호가 도착했습니다.");
        String contents = ReqUtils.unFilter("인증번호 : "+ctNumber);

        // 템플릿 호출을 위한 데이터 세팅
        HashMap<String, String> callTemplate = new HashMap<>();
        callTemplate.put("template", "MailTemplate");
        callTemplate.put("title", "인증번호 알림");
        callTemplate.put("content", contents);

        // 템플릿 TODO 템플릿 디자인 추가되면 수정
//        contents = mailSender.getHTML5(callTemplate);
        String reciverEmail = knEmail;
        String reciverName = "kokonut";

        boolean mailSenderResult = mailSender.sendMail(reciverEmail, reciverName, title, contents);
        if(mailSenderResult) {
            // mailSender 성공
            log.info("### 메일전송 성공했습니다. reciver Email : "+ knEmail);
        }else{
            // mailSender 실패
            log.error("### 해당 메일 전송에 실패했습니다. 관리자에게 문의하세요. reciverEmail : "+ knEmail);
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO041.getCode(), ResponseErrorCode.KO041.getDesc()));
        }

        // 인증번호 레디스에 담기
        redisDao.setValues("CT: " + knEmail, ctNumber, Duration.ofMillis(180000)); // 제한시간 3분
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

//        log.info("knEmail : "+knEmail);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 접속정보에서 필요한 정보 가져오기.
        Admin admin = adminRepository.findByKnEmail(knEmail)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다. : " + knEmail));

        String tempPassword = Utils.getRamdomStr(10);
//        log.info("임시비밀번호 : "+tempPassword);

        admin.setKnPassword(passwordEncoder.encode(tempPassword));
        adminRepository.save(admin);

        // 인증번호 메일전송
        // 이메일 전송을 위한 전처리 - filter, unfilter
        String title = ReqUtils.filter("코코넛 이메일 임시비밀번호가 도착했습니다.");
        String contents = ReqUtils.unFilter("임시비밀번호 : "+tempPassword);

        // 템플릿 호출을 위한 데이터 세팅
//        HashMap<String, String> callTemplate = new HashMap<>();
//        callTemplate.put("template", "MailTemplate");
//        callTemplate.put("title", "인증번호 알림");
//        callTemplate.put("content", contents);

        // 템플릿 TODO 템플릿 디자인 추가되면 수정
//        contents = mailSender.getHTML5(callTemplate);
        String reciverName = "kokonut";

        boolean mailSenderResult = mailSender.sendMail(knEmail, reciverName, title, contents);
        if(mailSenderResult){
            // mailSender 성공
            log.info("### 메일전송 성공했습니다. reciver Email : "+ knEmail);
        }else{
            // mailSender 실패
            log.error("### 해당 메일 전송에 실패했습니다. 관리자에게 문의하세요. reciverEmail : "+ knEmail);
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO041.getCode(), ResponseErrorCode.KO041.getDesc()));
        }

        data.put("tempPassword", tempPassword);

        return ResponseEntity.ok(res.success(data));
    }

    // 비밀번호 찾기(사용할 비밀번호로 변경하는) 기능
    @Transactional
    public ResponseEntity<Map<String, Object>> passwordUpdate(AdminPasswordChangeDto adminPasswordChangeDto) {
        log.info("passwordUpdate 호출");

//        log.info("adminPasswordChangeDto : "+adminPasswordChangeDto);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        Admin admin = adminRepository.findByKnEmail(adminPasswordChangeDto.getKnEmail())
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다. : " + adminPasswordChangeDto.getKnEmail()));

        // 임시비밀번호 일치한지 체크
        if (!passwordEncoder.matches(adminPasswordChangeDto.getTempPwd(), admin.getKnPassword())){
            log.error("보내드린 임시 비밀번호가 일치하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO078.getCode(), ResponseErrorCode.KO078.getDesc()));
        }

        // 비밀번호 일치한지 체크
        if (!adminPasswordChangeDto.getKnPassword().equals(adminPasswordChangeDto.getKnPasswordConfirm())) {
            log.error("입력하신 비밀번호가 서로 일치하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO013.getCode(), ResponseErrorCode.KO013.getDesc()));
        }

        admin.setKnPassword(passwordEncoder.encode(adminPasswordChangeDto.getKnPassword()));
        admin.setKnPwdChangeDate(LocalDateTime.now());
        admin.setModify_email(adminPasswordChangeDto.getKnEmail());
        admin.setModify_date(LocalDateTime.now());

        adminRepository.save(admin);

        return ResponseEntity.ok(res.success(data));
    }

    // 코코넛 회원가입 기능
    @Transactional
    public ResponseEntity<Map<String, Object>> kokonutSignUp(AuthRequestDto.KokonutSignUp kokonutSignUp, HttpServletRequest request) {
        log.info("kokonutSignUp 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

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

        log.info("회원가입 시작");
        log.info("받아온 값 kokonutSignUp : " + kokonutSignUp);

        if (!kokonutSignUp.getKnEmail().equals("kokonut@kokonut.me")) { // 테스트일 경우 패스
            String joinPhone = "";
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
//                log.info("현재 쿠키값들 : " + Arrays.toString(cookies));
                for (Cookie c : cookies) {
                    if (c.getName().equals("joinPhone")) {
                        joinPhone = c.getValue();
//                        log.info("본인인증 된 핸드폰번호 : " + joinPhone);

                    }
                }
            }
            // 본인인증 체크
            if (!kokonutSignUp.getKnPhoneNumber().equals(joinPhone)) {
                log.info("본인인증으로 입력된 핸드폰 번호가 아닙니다. 본인인증을 완료해주세요.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO033.getCode(), ResponseErrorCode.KO033.getDesc()));
            }
        }

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
        company.setInsert_email(kokonutSignUp.getKnEmail());
        company.setInsert_date(LocalDateTime.now());

        // 회사 암호화키(DataKey) 저장
        CompanyDataKey companyDataKey = new CompanyDataKey();
        companyDataKey.setCpCode(company.getCpCode());
        companyDataKey.setDataKey(dataKey);

        String ctName = cpCode+"_0001";

        // 회사 유저 기본테이블 저장
        CompanyTable companyTable = new CompanyTable();
        companyTable.setCpCode(cpCode);
        companyTable.setCtName(ctName);
        companyTable.setCtTableCount("0001");
        companyTable.setCtDesignation("기본");
        companyTable.setCtAddColumnCount(1);
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

        boolean result = kokonutUserService.createTableKokonutUser(ctName);
        log.warn("result : "+result);

        log.info("사업자 정보 저장 saveAdmin : "+saveAdmin.getAdminId());

        return ResponseEntity.ok(res.success(data));
    }

    // 회원가입 기능
    @Transactional
    public ResponseEntity<Map<String,Object>> signUp(AuthRequestDto.SignUp signUp, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("signUp 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        if (adminRepository.existsByKnEmail(signUp.getKnEmail())) {
            log.error("이미 회원가입된 이메일입니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO005.getCode(), ResponseErrorCode.KO005.getDesc()));
        }

        log.info("회원가입 시작");
        log.info("받아온 값 signUp : "+signUp);

//        String fileGroupId = "FATT_" + Calendar.getInstance().getTimeInMillis();
//        String fileGroupId = "FATT_" + UUID.randomUUID();  // 파일그룹ID? 바뀔수도..
        String knPhoneNumber = signUp.getKnPhoneNumber(); // 휴대폰번호
        String knEmailAuthNumber = CommonUtil.makeRandomChar(15); // 이메일인증 코드
        String businessNumber = signUp.getCpBusinessNumber(); // 사업자등록번호

        /* NICEID를 통해 휴대폰 인증을 완료했는 지 확인 */
        if(!signUp.getKnEmail().equals("kokonut@kokonut.me")) { // 테스트일 경우 패스
            String mobileno = "";
            Cookie[] cookies = request.getCookies();
            if(cookies != null) {
                log.info("현재 쿠키값들 : "+ Arrays.toString(cookies));
                for(Cookie c : cookies) {
                    if(c.getName().equals("mobileno") ) {
                        mobileno = c.getValue();
                        log.info("본인인증 된 핸드폰번호 : "+mobileno);
                    }
                }
            }

            // 본인인증 체크
            if(!knPhoneNumber.equals(mobileno)) {
                log.info("본인인증으로 입력된 핸드폰 번호가 아닙니다. 본인인증을 완료해주세요.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO033.getCode(), ResponseErrorCode.KO033.getDesc()));
            }
        }

        // 중복체크 로직
        // 핸드폰번호 중복 체크
        if (adminRepository.existsByKnPhoneNumber(knPhoneNumber)) {
            log.error("이미 회원가입된 핸드폰번호 입니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO034.getCode(), ResponseErrorCode.KO034.getDesc()));
        }

//        // 사업자번호 중복 체크
//        if (companyRepository.existsByCpBusinessNumber(businessNumber)) {
//            log.error("이미 회원가입된 사업자등록번호 입니다.");
//            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO035.getCode(), ResponseErrorCode.KO035.getDesc()));
//        } else if(businessNumber.length() != 10){
//            log.error("입력하신 사업자등록번호는 10자리가 아닙니다.");
//            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO035.getCode(), ResponseErrorCode.KO035.getDesc()));
//        }

        // 비밀번호 일치한지 체크
        if (!signUp.getKnPassword().equals(signUp.getKnPasswordConfirm())) {
            log.error("입력하신 비밀번호가 서로 일치하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO013.getCode(), ResponseErrorCode.KO013.getDesc()));
        }

        MultipartFile multipartFile = signUp.getMultipartFile();
        // 사업자등록증을 올렸는지 체크
        if(multipartFile == null && !signUp.getKnEmail().equals("kokonut@kokonut.me")) { // 테스트일 경우 패스
            log.error("사업자등록증을 업로드해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO038.getCode(), ResponseErrorCode.KO038.getDesc()));
        }

        // 이메일 인증번호 생성
        while(true) {
            // 해당 knEmailAuthNumber로 가입된 회원이 존재한다면 재생성
            if(!adminRepository.existsByKnEmailAuthNumber(knEmailAuthNumber)) {
                break;
            } else {
                knEmailAuthNumber = CommonUtil.makeRandomChar(15);
            }
        }


        log.info("Company 저장(Insert) 로직 시작");
        Company company = new Company();

        // 저장할 KMS 암호화키, 복호화키 생성
//        String encryptText = "";
        String dataKey = "";
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
//        String currDate = formatter.format(new Date());
//        String encKey = currDate + businessNumber; // -> 기존의 encKey 생성은 가입한 날짜시간+사업자등록번호
//        log.info("encKey : "+encKey);
        // -> woody 수정 가입날짜 년도+월+사업자등록번호 ex) 가입날짜 년월 : 202301, 사업자등록번호(10자리) 1234567890 -> encKey = 2023011234567890 으로 수정
        // -> 이유는 개인정보등록시 암호화가 필요한 필드일 경우 해당 key를 통해 암호화처리를 할때 Aes암호화를 해야하는데 encKey가 16byte가 넘어가게 되면 예외처리를 하여 16byte로 맞춤.
        // 사업자 등록번호는 10자리를 넘어가면 안되고, 10자리 미만이여도 안되는 조건 추가한다.

//        20230112 3488101536
//        if(!signUp.getKnEmail().equals("kokonut@kokonut.me")) { // 테스트일 경우 패스
            AwsKmsResultDto awsKmsResultDto = awsKmsUtil.dataKeyEncrypt();
            if (awsKmsResultDto.getResult().equals("success")) {
//                encryptText = awsKmsResultDto.getEncryptText();
                dataKey = awsKmsResultDto.getDataKey();
            } else {
                // 리턴처리를 어떻게해야 할지 내용 정하기 - 2022/12/22 to.woody
                log.error("암호화 키 생성 실패");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO036.getCode(), ResponseErrorCode.KO036.getDesc()));
            }
//        }

        String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        company.setCpCode(keyGenerateService.keyGenerate("kn_company", "kokonut"+nowDate, "KokonutSystem"));
        company.setCpName(signUp.getCpName());
//        company.setCpRepresentative(signUp.getCpRepresentative());
//        company.setCpTel(signUp.getCpTel());
//        company.setCpBusinessType(signUp.getCpBusinessType());
//        company.setCpBusinessNumber(businessNumber);
//        company.setCpAddress(signUp.getCpAddress());
//        company.setCpAddressNumber(signUp.getCpAddressNumber());
//        company.setCpAddressDetail(signUp.getCpAddressDetail());
//        company.setCpDataKey(dataKey);
        company.setInsert_email(signUp.getKnEmail());
        company.setInsert_date(LocalDateTime.now());
        Company saveCompany = companyRepository.save(company);
        log.info("기업 정보 저장 saveCompany : "+saveCompany.getCompanyId());

        log.info("KMS 발급 이력 저장(Insert) 로직 시작");
        AwsKmsHistory awsKmsHistory = new AwsKmsHistory();
        awsKmsHistory.setAkhType("ENC");
        awsKmsHistory.setAkhRegdate(LocalDateTime.now());
        AwsKmsHistory saveAwsKmsHistory =  awsKmsHistoryRepository.save(awsKmsHistory);
        log.info("KMS 이력 저장 saveAwsKmsHistory : "+saveAwsKmsHistory.getAkhIdx());

        log.info("Admin 저장(Insert) 로직 시작");
        // 회원가입 사업자 Defalut 값
        Integer userType = 1; // "1"일 경우 사업자, "2"일 경우 관리자
        Integer state = 1; // 0:정지(권한해제), 1:사용, 2:로그인제한(비번5회오류), 3:탈퇴, 4:휴면계정

        Admin admin = new Admin();
        admin.setKnEmail(signUp.getKnEmail());
        admin.setKnPassword(passwordEncoder.encode(signUp.getKnPassword()));
        admin.setCompanyId(saveCompany.getCompanyId());
        admin.setMasterId(0L); // 사업자는 0, 관리자가 등록한건 관리자IDX ?
        admin.setKnName(signUp.getKnName());
        admin.setKnPhoneNumber(signUp.getKnPhoneNumber());
        admin.setKnState(state);
        admin.setKnUserType(userType);
        admin.setKnRoleCode(AuthorityRole.ROLE_MASTER);
        admin.setInsert_email(signUp.getKnEmail());
        admin.setInsert_date(LocalDateTime.now());
        Admin saveAdmin = adminRepository.save(admin);
        log.info("사용자 정보 저장 saveAdmin : "+saveAdmin.getAdminId());


        if(multipartFile != null && !signUp.getKnEmail().equals("kokonut@kokonut.me")) {
            log.info("사업자등록증 업로드 시작 multipartFile : "+multipartFile);
            log.info("CompanyFile 저장(Insert) 로직 시작");

            CompanyFile companyFile = new CompanyFile();
            companyFile.setCpCode(saveCompany.getCpCode());

            // 파일 오리지널 Name
            String originalFilename = Normalizer.normalize(Objects.requireNonNull(multipartFile.getOriginalFilename()), Normalizer.Form.NFC);
            log.info("originalFilename : "+originalFilename);
            companyFile.setCfOriginalFilename(originalFilename);

            // 파일 Size
            long fileSize = multipartFile.getSize();
            log.info("fileSize : "+fileSize);
            companyFile.setCfVolume(fileSize);

            // 확장자
            String ext;
            ext = '.'+originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            log.info("ext : "+ext);

            // 파일 중복명 처리
            String fileName = UUID.randomUUID().toString().replace("-", "")+ext;
            log.info("fileName : "+fileName);
            companyFile.setCfFilename(fileName);

            // S3에 저장 할 파일주소
            SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
            String filePath = AWSURL+businessS3Folder+date.format(new Date());
            log.info("filePath : "+filePath);
            companyFile.setCfPath(filePath);

            companyFile.setInsert_email(saveAdmin.getKnEmail());
            companyFile.setInsert_date(LocalDateTime.now());

            // S3 파일업로드
            String storedFileName = awsS3Util.imageFileUpload(multipartFile, fileName, businessS3Folder+date.format(new Date()));
            if(storedFileName == null) {
                log.error("이미지 업로드를 실패했습니다. -관리자에게 문의해주세요-");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO039.getCode(), ResponseErrorCode.KO039.getDesc()));
            } else {
                companyFileRepository.save(companyFile);
            }
        }

        // 메일보내는 로직
        // 메일 서비스 완료되면 할 것 - 2022/12/23 to.woody
//        List<HashMap<String, Object>> systemAdminList = adminService.SelectSystemAdminList();
        List<String> systemAdminList = new ArrayList<>();
//            try {
//                String mailData = URLEncoder.encode(parameter.get("knEmail").toString(), "UTF-8");
//                String title = "사업자 회원가입 승인요청";
//                String contents = mailSender.getHTML2("/mail/knEmailForm/" + Integer.toString(MailController.knEmailFormType.REQ_AGREE_BIZ_MEM_JOIN.ordinal()) + "?data=" + mailData);
//
//                for(HashMap<String, Object> systemAdmin : systemAdminList) {
//                    if(systemAdmin.containsKey("knEmail") && systemAdmin.containsKey("NAME")) {
//                        String toknEmail = systemAdmin.get("knEmail").toString();
//                        String toName = systemAdmin.get("NAME").toString();
//
//                        mailSender.sendMail(toknEmail, toName, title, contents);
//                    } else {
//                        logger.error("not found system admin info.");
//                    }
//                }
//            } catch (IOException e) {
//                logger.error(e.getMessage());
//            }

        /* NICEID 본인인증 핸드폰 번호 쿠키 삭제 */
        if(!signUp.getKnEmail().equals("kokonut@kokonut.me")) { // 테스트일 경우 패스
            Cookie cookie = new Cookie("mobileno", null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        data.put("knEmail", saveAdmin.getKnEmail());

        return ResponseEntity.ok(res.success(data));
    }

    // 로그인, 구글OTP 확인후 -> JWT 발급기능
    public ResponseEntity<Map<String,Object>> authToken(AuthRequestDto.Login login, HttpServletResponse response) {
        log.info("authToken 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();
        
        // 이메일이 test@kokonut.me 체크하는 로직추가
        String knEmail = login.getKnEmail();

        if (knEmail.equals("test@kokonut.me")) {
            // 여기부터 우디(Wooody)가 작성
            System.out.println("체험하기 모드 : 체험하기 로그인");

            data.put("jwtToken", "체험하기JWT");
            return ResponseEntity.ok(res.success(data));
        }
        else {
            if(login.getOtpValue() == null || login.getOtpValue().equals("")) {
                log.error("구글 OTP 값이 존재하지 않습니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO010.getCode(),ResponseErrorCode.KO010.getDesc()));
            }

            Optional<Admin> optionalAdmin = adminRepository.findByKnEmail(knEmail);

            if (optionalAdmin.isEmpty()) {
                log.error("아이디 또는 비밀번호가 일치하지 않습니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO016.getCode(),ResponseErrorCode.KO016.getDesc()));
            }
            else {
                try {
                    // Login ID/PW 를 기반으로 Authentication 객체 생성 -> 아아디 / 비번 검증
                    // 이때 authentication은 인증 여부를 확인하는 authenticated 값이 false
                    UsernamePasswordAuthenticationToken authenticationToken = login.toAuthentication();

                    if(optionalAdmin.get().getKnOtpKey() == null){
                        log.error("등록된 OTP가 존재하지 않습니다. 구글 OTP 인증을 등록해주세요.");
                        return ResponseEntity.ok(res.fail(ResponseErrorCode.KO011.getCode(),ResponseErrorCode.KO011.getDesc()));
                    }
                    else {
                        // 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
                        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
                        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

                        // OTP 검증 절차
                        boolean auth = googleOTP.checkCode(login.getOtpValue(), optionalAdmin.get().getKnOtpKey());
                        log.info("auth : " + auth);

                        if (!auth) {
                            log.error("입력된 구글 OTP 값이 일치하지 않습니다. 확인해주세요.");
                            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO012.getCode(), ResponseErrorCode.KO012.getDesc()));
                        } else {
                            log.info("OTP인증완료 -> JWT토큰 발급");

                            AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(knEmail);
                            Long adminId = adminCompanyInfoDto.getAdminId();
                            String companyCode = adminCompanyInfoDto.getCompanyCode();

                            // 로그인 코드
                            ActivityCode activityCode = ActivityCode.AC_01;
                            String ip = CommonUtil.clientIp();

                            // 활동이력 저장 -> 비정상 모드
                            Long activityHistoryId = historyService.insertHistory(2, adminId, activityCode,
                                    companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, 0, knEmail);

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
                            if(optionalAdmin.get().getKnPwdErrorCount() != 0) {
                                optionalAdmin.get().setKnPwdErrorCount(0);
                                adminRepository.save(optionalAdmin.get());
                            }

                            /* 해외 아이피 차단 여부 */
//                            loginService.ResetPwdError(user.getIdx()); 패스워드에러카운트 리셋

                            historyService.updateHistory(activityHistoryId,
                                    companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", 1);

                            return ResponseEntity.ok(res.success(data));
                        }
                    }
                }
                catch (BadCredentialsException e) {
                    log.error("아이디 또는 비밀번호가 일치하지 않습니다.");
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
        if (jwtTokenProvider.validateToken(accessToken) == 400) {
            log.error("유효하지 않은 토큰정보임");
//            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO006.getCode(),ResponseErrorCode.KO006.getDesc()));
        } else {
            // Access Token 에서 User knEmail 을 가져옵니다.
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

            // Redis 에서 해당 User knEmail 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
            if (redisDao.getValues("RT: "+authentication.getName()) != null) {
                // Refresh Token 삭제
                redisDao.deleteValues("RT: "+authentication.getName());
            }

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
                log.error("입력된 구글 OTP 값이 일치하지 않습니다. 확인해주세요.");
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
    public ResponseEntity<Map<String, Object>> saveOTP(AdminGoogleOTPDto.GoogleOtpSave googleOtpSave) {
        log.info("saveOTP 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();
        
        Optional<Admin> optionalAdmin = adminRepository.findByKnEmail(googleOtpSave.getKnEmail());

        if (optionalAdmin.isEmpty()) {
            log.error("해당 유저가 존재하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(),"해당 유저가 "+ResponseErrorCode.KO004.getDesc()));
        }
        else {

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
    public ResponseEntity<Map<String, Object>> createCheck(AdminCreateDto adminCreateDto) throws Exception {
        log.info("createCheck 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

//        log.info("adminCreateDto.getEvKoData() : "+adminCreateDto.getEvKoData());
//        log.info("adminCreateDto.getKvKoData() : "+adminCreateDto.getKvKoData());
//        log.info("adminCreateDto.getIvKoData() : "+adminCreateDto.getIvKoData());

        long ev = Long.parseLong(adminCreateDto.getEvKoData());
        String kv = adminCreateDto.getKvKoData();
        String iv = adminCreateDto.getIvKoData();

        CompanyEncryptDto companyEncryptDto = companyRepository.findByDataKey(ev);
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
                    if(optionalAdmin.get().getKnIsEmailAuth().equals("Y")) {
                        log.error("이미 가입된 관리자 입니다.");
                        return ResponseEntity.ok(res.fail(ResponseErrorCode.KO086.getCode(), ResponseErrorCode.KO086.getDesc()));
                    } else {
                        data.put("userEmail", email);
                        log.info("검증완료");
                    }
                }
            } else {
                log.error("이메일 인증이 맞지 않습니다.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO084.getCode(), ResponseErrorCode.KO084.getDesc()));
            }
        } else {
            log.error("만료된 페이지 입니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO085.getCode(), ResponseErrorCode.KO085.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 관리자 등록 최종
    public ResponseEntity<Map<String, Object>> createUser(AuthRequestDto.KokonutCreateUser kokonutCreateUser, HttpServletRequest request) {
        log.info("createUser 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 비밀번호 일치한지 체크
        if (!kokonutCreateUser.getKnPassword().equals(kokonutCreateUser.getKnPasswordConfirm())) {
            log.error("입력하신 비밀번호가 서로 일치하지 않습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO013.getCode(), ResponseErrorCode.KO013.getDesc()));
        }

        log.info("관리자등록 시작");
        log.info("받아온 값 kokonutCreateUser : " + kokonutCreateUser);

        if (!kokonutCreateUser.getUserEmail().equals("kokonut@kokonut.me")) { // 테스트일 경우 패스
            String joinPhone = "";
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
//                log.info("현재 쿠키값들 : " + Arrays.toString(cookies));
                for (Cookie c : cookies) {
                    if (c.getName().equals("joinPhone")) {
                        joinPhone = c.getValue();
//                        log.info("본인인증 된 핸드폰번호 : " + joinPhone);
                    }
                }
            }
            // 본인인증 체크
            if (!kokonutCreateUser.getKnPhoneNumber().equals(joinPhone)) {
                log.info("본인인증으로 입력된 핸드폰 번호가 아닙니다. 본인인증을 완료해주세요.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO033.getCode(), ResponseErrorCode.KO033.getDesc()));
            }
        }

        Optional<Admin> optionalAdmin = adminRepository.findByKnEmail(kokonutCreateUser.getUserEmail());
        if (optionalAdmin.isEmpty()) {
            log.error("존재하지 않은 유저");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(),"해당 유저가 "+ResponseErrorCode.KO004.getDesc()));
        } else {
            log.info("관리자 최종등록 완료");

            optionalAdmin.get().setKnName(kokonutCreateUser.getKnName());
            optionalAdmin.get().setKnPhoneNumber(kokonutCreateUser.getKnPhoneNumber());
            optionalAdmin.get().setKnPassword(passwordEncoder.encode(kokonutCreateUser.getKnPassword()));
            optionalAdmin.get().setKnIsEmailAuth("Y");
            optionalAdmin.get().setKnEmailAuthCode(null);
            optionalAdmin.get().setModify_email(kokonutCreateUser.getUserEmail());
            optionalAdmin.get().setInsert_date(LocalDateTime.now());

            adminRepository.save(optionalAdmin.get());
        }

        return ResponseEntity.ok(res.success(data));
    }

}
