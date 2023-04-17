package com.app.kokonut.admin;

import com.app.kokonut.admin.dtos.*;
import com.app.kokonut.admin.enums.AuthorityRole;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.auth.jwt.dto.RedisDao;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.realcomponent.AESGCMcrypto;
import com.app.kokonut.common.realcomponent.AwsKmsUtil;
import com.app.kokonut.common.realcomponent.CommonUtil;
import com.app.kokonut.common.realcomponent.Utils;
import com.app.kokonut.company.company.Company;
import com.app.kokonut.company.company.CompanyRepository;
import com.app.kokonut.company.companydatakey.CompanyDataKeyService;
import com.app.kokonut.configs.MailSender;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dto.ActivityCode;
import com.app.kokonut.history.dto.HistoryLoginInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
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

    private final AwsKmsUtil awsKmsUtil;

    private final AdminRepository adminRepository;
    private final CompanyRepository companyRepository;
    private final CompanyDataKeyService companyDataKeyService;
    private final HistoryService historyService;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;

    private final RedisDao redisDao;

    @Autowired
    public AdminService(AwsKmsUtil awsKmsUtil, AdminRepository adminRepository, CompanyRepository companyRepository, CompanyDataKeyService companyDataKeyService,
                        HistoryService historyService, PasswordEncoder passwordEncoder, MailSender mailSender, RedisDao redisDao) {
        this.awsKmsUtil = awsKmsUtil;
        this.adminRepository = adminRepository;
        this.companyRepository = companyRepository;
        this.companyDataKeyService = companyDataKeyService;
        this.historyService = historyService;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
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
        log.info("phoneChange 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
        Long adminId = adminCompanyInfoDto.getAdminId();
//        Long companyId = adminCompanyInfoDto.getCompanyId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();

        // 휴대전화변경 코드
        ActivityCode activityCode = ActivityCode.AC_35;
        // 활동이력 저장 -> 비정상 모드
        String ip = CommonUtil.clientIp();

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
//        Long companyId = adminCompanyInfoDto.getCompanyId();
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

        String ip = CommonUtil.clientIp();

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
//        Long companyId = adminCompanyInfoDto.getCompanyId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();

        // 활동 코드
        ActivityCode activityCode = ActivityCode.AC_38;
        String ip = CommonUtil.clientIp();

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
                    companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, 0, jwtFilterDto.getEmail());

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

        log.info("jwtFilterDto : "+jwtFilterDto);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();
        if(email.equals("anonymousUser")){
            log.error("사용하실 수 없는 토큰정보 입니다. 다시 로그인 해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO009.getCode(),ResponseErrorCode.KO009.getDesc()));
        } else{
            log.info("해당 유저의 이메일 : "+email);

            AdminInfoDto adminInfoDto = adminRepository.findByAdminInfo(email);

            log.info("해당 유저의 권한 : "+jwtFilterDto.getRole().getDesc());
            data.put("knName",adminInfoDto.getKnName());
            data.put("cpName",adminInfoDto.getCpName());
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

        String roleCode;
        if(filterRole.equals("대표관리자")) {
            roleCode = "ROLE_MASTER";
        } else if(filterRole.equals("최고관리자")) {
            roleCode = "ROLE_ADMIN";
        } else if(filterRole.equals("일반관리자")) {
            roleCode = "ROLE_USER";
        } else if(filterRole.equals("임시관리자")) {
            roleCode = "ROLE_GUEST";
        } else {
            roleCode = "";
        }

        Integer knState;
        if(filterState.equals("정지")) {
            knState = 0;
        } else if(filterState.equals("정상")) {
            knState = 1;
        } else if(filterState.equals("로그인제한")) {
            knState = 2;
        } else if(filterState.equals("탈퇴")) {
            knState = 3;
        } else if(filterState.equals("휴면")) {
            knState = 4;
        } else {
            knState = null;
        }

        List<AdminListDto> adminListDtoList = new ArrayList<>();
        AdminListDto adminListDto;

        Page<AdminListSubDto> adminListDtos = adminRepository.findByAdminList(searchText, roleCode, knState, companyId, email, pageable);
        if(adminListDtos.getTotalPages() == 0) {
            log.info("조회된 데이터가 없습니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO003.getCode(), ResponseErrorCode.KO003.getDesc()));
        } else {
            HistoryLoginInfoDto historyLoginInfoDto;
//            log.info("adminListDtos.getSize() : "+adminListDtos.getSize());

            for(int i=0; i<adminListDtos.getNumberOfElements(); i++) {
                adminListDto = new AdminListDto();

                historyLoginInfoDto = historyService.findByLoginHistory(adminListDtos.getContent().get(i).getKnEmail());
//                log.info("historyLoginInfoDto :"+historyLoginInfoDto);

                if(historyLoginInfoDto != null) {
                    adminListDto.setAh_Insert_date(historyLoginInfoDto.getAh_Insert_date());
                    adminListDto.setAhIpAddr(historyLoginInfoDto.getAhIpAddr());
                }

                adminListDto.setKnName(adminListDtos.getContent().get(i).getKnName());
                adminListDto.setKnEmail(adminListDtos.getContent().get(i).getKnEmail());
                adminListDto.setKnState(adminListDtos.getContent().get(i).getKnState());
                adminListDto.setKnRoleDesc(adminListDtos.getContent().get(i).getKnRoleDesc());
                adminListDto.setKnRoleCode(adminListDtos.getContent().get(i).getKnRoleCode());
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
        if(choseRole.equals("대표관리자")) {
            roleCode = "ROLE_MASTER";
        } else if(choseRole.equals("최고관리자")) {
            roleCode = "ROLE_ADMIN";
        } else if(choseRole.equals("일반관리자")) {
            roleCode = "ROLE_USER";
        } else if(choseRole.equals("임시관리자")) {
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
        String ip = CommonUtil.clientIp();

        if(jwtFilterDto.getRole().getCode().equals("ROLE_SYSTEM") || jwtFilterDto.getRole().getCode().equals("ROLE_MASTER")) {
            log.info("관리자 등록 시작");

            // 관리자추가 저장 -> 비정상 모드
            Long activityHistoryId = historyService.insertHistory(2, adminId, activityCode,
                    companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, 0, jwtFilterDto.getEmail());

            SecretKey secretKey = companyDataKeyService.findByCompanyDataKey(companyCode);
            byte[] ivBytes = AESGCMcrypto.generateIV();

//            String title = ReqUtils.filter("관리자 등록 알림1");
//            String contents = ReqUtils.unFilter("" +
//                    "관리자등록 요청되었습니다. <br>" +
//                    "해당 링크를 통해 가입을 이어서 해주시길 바랍니다.<br>" +
//                    "링크 : "+frontServerDomainIp+"/#/join");
//
//            // 템플릿 호출을 위한 데이터 세팅
//            HashMap<String, String> callTemplate = new HashMap<>();
//            callTemplate.put("template", "MailTemplate");
//            callTemplate.put("title", "관리자 등록 알림2");
//            callTemplate.put("content", contents);
//
//            // 템플릿 TODO 템플릿 디자인 추가되면 수정
//            contents = mailSender.getHTML5(callTemplate);
//            String reciverName = "kokonut";


            // 이메일인증코드
            // -> 레디스서버에 24시간동안 보관
            String knEmailAuthCode = AESGCMcrypto.encrypt(userEmail.getBytes(StandardCharsets.UTF_8), secretKey, ivBytes);

            // 관리자 등록메일 보내기
            String title = "관리자 등록 알림";
            // TODO : 답변 내용을 HTML 태그를 붙여서 메일로 전송해준다. 화면단과 개발할 때 추가 개발해야함.
            String contents = "관리자등록 요청되었습니다. <br>해당 링크를 통해 가입을 이어서 해주시길 바랍니다.<br>링크 : "+
            "<a href=\""+frontServerDomainIp+"/#/create?" +
                    "evKo="+ companyId +"&" +
                    "ivKo="+Base64.getEncoder().encodeToString(ivBytes) +"&" +
                    "kvKo="+knEmailAuthCode+"\" target=\"_blank\">"+
                    "이어서 가입하기"+
                    "</a>";
            log.info("toEmail" + userEmail + ", toName" + "코코넛");

            boolean mailSenderResult = mailSender.sendMail(userEmail, "", title, contents);
            if(mailSenderResult) {
                log.info("### 메일전송 성공했습니다. reciver Email : "+ userEmail);

                // 인증번호 레디스에 담기
                redisDao.setValues("EV: " + userEmail, knEmailAuthCode, Duration.ofMillis(1000*60*60*24)); // 제한시간 3분

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
