package com.app.kokonut.provision;

import com.app.kokonut.admin.AdminService;
import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.provision.dtos.ProvisionSaveDto;
import com.app.kokonutapi.personalInfoProvision.dtos.PersonalInfoProvisionSaveDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<Map<String,Object>> offerAdminList(@RequestParam(value="type", defaultValue = "1") String type) {
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
    public ResponseEntity<Map<String, Object>> provisionSave(@RequestBody ProvisionSaveDto provisionSaveDto){
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
                                                           @PageableDefault Pageable pageable){
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return provisionService.provisionList(searchText, stime, filterDownload, filterState, jwtFilterDto, pageable);
    }







}