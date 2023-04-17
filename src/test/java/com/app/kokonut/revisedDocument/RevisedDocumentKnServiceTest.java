//package com.app.kokonut.revisedDocument;
//
//import com.app.kokonut.admin.AdminRepository;
//import com.app.kokonut.admin.Admin;
//import com.app.kokonut.admin.enums.AuthorityRole;
//import com.app.kokonut.revisedDocument.dtos.RevDocSaveDto;
//import com.app.kokonut.revisedDocumentFile.RevisedDocumentFileRepository;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.Map;
//import java.util.Objects;
//
//@AutoConfigureMockMvc
//@SpringBootTest
//class RevisedDocumentKnServiceTest {
//
//    // Service
//    @Autowired
//    private RevisedDocumentService revDocService;
//
//    // Repository
//    @Autowired
//    private RevisedDocumentRepository revDocRepository;
//    @Autowired
//    private RevisedDocumentFileRepository revDocFileRepository;
//    @Autowired
//    private AdminRepository adminRepository;
//
//    MockHttpServletRequest servletRequest;
//    MockHttpServletResponse servletResponse;
//    MockMvc mvc;
//
//    // 변수
//    Integer masterIdx;
//    String masterName;
//    String masterEmail;
//    Integer comapnyIdx;
//
//    @BeforeEach
//    void testDataInsert() throws IOException {
//        servletRequest = new MockHttpServletRequest();
//        servletResponse = new MockHttpServletResponse();
//
//        // 테스트용 masterAdmin
//        Admin masterAdmin = Admin.builder()
//                .email("master@kokonut.me")
//                .name("마스터")
//                .password("test")
//                .roleName(AuthorityRole.ROLE_MASTER)
//                .companyId(2)
//                .regdate(LocalDateTime.now())
//                .state(1)
//                .build();
//
//        Admin master = adminRepository.save(masterAdmin);
//
//        masterIdx = master.getIdx();
//        masterName = master.getName();
//        masterEmail = master.getEmail();
//        comapnyIdx = master.getCompanyId();
//        System.out.println("## adminRepository.save(masterAdmin) - mater Admin 등록");
//    }
//
//    @AfterEach
//    void testDataDelete() {
//        adminRepository.deleteAll();
//        revDocRepository.deleteAll();
//        revDocFileRepository.deleteAll();
//        System.out.println("## adminRepository.deleteAll()");
//        System.out.println("## revDocRepository.deleteAll()");
//        System.out.println("## revDocFileRepository.deleteAll()");
//    }
//
//    @Test
//    @DisplayName("개정문서 저장 성공 테스트")
//    void revDocSave1() throws IOException {
//        // given
//        // revDocSave(String userRole, String email, RevDocSaveDto revDocSaveDto, HttpServletRequest request, HttpServletResponse response)
//        String role = "[MASTER]";
//        String email = masterEmail;
//
//        PageRequest pageable = PageRequest.of(0, 10);
//        RevDocSaveDto revDocSaveDto = new RevDocSaveDto();
//        revDocSaveDto.setEnforceStartDate(LocalDateTime.now().plusDays(5));
//        revDocSaveDto.setEnforceEndDate(LocalDateTime.now().plusDays(10));
//
//        // multiParts
//        String fileName = "testDoc.pdf";
//        String contentType = "APPLICATION";
//        final String filePath = "src/test/resources/testFile/" + fileName; // 파일경로
//        FileInputStream fileInputStream = new FileInputStream(filePath);
//        MockMultipartFile docFile = new MockMultipartFile(
//                "d_", //name
//                fileName, //originalFilename
//                contentType,
//                fileInputStream
//        );
//        revDocSaveDto.setMultipartFile(docFile);
//
//        // when
//        ResponseEntity<Map<String,Object>> response =  revDocService.revDocSave(role, email, revDocSaveDto, servletRequest, servletResponse);
//
//        // then
//        Assertions.assertEquals("SUCCESS", Objects.requireNonNull(response.getBody()).get("message"));
//        Assertions.assertEquals(200, Objects.requireNonNull(response.getBody()).get("status"));
//    }
//
//}