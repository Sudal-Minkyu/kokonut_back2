package com.app.kokonutapi.personal;

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
 * Date : 2023-08-05
 * Time :
 * Remark : API 가이드용 개인정보 기능
 */
@Slf4j
@RestController
@RequestMapping("/v3/api/Personal")
public class PersonalApiRestController {

    private final PersonalApiService personalApiService;

    @Autowired
    public PersonalApiRestController(PersonalApiService personalApiService) {
        this.personalApiService = personalApiService;
    }

    @ApiOperation(value="개인정보 수")
    @GetMapping("/count")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> count(HttpServletRequest request) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return personalApiService.count(jwtFilterDto);
    }

    @ApiOperation(value="개인정보 검색")
    @PostMapping("/search")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> search(HttpServletRequest request) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return personalApiService.search(jwtFilterDto);
    }

    @ApiOperation(value="개인정보 삭제")
    @PostMapping("/delete")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> delete(HttpServletRequest request) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return personalApiService.delete(jwtFilterDto);
    }

}
