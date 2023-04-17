//package com.app.kokonut.qna;
//
//import com.app.kokonut.admin.AdminRepository;
//import com.app.kokonut.admin.Admin;
//import com.app.kokonut.admin.enums.AuthorityRole;
//import com.app.kokonut.qna.dtos.QnaAnswerSaveDto;
//import com.app.kokonut.qna.dtos.QnaQuestionSaveDto;
//import com.app.kokonut.qna.dtos.QnaSearchDto;
//import com.app.kokonut.qnaFile.QnaFileRepository;
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
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//
//@AutoConfigureMockMvc
//@SpringBootTest
//class QnaKnServiceTest {
//    private final String masterEmail = "joy@kokonut.me";
//    private final String systemEmail = "kokonut_admin@kokonut.me";
//
//    private Integer masterIdx;
//    private Integer systemIdx;
//
//    private String masterRole;
//    private String systemRole;
//
//    private Integer savedQnaIdx01;
//    private Integer savedQnaIdx02;
//    private Integer savedQnaIdx03;
//
//    @Autowired
//    private QnaService qnaService;
//
//    @Autowired
//    private QnaRepository qnaRepository;
//    @Autowired
//    private AdminRepository adminRepository;
//    @Autowired
//    private QnaFileRepository qnaFileRepository;
//
//    MockHttpServletRequest servletRequest;
//    MockHttpServletResponse servletResponse;
//    MockMvc mvc;
//    @BeforeEach
//        void testDataInsert() {
//            servletRequest = new MockHttpServletRequest();
//            servletResponse = new MockHttpServletResponse();
//
//            // 테스트용 masterAdmin
//            Admin masterAdmin = Admin.builder()
//                .email(masterEmail)
//                .password("test")
//                .roleName(AuthorityRole.ROLE_MASTER)
//                .regdate(LocalDateTime.now())
//                .state(1)
//                .build();
//
//
//        // 테스트용 systemAdmin
//        Admin systemAdmin = Admin.builder()
//                .email(systemEmail)
//                .name("코코넛시스템관리자")
//                .password("test")
//                .roleName(AuthorityRole.ROLE_SYSTEM)
//                .regdate(LocalDateTime.now())
//                .state(1)
//                .build();
//
//        Admin master = adminRepository.save(masterAdmin);
//        Admin system = adminRepository.save(systemAdmin);
//
//        masterIdx = master.getIdx();
//        systemIdx = system.getIdx();
//
//        masterRole = master.getRoleName().name();
//        systemRole = system.getRoleName().getCode();
//
//        // 테스트용 1:1 문의 내용 등록
//        // 첨부파일 없는 문의글
//        Qna savedQna01 = new Qna();
//        savedQna01.setadminId(masterIdx);
//        savedQna01.setTitle("제목 : 코코넛 서비스이용 문의드립니다.");
//        savedQna01.setContent("내용 : 코코넛 서비스이용 문의 내용입니다.");
//        // savedQna01.setFileGroupId();
//        savedQna01.setType(3); // 분류(0:기타,1:회원정보,2:사업자정보,3:Kokonut서비스,4:결제)
//        savedQna01.setRegdate(LocalDateTime.now());
//        savedQna01.setState(0);// 상태(0:답변대기,1:답변완료)
//        savedQnaIdx01 = qnaRepository.save(savedQna01).getIdx();
//
//        // 첨부파일 없는 문의글
//        Qna savedQna02 = new Qna();
//        savedQna02.setadminId(masterIdx);
//        savedQna02.setTitle("제목 : 코코넛 기타 문의드립니다.");
//        savedQna02.setContent("내용 : 코코넛 기타 문의 내용입니다.");
//        // savedQna01.setFileGroupId();
//        savedQna02.setType(0); // 분류(0:기타,1:회원정보,2:사업자정보,3:Kokonut서비스,4:결제)
//        savedQna02.setRegdate(LocalDateTime.now());
//        savedQna02.setState(1);// 상태(0:답변대기,1:답변완료)
//        savedQnaIdx02 = qnaRepository.save(savedQna01).getIdx();
//
//    }
//
//    @AfterEach
//    void testDataDelete() {
//        adminRepository.deleteAll();
//        qnaRepository.deleteAll();
//        qnaFileRepository.deleteAll();
//    }
//
//    @Test
//    @DisplayName("1:1 문의 목록 조회 성공 테스트")
//    void qnaListSuccess() {
//        List<Qna> qnaList = new ArrayList<>();
//        for(int i=1; i<6; i++) {
//            Qna saveQna = new Qna();
//            saveQna.setadminId(masterIdx);
//            saveQna.setTitle("제목 : 코코넛 기타 문의드립니다." + i);
//            saveQna.setContent("내용 : 코코넛 기타 문의 내용입니다." + i);
//            // savedQna01.setFileGroupId();
//            saveQna.setType(0); // 분류(0:기타,1:회원정보,2:사업자정보,3:Kokonut서비스,4:결제)
//            saveQna.setRegdate(LocalDateTime.now());
//            saveQna.setState(0);// 상태(0:답변대기,1:답변완료)
//            qnaList.add(saveQna);
//        }
//        qnaRepository.saveAll(qnaList);
//
//        List<Qna> qnaList2 = new ArrayList<>();
//        for(int i=7; i<18; i++) {
//            Qna saveQna2 = new Qna();
//            saveQna2.setadminId(masterIdx);
//            saveQna2.setTitle("제목 : 코코넛 결제 문의드립니다." + i);
//            saveQna2.setContent("내용 : 코코넛 결제 문의 내용입니다." + i);
//            // saveQna2.setFileGroupId();
//            saveQna2.setType(4); // 분류(0:기타,1:회원정보,2:사업자정보,3:Kokonut서비스,4:결제)
//            saveQna2.setRegdate(LocalDateTime.now());
//            saveQna2.setState(1);// 상태(0:답변대기,1:답변완료)
//            qnaList.add(saveQna2);
//        }
//        qnaRepository.saveAll(qnaList2);
//
//        // given
//        PageRequest pageable = PageRequest.of(0, 10);
//        QnaSearchDto qnaSearchDto = new QnaSearchDto();
//        qnaSearchDto.setState(1);
//        qnaSearchDto.setSearchText("결제");
//
//        // 시스템 관리자 접근
//        String userRole = systemRole;
//        String userEmail = systemEmail;
//        // 마스터 관리자 접근 String userRole = masterRole;
//        // when
//        ResponseEntity<Map<String,Object>> response =  qnaService.qnaList(userRole, userEmail, pageable);
//
//        // then
//        Assertions.assertEquals("SUCCESS", Objects.requireNonNull(response.getBody()).get("message"));
//        Assertions.assertEquals(200, Objects.requireNonNull(response.getBody()).get("status"));
//    }
//
//    @Test
//    @DisplayName("1:1 문의 상세 내용 조회 성공 테스트")
//    void qnaDetailSuccess() {
//        // given - none
//        // when
//        ResponseEntity<Map<String,Object>> response =  qnaService.qnaDetail("[SYSTEM]", "joy@kokonut.me", savedQnaIdx01);
//
//        // then
//        Assertions.assertEquals("SUCCESS", Objects.requireNonNull(response.getBody()).get("message"));
//        Assertions.assertEquals(200, Objects.requireNonNull(response.getBody()).get("status"));
//    }
//
//    @Test
//    @DisplayName("1:1 문의 상세 내용 조회 실패 테스트 - 해당 idx의 1:1 문의 게시글을 조회할 수 없습니다. 문의 게시글 idx : ")
//    void qnaDetailFail() {
//        // given - none
//        // when
//        ResponseEntity<Map<String,Object>> response =  qnaService.qnaDetail("[SYSTEM]", "joy@kokonut.me",3);
//
//        // then
//        Assertions.assertEquals("Error", Objects.requireNonNull(response.getBody()).get("message"));
//        Assertions.assertEquals(500, Objects.requireNonNull(response.getBody()).get("status"));
//
//    }
//
//    @Test
//    @DisplayName("문의 등록 성공 테스트 - 파일 첨부 없음")
//    void questionSaveSuccess() throws IOException {
//        // given
//        List<MultipartFile> multipartFiles = new ArrayList<>();
//
//        QnaQuestionSaveDto qnaQuestionSaveDto = new QnaQuestionSaveDto();
//        qnaQuestionSaveDto.setTitle("문의드립니다.");
//        qnaQuestionSaveDto.setContent("문의내용입니다...");
//        qnaQuestionSaveDto.setType(0);
//        qnaQuestionSaveDto.setRegdate(LocalDateTime.now());
//        qnaQuestionSaveDto.setMultipartFiles(multipartFiles);
//
//        // when
//        ResponseEntity<Map<String,Object>> response =  qnaService.questionSave("[SYSTEM]",masterEmail, qnaQuestionSaveDto, servletRequest, servletResponse);
//
//        // then
//        Assertions.assertEquals("SUCCESS", Objects.requireNonNull(response.getBody()).get("message"));
//        Assertions.assertEquals(200, Objects.requireNonNull(response.getBody()).get("status"));
//    }
//
//    @Test
//    @DisplayName("문의 등록 성공 테스트 - 파일 첨부 있음")
//    void questionSaveSuccess1() throws IOException {
//        // given
//        List<MultipartFile> multipartFiles = new ArrayList<>();
//
//        QnaQuestionSaveDto qnaQuestionSaveDto = new QnaQuestionSaveDto();
//        qnaQuestionSaveDto.setTitle("문의드립니당.");
//        qnaQuestionSaveDto.setContent("문의내용입니다. 첨부파일도 있습니다.");
//        qnaQuestionSaveDto.setType(3);
//        qnaQuestionSaveDto.setRegdate(LocalDateTime.now());
//
//        String fileName1 = "testImg1";
//        String fileName2 = "testImg2";
//        String contentType = "IMAGE";
//        final String filePath1 = "src/test/resources/testImage/"+fileName1+".png"; //파일경로
//        final String filePath2 = "src/test/resources/testImage/"+fileName2+".png"; //파일경로
//        FileInputStream fileInputStream1 = new FileInputStream(filePath1);
//        FileInputStream fileInputStream2 = new FileInputStream(filePath2);
//        MockMultipartFile image1 = new MockMultipartFile(
//                "images", //name
//                fileName1 + ".png", //originalFilename
//                contentType,
//                fileInputStream1
//        );
//        MockMultipartFile image2 = new MockMultipartFile(
//                "images", //name
//                fileName2 + "." + contentType, //originalFilename
//                contentType,
//                fileInputStream2
//        );
//
//        multipartFiles.add(image1);
//        multipartFiles.add(image2);
//
//        qnaQuestionSaveDto.setMultipartFiles(multipartFiles);
//
//        // when
//        ResponseEntity<Map<String,Object>> response =  qnaService.questionSave("[SYSTEM]",masterEmail, qnaQuestionSaveDto, servletRequest, servletResponse);
//
//        // then
//        Assertions.assertEquals("SUCCESS", Objects.requireNonNull(response.getBody()).get("message"));
//        Assertions.assertEquals(200, Objects.requireNonNull(response.getBody()).get("status"));
//    }
//
//    @Test
//    @DisplayName("답변 등록 성공 테스트")
//    void answerSave() throws IOException {
//        //String email, QnaAnswerSaveDto qnaAnswerSaveDto
//        QnaAnswerSaveDto qnaAnswerSaveDto = new QnaAnswerSaveDto();
//        qnaAnswerSaveDto.setIdx(savedQnaIdx01);
//        qnaAnswerSaveDto.setAnswer("    >>    !!!!!!!!!답변입니다..");
//
//        // when
//        ResponseEntity<Map<String,Object>> response =  qnaService.answerSave("[SYSTEM]",systemEmail, qnaAnswerSaveDto);
//
//        // then
//        Assertions.assertEquals("SUCCESS", Objects.requireNonNull(response.getBody()).get("message"));
//        Assertions.assertEquals(200, Objects.requireNonNull(response.getBody()).get("status"));
//    }
//}