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

    @ApiOperation(value="아이디 중복확인", notes="")
    @GetMapping("/checkId")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> checkId(@RequestParam(value="kokonutId", defaultValue = "") String kokonutId, HttpServletRequest request) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return authApiService.checkId(kokonutId, jwtFilterDto);
    }

    @ApiOperation(value="API용 개인정보(고객의 고객) 로그인", notes="" +
            "1. 아이디와 비밀번호를 받는다.<br/>" +
            "2. 해당 개인정보의 아이디와 비밀번호를 확인한다.<br/>" +
            "3. 로그인 성공시 200을 보낸다.")
    @PostMapping("/login")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> apiLogin(@RequestBody AuthApiLoginDto authApiLoginDto, HttpServletRequest request) throws Exception {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return authApiService.apiLogin(authApiLoginDto, jwtFilterDto);
    }

//    @ApiOperation(value="API용 개인정보(고객의 개인정보) 회원가입", notes="" +
//            "<br>1. 등록할 회원정보를 받는다.<br>" +
//            "2. 등록 성공시 생성된 IDX 값을 반환한다.<br>" +
//            "<br>" +
//            "* 모의해킹 테스트용 호출 데이터(아래) *<br><br>" +
//            "x-api-key <br>" +
//            "2a40c544978b48b374dfc91a2d2dfc72<br><br>" +
//            "pramMap의 key는 추가된 항목의 고유번호며, value는 해당 필드에 저장할 데이터입니다." +
//            "paramMap : <br>" +
//            "{\n" +
//            "\"1_id\": \"테스트id\",\n" +
//            "\"1_pw\": \"테스트pw\",\n" +
//            "\"1_30\": \"testemail@kokonut.me\",\n" +
//            "\"1_31\": \"테스트\",\n" +
//            "\"1_32\": \"서울특별시\",\n" +
//            "\"1_33\": \"01011112222\" - \"참고사항 : (해당 내용은 제외하고 호출할것 : 본인 휴대폰번호넣어야 알림톡 테스트할때 알림을 받아볼 수 있음 - '/v3/api/ThirdParty/alimTalkSend')\"\n" +
//            "}")
    @ApiOperation(value="API용 개인정보(고객의 개인정보) 회원가입", notes="")
    @PostMapping("/register")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> apiRegister(@RequestBody HashMap<String, Object> paramMap, HttpServletRequest request) throws Exception {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return authApiService.apiRegister(paramMap, jwtFilterDto);
    }

    @ApiOperation(value="회원탈퇴", notes="")
    @PostMapping("/secession")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> secession(@RequestParam(value="kokonut_IDX", defaultValue = "") String kokonut_IDX, HttpServletRequest request) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return authApiService.secession(kokonut_IDX, jwtFilterDto);
    }

    @ApiOperation(value="마이페이지", notes="")
    @PostMapping("/mypage")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> mypage(@RequestParam(value="kokonut_IDX", defaultValue = "") String kokonut_IDX, HttpServletRequest request) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return authApiService.mypage(kokonut_IDX, jwtFilterDto);
    }

    @ApiOperation(value="내정보 수정", notes="")
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
