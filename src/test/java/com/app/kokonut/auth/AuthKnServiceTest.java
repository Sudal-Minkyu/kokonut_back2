package com.app.kokonut.auth;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.Admin;
import com.app.kokonut.admin.enums.AuthorityRole;
import com.app.kokonut.auth.jwt.been.JwtTokenProvider;
import com.app.kokonut.configs.GoogleOTP;
import com.app.kokonut.auth.jwt.dto.AuthRequestDto;
import com.app.kokonut.auth.jwt.dto.AuthResponseDto;
import com.app.kokonut.auth.jwt.dto.GoogleOtpGenerateDto;
import com.app.kokonut.company.company.Company;
import com.app.kokonut.company.company.CompanyRepository;
import org.apache.commons.codec.binary.Base32;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Woody
 * Date : 2022-12-02
 * Time :
 * Remark : authService 테스트코드
 */
@AutoConfigureMockMvc
@SpringBootTest
class AuthKnServiceTest {

    private final String testemail = "testkokonut@kokonut.me";

    private final String testphoneNumber = "01022223355";

    private final String email = "kokonut@kokonut.me";

    private final String password = "kokonut123!!";

    private final String phoneNumber = "01022223344";

    private final AuthRequestDto.KokonutSignUp kokonutSignUp = AuthRequestDto.KokonutSignUp.builder()
            .knEmail(email)
            .knPassword(password)
            .knPasswordConfirm(password)
            .knPhoneNumber(phoneNumber)
            .knName("테스트명")
            .cpName("회사명")
            .build();

    private String testAccessToken = "";
    private String testRefreshAccessToken = "";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthService authService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private GoogleOTP googleOTP;

    @Autowired
    private PasswordEncoder passwordEncoder;

    MockHttpServletRequest servletRequest;
    MockHttpServletResponse servletResponse;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void testDataInsert() throws NoSuchAlgorithmException, InvalidKeyException {
        servletRequest = new MockHttpServletRequest();
        servletResponse = new MockHttpServletResponse();

        GoogleOtpGenerateDto googleOtpGenerateDto = googleOTP.generate(testemail);

        Admin admin = Admin.builder()
                .knEmail(testemail)
                .knPassword(passwordEncoder.encode(password))
                .knPhoneNumber(testphoneNumber)
                .knOtpKey(googleOtpGenerateDto.getOtpKey())
                .knRoleCode(AuthorityRole.ROLE_MASTER)
                .insert_date(LocalDateTime.now())
                .build();

        adminRepository.save(admin);

        Company company = new Company();
        company.setCpBusinessNumber("123456");
        company.setInsert_date(LocalDateTime.now());
        companyRepository.save(company);

        AuthRequestDto.Login login = new AuthRequestDto.Login();
        login.setKnEmail(testemail);
        login.setKnPassword(password);

        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(googleOtpGenerateDto.getOtpKey());
        long wave = new java.util.Date().getTime() / 30000;
        long hash = GoogleOTP.verify_code(decodedKey, wave + 2);

        login.setOtpValue(String.valueOf(hash));
        ResponseEntity<Map<String, Object>> response = authService.authToken(login, null);

        HashMap<String, Object> sendData = (HashMap<String, Object>) Objects.requireNonNull(response.getBody()).get("sendData");
        AuthResponseDto.TokenInfo tokenInfo = (AuthResponseDto.TokenInfo) sendData.get("jwtToken");
        testAccessToken = tokenInfo.getAccessToken();
        testRefreshAccessToken = tokenInfo.getRefreshToken();
    }

    @AfterEach
    void testDataDelete() {
        adminRepository.deleteAll();
        companyRepository.deleteAll();
    }


//    @Test
//    @DisplayName("사업자 회원가입 성공 테스트 - 회원가입이후 조회 및 검증")
//    public void signUpTest1() throws IOException {
//
//        // given
//        // signUp -> final 변수로 선언
//
//        // when
//        ResponseEntity<Map<String,Object>> response =  authService.signUp(signUp, servletRequest, servletResponse);
//
//        Admin admin = adminRepository.findByKnEmail(email)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 'Admin' 입니다."));
//
//        // then
//        assertEquals(email,admin.getKnEmail());
//        assertEquals(AuthorityRole.ROLE_MASTER,admin.getKnRoleCode());
//
//        assertEquals("SUCCESS", Objects.requireNonNull(response.getBody()).get("message"));
//        assertEquals(200, Objects.requireNonNull(response.getBody()).get("status"));
//
//    }
//
//    @Test
//    @DisplayName("사업자 회원가입 실패 테스트 - 이메일이 이미 존재 할 경우")
//    public void signUpTest2() throws IOException {
//
//        // given
//        signUp.setKnEmail(testemail);
//
//        // when
//        ResponseEntity<Map<String,Object>> response = authService.signUp(signUp, servletRequest, servletResponse);
//
//        // then
//        assertEquals(ResponseErrorCode.KO005.getCode(), Objects.requireNonNull(response.getBody()).get("err_code"));
//        assertEquals("Error", Objects.requireNonNull(response.getBody()).get("message"));
//        assertEquals(500, Objects.requireNonNull(response.getBody()).get("status"));
//
//    }
//
//    @Test
//    @DisplayName("사업자 회원가입 실패 테스트 - 이미 회원가입된 핸드폰번호 일 경우")
//    public void signUpTest3() throws IOException {
//
//        // given
//        signUp.setKnPhoneNumber(testphoneNumber);
//
//        // when
//        ResponseEntity<Map<String,Object>> response = authService.signUp(signUp, servletRequest, servletResponse);
//
//        // then
//        assertEquals(ResponseErrorCode.KO034.getCode(), Objects.requireNonNull(response.getBody()).get("err_code"));
//        assertEquals("Error", Objects.requireNonNull(response.getBody()).get("message"));
//        assertEquals(500, Objects.requireNonNull(response.getBody()).get("status"));
//
//    }
//
//    @Test
//    @DisplayName("사업자 회원가입 실패 테스트 - 비밀번호가 서로 일치하지 않을 일 경우")
//    public void signUpTest4() throws IOException {
//
//        // given
//        signUp.setKnPasswordConfirm("test");
//
//        // when
//        ResponseEntity<Map<String,Object>> response = authService.signUp(signUp, servletRequest, servletResponse);
//
//        // then
//        assertEquals(ResponseErrorCode.KO013.getCode(), Objects.requireNonNull(response.getBody()).get("err_code"));
//        assertEquals("Error", Objects.requireNonNull(response.getBody()).get("message"));
//        assertEquals(500, Objects.requireNonNull(response.getBody()).get("status"));
//
//    }
//
//    @Test
//    @DisplayName("사업자 회원가입 실패 테스트 - 이미 가입된 사업자등록번호 일 경우")
//    public void signUpTest5() throws IOException {
//
//        // given
//        signUp.setCpBusinessNumber("123456");
//
//        // when
//        ResponseEntity<Map<String,Object>> response = authService.signUp(signUp, servletRequest, servletResponse);
//
//        // then
//        assertEquals(ResponseErrorCode.KO035.getCode(), Objects.requireNonNull(response.getBody()).get("err_code"));
//        assertEquals("Error", Objects.requireNonNull(response.getBody()).get("message"));
//        assertEquals(500, Objects.requireNonNull(response.getBody()).get("status"));
//
//    }
//
//    @Test
//    @DisplayName("로그인, OTP JWT토큰 발급 성공 테스트 - 발급받은 accessToken 확인")
//    @SuppressWarnings("unchecked")
//    public String authTokenTest1() throws NoSuchAlgorithmException, InvalidKeyException {
//
//        // given
//        Admin admin = adminRepository.findByKnEmail(testemail)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 'Admin' 입니다."));
//
//        AuthRequestDto.Login login = new AuthRequestDto.Login();
//        login.setEmail(admin.getKnEmail());
//        login.setPassword(password);
//
//        Base32 codec = new Base32();
//        byte[] decodedKey = codec.decode(admin.getKnOtpKey());
//        long wave = new java.util.Date().getTime() / 30000;
//        long hash = GoogleOTP.verify_code(decodedKey, wave + 2);
//
//        login.setOtpValue(String.valueOf(hash));
//
//        // when
//        ResponseEntity<Map<String,Object>> response =  authService.authToken(login);
//
//        HashMap<String,Object> sendData = (HashMap<String, Object>) Objects.requireNonNull(response.getBody()).get("sendData");
//        AuthResponseDto.TokenInfo tokenInfo = (AuthResponseDto.TokenInfo) sendData.get("jwtToken");
//        boolean result = jwtTokenProvider.validateToken(tokenInfo.getAccessToken());
//
//        System.out.println("jwt : "+tokenInfo.getAccessToken());
//
//        // then
//        assertTrue(result);
//        assertEquals("SUCCESS", Objects.requireNonNull(response.getBody()).get("message"));
//        assertEquals(200, Objects.requireNonNull(response.getBody()).get("status"));
//
//        return tokenInfo.getAccessToken();
//    }
//
//    @Test
//    @DisplayName("로그인 실패 테스트 - 존재하지 않은 이메일일 경우")
//    public void authTokenTest2(){
//
//        // given
//        AuthRequestDto.Login login = new AuthRequestDto.Login();
//        login.setEmail(email);
//        login.setPassword(password);
//        login.setOtpValue("123456");
//
//        // when
//        ResponseEntity<Map<String,Object>> response =  authService.authToken(login);
//
//        // then
//        assertEquals(ResponseErrorCode.KO004.getCode(), Objects.requireNonNull(response.getBody()).get("err_code"));
//        assertEquals("Error", Objects.requireNonNull(response.getBody()).get("message"));
//        assertEquals(500, Objects.requireNonNull(response.getBody()).get("status"));
//
//    }
//
//    @Test
//    @DisplayName("로그인, OTP값 JWT토큰 발급 실패 테스트 - OtpValue 값이 존재하지 않을 경우(Null 값인 경우)")
//    public void authTokenTest3_1() throws IOException {
//
//        // given
//        authService.signUp(signUp, servletRequest, servletResponse);
//
//        // given
//        AuthRequestDto.Login login = new AuthRequestDto.Login();
//        login.setEmail(email);
//        login.setPassword(password);
//
//        // when
//        ResponseEntity<Map<String,Object>> response =  authService.authToken(login);
//
//        // then
//        assertEquals(ResponseErrorCode.KO010.getCode(), Objects.requireNonNull(response.getBody()).get("err_code"));
//        assertEquals("Error", Objects.requireNonNull(response.getBody()).get("message"));
//        assertEquals(500, Objects.requireNonNull(response.getBody()).get("status"));
//    }
//
//    @Test
//    @DisplayName("로그인, OTP값 JWT토큰 발급 실패 테스트 - 발급받은 OtpKey 가 존재하지 않을 경우")
//    public void authTokenTest3_2() throws IOException {
//
//        // given
//        authService.signUp(signUp, servletRequest, servletResponse);
//
//        // given
//        AuthRequestDto.Login login = new AuthRequestDto.Login();
//        login.setEmail(signUp.getKnEmail());
//        login.setPassword(signUp.getKnPassword());
//        login.setOtpValue("123456");
//
//        // when
//        ResponseEntity<Map<String,Object>> response =  authService.authToken(login);
//
//        // then
//        assertEquals(ResponseErrorCode.KO011.getCode(), Objects.requireNonNull(response.getBody()).get("err_code"));
//        assertEquals("Error", Objects.requireNonNull(response.getBody()).get("message"));
//        assertEquals(500, Objects.requireNonNull(response.getBody()).get("status"));
//    }
//
//    @Test
//    @DisplayName("로그인, OTP값 JWT토큰 발급 실패 테스트 - OtpValue 값이 일치하지 않을 경우")
//    public void authTokenTest3_3(){
//
//        // given
//        AuthRequestDto.Login login = new AuthRequestDto.Login();
//        login.setEmail(testemail);
//        login.setPassword(password);
//        login.setOtpValue("123456");
//
//        // when
//        ResponseEntity<Map<String,Object>> response =  authService.authToken(login);
//
//        // then
//        assertEquals(ResponseErrorCode.KO012.getCode(), Objects.requireNonNull(response.getBody()).get("err_code"));
//        assertEquals("Error", Objects.requireNonNull(response.getBody()).get("message"));
//        assertEquals(500, Objects.requireNonNull(response.getBody()).get("status"));
//    }
//
//    @Test
//    @DisplayName("로그인, OTP값 JWT토큰 발급 실패 테스트 - 비밀번호 틀렸을 경우")
//    public void authTokenTest3_4() throws NoSuchAlgorithmException, InvalidKeyException {
//
//        // given
//        Admin admin = adminRepository.findByKnEmail(testemail)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 'Admin' 입니다."));
//
//        AuthRequestDto.Login login = new AuthRequestDto.Login();
//        login.setEmail(admin.getKnEmail());
//        login.setPassword("123456");
//
//        Base32 codec = new Base32();
//        byte[] decodedKey = codec.decode(admin.getKnOtpKey());
//        long wave = new java.util.Date().getTime() / 30000;
//        long hash = GoogleOTP.verify_code(decodedKey, wave + 2);
//
//        login.setOtpValue(String.valueOf(hash));
//
//        ResponseEntity<Map<String,Object>> response =  authService.authToken(login);
//
//        // then
//        assertEquals(ResponseErrorCode.KO016.getCode(), Objects.requireNonNull(response.getBody()).get("err_code"));
//        assertEquals("Error", Objects.requireNonNull(response.getBody()).get("message"));
//        assertEquals(500, Objects.requireNonNull(response.getBody()).get("status"));
//    }
//
//
//    @Test
//    @DisplayName("JWT토큰 새로고침 성공 테스트")
//    @SuppressWarnings("unchecked")
//    public void reissueTest1(){
//
//        // given
//        AuthRequestDto.Reissue reissue = new AuthRequestDto.Reissue();
//        reissue.setAccessToken(testAccessToken);
//        reissue.setRefreshToken(testRefreshAccessToken);
//
//        boolean resultTrue = jwtTokenProvider.validateToken(testAccessToken);
//        assertTrue(resultTrue); // 해당 토큰이 유효한지 테스트
//
//        // when
//        ResponseEntity<Map<String,Object>> response =  authService.reissue(reissue);
//
//        boolean resultFalse = jwtTokenProvider.validateToken(testAccessToken);
//        assertTrue(resultFalse); // 해당 토큰이 유효하지 않은지 테스트
//
//        HashMap<String,Object> sendData = (HashMap<String, Object>) Objects.requireNonNull(response.getBody()).get("sendData");
//        AuthResponseDto.TokenInfo tokenInfo = (AuthResponseDto.TokenInfo) sendData.get("jwtToken");
//        boolean result = jwtTokenProvider.validateToken(tokenInfo.getAccessToken());
//
//        // then
//        assertTrue(result); // 새로받은 토큰이 유효한지 테스트
//        assertEquals("SUCCESS", Objects.requireNonNull(response.getBody()).get("message"));
//        assertEquals(200, Objects.requireNonNull(response.getBody()).get("status"));
//    }
//
//    @Test
//    @DisplayName("로그인 JWT토큰 새로고침 실패 테스트 - 유효한 Refresh 토큰이 아닐 경우")
//    public void reissueTest2(){
//
//        // given
//        AuthRequestDto.Reissue reissue = new AuthRequestDto.Reissue();
//        reissue.setAccessToken(testAccessToken);
//        reissue.setRefreshToken(testRefreshAccessToken+"test");
//
//        // when
//        ResponseEntity<Map<String,Object>> response =  authService.reissue(reissue);
//
//        // then
//        assertEquals(ResponseErrorCode.KO007.getCode(), Objects.requireNonNull(response.getBody()).get("err_code"));
//        assertEquals("Error", Objects.requireNonNull(response.getBody()).get("message"));
//        assertEquals(500, Objects.requireNonNull(response.getBody()).get("status"));
//    }
//
//    @Test
//    @DisplayName("JWT토큰 새로고침 실패 테스트 - 받은 토큰이 이미 로그아웃된 토큰일 경우")
//    public void reissueTest3(){
//
//        // given
//        AuthRequestDto.Reissue reissue = new AuthRequestDto.Reissue();
//        reissue.setAccessToken(testAccessToken);
//        reissue.setRefreshToken(testRefreshAccessToken);
//
//        boolean resultTrue = jwtTokenProvider.validateToken(testAccessToken);
//        assertTrue(resultTrue); // 해당 토큰이 유효한지 테스트
//
//        AuthRequestDto.Logout logout = new AuthRequestDto.Logout();
//        logout.setAccessToken(testAccessToken);
//        logout.setRefreshToken(testRefreshAccessToken);
//        authService.logout(logout);
//
//        // when
//        ResponseEntity<Map<String,Object>> response =  authService.reissue(reissue);
//
//        // then
//        assertEquals(ResponseErrorCode.KO006.getCode(), Objects.requireNonNull(response.getBody()).get("err_code"));
//        assertEquals("Error", Objects.requireNonNull(response.getBody()).get("message"));
//        assertEquals(500, Objects.requireNonNull(response.getBody()).get("status"));
//    }
//
//    @Test
//    @DisplayName("JWT토큰 새로고침 실패 테스트 - Access 토큰의 Refresh 토큰이 맞지 않을 경우")
//    public void reissueTest4(){
//
//        // given
//        AuthRequestDto.Reissue reissue = new AuthRequestDto.Reissue();
//        reissue.setAccessToken(testAccessToken);
//        reissue.setRefreshToken(testRefreshAccessToken);
//
//        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//        valueOperations.set("RT: "+testemail, testRefreshAccessToken+"test");
//
//        // when
//        ResponseEntity<Map<String,Object>> response =  authService.reissue(reissue);
//
//        // then
//        assertEquals(ResponseErrorCode.KO008.getCode(), Objects.requireNonNull(response.getBody()).get("err_code"));
//        assertEquals("Error", Objects.requireNonNull(response.getBody()).get("message"));
//        assertEquals(500, Objects.requireNonNull(response.getBody()).get("status"));
//    }
//
//    @Test
//    @DisplayName("로그아웃 성공 테스트")
//    public void logoutTest1(){
//
//        // given
//        AuthRequestDto.Logout logout = new AuthRequestDto.Logout();
//        logout.setAccessToken(testAccessToken);
//        logout.setRefreshToken(testRefreshAccessToken);
//
//        boolean resultTrue = jwtTokenProvider.validateToken(testAccessToken);
//        assertTrue(resultTrue); // 해당 토큰이 유효한지 테스트
//
//        // when
//        ResponseEntity<Map<String,Object>> response =  authService.logout(logout);
//
//        boolean resultFalse = jwtTokenProvider.validateToken(testAccessToken);
//        assertTrue(resultFalse); // 해당 토큰이 유효하지 않은지 테스트
//
//        String result = redisTemplate.opsForValue().get(logout.getAccessToken()); // 해당 토큰이 BlackList로 올라갔는지 테스트
//
//        // then
//        assertEquals("logout", result);
//        assertEquals("SUCCESS", Objects.requireNonNull(response.getBody()).get("message"));
//        assertEquals(200, Objects.requireNonNull(response.getBody()).get("status"));
//    }
//
//    @Test
//    @DisplayName("로그아웃 실패 테스트 - 받은 Access 토큰정보가 유효하지 않을 경우")
//    public void logoutTest2(){
//
//        // given
//        AuthRequestDto.Logout logout = new AuthRequestDto.Logout();
//        logout.setAccessToken(testAccessToken+"test");
//        logout.setRefreshToken(testRefreshAccessToken);
//
//        // when
//        ResponseEntity<Map<String,Object>> response =  authService.logout(logout);
//
//        // then
//        assertEquals(ResponseErrorCode.KO006.getCode(), Objects.requireNonNull(response.getBody()).get("err_code"));
//        assertEquals("Error", Objects.requireNonNull(response.getBody()).get("message"));
//        assertEquals(500, Objects.requireNonNull(response.getBody()).get("status"));
//    }
//
//    @Test
//    @DisplayName("OTP QR코드 발급 성공 테스트")
//    @SuppressWarnings("unchecked")
//    public void otpQRcodeTest1_1(){
//
//        // given, when
//        ResponseEntity<Map<String,Object>> response =  authService.otpQRcode(testemail);
//
//        HashMap<String,Object> sendData = (HashMap<String, Object>) Objects.requireNonNull(response.getBody()).get("sendData");
//
//        String otpKey = (String) sendData.get("otpKey");
//        String url = (String) sendData.get("otpKey");
//
//        System.out.println("otpKey : "+otpKey);
//        System.out.println("url : "+url);
//
//        // then
//        assertEquals("SUCCESS", Objects.requireNonNull(response.getBody()).get("message"));
//        assertEquals(200, Objects.requireNonNull(response.getBody()).get("status"));
//    }
//
//    @Test
//    @DisplayName("OTP QR코드 발급 실패 테스트 - 받은 이메일의 유저 정보가 존재하지 않을 때")
//    public void otpQRcodeTest1_2(){
//
//        // given, when
//        ResponseEntity<Map<String,Object>> response =  authService.otpQRcode(email);
//
//        // then
//        assertEquals(ResponseErrorCode.KO004.getCode(), Objects.requireNonNull(response.getBody()).get("err_code"));
//        assertEquals("Error", Objects.requireNonNull(response.getBody()).get("message"));
//        assertEquals(500, Objects.requireNonNull(response.getBody()).get("status"));
//    }
//
//    @Test
//    @DisplayName("OTP 입력확인 성공 테스트")
//    @SuppressWarnings("unchecked")
//    public void checkOTPTest1_1() throws NoSuchAlgorithmException, InvalidKeyException {
//
//        // given
//        Admin admin = adminRepository.findByKnEmail(testemail)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 'Admin' 입니다."));
//
//        Base32 codec = new Base32();
//        byte[] decodedKey = codec.decode(admin.getKnOtpKey());
//        long wave = new java.util.Date().getTime() / 30000;
//        long hash = GoogleOTP.verify_code(decodedKey, wave + 2);
//
//        // when
//        AdminGoogleOTPDto.GoogleOtpCertification googleOtpCertification = new AdminGoogleOTPDto.GoogleOtpCertification();
//        googleOtpCertification.setKnOtpKey(admin.getKnOtpKey());
//        googleOtpCertification.setOtpValue(String.valueOf(hash));
//
//        ResponseEntity<Map<String,Object>> response =  authService.checkOTP(googleOtpCertification);
//        HashMap<String,Object> sendData = (HashMap<String, Object>) Objects.requireNonNull(response.getBody()).get("sendData");
//
//        boolean auth = (boolean) sendData.get("auth");
//
//        // then
//        assertTrue(auth);
//        assertEquals("SUCCESS", Objects.requireNonNull(response.getBody()).get("message"));
//        assertEquals(200, Objects.requireNonNull(response.getBody()).get("status"));
//    }
//
//    @Test
//    @DisplayName("OTP 입력확인 실패 테스트 - 입력한 OTP 값이 틀릴 경우")
//    public void checkOTPTest1_2() {
//
//        // given
//        Admin admin = adminRepository.findByKnEmail(testemail)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 'Admin' 입니다."));
//
//        // when
//        AdminGoogleOTPDto.GoogleOtpCertification googleOtpCertification = new AdminGoogleOTPDto.GoogleOtpCertification();
//        googleOtpCertification.setKnOtpKey(admin.getKnOtpKey());
//        googleOtpCertification.setOtpValue("123456");
//
//        ResponseEntity<Map<String,Object>> response =  authService.checkOTP(googleOtpCertification);
//
//        // then
//        assertEquals(ResponseErrorCode.KO012.getCode(), Objects.requireNonNull(response.getBody()).get("err_code"));
//        assertEquals("Error", Objects.requireNonNull(response.getBody()).get("message"));
//        assertEquals(500, Objects.requireNonNull(response.getBody()).get("status"));
//    }
//
//    @Test
//    @DisplayName("OTP 입력확인 실패 테스트 - 입력한 OTP 값에 문자가 포함되 있을 경우")
//    public void checkOTPTest1_3() {
//
//        // given
//        Admin admin = adminRepository.findByKnEmail(testemail)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 'Admin' 입니다."));
//
//        // when
//        AdminGoogleOTPDto.GoogleOtpCertification googleOtpCertification = new AdminGoogleOTPDto.GoogleOtpCertification();
//        googleOtpCertification.setKnOtpKey(admin.getKnOtpKey());
//        googleOtpCertification.setOtpValue("12a4b5c6");
//
//        ResponseEntity<Map<String,Object>> response =  authService.checkOTP(googleOtpCertification);
//
//        // then
//        assertEquals(ResponseErrorCode.KO015.getCode(), Objects.requireNonNull(response.getBody()).get("err_code"));
//        assertEquals("Error", Objects.requireNonNull(response.getBody()).get("message"));
//        assertEquals(500, Objects.requireNonNull(response.getBody()).get("status"));
//    }
//
//    // saveOTP 저장 테스트는 제외 -> 핸드폰 인증이 필요한 테스트(Nice인증 테스트 필요한데 테스트하기 번잡함)
//
}