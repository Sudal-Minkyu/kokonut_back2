package com.app.kokonut.company.companycategory;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.company.companycategory.dtos.CompanyCategoryListDto;
import com.app.kokonut.company.companytable.CompanyTableRepository;
import com.app.kokonut.company.companytable.dtos.CompanyTableListDto;
import com.app.kokonut.company.companytable.dtos.CompanyTableSubListDto;
import com.app.kokonutuser.DynamicUserService;
import com.app.kokonutuser.dtos.KokonutUserFieldListDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Woody
 * Date : 2023-04-11
 * Time :
 * Remark :
 */
@Slf4j
@Service
public class CompanyCategoryService {

    private final AdminRepository adminRepository;
    private final CompanyCategoryRepository companyCategoryRepository;
    private final CompanyTableRepository companyTableRepository;
    private final DynamicUserService dynamicUserService;

    @Autowired
    public CompanyCategoryService(AdminRepository adminRepository, CompanyCategoryRepository companyCategoryRepository, CompanyTableRepository companyTableRepository, DynamicUserService dynamicUserService){
        this.adminRepository = adminRepository;
        this.companyCategoryRepository = companyCategoryRepository;
        this.companyTableRepository = companyTableRepository;
        this.dynamicUserService = dynamicUserService;
    }


    // 추가 카테고리 항목 호출
    public ResponseEntity<Map<String, Object>> addCategoryList(JwtFilterDto jwtFilterDto) {
        log.info("addCategoryList 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String email = jwtFilterDto.getEmail();

        AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
        String companyCode = adminCompanyInfoDto.getCompanyCode();
//        log.info("companyCode : "+companyCode);

        List<CompanyCategoryListDto> companyCategoryListDtoList = companyCategoryRepository.findByCategoryList(companyCode);
        data.put("categoryList", companyCategoryListDtoList);

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
//        log.info("companyCode : "+companyCode);

        // 보낼 데이터를 담는 변수
        List<CompanyTableListDto> companyTableListDtos = new ArrayList<>();
        CompanyTableListDto companyTableListDto;

//        List<FranchiseManagerListDto> franchiseManagerListDtos =  franchiseRepository.findByManagerInFranchise(brCode);
//        List<HashMap<String,Object>> franchiseManagerData = new ArrayList<>();
//        HashMap<String,Object> franchiseManagerListInfo;
//        for (FranchiseManagerListDto franchiseManagerListDto: franchiseManagerListDtos) {
//            franchiseManagerListInfo = new HashMap<>();
//            franchiseManagerListInfo.put("frId", franchiseManagerListDto.getFrId());
//            franchiseManagerListInfo.put("frName", franchiseManagerListDto.getFrName());
//            franchiseManagerListInfo.put("frTagNo", franchiseManagerListDto.getFrTagNo());
//            franchiseManagerData.add(franchiseManagerListInfo);
//        }
//        if(franchiseManagerListDtos.size() != 0){
//            data.put("branchId",franchiseManagerListDtos.get(0).getBranchId());
//        }
//        data.put("franchiseList",franchiseManagerData);

//        // 해당 회사에 등록된 테이블의 리스트를 가져온다.
//        List<HashMap<String,Object>> companyTableColumnData = new ArrayList<>();
//        HashMap<String,Object> companyTableColumnDataInfo;

        List<CompanyTableSubListDto> companyTableSubListDtos = companyTableRepository.findByTableList(companyCode);
//        for(CompanyTableSubListDto companyTableSubListDto : companyTableSubListDtos) {
//            // 테이블의 컬럼리스트를 가져온다.
//            List<KokonutUserFieldListDto> kokonutUserFieldListDtos = dynamicUserService.tableColumnList(companyTableSubListDto.getCtName());
//
////            for(KokonutUserFieldListDto kokonutUserFieldListDto : kokonutUserFieldListDtos) {
////                companyTableColumnDataInfo = new HashMap<>();
////                companyTableColumnDataInfo.put("fieldName", kokonutUserFieldListDto.getFieldName());
////                companyTableColumnDataInfo.put("fieldComment", kokonutUserFieldListDto.getFieldComment());
////                companyTableColumnDataInfo.put("fieldSecrity", kokonutUserFieldListDto.getFieldSecrity());
////                companyTableColumnDataInfo.put("fieldCategory", kokonutUserFieldListDto.getFieldCategory());
////                companyTableColumnDataInfo.put("fieldColor", kokonutUserFieldListDto.getFieldColor());
////                companyTableColumnData.add(companyTableColumnDataInfo);
////            }
//
//            // 하나로 묶어서 보내기
//            companyTableListDto = new CompanyTableListDto();
//            companyTableListDto.setCtName(companyTableSubListDto.getCtName());
//            companyTableListDto.setCtDesignation(companyTableSubListDto.getCtDesignation());
//            companyTableListDto.setKokonutUserFieldListDtos(kokonutUserFieldListDtos);
//
//            companyTableListDtos.add(companyTableListDto);
//        }

        // 각 리스트별로 보내기
        data.put("companyTableList", companyTableSubListDtos);
        data.put("companyColumnList", companyTableListDtos);

//        data.put("companyTableColumnData", companyTableColumnData);

        return ResponseEntity.ok(res.success(data));
    }
}
