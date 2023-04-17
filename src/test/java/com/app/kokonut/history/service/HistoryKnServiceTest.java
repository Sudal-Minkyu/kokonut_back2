package com.app.kokonut.history.service;

import com.app.kokonut.history.HistoryRepository;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dto.ActivityCode;
import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.company.company.CompanyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@AutoConfigureMockMvc
@SpringBootTest
class HistoryKnServiceTest {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CompanyRepository companyRepository;

//    @Autowired
//    private ActivityRepository activityRepository;

//    EntityManager entityManager;

    // 테스트하기 사전데이터 넣기
//    @BeforeEach
//    void testDataInsert(){
//
//        // https://devbksheen.tistory.com/entry/H2-Database-%EC%82%AC%EC%9A%A9%EC%9E%90-%EC%A0%95%EC%9D%98-%ED%95%A8%EC%88%98-%EB%A7%8C%EB%93%A4%EA%B8%B0
//        // 이게 왜 안될까..
////        entityManager
////                .createNativeQuery("CREATE ALIAS IF NOT EXISTS DATE_FORMAT FOR \"com.app.kokonut.H2CustomAlias.date_format\"")
////                .executeUpdate();
//
//        LocalDateTime systemDate = LocalDateTime.now();
//        System.out.println("현재 날짜 : "+systemDate);
//
//        Company company = new Company();
//        company.setCompanyName("기업명");
//        company.setRegdate(LocalDateTime.now());
//        Company saveCompany = companyRepository.save(company);
//        System.out.println("저장된 saveCompany : "+saveCompany);
//
//        Admin admin = new Admin();
//        admin.setcompanyId(saveCompany.getIdx());
//        admin.setName("사용자");
//        Admin saveAdmin = adminRepository.save(admin);
//        System.out.println("저장된 saveAdmin : "+saveAdmin);
//
////        Activity activity = new Activity();
////        activity.setType(1);
////        activity.setRegdate(systemDate);
////        activity.setModifyDate(systemDate);
////        Activity saveActivity = activityRepository.save(activity);
////        System.out.println("저장된 saveAdmin : "+saveAdmin);
//
//
//        // given
//        Long adminId = saveadmin.getAdminId();
//        Long companyId = saveCompany.getIdx();
////        int activityIdx = saveActivity.getIdx();
//
//        ActivityHistory activityHistory = new ActivityHistory();
//
//        activityHistory.setadminId(adminId);
//        activityHistory.setcompanyId(companyId);
//        activityHistory.setType(2);
//        activityHistory.setRegdate(systemDate);
//
//        activityHistoryRepository.save(activityHistory);
//
//    }
//
//    // 사전데이터 삭제
//    @AfterEach
//    void testDataDelete(){
//        adminRepository.deleteAll();
//        activityHistoryRepository.deleteAll();
//        adminLevelRepository.deleteAll();
////        activityRepository.deleteAll();
//    }

//    @Test
//    @DisplayName("활동내역 Column 리스트 조회 테스트")
//    public void findByActivityHistoryColumnListTest(){
//        // when
//        List<Column> columnList = activityHistoryService.findByActivityHistoryColumnList();
//        System.out.println("columnList : "+ columnList);
//    }

//    @Test
//    @DisplayName("활동내역 리스트 조회 테스트1 : 타입이 4일 경우 ->  type In 2,3")
//    public void findByActivityHistoryListTest1(){
//
//        // given
//        ActivityHistorySearchDto activityHistorySearchDto = new ActivityHistorySearchDto();
//        activityHistorySearchDto.setType(4);
//
//        // when
//        List<ActivityHistoryListDto> activityHistoryListDtos = activityHistoryService.findByActivityHistoryList(activityHistorySearchDto);
//        System.out.println("activityHistoryListDtos : "+ activityHistoryListDtos);
//    }

//    @Test
//    @DisplayName("활동내역 리스트 조회 테스트2 : 타입이 1일경우 type = 타입")
//    public void findByActivityHistoryListTest2(){
//
//        // given
//        ActivityHistorySearchDto activityHistorySearchDto = new ActivityHistorySearchDto();
//        activityHistorySearchDto.setType(1);
//
//        // when
//        List<ActivityHistoryListDto> activityHistoryListDtos = activityHistoryService.findByActivityHistoryList(activityHistorySearchDto);
//        System.out.println("activityHistoryListDtos : "+ activityHistoryListDtos);
//    }
//
//    @Test
//    @DisplayName("활동내역 정보 리스트 조회 테스트 : type 4 일 경우")
//    public void findByActivityHistoryBycompanyIdAndTypeListTest1(){
//
//        // given
//        Long companyId = 1;
//        Integer type = 4;
//
//        // when
//        List<ActivityHistoryInfoListDto> activityHistoryInfoListDtos = activityHistoryService.findByActivityHistoryBycompanyIdAndTypeList(companyId, type);
//        System.out.println("activityHistoryInfoListDtos : "+ activityHistoryInfoListDtos);
//    }

//    @Test
//    @DisplayName("활동내역 정보 리스트 조회 테스트 : type 2 경우")
//    public void findByActivityHistoryBycompanyIdAndTypeListTest2(){
//
//        // given
//        Long companyId = 1;
//        Integer type = 2;
//
//        // when
//        List<ActivityHistoryInfoListDto> activityHistoryInfoListDtos = activityHistoryService.findByActivityHistoryBycompanyIdAndTypeList(companyId, type);
//        System.out.println("activityHistoryInfoListDtos : "+ activityHistoryInfoListDtos);
//    }

//    @Test
//    @DisplayName("활동내역 상세보기 테스트")
//    public void findByActivityHistoryByIdxTest(){
//
//        // given
////        ActivityHistory saveActivityHistory = new ActivityHistory();
////        saveActivityHistory.setRegdate(new Date());
////
////        ActivityHistory activityHistory = activityHistoryRepository.save(saveActivityHistory);
////
////        Integer idx = activityHistory.getIdx();
////        System.out.println("저장한 idx : "+ idx);
//
//        // when
//        ActivityHistoryDto activityHistoryDto = activityHistoryService.findByActivityHistoryByIdx(1);
//        System.out.println("activityHistoryDto : "+ activityHistoryDto);
//
////        activityHistoryRepository.delete(activityHistory);
//
//    }

    // Function "DATE_FORMAT" not found; SQL statement: 에러발생
//    @Test
//    @DisplayName("활동내역 통계 테스트")
//    public void findByActivityHistoryStatisticsTest(){
//
//        // given
//        Long companyId = 1;
//        int day = 1;
//
//        // when
//        ActivityHistoryStatisticsDto activityHistoryStatisticsDto = activityHistoryService.findByActivityHistoryStatistics(companyId, day);
//        System.out.println("activityHistoryStatisticsDto : "+ activityHistoryStatisticsDto);
//    }

    @Test
    @DisplayName("활동내역 저장 테스트")
    public void insertHistoryTest() {

        int type = 1;
        Long adminId = 1L;
        ActivityCode activityCode = ActivityCode.AC_01;
        String activityDetail = "저장테스트";
        String reason = "저장테스트2";
        String ipAddr = "ip테스트";
        int state = 1;

        Long saveactivityHistoryId = historyService.insertHistory(type, adminId, activityCode, activityDetail, reason, ipAddr, state, "");
        System.out.println("저장된 IDX : "+saveactivityHistoryId);
    }




}