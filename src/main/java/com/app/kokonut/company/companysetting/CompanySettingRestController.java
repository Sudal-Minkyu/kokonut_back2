package com.app.kokonut.company.companysetting;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.company.companysettingaccessip.dtos.AccessIpDeleteDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/v2/api/CompanySetting")
public class CompanySettingRestController {

    private final CompanySettingService companySettingService;

    @Autowired
    public CompanySettingRestController(CompanySettingService companySettingService) {
        this.companySettingService = companySettingService;
    }

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
