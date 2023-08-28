package com.app.kokonutapi.auth;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonutapi.auth.dtos.AuthApiLoginDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Woody
 * Date : 2023-04-27
 * Time :
 * Remark : API 가이드용 로그인 & 회원가입 기능
 */
@Slf4j
@RestController
@RequestMapping("/v3/api/Auth")
public class AuthApiRestController {

    private final AuthApiService authApiService;

    @Autowired
    public AuthApiRestController(AuthApiService authApiService) {
        this.authApiService = authApiService;
    }

    @ApiOperation(value="아이디 중복확인")
    @GetMapping("/checkId")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> checkId(@RequestParam(value="kokonutId", defaultValue = "") String kokonutId, HttpServletRequest request) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return authApiService.checkId(kokonutId, jwtFilterDto);
    }

    @ApiOperation(value="API용 개인정보(고객의 고객) 로그인", notes="1. 아이디와 비밀번호를 받는다.<br/>" +
            "2. 해당 개인정보의 아이디와 비밀번호를 확인한다.<br/>" +
            "3. 로그인 성공시 200을 보낸다.")
    @PostMapping("/login")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> apiLogin(@RequestBody AuthApiLoginDto authApiLoginDto, HttpServletRequest request) throws Exception {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return authApiService.apiLogin(authApiLoginDto, jwtFilterDto);
    }

    @ApiOperation(value="API용 개인정보(고객의 개인정보) 회원가입")
    @PostMapping("/register")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> apiRegister(@RequestBody HashMap<String, Object> paramMap, HttpServletRequest request) throws Exception {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return authApiService.apiRegister(paramMap, jwtFilterDto);
    }

    @ApiOperation(value="회원탈퇴")
    @PostMapping("/secession")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> secession(@RequestParam(value="kokonut_IDX", defaultValue = "") String kokonut_IDX, HttpServletRequest request) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return authApiService.secession(kokonut_IDX, jwtFilterDto);
    }

    @ApiOperation(value="마이페이지")
    @PostMapping("/mypage")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> mypage(@RequestParam(value="kokonut_IDX", defaultValue = "") String kokonut_IDX, HttpServletRequest request) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return authApiService.mypage(kokonut_IDX, jwtFilterDto);
    }

    @ApiOperation(value="내정보 수정")
    @PostMapping("/update")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> update(@RequestBody HashMap<String, Object> paramMap, HttpServletRequest request) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return authApiService.update(paramMap, jwtFilterDto);
    }

//    @ApiOperation(value="GET 호출테스트하기", notes="")
//    @GetMapping("/hoculGetTest")
//    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
//    public ResponseEntity<Map<String,Object>> hoculGetTest(HttpServletRequest request) {
//        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
//
//        log.info("hoculGetTest 호출 성공");
//
//        AjaxResponse res = new AjaxResponse();
//        HashMap<String, Object> data = new HashMap<>();
//
//        data.put("데이터","성공1");
//
//        return ResponseEntity.ok(res.success(data));
//    }
//
//    @ApiOperation(value="POST 호출테스트하기", notes="")
//    @PostMapping("/hoculPostTest")
//    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
//    public ResponseEntity<Map<String,Object>> hoculPostTest(HttpServletRequest request) {
//        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
//
//        log.info("hoculPostTest 호출 성공");
//
//        AjaxResponse res = new AjaxResponse();
//        HashMap<String, Object> data = new HashMap<>();
//
//        data.put("데이터","성공2");
//
//        return ResponseEntity.ok(res.success(data));
//    }

}
