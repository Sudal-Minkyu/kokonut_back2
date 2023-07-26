package com.app.kokonutapi.index;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.index.IndexService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Woody
 * Date : 2023-07-20
 * Time :
 * Remark : API용 IndexRestConrtroller
 */
@Slf4j
@RequestMapping("/v3/api/Index")
@RestController
public class IndexApiRestController {

    private final IndexService indexService;

    @Autowired
    public IndexApiRestController(IndexService indexService){
        this.indexService = indexService;
    }

    @ApiOperation(value="금일 API 호출수를 호출한다.", notes="")
    @GetMapping(value = "/apiCount")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> apiCount(HttpServletRequest request) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return indexService.apiCount(jwtFilterDto);
    }

    @ApiOperation(value="금일 암호화, 복호화 수를 호출한다.", notes="")
    @GetMapping(value = "/endeCount")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> endeCount(HttpServletRequest request) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return indexService.endeCount(jwtFilterDto);
    }

    @ApiOperation(value="개인정보 항목(암호화 항목, 고유식별정보 항목, 민감정보 항목)의 추가 카운팅 수 데이터를 가져온다.", notes="")
    @GetMapping(value = "/privacyItemCount")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> privacyItemCount(HttpServletRequest request) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return indexService.privacyItemCount(jwtFilterDto);
    }

    @ApiOperation(value="개인정보 제공의 금일 건수와 데이트타입의 따라 건수를 가져온다.", notes="")
    @GetMapping(value = "/provisionCount")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> provisionCount(@RequestParam(value="dateType", defaultValue = "1") String dateType,
                                                                  HttpServletRequest request) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return indexService.provisionIndexCount(dateType, jwtFilterDto);
    }

    @ApiOperation(value="이메일 발송 완료 및 예약 건수 + 수신건수와 청구금액을 가져온다.", notes="")
    @GetMapping(value = "/emailSendInfo")
    @ApiImplicitParam(name ="x-api-key", required = true, dataTypeClass = String.class, paramType = "header")
    public ResponseEntity<Map<String,Object>> emailSendInfo(@RequestParam(value="dateType", defaultValue = "1") String dateType,
                                                             HttpServletRequest request) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return indexService.emailSendInfo(dateType, jwtFilterDto);
    }






}
