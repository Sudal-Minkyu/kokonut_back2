//package com.app.kokonutuser;
//
//import com.app.kokonut.admin.AdminRepository;
//import com.app.kokonut.admin.Admin;
//import com.app.kokonut.admin.enums.AuthorityRole;
//import com.app.kokonut.company.company.Company;
//import com.app.kokonut.company.company.CompanyRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDateTime;
//
//@AutoConfigureMockMvc
//@SpringBootTest
//class DynamicUserKnServiceTest {
//
//    private final String email = "kokonut@kokonut.me";
//
//    private Integer saveadminId;
//
//    private Integer savecompanyId;
//
//    @Autowired
//    private DynamicUserService dynamicUserService;
//
//    @Autowired
//    private AdminRepository adminRepository;
//
//    @Autowired
//    private CompanyRepository companyRepository;
//
//
//    @BeforeEach
//    void testDataInsert() {
//
//        // 테스트용 company
//        Company company = Company.builder()
//                .businessNumber("00001111")
//                .regdate(LocalDateTime.now())
//                .build();
//
//        savecompanyId= companyRepository.save(company).getIdx();
//
//        // 테스트용 admin
//        Admin admin = Admin.builder()
//                .email(email)
//                .password("test")
//                .companyId(savecompanyId)
//                .roleName(AuthorityRole.ROLE_MASTER)
//                .regdate(LocalDateTime.now())
//                .build();
//
//        saveadminId = adminRepository.save(admin).getIdx();
//
//    }
//
//    @AfterEach
//    void testDataDelete() {
//        adminRepository.deleteAll();
//        companyRepository.deleteAll();
//    }
//
////    @Test
////    @DisplayName("회원 테이블 생성 성공 테스트")
////    void createTableKokonutUserTest1() {
////
////        System.out.println("저장된 saveadminId : "+saveadminId);
////
////        System.out.println("저장된 savecompanyId : "+savecompanyId);
////
////        ResponseEntity<Map<String, Object>> response = dynamicUserService.createTable(email);
////        System.out.println("response : "+response);
////    }
//}