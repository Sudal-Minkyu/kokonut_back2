package com.app.kokonut.email.email;

import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.email.email.dtos.EmailDetailDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v2/api/Email")
public class EmailRestController {
    // 기존 코코넛 SystemEmailController 컨트롤러 리팩토링
    // 기존 url : /system/email , 변경 url : /api/Email
    private final EmailService emailService;

    @Autowired
    public EmailRestController(EmailService emailService) {
        this.emailService = emailService;
    }
    @ApiOperation(value="이메일 목록 조회", notes="" +
            "1. 토큰과 페이지 처리를 위한 값을 받는다." +
            "2. 발송한 메일 목록을 조회한다.")
    @GetMapping(value = "/emailList") // -> 기존의 코코넛 호출 메서드명 : getEmail
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> emailList(@RequestParam(value="searchText", defaultValue = "") String searchText,
                                                        @RequestParam(value="stime", defaultValue = "") String stime,
                                                        @RequestParam(value="emailType", defaultValue = "") String emailType,
                                                        Pageable pageable) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
         return emailService.emailList(jwtFilterDto.getEmail(), searchText, stime, emailType, pageable);
    }

    @ApiOperation(value="이메일 보내기", notes="" +
            "1. 이메일을 전송한다.")
    @PostMapping("/sendEmail")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> sendEmail(@RequestBody EmailDetailDto emailDetailDto) {
        // 접속한 사용자 이메일
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return emailService.sendEmail(jwtFilterDto.getEmail(), emailDetailDto);
    }

    @ApiOperation(value="이메일 상세보기", notes="" +
            "1. 조회하고자 하는 이메일 id 값을 받는다." +
            "2. 메일 상세 내용을 조회한다.")
    @GetMapping("/sendEmail/detail/{emId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> sendEmailDetail(@PathVariable("emId") Long emId) {
        return emailService.sendEmailDetail(emId);
    }

    @ApiOperation(value="이메일 발송 대상 조회", notes="" +
            "1. 토큰과 페이지 처리를 위한 값을 받는다." +
            "2. 메일 발송 대상 목록을 조회한다.")
    @GetMapping("/emailTargetGroupList") // -> 기존의 코코넛 호출 메서드명 : selectEmailTargetPopup
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> emailTargetGroupList(Pageable pageable) {
        return emailService.emailTargetGroupList(pageable);
    }
    // 기존 코코넛 컨트롤러
    // 이동 mappingValue : /emailManagement, view : /System/Email/EmailManagementUI
    // 로직 mappingValue : /getEmail
    // 이동 mappingValue : /sendEmail, view : /System/Email/SendEmailUI
    // 이동 mappingValue : /sendEmail
    // 이동, 로직 mappingValue : /sendEmail/detail/{idx}, view : /System/Email/SendEmailUI
    // 이동, 로직 mappingValue : /selectEmailTargetPopup, view : /System/Email/Popup/SelectEmailTargetPopup
    // 이동 mappingValue : /adminMemberEmailManagement, view : /System/Email/EmailManagementUI
    // 이동 mappingValue : /adminMemberPrivacyNotice, view : /System/Email/AdminPrivacyNoticeUI
}
