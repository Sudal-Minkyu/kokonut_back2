package com.app.kokonut.admin;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * @author Woody
 * Date : 2022-12-03
 * Time :
 * Remark :
 */
@Slf4j
@RequestMapping("/v2/api/Admin")
@RestController
public class AdminRestController {

    private final AdminService adminService;

    @Autowired
    public AdminRestController(AdminService adminService){
        this.adminService=adminService;
    }

    @GetMapping("/myInfo")
    @ApiOperation(value = "마이페이지 데이터 가져오기" , notes = "1. 유저가 내정보페이지를 들어간다." +
            "2. 해당 유저의 정보를 프론트로 보내준다.")
    @ApiImplicitParam(name ="Authorization", value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> myInfo() {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return adminService.myInfo(jwtFilterDto);
    }

    @PostMapping("/phoneChange")
    @ApiOperation(value = "휴대전화번호 변경" , notes = "1. 변경할 핸드폰번호로 인증한다." +
            "2. 인증을 성공하면 인증된번호로 변경한다.")
    @ApiImplicitParam(name ="Authorization", value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> phoneChange(@RequestParam(value="knName", defaultValue = "") String knName,
                                                          @RequestParam(value="knPhoneNumber", defaultValue = "") String knPhoneNumber) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return adminService.phoneChange(knName, knPhoneNumber, jwtFilterDto);
    }

    @PostMapping("/cpChange")
    @ApiOperation(value = "소속명 변경 + 부서 변경/등록" , notes = "1. 소속 및 부서를 변경하거나 등록할때 사용됨" +
            "2. 변경할 내용과 비밀번호를 받는다." +
            "3. state 값을 비교하여 구분한다. 1 -> 소속명변경, 2 -> 부서 변경및등록")
    @ApiImplicitParam(name ="Authorization", value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> cpChange(@RequestParam(value="cpContent", defaultValue = "") String cpContent,
                                                          @RequestParam(value="knPassword", defaultValue = "") String knPassword,
                                                          @RequestParam(value="state", defaultValue = "") Integer state) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return adminService.cpChange(cpContent, knPassword, state, jwtFilterDto);
    }

    @PostMapping("/pwdChange")
    @ApiOperation(value = "비밀번호 변경" , notes = "1. 현재비밀번호와 변경할 비밀번호를 받는다." +
            "2. 현재비밀번호를 검증한다." +
            "3. 변경할 비밀번호와 비밀번호확인 값과 비교한다." +
            "4. 모든 조건이 충족되면 비밀번호를 변경한다.")
    @ApiImplicitParam(name ="Authorization", value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> pwdChange(@RequestParam(value="oldknPassword", defaultValue = "") String oldknPassword,
                                                       @RequestParam(value="newknPassword", defaultValue = "") String newknPassword,
                                                       @RequestParam(value="newknPasswordCheck", defaultValue = "") String newknPasswordCheck) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return adminService.pwdChange(oldknPassword, newknPassword, newknPasswordCheck, jwtFilterDto);
    }

    @PostMapping("/deactivateAdmin")
    @ApiOperation(value = "본인 관리자 계정 비활성화" , notes = "1. otp입력 값을 받는다." +
            "2. otp 값을 검증한다." +
            "3. 관리자 본인의 계정을 비활성화 한다.")
    @ApiImplicitParam(name ="Authorization", value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> deactivateAdmin(@RequestParam(value="otpValue", defaultValue = "") String otpValue) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return adminService.deactivateAdmin(otpValue, jwtFilterDto);
    }

    @PostMapping("/updateAdminData")
    @ApiOperation(value = "관리자 정보 업데이트" , notes = "1. 변경 가능한 관리자 정보를 받는다." +
            "2. 요청자가 변경할 수 있는 관리자 대상인지 확인한다." +
            "3. 각 정보가 변경가능한 정보인지 검증한다." +
            "4. 모든 조건이 충족되면 관리자 정보를 변경한다.")
    @ApiImplicitParam(name ="Authorization", value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> updateAdminData(@RequestParam(value="knEmail") String knEmail,
                                                              @RequestParam(value="knIsEmailAuth") String knIsEmailAuth,
                                                              @RequestParam(value="knRoleCode") String knRoleCode,
                                                              @RequestParam(value="knActiveStatus") String knActiveStatus,
                                                              @RequestParam(value="otpValue", defaultValue = "") String otpValue) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return adminService.updateAdminData(knEmail, knRoleCode, knActiveStatus, otpValue, jwtFilterDto);
    }

    @GetMapping("/list")
    @ApiOperation(value = "관리자 목록 리스트 호출" , notes = "검색할 문자와 관리자등급 계정상태의 대한 필터로 목록을 조회한다.")
    @ApiImplicitParam(name ="Authorization", value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> list(@RequestParam(value="searchText", defaultValue = "") String searchText,
                                                   @RequestParam(value="filterRole", defaultValue = "") String filterRole,
                                                   @RequestParam(value="filterState", defaultValue = "") String filterState,
                                                   @PageableDefault Pageable pageable) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return adminService.list(searchText, filterRole, filterState, jwtFilterDto, pageable);
    }

    @PostMapping("/create")
    @ApiOperation(value = "관리자 등록(24시간후 만료)" , notes = "1. 대표관리자, 최고관리자만 할 수 있는 권한" +
            "2. 이메일중복체크후 해당 관리자의 권한을 선택후 등록을 누른다." +
            "3. 입력한 이메일로 인증메일을 전송한다." +
            "4. 해당메일의 링크를 통해 사용할 비밀번호를 입력하여 비밀번호를 등록한다." +
            "5. 최종적으로 비밀번호가 변경되면, 로그인을 할 수 있다.")
    @ApiImplicitParam(name ="Authorization", value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> create(@RequestParam(value="userEmail", defaultValue = "") String userEmail,
                                                     @RequestParam(value="choseRole", defaultValue = "") String choseRole) throws Exception {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return adminService.create(userEmail, choseRole, jwtFilterDto);
    }

    @PostMapping("/createMailAgain")
    @ApiOperation(value = "관리자등록 인증메일 재전송(24시간후 만료)")
    @ApiImplicitParam(name ="Authorization", value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> createMailAgain(@RequestParam(value="userEmail", defaultValue = "") String userEmail) throws Exception {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return adminService.createMailAgain(userEmail, jwtFilterDto);
    }

    @PostMapping("/passwordChangeMail")
    @ApiOperation(value = "비밀번호변경 메일전송(24시간후 만료)")
    @ApiImplicitParam(name ="Authorization", value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> passwordChangeMail(@RequestParam(value="userEmail", defaultValue = "") String userEmail) throws Exception {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return adminService.passwordChangeMail(userEmail, jwtFilterDto);
    }

    @GetMapping("/authorityCheck")
    @ApiOperation(value = "JWT토큰 호출 기본정보 호출" , notes = "1. 로그인한 유저의 기본정보를 가져오는 API")
    @ApiImplicitParam(name ="Authorization", value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> authorityCheck() {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return adminService.authorityCheck(jwtFilterDto);
    }

    // 시스템관리자 호출
    @GetMapping("/systemTest")
    @ApiImplicitParam(name ="authorization", value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> systemTest() {
        log.info("ROLE_SYSTEM TEST");
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return adminService.authorityCheck(jwtFilterDto);
    }

    // 대표관리자 호출
    @GetMapping("/masterTest")
    @ApiImplicitParam(name ="authorization", value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> masterTest() {
        log.info("ROLE_MASTER TEST");
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return adminService.authorityCheck(jwtFilterDto);
    }

    // 최고관리자 호출
    @GetMapping("/adminTest")
    @ApiImplicitParam(name ="authorization", value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> adminTest() {
        log.info("ROLE_ADMIN TEST");
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return adminService.authorityCheck(jwtFilterDto);
    }

    // 일반관리자 호출
    @GetMapping("/userTest")
    @ApiImplicitParam(name ="authorization", value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> userTest() {
        log.info("ROLE_USER TEST");
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return adminService.authorityCheck(jwtFilterDto);
    }

    // 게스트 호출
    @GetMapping("/guestTest")
    @ApiImplicitParam(name ="authorization", value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> guestTest() {
        log.info("ROLE_GUEST TEST");
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return adminService.authorityCheck(jwtFilterDto);
    }


}
