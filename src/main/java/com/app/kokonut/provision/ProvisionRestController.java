package com.app.kokonut.provision;

import com.app.kokonut.admin.AdminService;
import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.provision.dtos.ProvisionSaveDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @Autowired
    public ProvisionRestController(AdminService adminService, ProvisionService provisionService){
        this.adminService = adminService;
        this.provisionService = provisionService;
    }

    @ApiOperation(value="내부제공, 외부제공 관리자목록 리스트 호출")
    @GetMapping(value = "/offerAdminList")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> offerAdminList(@RequestParam(value="type", defaultValue = "1") String type) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return adminService.offerAdminList(type, jwtFilterDto);
    }

    @PostMapping("/provisionSave")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    @ApiOperation(value = "개인정보제공 저장 API", notes = "" +
            "1. PersonalInfoProvision 저장할 데이터를 받는다." +
            "2. ")
    public ResponseEntity<Map<String, Object>> provisionSave(@RequestBody ProvisionSaveDto provisionSaveDto) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return provisionService.provisionSave(provisionSaveDto, jwtFilterDto);
    }

    @GetMapping("/provisionList")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    @ApiOperation(value = "개인정보제공 리스트 조회 API")
    public ResponseEntity<Map<String, Object>> provisionList(@RequestParam(value="searchText", defaultValue = "") String searchText,
                                                           @RequestParam(value="stime", defaultValue = "") String stime,
                                                           @RequestParam(value="filterOfferType", defaultValue = "") String filterOfferType,
                                                           @RequestParam(value="filterState", defaultValue = "") String filterState,
                                                           @PageableDefault Pageable pageable) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return provisionService.provisionList(searchText, stime, filterOfferType, filterState, jwtFilterDto, pageable);
    }

    @GetMapping("/provisionDownloadList")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    @ApiOperation(value = "개인정보제공 다운로드 리스트 조회 API")
    public ResponseEntity<Map<String, Object>> provisionDownloadList(@RequestParam(value="proCode", defaultValue = "") String proCode,
                                                                     @PageableDefault Pageable pageable) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return provisionService.provisionDownloadList(proCode, jwtFilterDto, pageable);
    }

    @ApiOperation(value="개인정보제공 상세내용 조회", notes="" +
            "1. 상세내용을 조회한 proCode를 받는다." +
            "2. 해당 코드값에 합당한 데이터를 보낸다.")
    @GetMapping(value = "/provisionDetail/{proCode}") // -> 기존의 코코넛 호출 메서드명 : detailView - SystemQnaController, MemberQnaController
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> provisionDetail(@PathVariable("proCode") String proCode) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return provisionService.provisionDetail(proCode, jwtFilterDto);
    }

    @PostMapping("/provisionListDownloadExcel")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    @ApiOperation(value = "개인정보제공 리스트 엑셀 다운로드 API", notes = "")
    public ResponseEntity<Map<String, Object>> provisionListDownloadExcel(@RequestParam(value="searchText", defaultValue = "") String searchText,
                                                                          @RequestParam(value="stime", defaultValue = "") String stime,
                                                                          @RequestParam(value="filterOfferType", defaultValue = "") String filterOfferType,
                                                                          @RequestParam(value="filterState", defaultValue = "") String filterState,
                                                                          @RequestParam(value="otpValue", defaultValue = "") String otpValue,
                                                                          @RequestParam(value="downloadReason", defaultValue = "") String downloadReason) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return provisionService.provisionListDownloadExcel(searchText, stime, filterOfferType, filterState, otpValue, downloadReason, jwtFilterDto.getEmail());
    }

    @PostMapping("/provisionDownloadExcel")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    @ApiOperation(value = "개인정보제공 다운로드 API", notes = "")
    public ResponseEntity<Map<String, Object>> provisionDownloadExcel(@RequestParam(value="proCode", defaultValue = "") String proCode,
                                                                      @RequestParam(value="otpValue", defaultValue = "") String otpValue,
                                                                      @RequestParam(value="downloadReason", defaultValue = "") String downloadReason) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return provisionService.provisionDownloadExcel(proCode, otpValue, downloadReason, jwtFilterDto);
    }

    @PostMapping("/provisionExit")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    @ApiOperation(value = "개인정보제공 종료 API")
    public ResponseEntity<Map<String, Object>> provisionExit(@RequestParam(value="proCode", defaultValue = "") String proCode){
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return provisionService.provisionExit(proCode, jwtFilterDto);
    }



}