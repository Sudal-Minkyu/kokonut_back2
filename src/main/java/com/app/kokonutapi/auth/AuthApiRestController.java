package com.app.kokonutapi.auth;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.privacy.policy.dtos.*;
import com.app.kokonutapi.auth.dtos.AuthApiLoginDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Woody
 * Date : 2023-04-27
 * Time :
 * Remark : API 가이드용 로그인 & 회원가입 기능
 */
@RestController
@RequestMapping("/v3/api/Auth")
public class AuthApiRestController {

    private final AuthApiService authApiService;

    @Autowired
    public AuthApiRestController(AuthApiService authApiService) {
        this.authApiService = authApiService;
    }

    @PostMapping("/login")
    @ApiOperation(value="API용 개인정보(고객의 고객) 로그인", notes="" +
            "1. 아이디와 비밀번호를 받는다.<br/>" +
            "2. 해당 개인정보의 아이디와 비밀번호를 확인한다.<br/>" +
            "3. 로그인 성공시 200을 보낸다.")
    @ApiImplicitParam(name ="ApiKey", value="API Key",required = false, dataTypeClass = String.class, paramType = "header", example = "")
    public ResponseEntity<Map<String,Object>> apiLogin(@RequestBody AuthApiLoginDto authApiLoginDto, HttpServletRequest request) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return authApiService.apiLogin(authApiLoginDto, jwtFilterDto);
    }

//    @ApiOperation(value="API용 개인정보(고객의 고객) 회원가입", notes="" +
//            "1. 아이디와 비밀번호를 받는다.<br/>" +
//            "2. 해당 개인정보의 아이디와 비밀번호를 확인한다.<br/>" +
//            "3. 로그인 성공시 200을 보낸다.")
//    @PostMapping("/register")
//    @ApiImplicitParam(name ="ApiKey", value="API Key",required = false, dataTypeClass = String.class, paramType = "header", example = "")
//    public ResponseEntity<Map<String,Object>> apiRegister(@RequestBody AuthApiLoginDto,HttpServletRequest request) {
//        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
//        return authApiService.apiRegister(jwtFilterDto);
//    }



}
