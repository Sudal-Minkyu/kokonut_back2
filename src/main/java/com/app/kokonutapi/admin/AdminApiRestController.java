package com.app.kokonutapi.admin;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Woody
 * Date : 2023-08-01
 * Time :
 * Remark : API 가이드용 관리자 기능
 */
@Slf4j
@RestController
@RequestMapping("/v3/api/Admin")
public class AdminApiRestController {

    private final AdminApiService adminApiService;

    @Autowired
    public AdminApiRestController(AdminApiService adminApiService) {
        this.adminApiService = adminApiService;
    }

    @ApiOperation(value="관리자 목록")
    @GetMapping("/list")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> list(HttpServletRequest request) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return adminApiService.list(jwtFilterDto);
    }

    @ApiOperation(value="관리자 금일 접속 수")
    @PostMapping("/todayCount")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> apiLogin(HttpServletRequest request) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return adminApiService.todayCount(jwtFilterDto);
    }

}
