package com.app.kokonutapi.auth;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.common.AjaxResponse;
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

    // 아이디 중복확인 API


    @PostMapping("/login")
    @ApiOperation(value="API용 개인정보(고객의 고객) 로그인", notes="" +
            "1. 아이디와 비밀번호를 받는다.<br/>" +
            "2. 해당 개인정보의 아이디와 비밀번호를 확인한다.<br/>" +
            "3. 로그인 성공시 200을 보낸다.")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> apiLogin(@RequestBody AuthApiLoginDto authApiLoginDto, HttpServletRequest request) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        if(jwtFilterDto == null) {
            return null;
        } else {
            return authApiService.apiLogin(authApiLoginDto, jwtFilterDto);
        }
    }


    @ApiOperation(value="API용 개인정보(고객의 개인정보) 회원가입", notes="" +
            "1. 기본테이블의 키와 벨류값을 가져온다." +
            "2. 해당 키를 통해 컬럼값을 조회하고 해당 값을 인서트할 쿼리문에 추가한다." +
            "3. 로그인 성공시 200을 보낸다.")
    @PostMapping("/register")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> apiRegister(@RequestBody HashMap<String, Object> paramMap, HttpServletRequest request) throws Exception {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return authApiService.apiRegister(paramMap, jwtFilterDto);
    }

    @ApiOperation(value="GET 호출테스트하기", notes="")
    @GetMapping("/hoculGetTest")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> hoculGetTest(HttpServletRequest request) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);

        log.info("hoculGetTest 호출 성공");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        data.put("데이터","성공1");

        return ResponseEntity.ok(res.success(data));
    }

    @ApiOperation(value="POST 호출테스트하기", notes="")
    @PostMapping("/hoculPostTest")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> hoculPostTest(HttpServletRequest request) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);

        log.info("hoculPostTest 호출 성공");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        data.put("데이터","성공2");

        return ResponseEntity.ok(res.success(data));
    }


}
