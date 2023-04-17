package com.app.kokonut.auth;

import com.app.kokonut.auth.dtos.AdminCreateDto;
import com.app.kokonut.auth.dtos.AdminGoogleOTPDto;
import com.app.kokonut.auth.dtos.AdminPasswordChangeDto;
import com.app.kokonut.auth.jwt.dto.AuthRequestDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Woody
 * Date : 2022-12-01
 * Time :
 * Remark : 로그인, 회원가입 등 JWT토큰 필요없이 호출할 수 있는 컨트롤러
 */
@Slf4j
@RequestMapping("/v1/api/Auth")
@RestController
public class AuthRestController {

    private final AuthService authService;

    @Autowired
    public AuthRestController(AuthService authService){
        this.authService = authService;
    }

    // 이메일 가입존재 여부
    @GetMapping(value = "/checkKnEmail")
    @ApiOperation(value = "이메일 이메일 존재여부 확인" , notes = "" +
            "1. 존재여부를 조회할 이메일을 받는다." +
            "2. 해당이메일이 존재한지 체크하여 반환한다.")
    public ResponseEntity<Map<String,Object>> checkKnEmail(@RequestParam(value="knEmail", defaultValue = "") String knEmail) {
        return authService.checkKnEmail(knEmail);
    }

    // 이메일 중복체크
    @GetMapping(value = "/existsByKnEmail")
    @ApiOperation(value = "이메일 중복확인 버튼" , notes = "" +
            "1. 이메일 중복확인을 한다." +
            "2. 결과의 대해 false 또는 true를 보낸다.")
    public ResponseEntity<Map<String,Object>> existsKnEmail(@RequestParam(value="knEmail", defaultValue = "") String knEmail) {
        return authService.existsKnEmail(knEmail);
    }

    // 이메일 인증번호 보내기(6자리 번호 형식, 유효기간 3분)
    @GetMapping(value = "/numberSendKnEmail")
    @ApiOperation(value = "이메일 인증번호 보내기 버튼" , notes = "" +
            "1. 이메일 중복확인한 이메일의 대해 인증번호를 보낸다." +
            "2. 번호를 레디스DB에 담는다. (유효기간은 3분)")
    public ResponseEntity<Map<String,Object>> numberSendKnEmail(@RequestParam(value="knEmail", defaultValue = "") String knEmail) throws IOException {
        return authService.numberSendKnEmail(knEmail);
    }

    // 이메일 인증번호 검증
    @GetMapping(value = "/numberCheckKnEmail")
    @ApiOperation(value = "이메일 인증번호 보내기 검증" , notes = "" +
            "1. 받은 인증번호를 받아 맞는지 확인한다.")
    public ResponseEntity<Map<String,Object>> numberCheckKnEmail(@RequestParam(value="knEmail", defaultValue = "") String knEmail,
                                                                 @RequestParam(value="ctNumber", defaultValue = "") String ctNumber) {
        return authService.numberCheckKnEmail(knEmail, ctNumber);
    }

    // 이메일찾기 기능
    @GetMapping(value = "/findKnEmail")
    @ApiOperation(value = "이메일찾기 기능" , notes = "" +
            "1. 가입한 휴대전화번호를 인증받는다." +
            "2. 인증이 성공을 성공하게되면 해당 휴대폰번희에 가입된 이메일을 화면단에 보내준다.")
    public ResponseEntity<Map<String,Object>> findKnEmail(@RequestParam(value="keyEmail", defaultValue = "") String keyEmail) {
        return authService.findKnEmail(keyEmail);
    }

    // 이메일로 임시비밀번호 보내는 기능
    @PostMapping(value = "/passwordSendKnEmail")
    @ApiOperation(value = "비밀번호찾기 기능" , notes = "" +
            "1. 인증을 완료한 이메일의 대해 임시비밀번호로 업데이트한다." +
            "2. 임시비밀번호를 이메일에 보낸다.")
    public ResponseEntity<Map<String,Object>> passwordSendKnEmail(@RequestParam(value="knEmail", defaultValue = "") String knEmail) throws IOException {
        return authService.passwordSendKnEmail(knEmail);
    }

    // 비밀번호 찾기(사용할 비밀번호로 변경하는) 기능
    @PostMapping(value = "/passwordUpdate")
    @ApiOperation(value = "비밀번호찾기 기능" , notes = "" +
            "1. 임시비밀번호를 받는다." +
            "2. 이메일을 통해 사용자정보를 가져온다." +
            "3. 해당 사용자의 비밀번호와 받은 임시비밀번호와 비교한다." +
            "4. 일치할시 사용할 비밀번호와, 비밀번호체크를 체크한다." +
            "5. 또 일치할시 해당 비밀번호로 업데이트한다.")
    public ResponseEntity<Map<String,Object>> passwordUpdate(@RequestBody AdminPasswordChangeDto adminPasswordChangeDto) {
        return authService.passwordUpdate(adminPasswordChangeDto);
    }

    // 관리자 등록하기전 키 검증
    @PostMapping(value = "/createCheck")
    @ApiOperation(value = "이메일 키검증" , notes = "" +
            "1. 관리자등록을 통해 메일이 온다." +
            "2. 해당 메일의 링크를 누르게 되면 키검증을 한다." +
            "3. 만료기간이 넘어가면 에러페이지로 안내한다." +
            "4. 휴대폰인증과 비밀번호를 입력하여 완료한다.")
    public ResponseEntity<Map<String,Object>> createCheck(@RequestBody AdminCreateDto adminCreateDto) throws Exception {
        return authService.createCheck(adminCreateDto);
    }

    // 관리자 등록 최종
    @PostMapping(value = "/createUser")
    @ApiOperation(value = "관리자등록" , notes = "" +
            "1. 이메일로 등록신청의 링크를 진입한다." +
            "2. 해당 링크의 키를 통해 검증한다." +
            "3. 휴대폰인증을한다." +
            "4. 사용할 비밀번호를 입력하여 등록한다.")
    public ResponseEntity<Map<String,Object>> createUser(@RequestBody AuthRequestDto.KokonutCreateUser kokonutCreateUser, HttpServletRequest request) {
        log.info("사업자 회원가입 API 호출");
        return authService.createUser(kokonutCreateUser, request);
    }

    // 리뉴얼 회원가입
    @PostMapping(value = "/kokonutSignUp")
    @ApiOperation(value = "사업자 회원가입" , notes = "" +
            "1. Param 값으로 유저 이메일과 사용할 비밀번호를 받는다." +
            "2. 이메일 중복체크를 한다." +
            "3. 이메일 인증체크를 한다." +
            "4. 회원가입 완료후 메일을 보낸다.")
    public ResponseEntity<Map<String,Object>> kokonutSignUp(@RequestBody AuthRequestDto.KokonutSignUp kokonutSignUp, HttpServletRequest request) {
        log.info("사업자 회원가입 API 호출");
        return authService.kokonutSignUp(kokonutSignUp, request);
    }

    // 회원가입
    @PostMapping(value = "/signUp", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "사업자 회원가입" , notes = "" +
            "1. Param 값으로 유저정보와 기업정보를 받는다." +
            "2. 유니크 값 중복체크를 한다." +
            "3. 기업정보를 저장한다." +
            "4. 사업자등록증, KMS인증키 등을 발급받는다.")
    public ResponseEntity<Map<String,Object>> signUp(@Parameter(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @Validated AuthRequestDto.SignUp signUp, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("사업자 회원가입 API 호출");
        return authService.signUp(signUp, request, response);
    }

    @GetMapping(value = "/cookieTest")
    public void cookieTest(HttpServletRequest request) {
        log.info("쿠키테스트");
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            log.info("현재 쿠키값들 : "+ Arrays.toString(cookies));
            for(Cookie cookie : cookies) {
                log.info("cookie.getName() : "+cookie.getName());
                log.info("cookie.getValue() : "+cookie.getValue());
            }
        } else {
            log.info("cookies : null");
        }
    }

    // 로그인 성공 이후 JWT Token 발급 및 업데이트
    // "loginVerify" + "/otpVerify" 합쳐진 메서드
    @PostMapping("/authToken")
    @ApiOperation(value = "로그인 - 로그인 성공후 JWT 토큰 발급" , notes = "" +
            "1. 입력한 아이디와 비밀번호와 OTP값을 보낸다." +
            "2. 해당 이메일의 OTP값이 맞는지 확인하고, 로그인 여부를 체크한다." +
            "3. 확인이 다 되면, 쿠키에 refreshToken을 저장하고, 결과값으로 accessToken을 보내준다.")
    public ResponseEntity<Map<String,Object>> authToken(@Validated AuthRequestDto.Login login, HttpServletResponse response) {
        log.info("로그인한 이메일 : "+login.getKnEmail());
        return authService.authToken(login, response);
    }

    @PostMapping("/reissue")
    @ApiOperation(value = "토큰 새로고침" , notes = "" +
            "1. Param 값으로 accessToken, refreshToken을 받는다." +
            "2. 해당 리플레쉬 토큰을 통해 새 토큰을 발급하며 이전의 토큰은 사용 불가처리를 한다.")
    public ResponseEntity<Map<String,Object>> reissue(@Validated AuthRequestDto.Reissue reissue) {
        return authService.reissue(reissue);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "로그아웃 처리" , notes = "" +
            "1. Param 값으로 accessToken, refreshToken을 받는다." +
            "2. 받은 두 토큰을 검사하고 해당 토큰을 삭제처리 한다.")
//    public ResponseEntity<Map<String,Object>> logout(@Validated AuthRequestDto.Logout logout, HttpServletResponse response) {
    public ResponseEntity<Map<String,Object>> logout(@Validated AuthRequestDto.Logout logout, HttpServletRequest request, HttpServletResponse response) {
        return authService.logout(logout, request, response);
    }

    @GetMapping(value = "/otpQRcode")
    @ApiOperation(value = "구글 QRCode 보내기 " , notes = "" +
            "1. 로그인한 이메일 값을 받는다" +
            "2. 해당 유저 이메일을 통해 QRCode값을 반환한다.")
    public ResponseEntity<Map<String,Object>> otpQRcode(@RequestParam(value="knEmail", defaultValue = "") String knEmail) {
        return authService.otpQRcode(knEmail);
    }

    @GetMapping(value = "/checkOTP")
    @ApiOperation(value = "구글 OTP 등록전 값 확인" , notes = "" +
            "1. Param 값으로 발급한 otpKey와 입력한 otpValue를 받는다." +
            "2. 두 값을 통해 성공여부를 판단한다." +
            "3. 인증키를 추가로 반환한다.")
    public ResponseEntity<Map<String,Object>> checkOTP(@Validated AdminGoogleOTPDto.GoogleOtpCertification googleOtpCertification) {
        return authService.checkOTP(googleOtpCertification);
    }

    @PostMapping(value = "/saveOTP")
    @ApiOperation(value = "구글 OTP 등록" , notes = "" +
            "1. 인증된 OTP값과 Key,Value+ 이메일, 비밀번호를 받는다." +
            "2. 받은 값을 통해 체크한다." +
            "3. 체크된 OTP를 등록한다.")
    public ResponseEntity<Map<String,Object>> saveOTP(@Validated AdminGoogleOTPDto.GoogleOtpSave googleOtpSave) {
        return authService.saveOTP(googleOtpSave);
    }























}
