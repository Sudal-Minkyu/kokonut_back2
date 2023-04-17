//package com.app.kokonutuser;
//
//import com.app.kokonut.admin.AdminRepository;
//import com.app.kokonut.admin.Admin;
//import com.app.kokonut.admin.enums.AuthorityRole;
//import com.app.kokonut.auth.AuthService;
//import com.app.kokonut.configs.GoogleOTP;
//import com.app.kokonut.auth.jwt.dto.AuthRequestDto;
//import com.app.kokonut.auth.jwt.dto.AuthResponseDto;
//import com.app.kokonut.auth.jwt.dto.GoogleOtpGenerateDto;
//import com.app.kokonut.company.company.Company;
//import com.app.kokonut.company.company.CompanyRepository;
//import org.apache.commons.codec.binary.Base32;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//
//@AutoConfigureMockMvc
//@SpringBootTest
//class DynamicUserRestControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    private final String testemail = "testkokonut@kokonut.me";
//
//    private final String testphoneNumber = "01022223355";
//
//    private final String password = "kokonut123!!";
//
//    private String testAccessToken = "";
//
//    @Autowired
//    private AuthService authService;
//
//    @Autowired
//    private AdminRepository adminRepository;
//
//    @Autowired
//    private CompanyRepository companyRepository;
//
//    @Autowired
//    private GoogleOTP googleOTP;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    MockHttpServletRequest servletRequest;
//    MockHttpServletResponse servletResponse;
//
//    @BeforeEach
//    @SuppressWarnings("unchecked")
//    void testDataInsert() throws NoSuchAlgorithmException, InvalidKeyException {
//        servletRequest = new MockHttpServletRequest();
//        servletResponse = new MockHttpServletResponse();
//
//        GoogleOtpGenerateDto googleOtpGenerateDto = googleOTP.generate(testemail);
//
//        Company company = new Company();
//        company.setBusinessNumber("123456");
//        company.setRegdate(LocalDateTime.now());
//        Long companyId = companyRepository.save(company).getIdx();
//
//        Admin admin = Admin.builder()
//                .email(testemail)
//                .companyId(companyId)
//                .password(passwordEncoder.encode(password))
//                .phoneNumber(testphoneNumber)
//                .otpKey(googleOtpGenerateDto.getOtpKey())
//                .roleName(AuthorityRole.ROLE_MASTER)
//                .regdate(LocalDateTime.now())
//                .build();
//
//        adminRepository.save(admin);
//
//        AuthRequestDto.Login login = new AuthRequestDto.Login();
//        login.setEmail(testemail);
//        login.setPassword(password);
//
//        Base32 codec = new Base32();
//        byte[] decodedKey = codec.decode(googleOtpGenerateDto.getOtpKey());
//        long wave = new java.util.Date().getTime() / 30000;
//        long hash = GoogleOTP.verify_code(decodedKey, wave + 2);
//
//        login.setOtpValue(String.valueOf(hash));
//
//        ResponseEntity<Map<String, Object>> response = authService.authToken(login);
//
//        HashMap<String,Object> sendData = (HashMap<String, Object>) Objects.requireNonNull(response.getBody()).get("sendData");
//        AuthResponseDto.TokenInfo tokenInfo = (AuthResponseDto.TokenInfo) sendData.get("jwtToken");
//        testAccessToken = tokenInfo.getAccessToken();
//    }
//
//    @AfterEach
//    void testDataDelete() {
//        adminRepository.deleteAll();
//        companyRepository.deleteAll();
//    }
//
//    @Test
//    @WithMockUser(authorities = "MASTER")
//    @DisplayName("회원DB 생성 호출 테스트")
//    public void createUserDatabaseTest1() throws Exception {
//
//        // when
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/DynamicUser/createUserDatabase")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Bearer",testAccessToken)
//                )
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("SUCCESS"))
//                // 실패 테스트
////                .andExpect(MockMvcResultMatchers.jsonPath("$.err_code").value(ResponseErrorCode.CODE002.getCode()))
////                .andExpect(MockMvcResultMatchers.jsonPath("$.err_msg").value("해당 아이디는 "+ResponseErrorCode.CODE002.getDesc()))
//                .andDo(MockMvcResultHandlers.print());
//    }
//
//}