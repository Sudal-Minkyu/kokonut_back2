package com.app.kokonut.company.companyitem;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.category.categorydefault.CategoryDefaultRepository;
import com.app.kokonut.category.categorydefault.dtos.CategoryDefaultListDto;
import com.app.kokonut.category.categorydefault.dtos.CategoryDefaultListSubDto;
import com.app.kokonut.category.categoryitem.CategoryItemRepository;
import com.app.kokonut.category.categoryitem.dtos.CategoryItemListDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.CommonUtil;
import com.app.kokonut.company.company.Company;
import com.app.kokonut.company.company.CompanyRepository;
import com.app.kokonut.company.companyitem.dtos.CompanyItemListDto;
import com.app.kokonut.company.companytable.CompanyTable;
import com.app.kokonut.company.companytable.CompanyTableRepository;
import com.app.kokonut.company.companytable.dtos.CompanyPrivacyTableListDto;
import com.app.kokonut.company.companytable.dtos.CompanyTableListDto;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dtos.ActivityCode;
import com.app.kokonutuser.KokonutUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Woody
 * Date : 2023-04-11
 * Time :
 * Remark :
 */
@Slf4j
@Service
public class CompanyItemService {

    private final KokonutUserService kokonutUserService;
    private final HistoryService historyService;

    private final AdminRepository adminRepository;
    private final CompanyRepository companyRepository;
    private final CompanyItemRepository companyItemRepository;
    private final CompanyTableRepository companyTableRepository;
    private final CategoryDefaultRepository categoryDefaultRepository;
    private final CategoryItemRepository categoryItemRepository;

    @Autowired
    public CompanyItemService(KokonutUserService kokonutUserService, HistoryService historyService,
                              AdminRepository adminRepository, CompanyRepository companyRepository, CompanyItemRepository companyItemRepository,
                              CompanyTableRepository companyTableRepository, CategoryDefaultRepository categoryDefaultRepository,
                              CategoryItemRepository categoryItemRepository){
        this.kokonutUserService = kokonutUserService;
        this.historyService = historyService;
        this.adminRepository = adminRepository;
        this.companyRepository = companyRepository;
        this.companyItemRepository = companyItemRepository;
        this.companyTableRepository = companyTableRepository;
        this.categoryDefaultRepository = categoryDefaultRepository;
        this.categoryItemRepository = categoryItemRepository;
    }

    // 기본 카테고리 항목 호출
    public ResponseEntity<Map<String, Object>> categoryList() {
        log.info("categoryList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<CategoryDefaultListDto> companyCategoryListDtos = new ArrayList<>();
        CategoryDefaultListDto categoryDefaultListDto;

        List<CategoryDefaultListSubDto> categoryDefaultListSubDtos = categoryDefaultRepository.findByCategoryDefaultList();

        for(CategoryDefaultListSubDto categoryDefaultListSubDto : categoryDefaultListSubDtos) {

            categoryDefaultListDto = new CategoryDefaultListDto();
            List<CategoryItemListDto> categoryItemListDtoList = categoryItemRepository.findByCategoryItemList(categoryDefaultListSubDto.getCdId());

            categoryDefaultListDto.setCdName(categoryDefaultListSubDto.getCdName());
            categoryDefaultListDto.setCategoryItemListDtoList(categoryItemListDtoList);
            companyCategoryListDtos.add(categoryDefaultListDto);

        }

        data.put("defaultCategoryList", companyCategoryListDtos);

        return ResponseEntity.ok(res.success(data));
    }

    // 추가 항목리스트 호출
    public ResponseEntity<Map<String, Object>> addItemList(JwtFilterDto jwtFilterDto) {
        log.info("addItemList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
        String companyCode = adminCompanyInfoDto.getCompanyCode();
//        log.info("companyCode : "+companyCode);

        List<CompanyItemListDto> companyCategoryListDtoList = companyItemRepository.findByItemList(companyCode);
        data.put("itemList", companyCategoryListDtoList);

        return ResponseEntity.ok(res.success(data));
    }

    // 추가 카테고리의 항목을 추가한다.
    @Transactional
    public ResponseEntity<Map<String, Object>> saveItem(JwtFilterDto jwtFilterDto, String ciName, Integer ciSecurity) {
        log.info("saveItem 호출");

        log.info("ciName : "+ciName);
        log.info("ciSecurity : "+ciSecurity);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
        Long adminId = adminCompanyInfoDto.getAdminId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();

        ActivityCode activityCode;
        String ip = CommonUtil.publicIp();
        Long activityHistoryId;

        if(companyItemRepository.existsByCiNameAndCpCode(ciName, companyCode) || categoryItemRepository.existsByCddName(ciName)) {
            log.error("이미 등록되어 있는 항목입니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO087.getCode(), ResponseErrorCode.KO087.getDesc()));
        } else {
            // 추가카테고리의 항목 추가 코드
            activityCode = ActivityCode.AC_42;

            // 활동이력 저장 -> 비정상 모드
            activityHistoryId = historyService.insertHistory(2, adminId, activityCode,
                    companyCode+" - ", "", ip, 0, email);

            CompanyItem companyItem = new CompanyItem();
            companyItem.setCpCode(companyCode);
            companyItem.setCiName(ciName);
            companyItem.setCiSecurity(ciSecurity);
            companyItem.setInsert_email(jwtFilterDto.getEmail());
            companyItem.setInsert_date(LocalDateTime.now());
            companyItemRepository.save(companyItem);

            historyService.updateHistory(activityHistoryId, null, "", 1);
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 추가 카테고리의 항목을 수정한다.
    @Transactional
    public ResponseEntity<Map<String, Object>> updateItem(Long ciId, String ciName, JwtFilterDto jwtFilterDto) {
        log.info("updateItem 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
        Long adminId = adminCompanyInfoDto.getAdminId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();

        ActivityCode activityCode;
        String ip = CommonUtil.publicIp();
        Long activityHistoryId;
        if(companyItemRepository.existsByCiNameAndCpCode(ciName, companyCode)) {
            log.error("이미 등록되어 있는 항목입니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO087.getCode(), ResponseErrorCode.KO087.getDesc()));
        } else {
            Optional<CompanyItem> optionalCompanyItem = companyItemRepository.findById(ciId);
            if(optionalCompanyItem.isPresent()) {
                // 추가카테고리의 항목 수정 코드
                activityCode = ActivityCode.AC_44;

                // 활동이력 저장 -> 비정상 모드
                activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                        companyCode+" - ", "", ip,0, email);

                optionalCompanyItem.get().setCiName(ciName);
                optionalCompanyItem.get().setModify_email(jwtFilterDto.getEmail());
                optionalCompanyItem.get().setModify_date(LocalDateTime.now());
                companyItemRepository.save(optionalCompanyItem.get());

                historyService.updateHistory(activityHistoryId, null, "", 1);
            } else {
                log.error("존재하지 않은 항목입니다. 새로고침이후 진행해주세요.");
                return ResponseEntity.ok(res.fail(ResponseErrorCode.KO091.getCode(), ResponseErrorCode.KO091.getDesc()));
            }

        }

        return ResponseEntity.ok(res.success(data));
    }

    // 추가 카테고리의 항목을 삭제한다.
    @Transactional
    public ResponseEntity<Map<String, Object>> deleteItem(Long ciId, JwtFilterDto jwtFilterDto) {
        log.info("deleteItem 호출");

        log.info("ciId : "+ciId);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
        Long adminId = adminCompanyInfoDto.getAdminId();
        String companyCode = adminCompanyInfoDto.getCompanyCode();

        ActivityCode activityCode;
        String ip = CommonUtil.publicIp();
        Long activityHistoryId;

        Optional<CompanyItem> optionalCompanyItem = companyItemRepository.findById(ciId);
        if(optionalCompanyItem.isPresent()) {
            // 추가카테고리의 항목 삭제 코드
            activityCode = ActivityCode.AC_45;

            // 활동이력 저장 -> 비정상 모드
            activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    companyCode+" - ", "", ip,0, email);

            companyItemRepository.delete(optionalCompanyItem.get());

            historyService.updateHistory(activityHistoryId, null, "", 1);
        } else {
            log.error("존재하지 않은 항목입니다. 새로고침이후 진행해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO091.getCode(), ResponseErrorCode.KO091.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 테이블 추가
    @Transactional
    public ResponseEntity<Map<String, Object>> userTableSave(JwtFilterDto jwtFilterDto, String ctDesignation) {
        log.info("userTableSave 호출");

        log.info("ctDesignation : "+ctDesignation);

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        if(ctDesignation.equals("")) {
            log.error("추가할 테이블명을 입력해주세요.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO088.getCode(), ResponseErrorCode.KO088.getDesc()));
        }

        String email = jwtFilterDto.getEmail();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
        Long adminId = adminCompanyInfoDto.getAdminId();
        String cpCode = adminCompanyInfoDto.getCompanyCode();

        ActivityCode activityCode;
        String ip = CommonUtil.publicIp();
        Long activityHistoryId;
        if(companyTableRepository.existsByCtDesignation(ctDesignation)) {
            log.error("이미 등록되어 있는 테이블명 입니다.");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO088.getCode(), ResponseErrorCode.KO088.getDesc()));
        } else {
            // 테이블추가의 활동 코드
            activityCode = ActivityCode.AC_16;

            // 활동이력 저장 -> 비정상 모드
            activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
                    cpCode+" - ", "", ip,0, email);

            Optional<Company> optionalCompany = companyRepository.findByCpCode(cpCode);

            int cpTableCount;
            if(optionalCompany.isPresent()) {
                cpTableCount = optionalCompany.get().getCpTableCount()+1;
                optionalCompany.get().setCpTableCount(cpTableCount);
            } else {
                log.error("userTableSave -> 존재하지않은 회사입니다.");
                cpTableCount = 0;
            }

            String ctName = cpCode+"_"+cpTableCount;

            CompanyTable companyTable = new CompanyTable();
            companyTable.setCpCode(cpCode);
            companyTable.setCtName(ctName);
            companyTable.setCtTableCount(String.valueOf(cpTableCount));
            companyTable.setCtDesignation(ctDesignation);
            companyTable.setCtAddColumnCount(1);
            companyTable.setCtNameStatus("");
            companyTable.setCtPhoneStatus("");
            companyTable.setCtBirthStatus("");
            companyTable.setCtGenderStatus("");
            companyTable.setCtEmailStatus("");
            companyTable.setCtAddColumnSecurityCount(0);
            companyTable.setCtAddColumnUniqueCount(0);
            companyTable.setCtAddColumnSensitiveCount(0);
            companyTable.setCtGenderStatus("");
            companyTable.setCtEmailStatus("");
            companyTable.setInsert_email(jwtFilterDto.getEmail());
            companyTable.setInsert_date(LocalDateTime.now());
            companyTableRepository.save(companyTable);

            boolean result = kokonutUserService.createTableKokonutUser(ctName, 1);
            if(result) {

                if(optionalCompany.isPresent()) {
                    companyRepository.save(optionalCompany.get());
                } else {
                    log.error("userTableSave -> 존재하지않은 회사입니다.");
                }

                historyService.updateHistory(activityHistoryId,
                        null, "", 1);
            }
        }

        return ResponseEntity.ok(res.success(data));
    }

    // 유저테이블 리스트 호출
    public ResponseEntity<Map<String, Object>> userTableList(JwtFilterDto jwtFilterDto) {
        log.info("userTableList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
        String companyCode = adminCompanyInfoDto.getCompanyCode();

        List<CompanyTableListDto> companyTableListDtos = companyTableRepository.findByTableList(companyCode);

        // 각 리스트별로 보내기
        data.put("companyTableList", companyTableListDtos);

        return ResponseEntity.ok(res.success(data));
    }

    // 개인정보검색용 테이블리스트 호출
    public ResponseEntity<Map<String, Object>> privacyTableList(JwtFilterDto jwtFilterDto) {
        log.info("privacyTableList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
        String companyCode = adminCompanyInfoDto.getCompanyCode();

        List<CompanyPrivacyTableListDto> companyPrivacyTableListDtos = companyTableRepository.findByPrivacyTableList(companyCode);

        // 각 리스트별로 보내기
        data.put("companyTableList", companyPrivacyTableListDtos);

        return ResponseEntity.ok(res.success(data));
    }

}
