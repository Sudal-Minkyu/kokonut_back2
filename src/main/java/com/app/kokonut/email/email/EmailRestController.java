package com.app.kokonut.email.email;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.email.email.dtos.EmailSendDto;
import com.app.kokonutuser.DynamicUserService;
import com.app.kokonutuser.dtos.KokonutSearchDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/v2/api/Email")
public class EmailRestController {

    private final EmailService emailService;
    private final DynamicUserService dynamicUserService;

    @Autowired
    public EmailRestController(EmailService emailService, DynamicUserService dynamicUserService) {
        this.emailService = emailService;
        this.dynamicUserService = dynamicUserService;
    }

    @ApiOperation(value="발송할 이메일 리스트호출")
    @PostMapping("/sendEmailList")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> sendEmailList(@RequestBody KokonutSearchDto kokonutSearchDto) throws Exception {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return dynamicUserService.privacyUserSearch(kokonutSearchDto,"2",jwtFilterDto);
    }

    @ApiOperation(value="이메일 목록 조회", notes="" +
            "1. 토큰과 페이지 처리를 위한 값을 받는다." +
            "2. 발송한 메일 목록을 조회한다.")
    @GetMapping(value = "/emailList")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> emailList(@RequestParam(value="searchText", defaultValue = "") String searchText,
                                                        @RequestParam(value="stime", defaultValue = "") String stime,
                                                        @RequestParam(value="emPurpose", defaultValue = "") String emPurpose,
                                                        Pageable pageable) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return emailService.emailList(jwtFilterDto, searchText, stime, emPurpose, pageable);
    }

    @ApiOperation(value="이메일 발송 예약 취소")
    @PostMapping(value = "/emailReservedCancel")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> emailReservedCancel(@RequestParam(value="emId", defaultValue = "") Long emId) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return emailService.emailReservedCancel(emId, jwtFilterDto);
    }

    @ApiOperation(value="이메일발송 호출")
    @PostMapping("/sendEmailService")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> sendEmailService(@ModelAttribute EmailSendDto emailSendDto) throws Exception {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return emailService.sendEmailService(emailSendDto, jwtFilterDto);
    }

}
