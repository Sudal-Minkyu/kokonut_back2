//package com.app.kokonut.faq;
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
//import com.app.kokonut.faq.dtos.FaqSearchDto;
//import com.google.gson.Gson;
//import org.apache.commons.codec.binary.Base32;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//
//@AutoConfigureMockMvc
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//class FaqRestControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    // COMPANY
//    private final String businessNum = "123456";
//
//    // SYSTEM USER
//    private final String sEmail = "kokonut_admin@kokonut.me";
//    private final String sPhoneNum = "01027330129";
//    private final String sPassword = "kokonut123##";
//    private String sAccessToken = "";
//
//    // MASTER USER
//    private final String mEmail = "kokonut_master@kokonut.me";
//    private final String mPhoneNum = "01026940129";
//    private final String mPassword = "kokonut123##";
//    private String mAccessToken = "";
//
//    @Autowired
//    private AuthService authService;
//
//    @MockBean
//    private FaqService faqService;
//
//    @Autowired
//    private AdminRepository adminRepository;
//    @Autowired
//    private CompanyRepository companyRepository;
//
//    @Autowired
//    private GoogleOTP googleOTP;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    MockHttpServletRequest servletRequest;
//    MockHttpServletResponse servletResponse;
//    @BeforeEach
//    @SuppressWarnings("unchecked")
//    void testDataInsert() throws Exception {
//        servletRequest = new MockHttpServletRequest();
//        servletResponse = new MockHttpServletResponse();
//
//        // SYSTEM
//        GoogleOtpGenerateDto googleOtpGenerateDto = googleOTP.generate(sEmail);
//
//        // 회사 생성
//        Company company = new Company();
//        company.setBusinessNumber(businessNum);
//        company.setRegdate(LocalDateTime.now());
//        Long companyId = companyRepository.save(company).getIdx();
//
//        // 시스템 회원 등록
//        Admin admin = Admin.builder()
//                .email(sEmail)
//                .companyId(companyId)
//                .password(passwordEncoder.encode(sPassword))
//                .phoneNumber(sPhoneNum)
//                .otpKey(googleOtpGenerateDto.getOtpKey())
//                .roleName(AuthorityRole.ROLE_SYSTEM)
//                .regdate(LocalDateTime.now())
//                .build();
//
//        adminRepository.save(admin);
//
//        // 시스템 회원 로그인
//        AuthRequestDto.Login login = new AuthRequestDto.Login();
//        login.setEmail(sEmail);
//        login.setPassword(sPassword);
//
//        Base32 codec = new Base32();
//        byte[] decodedKey = codec.decode(googleOtpGenerateDto.getOtpKey());
//        long wave = new java.util.Date().getTime() / 30000;
//        long hash = GoogleOTP.verify_code(decodedKey, wave + 2);
//
//        login.setOtpValue(String.valueOf(hash));
//
//        // 토큰 발급
//        ResponseEntity<Map<String, Object>> response = authService.authToken(login);
//
//        HashMap<String,Object> sendData = (HashMap<String, Object>) Objects.requireNonNull(response.getBody()).get("sendData");
//        AuthResponseDto.TokenInfo tokenInfo = (AuthResponseDto.TokenInfo) sendData.get("jwtToken");
//        sAccessToken = tokenInfo.getAccessToken();
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/DynamicUser/createUserDatabase")
//                .contentType(MediaType.APPLICATION_JSON)
//                .header("Bearer",sAccessToken)
//        );
//    }
//
//    @AfterEach
//    void testDataDelete() {
//        adminRepository.deleteAll();
//        companyRepository.deleteAll();
//    }
//
//    @Test
//    @DisplayName("faqList 호출 테스트")
//    void faqList() throws Exception {
//        // give
//        FaqSearchDto faqSearchDto = new FaqSearchDto();
//        faqSearchDto.setSearchText("test");
//        Gson gson =  new Gson();
//        String content = gson.toJson(faqSearchDto);
//
//        // when
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/Faq/faqList")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Bearer",sAccessToken)
//                        .content(content)
//                )
//                .andExpect(MockMvcResultMatchers.status().isOk());
//
//    }
//    @Test
//    void faqDetail() {
//    }
//
//    @Test
//    void faqSave() {
//    }
//
//    @Test
//    void faqDelete() {
//    }
//}