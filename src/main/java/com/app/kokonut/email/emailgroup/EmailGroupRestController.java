package com.app.kokonut.email.emailgroup;

import com.app.kokonut.email.emailgroup.dtos.EmailGroupDetailDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "")
@Validated
@RestController
@RequestMapping("/v2/api/EmailGroup")
public class EmailGroupRestController {
    // 기존 코코넛 SystemEmailGroupController 컨트롤러 리팩토링
    // 기존 url : /system/emailGroup , 변경 url : /api/EmailGroup
    private final EmailGroupService emailGroupService;

    public EmailGroupRestController(EmailGroupService emailGroupService) {
        this.emailGroupService = emailGroupService;
    }
    @ApiOperation(value="이메일 그룹 목록 조회", notes="" +
            "1. 토큰과 페이지 처리를 위한 값을 받는다." +
            "2. 이메일 그룹 목록을 조회한다")
    @GetMapping(value = "/emailGroupList") // -> 기존의 코코넛 호출 메서드명 : getEmailGroup
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> emailGroupList(@RequestBody Pageable pageable) {
        return emailGroupService.emailGroupList(pageable);
    }

    @ApiOperation(value="이메일 그룹 상세조회", notes="" +
            "1. 토큰과 조회하고자 하는 이메일 그룹의 아이디를 받는다." +
            "2. 이메일 그룹의 상세 내용을 조회한다.")
    @GetMapping(value = "/emailGroupDetail/{egId}") // -> 기존의 코코넛 호출 메서드명 : SelectEmailGroupByIdx
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> emailGroupDetail(@PathVariable("egId") Long egId) {
        return emailGroupService.emailGroupDetail(egId);
    }

    @ApiOperation(value="이메일 그룹 저장", notes="" +
            "1. 이메일 그룹을 저장한다.")
    @PostMapping("/saveEmailGroup") // -> 기존의 코코넛 호출 메서드명 : save
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true,dataType="string",paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> saveEmailGroup(@RequestBody EmailGroupDetailDto emailGroupDetailDto) {
        return emailGroupService.saveEmailGroup(emailGroupDetailDto);
    }

    @ApiOperation(value="이메일 그룹 삭제", notes="" +
            "1. 토큰과 삭제하고자 하는 이메일 그룹의 아이디를 받는다." +
            "2. 이메일 그룹을 삭제한다.")
    @PostMapping("/deleteEmailGroup") // -> 기존의 코코넛 호출 메서드명 : delete
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> deleteEmailGroup(@RequestParam(name="egId") Long egId){
        return emailGroupService.deleteEmailGroup(egId);
    }

    @ApiOperation(value="이메일 그룹 수정", notes="" +
            "1. 이메일 그룹 내용을 수정한다.")
    @PostMapping("/updateEmailGroup") // -> 기존의 코코넛 호출 메서드명 : update
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> updateEmailGroup(@RequestBody EmailGroupDetailDto emailGroupDetailDto){
        return emailGroupService.UpdateEmailGroup(emailGroupDetailDto);
    }


    // 기존 코코넛 컨트롤러
    // 이동 mappingValue : /groupManagement, view : /System/EmailGroup/GroupManagementUI
    // 로직 mappingValue : /getEmailGroup
    // 로직 mappingValue : /save
    // 로직 mappingValue : /delete/{idx}
    // 로직 mappingValue : /update
    // 이동 mappingValue : /addGroupPopup, view : /System/EmailGroup/Popup/AddGroupPopup
    // 이동, 로직 mappingValue : /modifyGroupPopup/{idx}, view : /System/EmailGroup/Popup/AddGroupPopup"

    // 이동 mappingValue : /sendEmail
    // 이동, 로직 mappingValue : /sendEmail/detail/{idx}, view : /System/Email/SendEmailUI
    // 이동, 로직 mappingValue : /selectEmailTargetPopup, view : /System/Email/Popup/SelectEmailTargetPopup
    // 이동 mappingValue : /adminMemberEmailManagement, view : /System/Email/EmailManagementUI
    // 이동 mappingValue : /adminMemberPrivacyNotice, view : /System/Email/AdminPrivacyNoticeUI
}
