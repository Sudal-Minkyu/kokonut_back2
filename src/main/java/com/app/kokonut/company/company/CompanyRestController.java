package com.app.kokonut.company.company;

import com.app.kokonut.apiKey.dtos.ApiKeyIpDeleteDto;
import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.company.companyitem.CompanyItemService;
import com.app.kokonut.company.companysetting.CompanySettingService;
import com.app.kokonut.company.companysettingaccessip.dtos.AccessIpDeleteDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/v2/api/Company")
public class CompanyRestController {

    private final CompanyItemService companyItemService;
    private final CompanySettingService companySettingService;

    @Autowired
    public CompanyRestController(CompanyItemService companyItemService, CompanySettingService companySettingService) {
        this.companyItemService = companyItemService;
        this.companySettingService = companySettingService;
    }

//  @@@@@@@@@@@@@@@@@@@@@@@@@ 개인정보 항목관리 페이지 카테고리 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    @ApiOperation(value="기본 카테고리 항목을 가져온다.", notes="" +
            "1. 기본으로 제공하는 카테고리를 조회한다." +
            "2. DB내에 추가된 카테고리와 해당 카테고리의 항목을 보내준다.")
    @GetMapping(value = "/categoryList") // -> 추가된 카테고리 리스트가져오기
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> categoryList() {
        return companyItemService.categoryList();
    }

    @ApiOperation(value="추가 카테고리 항목을 가져온다.", notes="" +
            "1. cpCode를 통해 추가된 카테고리를 조회한다." +
            "2. 결과값을 보낸다.")
    @GetMapping(value = "/addItemList") // -> 추가된 카테고리 리스트가져오기
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> addItemList() {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companyItemService.addItemList(jwtFilterDto);
    }

    @ApiOperation(value="추가 카테고리의 항목을 추가한다.", notes="" +
            "1. 항목의 이름, 암호화여부의 데이터를 받는다." +
            "2. 해당 항목을 저정한다.")
    @PostMapping(value = "/saveItem") // -> 추가 카테고리의 항목을 추가한다.
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> saveItem(@RequestParam(name="ciName", defaultValue = "") String ciName,
                                                              @RequestParam(name="ciSecurity", defaultValue = "") Integer ciSecurity) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companyItemService.saveItem(jwtFilterDto, ciName, ciSecurity);
    }

    @ApiOperation(value="추가 카테고리의 항목을 수정한다.", notes="" +
            "1. 해당 항목의 고유ID, 항목의 이름 데이터를 받는다." +
            "2. 해당 항목을 조회하고 존해하면 받은 이름으로 수정한다.")
    @PostMapping(value = "/updateItem") // -> 추가 카테고리의 수정을 추가한다.
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> updateItem(@RequestParam(name="ciId", defaultValue = "") Long ciId,
                                                         @RequestParam(name="ciName", defaultValue = "") String ciName) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companyItemService.updateItem(ciId, ciName, jwtFilterDto);
    }

    @ApiOperation(value="추가 카테고리의 항목을 삭제한다.", notes="" +
            "1. 삭제할 항목의 고유ID 데이터를 받는다." +
            "2. 해당 항목을 조회하여 삭제한다.")
    @PostMapping(value = "/deleteItem") // -> 추가 카테고리의 항목을 삭제한다.
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> deleteItem(@RequestParam(name="ciId", defaultValue = "") Long ciId) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companyItemService.deleteItem(ciId, jwtFilterDto);
    }

//  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@



//  @@@@@@@@@@@@@@@@@@@@@@@@@ 개인정보 테이블 정보 호출 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    @ApiOperation(value="테이블을 추가한다.", notes="" +
            "1. 추가할 테이블명을 받는다." +
            "2. 해당 테이블을 저정한다.")
    @PostMapping(value = "/userTableSave")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> userTableSave(@RequestParam(name="ctDesignation", defaultValue = "") String ctDesignation) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companyItemService.userTableSave(jwtFilterDto, ctDesignation);
    }

    @ApiOperation(value="모든 테이블을 가져온다.", notes="" +
            "1. cpCode를 통해 추가된 카테고리를 조회한다." +
            "2. 결과값을 보낸다.")
    @GetMapping(value = "/userTableList")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> userTableList() {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companyItemService.userTableList(jwtFilterDto);
    }

    @ApiOperation(value="개인정보검색용 테이블리스트를 가져온다.", notes="")
    @GetMapping(value = "/privacyTableList")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> privacyTableList() {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companyItemService.privacyTableList(jwtFilterDto);
    }

//  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


//  @@@@@@@@@@@@@@@@@@@@@@@@@ 서비스설정 호출 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    @ApiOperation(value="서비스 설정값 정보 가져오기", notes="")
    @GetMapping(value = "/settingInfo")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> settingInfo() {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companySettingService.settingInfo(jwtFilterDto);
    }

    @ApiOperation(value="해외로그인 차단 서비스 설정", notes="")
    @PostMapping(value = "/overseasBlockSetting")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> overseasBlockSetting() throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companySettingService.overseasBlockSetting(jwtFilterDto);
    }

    @ApiOperation(value="접속허용 IP설정", notes="")
    @PostMapping(value = "/accessSetting")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> accessSetting() throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companySettingService.accessSetting(jwtFilterDto);
    }

    @ApiOperation(value="비밀번호 변경주기 설정", notes="")
    @PostMapping(value = "/passwordChangeSetting")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> passwordChangeSetting(@RequestParam(name="csPasswordChangeSetting", defaultValue = "") String csPasswordChangeSetting) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companySettingService.passwordChangeSetting(jwtFilterDto, csPasswordChangeSetting);
    }

    @ApiOperation(value="비밀번호 오류 접속제한 설정", notes="")
    @PostMapping(value = "/passwordErrorCountSetting")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> passwordErrorCountSetting(@RequestParam(name="csPasswordErrorCountSetting", defaultValue = "") String csPasswordErrorCountSetting) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companySettingService.passwordErrorCountSetting(jwtFilterDto, csPasswordErrorCountSetting);
    }

    @ApiOperation(value="자동 로그아웃 시간 설정", notes="")
    @PostMapping(value = "/autoLogoutSetting")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> autoLogoutSetting(@RequestParam(name="csAutoLogoutSetting", defaultValue = "") String csAutoLogoutSetting) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companySettingService.autoLogoutSetting(jwtFilterDto, csAutoLogoutSetting);
    }

    @ApiOperation(value="장기 미접속 접근제한 설정", notes="")
    @PostMapping(value = "/longDisconnectionSetting")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> longDisconnectionSetting(@RequestParam(name="csLongDisconnectionSetting", defaultValue = "") String csLongDisconnectionSetting) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companySettingService.longDisconnectionSetting(jwtFilterDto, csLongDisconnectionSetting);
    }

    @PostMapping("/accessIpSave")
    @ApiOperation(value = "접속허용 IP 등록", notes = "")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token", required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> apiKeyIpSave(@RequestParam(value="csipIp", defaultValue = "") String csipIp,
                                                           @RequestParam(value="csipRemarks", defaultValue = "") String csipRemarks) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companySettingService.accessIpSave(csipIp, csipRemarks, jwtFilterDto);
    }

    @PostMapping("/accessIpDelete")
    @ApiOperation(value = "접속허용 IP 삭제", notes = "" +
            "1. 등록한 허용 IP를 삭제한다.")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token", required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> apiKeyIpDelete(@RequestBody AccessIpDeleteDto accessIpDeleteDto) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companySettingService.apiKeyIpDelete(accessIpDeleteDto, jwtFilterDto);
    }

//  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@











}
