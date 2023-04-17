//package com.app.kokonut.notice;
//
//import com.app.kokonut.admin.AdminRepository;
//import com.app.kokonut.admin.Admin;
//import com.app.kokonut.admin.enums.AuthorityRole;
//import com.app.kokonut.notice.dtos.NoticeDetailDto;
//import com.app.kokonut.notice.dtos.NoticeSearchDto;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.http.ResponseEntity;
//
//import java.time.LocalDateTime;
//import java.util.*;
//
//@AutoConfigureMockMvc
//@SpringBootTest
//class NoticeKnServiceTest {
//    /* 공지사항 서비스 테스트 코드
//     *  1. 공지사항 목록 조회 성공 테스트    - 사용자 권한에 따른 목록 조회
//     *  2. 공지사항 상세 조회 실패 테스트    - 사용자 권한에 따른 접근여부 확인. (시스템 권한의 경우에만 상세 조회 가능)
//     *     공지사항 상세 조회 성공 테스트    - 사용자 권한에 따른 접근여부 확인, 조회, 카운트 증가
//     *  3. 공지사항 등록 성공 테스트 1     - idx 값에 따라 신규 등록
//     *  4. 공지사항 등록 성공 테스트 2     - idx 값에 따라 내용 수정
//     *  5. 공지사항 삭제 성공 테스트
//     *  ...
//     */
//
//    // Service 목록
//    @Autowired
//    private NoticeService noticeService;
//
//    // Repository 목록
//    @Autowired
//    private NoticeRepository noticeRepository;
//    @Autowired
//    private AdminRepository adminRepository;
//
//    // 변수 목록
//    private Integer masterUserIdx;
//    private Integer systemUserIdx;
//
//    private String masterUserRole = "[MASTER]";
//    private String systemUserRole = "[SYSTEM]";
//
//    private String masterUserName;
//    private String systemUserName;
//
//    @BeforeEach
//    void testDataInsert() {
//        // 테스트용 masterAdmin
//        Admin masterAdmin = Admin.builder()
//                .email("master@kokonut.me")
//                .name("코코넛 마스터관리자")
//                .password("test")
//                .roleName(AuthorityRole.ROLE_MASTER)
//                .regdate(LocalDateTime.now())
//                .state(1)
//                .build();
//
//        // 테스트용 systemAdmin
//        Admin systemAdmin = Admin.builder()
//                .email("system@kokonut.me")
//                .name("코코넛 시스템관리자")
//                .password("test")
//                .roleName(AuthorityRole.ROLE_SYSTEM)
//                .regdate(LocalDateTime.now())
//                .state(1)
//                .build();
//
//
//        Admin master = adminRepository.save(masterAdmin);
//        Admin system = adminRepository.save(systemAdmin);
//
//        masterUserIdx = master.getIdx();
//        systemUserIdx = system.getIdx();
//
//        masterUserName = master.getName();
//        systemUserName = system.getName();
//
//        // 테스트용 공지사항 게시글 등록
//        Notice savedNotice = new Notice();
//        savedNotice.setIdx(1);
//
//        savedNotice.setTitle("[공지]코코넛 보안 관련 공지사항 입니다.");
//        savedNotice.setContent("코코넛 공지사항 내용 입니다.");
//        savedNotice.setIsNotice(0); // 0:일반,1:상단공지
//        savedNotice.setState(2);    // 0:게시중지,1:게시중,2:게시대기
//        savedNotice.setViewCount(0);
//        savedNotice.setRegistDate(LocalDateTime.now());
//
//        savedNotice.setadminId(systemUserIdx);
//        savedNotice.setRegdate(LocalDateTime.now());
//        savedNotice.setRegisterName(systemAdmin.getName());
//
//        noticeRepository.save(savedNotice);
//
//
//    }
//
//    @AfterEach
//    void testDataDelete() {
//        adminRepository.deleteAll();
//        noticeRepository.deleteAll();
//    }
//
//    @Test
//    @DisplayName("공지사항 목록 조회 성공 테스트")
//    void noticeList() {
//        List<Notice> notices = new ArrayList<>();
//        for(int i=2; i<10; i++) {
//            Notice saveNotice = new Notice();
//            saveNotice.setIdx(i);
//            saveNotice.setTitle("[공지] 2022년 " +i +"월 코코넛 주요 보안 이슈에 대한 공지사항 입니다.");
//            saveNotice.setContent( i +"월 코코넛 주요 이슈 공지사항 내용 입니다.");
//            saveNotice.setIsNotice(0); // 0:일반,1:상단공지
//            saveNotice.setState(1);    // 0:게시중지,1:게시중,2:게시대기
//            saveNotice.setViewCount(0);
//            saveNotice.setRegistDate(LocalDateTime.now());
//            saveNotice.setadminId(systemUserIdx);
//            saveNotice.setRegdate(LocalDateTime.now());
//            saveNotice.setRegisterName(systemUserName);
//            notices.add(saveNotice);
//        }
//        noticeRepository.saveAll(notices);
//
//        notices.clear();
//        for(int i=10; i<13; i++) {
//            Notice saveNotice = new Notice();
//            saveNotice.setIdx(i);
//            saveNotice.setTitle("[공지] 2022년 " +i +"월 코코넛 주요 보안 이슈에 대한 공지사항 입니다.");
//            saveNotice.setContent( i +"월 코코넛 주요 이슈 공지사항 내용 입니다.");
//            saveNotice.setIsNotice(0); // 0:일반,1:상단공지
//            saveNotice.setState(0);    // 0:게시중지,1:게시중,2:게시대기
//            saveNotice.setViewCount(0);
//            saveNotice.setRegistDate(LocalDateTime.now());
//            saveNotice.setadminId(systemUserIdx);
//            saveNotice.setRegdate(LocalDateTime.now());
//            saveNotice.setRegisterName(systemUserName);
//            notices.add(saveNotice);
//        }
//        noticeRepository.saveAll(notices);
//
//        // given
//        PageRequest pageable = PageRequest.of(0, 10);
//        NoticeSearchDto search = new NoticeSearchDto();
//        //search.setSearchText("12월");
//        //search.setIsNotice(0);
//        //search.setIsNotice(1);
//        //search.setState(0);
//
//        // when
//        ResponseEntity<Map<String,Object>> response =
//                noticeService.noticeList(masterUserRole, search, pageable);
//
//        System.out.println("################ " + response.getBody().get("total_rows") + "건 조회");
//        System.out.println(response.getBody().get("datalist"));
//        System.out.println("################ ");
//
//        // then
//        Assertions.assertEquals("SUCCESS", Objects.requireNonNull(response.getBody()).get("message"));
//        Assertions.assertEquals(200, Objects.requireNonNull(response.getBody()).get("status"));
//    }
//
//    @Test
//    @DisplayName("공지사항 목록 조회 실패 테스트")
//    void noticeDetail_1() {
//        // given
//        // when
//        ResponseEntity<Map<String,Object>> response =
//                noticeService.noticeDetail(masterUserRole, 1);
//        // then
//        Assertions.assertEquals("Error", Objects.requireNonNull(response.getBody()).get("message"));
//        Assertions.assertEquals(500, Objects.requireNonNull(response.getBody()).get("status"));
//    }
//
//    @Test
//    @DisplayName("공지사항 상세 조회 성공 테스트")
//    void noticeDetail_2() {
//        // given
//        // when
//        ResponseEntity<Map<String,Object>> response =
//                noticeService.noticeDetail(systemUserRole, 1);
//        System.out.println("####### "+noticeRepository.findById(1).get().getViewCount());
//        System.out.println(response.getBody().get("sendData"));
//        // then
//        Assertions.assertEquals("SUCCESS", Objects.requireNonNull(response.getBody()).get("message"));
//        Assertions.assertEquals(200, Objects.requireNonNull(response.getBody()).get("status"));
//    }
//
//    @Test
//    @DisplayName("공지사항 등록 성공 테스트 - 등록")
//    void noticeSave_1() {
//        // given
//        NoticeDetailDto saveNotice = new NoticeDetailDto();
//        saveNotice.setTitle("[공지] 2022년 " +2 +"월 코코넛 주요 보안 이슈에 대한 공지사항 입니다.");
//        saveNotice.setContent( 2 +"월 코코넛 주요 이슈 공지사항 내용 입니다.");
//        saveNotice.setIsNotice(0); // 0:일반,1:상단공지
//        saveNotice.setRegistDate(LocalDateTime.now());
//
//        // when
//        ResponseEntity<Map<String,Object>> response =
//                noticeService.noticeSave(systemUserRole, "system@kokonut.me", saveNotice);
//        if(noticeRepository.findById(2).isPresent()) {
//            System.out.println("####### 등록상태 (0:게시중지,1:게시중,2:게시대기) : " + noticeRepository.findById(2).get().getState());
//            System.out.println("####### idx : " + noticeRepository.findById(2).get().getIdx());
//            System.out.println("####### adminId : " + noticeRepository.findById(2).get().getadminId());
//            System.out.println("####### 등록일 : " + noticeRepository.findById(2).get().getRegdate());
//        }
//        // then
//        Assertions.assertEquals("SUCCESS", Objects.requireNonNull(response.getBody()).get("message"));
//        Assertions.assertEquals(200, Objects.requireNonNull(response.getBody()).get("status"));
//    }
//
//    @Test
//    @DisplayName("공지사항 등록 성공 테스트 - 수정")
//    void noticeSave_2() {
//        // given
//        NoticeDetailDto saveNotice = new NoticeDetailDto();
//        saveNotice.setIdx(1);
//        saveNotice.setTitle("[공지] 2022년 1월 코코넛 주요 보안 이슈에 대한 공지사항 입니다.");
//        saveNotice.setContent( "1월 코코넛 주요 이슈 공지사항 내용 입니다.");
//        saveNotice.setIsNotice(1); // 0:일반,1:상단공지
//        saveNotice.setRegistDate(LocalDateTime.now().minusDays(1));
//
//        Optional<Notice> beforeNoti = noticeRepository.findById(1);
//        // when
//        ResponseEntity<Map<String,Object>> response =
//                noticeService.noticeSave(systemUserRole, "system@kokonut.me", saveNotice);
//
//        Optional<Notice> afterNoti = noticeRepository.findById(1);
//
//        System.out.println("###############################################");
//        System.out.println("[변경] title          :: "+ beforeNoti.get().getTitle() +"->" + afterNoti.get().getTitle());
//        System.out.println("[변경] content        :: "+ beforeNoti.get().getContent() +"->" + afterNoti.get().getContent());
//        System.out.println("[변경] isNotice       :: "+ beforeNoti.get().getIsNotice() +"->" + afterNoti.get().getIsNotice());
//        System.out.println("[변경] registDate     :: "+ beforeNoti.get().getRegistDate() +"->" + afterNoti.get().getRegistDate());
//        System.out.println("[변경] modifierIdx    :: "+ beforeNoti.get().getModifierIdx() +"->" + afterNoti.get().getModifierIdx());
//        System.out.println("[변경] modifierName   :: "+ beforeNoti.get().getModifierName() +"->" + afterNoti.get().getModifierName());
//        System.out.println("[변경] modifyDate     :: "+ beforeNoti.get().getModifyDate() +"->" + afterNoti.get().getModifyDate());
//        System.out.println("[유지] adminId       :: "+ beforeNoti.get().getadminId() +"->" + afterNoti.get().getadminId());
//        System.out.println("[유지] idx            :: "+ beforeNoti.get().getIdx() +"->" + afterNoti.get().getIdx());
//        System.out.println("[유지] state          :: "+ beforeNoti.get().getState() +"->" + afterNoti.get().getState());
//        System.out.println("[유지] regDate        :: "+ beforeNoti.get().getRegdate() +"->" + afterNoti.get().getRegdate());
//        System.out.println("###############################################");
//
//        // then
//        Assertions.assertEquals("SUCCESS", Objects.requireNonNull(response.getBody()).get("message"));
//        Assertions.assertEquals(200, Objects.requireNonNull(response.getBody()).get("status"));
//    }
//
//    @Test
//    void noticeDelete() {
//    }
//
//    @Test
//    void noticeState() {
//    }
//}