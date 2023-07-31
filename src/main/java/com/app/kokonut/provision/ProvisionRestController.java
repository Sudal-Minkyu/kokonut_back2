package com.app.kokonut.provision;

import com.app.kokonut.admin.AdminService;
import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.configs.ExcelService;
import com.app.kokonut.provision.dtos.ProvisionSaveDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Woody
 * Date : 2023-01-17
 * Remark :
 */
@Slf4j
@RestController
@RequestMapping("/v2/api/Provision")
public class ProvisionRestController {

    private final AdminService adminService;
    private final ProvisionService provisionService;
    private final ExcelService excelService;

    @Autowired
    public ProvisionRestController(AdminService adminService, ProvisionService provisionService, ExcelService excelService){
        this.adminService = adminService;
        this.provisionService = provisionService;
        this.excelService = excelService;
    }

    @ApiOperation(value="내부제공, 외부제공 관리자목록 리스트 호출")
    @GetMapping(value = "/offerAdminList")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> offerAdminList(@RequestParam(value="type", defaultValue = "1") String type) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return adminService.offerAdminList(type, jwtFilterDto);
    }

    @PostMapping("/provisionSave")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = false, dataTypeClass = String.class, paramType = "header", example = ""),
    })
    @ApiOperation(value = "개인정보제공 저장 API", notes = "" +
            "1. PersonalInfoProvision 저장할 데이터를 받는다." +
            "2. ")
    public ResponseEntity<Map<String, Object>> provisionSave(@RequestBody ProvisionSaveDto provisionSaveDto) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return provisionService.provisionSave(provisionSaveDto, jwtFilterDto);
    }

    @GetMapping("/provisionList")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = false, dataTypeClass = String.class, paramType = "header", example = ""),
    })
    @ApiOperation(value = "개인정보제공 리스트 조회 API", notes = "" +
            "")
    public ResponseEntity<Map<String, Object>> provisionList(@RequestParam(value="searchText", defaultValue = "") String searchText,
                                                           @RequestParam(value="stime", defaultValue = "") String stime,
                                                           @RequestParam(value="filterDownload", defaultValue = "") String filterDownload,
                                                           @RequestParam(value="filterState", defaultValue = "") String filterState,
                                                           @PageableDefault Pageable pageable) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return provisionService.provisionList(searchText, stime, filterDownload, filterState, jwtFilterDto, pageable);
    }

    @GetMapping("/provisionDownloadList")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = false, dataTypeClass = String.class, paramType = "header", example = ""),
    })
    @ApiOperation(value = "개인정보제공 다운로드 리스트 조회 API", notes = "" +
            "")
    public ResponseEntity<Map<String, Object>> provisionDownloadList(@RequestParam(value="proCode", defaultValue = "") String proCode,
                                                                     @PageableDefault Pageable pageable) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return provisionService.provisionDownloadList(proCode, jwtFilterDto, pageable);
    }

    @ApiOperation(value="개인정보제공 상세내용 조회", notes="" +
            "1. 상세내용을 조회한 proCode를 받는다." +
            "2. 해당 코드값에 합당한 데이터를 보낸다.")
    @GetMapping(value = "/provisionDetail/{proCode}") // -> 기존의 코코넛 호출 메서드명 : detailView - SystemQnaController, MemberQnaController
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> provisionDetail(@PathVariable("proCode") String proCode) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return provisionService.provisionDetail(proCode, jwtFilterDto);
    }

    @PostMapping("/provisionDownloadExcel")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = false, dataTypeClass = String.class, paramType = "header", example = ""),
    })
    @ApiOperation(value = "개인정보제공 다운로드 API", notes = "")
    public ResponseEntity<Map<String, Object>> provisionDownloadExcel(@RequestParam(value="proCode", defaultValue = "") String proCode) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return provisionService.provisionDownloadExcel(proCode, jwtFilterDto);
    }

    @GetMapping("/provisionDownloadExcel2")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = false, dataTypeClass = String.class, paramType = "header", example = ""),
    })
    @ApiOperation(value = "개인정보 엑셀 다운로드 API", notes = "" +
            "")
    public ResponseEntity<Map<String, Object>> provisionDownloadExcel2() throws  IOException {
        List<Map<String, Object>> dataList = new ArrayList<>();

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data;

        Map<String, Object> row1 = new HashMap<>();
        row1.put("id", 1);
        row1.put("name2", "John");
        row1.put("age", 35);

        Map<String, Object> row2 = new HashMap<>();
        row2.put("id", 2);
        row2.put("name", "Sally");
        row2.put("age", 28);

        dataList.add(row1);
        dataList.add(row2);

        data = excelService.createExcelFile("테스트압축파일", "테스트시트명", dataList, "1234");

        return ResponseEntity.ok(res.success(data));
    }

}