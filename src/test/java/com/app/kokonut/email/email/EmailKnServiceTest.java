//package com.app.kokonut.email.email;
//
//import com.app.kokonut.admin.AdminRepository;
//import com.app.kokonut.admin.Admin;
//import com.app.kokonut.admin.enums.AuthorityRole;
//import com.app.kokonut.email.email.dtos.EmailDetailDto;
//import com.app.kokonut.email.emailGroup.EmailGroupRepository;
//import com.app.kokonut.email.emailGroup.EmailGroupService;
//import com.app.kokonut.common.ResponseErrorCode;
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
//class EmailKnServiceTest {
//
//    private final String email = "kokonut@kokonut.me";
//
//    private Integer saveadminId;
//
//    private Integer saveEmailIdx;
//
//    @Autowired
//    private EmailService emailService;
//
//    @Autowired
//    private EmailGroupService emailGroupService;
//
//    @Autowired
//    private EmailRepository emailRepository;
//
//    @Autowired
//    private AdminRepository adminRepository;
//
//    @Autowired
//    private EmailGroupRepository emailGroupRepository;
//
//    @BeforeEach
//    void testDataInsert() {
//
//        // 테스트용 admin
//        Admin admin = Admin.builder()
//                .email(email)
//                .password("test")
//                .roleName(AuthorityRole.ROLE_MASTER)
//                .regdate(LocalDateTime.now())
//                .build();
//
//
//
//        // 테스트용 admin2
//        Admin admin2 = Admin.builder()
//                .email("joy@kokonut.me")
//                .password("test")
//                .roleName(AuthorityRole.ROLE_MASTER)
//                .regdate(LocalDateTime.now())
//                .build();
//
//        // 테스트용 admin3
//        Admin admin3 = Admin.builder()
//                .email("test@kokonut.me")
//                .password("test")
//                .roleName(AuthorityRole.ROLE_MASTER)
//                .regdate(LocalDateTime.now())
//                .build();
//        saveadminId = adminRepository.save(admin).getIdx();
//        Integer saveadminId2 = adminRepository.save(admin2).getIdx();
//        Integer saveadminId3 = adminRepository.save(admin3).getIdx();
//
//
//        Email saveEmail = new Email();
//        saveEmail.setTitle("테스트제목");
//        saveEmail.setContents("테스트내용");
//        saveEmail.setSenderadminId(saveadminId);
//        saveEmail.setReceiverType("I");
//        saveEmail.setReceiveradminIdList("1");
//        saveEmail.setRegdate(LocalDateTime.now());
//        saveEmailIdx = emailRepository.save(saveEmail).getIdx();
////
////        // 테스트용 그룹 1
////        EmailGroup saveEmailGroup1 = new EmailGroup();
////        saveEmailGroup1.setIdx(1);
////        saveEmailGroup1.setName("테스트용 그룹 1");
////        saveEmailGroup1.setDesc("이메일 그룹 테스트를 위한 이메일 그룹");
////        saveEmailGroup1.setUseYn("Y");
////        saveEmailGroup1.setRegdate(LocalDateTime.now());
////        saveEmailGroup1.setadminIdList("1,2");
////
////        // 테스트용 그룹 2
////        EmailGroup saveEmailGroup2 = new EmailGroup();
////        saveEmailGroup2.setIdx(2);
////        saveEmailGroup2.setName("테스트용 그룹 2");
////        saveEmailGroup2.setDesc("이메일 그룹 테스트를 위한 이메일 그룹");
////        saveEmailGroup2.setUseYn("Y");
////        saveEmailGroup2.setRegdate(LocalDateTime.now());
////        saveEmailGroup2.setadminIdList("2,3");
////
////        Integer saveEmailGroupIdx1 = emailGroupRepository.save(saveEmailGroup1).getIdx();
////        Integer saveEmailGroupIdx2 = emailGroupRepository.save(saveEmailGroup2).getIdx();
//
//    }
//
//    @AfterEach
//    void testDataDelete() {
//        adminRepository.deleteAll();
//        emailRepository.deleteAll();
//        emailGroupRepository.deleteAll();
//    }
//
//    @Test
//    @DisplayName("이메일 리스트 호출 성공 테스트")
//    public void emailListTest1() {
//
//        List<Email> emailList = new ArrayList<>();
//        for(int i=1; i<11; i++) {
//            Email saveEmail = new Email();
//            saveEmail.setTitle("테스트제목"+i);
//            saveEmail.setContents("테스트내용"+i);
//            saveEmail.setSenderadminId(saveadminId);
//            saveEmail.setReceiverType("I");
//            saveEmail.setReceiveradminIdList("1");
//            saveEmail.setRegdate(LocalDateTime.now());
//            emailList.add(saveEmail);
//        }
//        emailRepository.saveAll(emailList);
//
//        // given
//        PageRequest pageable = PageRequest.of(0, 10);
//
//        // when
//        ResponseEntity<Map<String,Object>> response =  emailService.emailList(pageable);
//
//        // then
//        Assertions.assertEquals("SUCCESS", Objects.requireNonNull(response.getBody()).get("message"));
//        Assertions.assertEquals(200, Objects.requireNonNull(response.getBody()).get("status"));
//
//    }
//
//    @Test
//    @DisplayName("이메일 보내기 성공 테스트")
//    public void sendEmailTest1() {
//
//        // given
//        EmailDetailDto emailDetailDto = new EmailDetailDto();
//        emailDetailDto.setSenderadminId(saveadminId);
//        emailDetailDto.setReceiverType("I");
//        emailDetailDto.setReceiveradminIdList("1");
//        emailDetailDto.setTitle("제목 후");
//        emailDetailDto.setContents("내용 후...");
//
//        // when
//        ResponseEntity<Map<String,Object>> response =  emailService.sendEmail(email, emailDetailDto);
//
//        // then
//        Assertions.assertEquals("SUCCESS", Objects.requireNonNull(response.getBody()).get("message"));
//        Assertions.assertEquals(200, Objects.requireNonNull(response.getBody()).get("status"));
//
//    }
//
//    @Test
//    @DisplayName("이메일 보내기 실패 테스트 - 받는사람 타입을 알 수 없을 경우")
//    public void sendEmailTest2() {
//
//        // given
//        EmailDetailDto emailDetailDto = new EmailDetailDto();
//        emailDetailDto.setSenderadminId(saveadminId);
//        emailDetailDto.setReceiverType("A");
//        emailDetailDto.setReceiveradminIdList("1");
//        emailDetailDto.setTitle("제목 후");
//        emailDetailDto.setContents("내용 후...");
//
//        // when
//        ResponseEntity<Map<String,Object>> response =  emailService.sendEmail(email, emailDetailDto);
//
//        // then
//        Assertions.assertEquals(ResponseErrorCode.KO040.getCode(), Objects.requireNonNull(response.getBody()).get("err_code"));
//        Assertions.assertEquals("Error", Objects.requireNonNull(response.getBody()).get("message"));
//        Assertions.assertEquals(500, Objects.requireNonNull(response.getBody()).get("status"));
//
//    }
//
//
//    @Test
//    @DisplayName("이메일 상세보기 성공 테스트")
//    @SuppressWarnings("unchecked")
//    public void sendEmailDetailTest1() {
//
//        // when
//        ResponseEntity<Map<String,Object>> response =  emailService.sendEmailDetail(saveEmailIdx);
//
//        HashMap<String,Object> sendData = (HashMap<String, Object>) Objects.requireNonNull(response.getBody()).get("sendData");
//        String emailList =  String.valueOf(sendData.get("emailList"));
//        String title = String.valueOf(sendData.get("title"));
//        String contents = String.valueOf(sendData.get("contents"));
//
//        // then
//        Assertions.assertEquals(email, emailList);
//        Assertions.assertEquals("테스트제목", title);
//        Assertions.assertEquals("테스트내용", contents);
//        Assertions.assertEquals("SUCCESS", Objects.requireNonNull(response.getBody()).get("message"));
//        Assertions.assertEquals(200, Objects.requireNonNull(response.getBody()).get("status"));
//
//    }
//
//    @Test
//    @DisplayName("이메일 상세보기 실패 테스트 - 요청한 idx가 Null값 일 경우")
//    public void sendEmailDetailTest2() {
//
//        // when
//        ResponseEntity<Map<String,Object>> response =  emailService.sendEmailDetail(null);
//
//        // then
//        Assertions.assertEquals(ResponseErrorCode.KO031.getCode(), Objects.requireNonNull(response.getBody()).get("err_code"));
//        Assertions.assertEquals("Error", Objects.requireNonNull(response.getBody()).get("message"));
//        Assertions.assertEquals(500, Objects.requireNonNull(response.getBody()).get("status"));
//
//    }
//
//    @Test
//    @DisplayName("이메일 상세보기 실패 테스트 - 요청한 idx가 존재하지 않을 경우")
//    public void sendEmailDetailTest3() {
//
//        // when
//        ResponseEntity<Map<String,Object>> response =  emailService.sendEmailDetail(2);
//
//        // then
//        Assertions.assertEquals(ResponseErrorCode.KO031.getCode(), Objects.requireNonNull(response.getBody()).get("err_code"));
//        Assertions.assertEquals("Error", Objects.requireNonNull(response.getBody()).get("message"));
//        Assertions.assertEquals(500, Objects.requireNonNull(response.getBody()).get("status"));
//
//    }
//
//    @Test
//    @DisplayName("그룹목록 조회 성공테스트")
//    public void emailGroupListTest1() {
//
//        // given
//        PageRequest pageable = PageRequest.of(0, 10);
//
//        // when
//        ResponseEntity<Map<String,Object>> response = emailService.emailTargetGroupList(pageable);
//
//        // then
//        Assertions.assertEquals("SUCCESS", Objects.requireNonNull(response.getBody()).get("message"));
//        Assertions.assertEquals(200, Objects.requireNonNull(response.getBody()).get("status"));
//
//    }
//
//}