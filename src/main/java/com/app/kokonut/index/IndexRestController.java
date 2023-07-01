package com.app.kokonut.index;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Woody
 * Date : 2023-06-22
 * Time :
 * Remark : 인덱스페이지 관련 RestController
 */
@Slf4j
@RequestMapping("/v2/api/Index")
@RestController
public class IndexRestController {

    private final IndexService indexService;

    @Autowired
    public IndexRestController(IndexService indexService){
        this.indexService = indexService;
    }

    @ApiOperation(value="나의 접속 현황(로그인현황) 최근일자로부터 5건을 가져온다.", notes="")
    @GetMapping(value = "/myLoginInfo")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> myLoginInfo() {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return indexService.myLoginInfo(jwtFilterDto);
    }

    @ApiOperation(value="관리자 접속현황 데이터를 가져온다.", notes="")
    @GetMapping(value = "/adminConnectInfo")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> adminConnectInfo() {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return indexService.adminConnectInfo(jwtFilterDto);
    }

    @ApiOperation(value="인덱스페이지에 표출할 개인정보 제공 건수를 가져온다.", notes="")
    @GetMapping(value = "/provisionIndexCount")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> privacyOfferCount(@RequestParam(value="dateType", defaultValue = "") String dateType) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return indexService.provisionIndexCount(dateType, jwtFilterDto);
    }

    @ApiOperation(value="인덱스페이지에 표출할 개인정보 수를 가져온다.", notes="")
    @GetMapping(value = "/privacyIndexCount")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> privacyCount(@RequestParam(value="dateType", defaultValue = "") String dateType) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return indexService.privacyIndexCount(dateType, jwtFilterDto);
    }

    @ApiOperation(value="오늘의 현황 그래프 데이터를 가져온다.", notes="")
    @GetMapping(value = "/todayIndexGraph")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> todayIndexGraph() {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return indexService.todayIndexGraph(jwtFilterDto);
    }

    @ApiOperation(value="개인정보 항목(암호화 항목, 고유식별정보 항목, 민감정보 항목)의 추가 카운팅 수 데이터를 가져온다.", notes="")
    @GetMapping(value = "/privacyItemCount")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> privacyItemCount() {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return indexService.privacyItemCount(jwtFilterDto);
    }

    // 서드파티 연동 현황 - 기업

    // 구독관리 관련 - 기업 (디폴트값 이번달)

    // 이메일발송 현황 건수 데이터 - 기업 (디폴트 오늘)

}
