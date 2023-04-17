//package com.app.kokonut.faq;
//
//import com.app.kokonut.admin.AdminRepository;
//import com.app.kokonut.admin.Admin;
//import com.app.kokonut.admin.enums.AuthorityRole;
//import com.app.kokonut.qna.Qna;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import java.time.LocalDateTime;
//
//@AutoConfigureMockMvc
//@SpringBootTest
//class FaqKnServiceTest {
//    /* 자주 묻는 질문 테스트 코드
//        1. 자주 묻는 질문 목록 조회 성공 테스트
//            - 사용자 권한에 따른 목록 조회 건수 다름. (시스템권한 : 모든 목록, 시스템권한 외 : 게시중 상태의 자주 묻는 질문 목록만 조회)
//        2. 자주 묻는 질문 상세 조회 성공 테스트
//            - 사용자 권한에 따른 접근여부 확인. (시스템 권한의 경우에만 상세 조회 가능)
//        3. 자주 묻는 질문 등록 성공 테스트 1
//            - idx 값에 따라 신규 등록
//        4. 자주 묻는 질문 등록 성공 테스트 2
//            - idx 값에 따라 내용 수정
//        5. 자주 묻는 질문 삭제 성공 테스트
//        6. 자주 묻는 질문 삭제 실패 테스트
//     */
//
//    private final String masterEmail = "joy@kokonut.me";
//    private Integer masterIdx;
//    private String masterRole;
//
//    private final String systemEmail = "kokonut_admin@kokonut.me";
//    private Integer systemIdx;
//    private String systemRole;
//
//    @Autowired
//    private FaqService faqService;
//
//    @Autowired
//    private FaqRepository faqRepo;
//    @Autowired
//    private AdminRepository adminRepo;
//
//    @BeforeEach
//    void testDataInsert() {
//        // 테스트용 masterAdmin
//        Admin masterAdmin = Admin.builder()
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
//        Admin master = adminRepo.save(masterAdmin);
//        Admin system = adminRepo.save(systemAdmin);
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
//
//    }
//
//    @AfterEach
//    void testDataDelete() {
//        adminRepo.deleteAll();
//    }
//
//}