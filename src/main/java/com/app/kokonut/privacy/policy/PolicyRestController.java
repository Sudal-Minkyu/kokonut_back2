package com.app.kokonut.privacy.policy;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.privacy.policy.dtos.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
 * Date : 2023-04-21
 * Time :
 * Remark :
 */
@RestController
@RequestMapping("/v2/api/Policy")
public class PolicyRestController {

    private final PolicyService policyService;

    @Autowired
    public PolicyRestController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @ApiOperation(value="개인정보처리방침 리스트 조회", notes="" +
            "1. 개인정보처리방침을 제작한 리스트를 보여준다.")
    @GetMapping(value = "/policyList")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> policyList(@PageableDefault Pageable pageable) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return policyService.policyList(jwtFilterDto, pageable);
    }

    @ApiOperation(value="개인정보처리방침 상세내용 조회", notes="" +
            "1. 리스트의 상세보기를 누르면 나오는 데이터를 보내준다.")
    @GetMapping(value = "/policyDetail/{idx}")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> policyDetail(@PathVariable("idx") Long qnaId) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return policyService.policyDetail(qnaId, jwtFilterDto);
    }

    @ApiOperation(value="개인정보 처리방침 작성중인글 체크", notes= "" +
            "1. 개인정보처리방침의 piId를 받는다." +
            "2. 단계의 따라 해당값을 보내준다.")
    @GetMapping(value = "/policyCheck")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> policyCheck() {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return policyService.policyCheck(jwtFilterDto);
    }

    // 작성중단 클릭후 작동하는 함수
    @ApiOperation(value="개인정보처리방침 삭제", notes= "" +
            "1. 작성중단 여부를 묻는다." +
            "2. 삭제를 선택할 경우 받은 'piId'를 통해 조회하여 삭제처리한다.")
    @PostMapping(value = "/privacyPolicyDelete")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> privacyPolicyDelete(@RequestParam(name="piId", defaultValue = "") Long piId) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return policyService.privacyPolicyDelete(piId, jwtFilterDto);
    }

    @ApiOperation(value="개인정보 처리방침 작성중인글 단계 체크", notes= "" +
            "1. 개인정보처리방침의 piId를 받는다." +
            "2. 단계의 따라 해당값을 보내준다.")
    @GetMapping(value = "/privacyPolicyWriting")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> privacyPolicyWriting(@RequestParam(name="piId", defaultValue = "") Long piId,
                                                                 @RequestParam(name="piStage", defaultValue = "") Integer piStage) {
        return policyService.privacyPolicyWriting(piId, piStage);
    }

    // 버전, 개정일, 머릿말 회사명
    @ApiOperation(value="개인정보보호 첫번째 뎁스 CUD", notes= "" +
            "1. 개인정보처리방침 제작을 한다." +
            "2. 첫번째 뎁스의 대한 데이터를 받는다." +
            "3. 해당 내용을 저장한다.")
    @PostMapping(value = "/privacyPolicyFirstSave")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> privacyPolicyFirstSave(@RequestBody PolicySaveFirstDto policySaveFirstDto) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return policyService.privacyPolicyFirstSave(policySaveFirstDto, jwtFilterDto);
    }

    // 처리목적
    @ApiOperation(value="개인정보보호 두번쨰 뎁스 CUD", notes= "" +
            "1. 개인정보처리방침 두번쨰 값을 저장 한다.<br/>"+
            "2. 두번째 뎁스의 대한 데이터를 받는다.<br/>"+
            "3. 해당 내용을 저장한다.")
    @PostMapping(value = "/privacyPolicySecondSave")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> privacyPolicySecondSave(@RequestBody PolicySaveSecondDto policySaveSecondDto) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return policyService.privacyPolicySecondSave(policySaveSecondDto, jwtFilterDto);
    }

    // 가입시 수집, 가입후 수집, 서비스이용 중 자동수집
    @ApiOperation(value="개인정보보호 세번쨰 뎁스 등록", notes= "" +
            "1. 개인정보처리방침 세번쨰 값을 저장 한다." +
            "2. 세번째 뎁스의 대한 데이터를 받는다." +
            "3. 해당 내용을 저장한다.")
    @PostMapping(value = "/privacyPolicyThirdSave")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> privacyPolicyThirdSave(@RequestBody PolicySaveThirdDto policySaveThirdDto) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return policyService.privacyPolicyThirdSave(policySaveThirdDto, jwtFilterDto);
    }

    // 처리업무 위탁, 국외위탁
    @ApiOperation(value="개인정보보호 네번쨰 뎁스 등록", notes= "" +
            "1. 개인정보처리방침 네번쨰 값을 저장 한다." +
            "2. 네번째 뎁스의 대한 데이터를 받는다." +
            "3. 해당 내용을 저장한다.")
    @PostMapping(value = "/privacyPolicyFourthSave")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> privacyPolicyFourthSave(@RequestBody PolicySaveFourthDto policySaveFourthDto) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return policyService.privacyPolicyFourthSave(policySaveFourthDto, jwtFilterDto);
    }

    // 처리업무 위탁, 국외위탁
    @ApiOperation(value="개인정보보호 다섯번쨰 뎁스 등록", notes= "" +
            "1. 개인정보처리방침 다섯번쨰 값을 저장 한다." +
            "2. 네번째 뎁스의 대한 데이터를 받는다." +
            "3. 해당 내용을 저장한다.")
    @PostMapping(value = "/privacyPolicyFifthSave")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> privacyPolicyFifthSave(@RequestBody PolicySaveFifthDto policySaveFifthDto) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return policyService.privacyPolicyFifthSave(policySaveFifthDto, jwtFilterDto);
    }


    @ApiOperation(value="개인정보보호 여섯번쨰 뎁스 등록", notes= "" +
            "1. 개인정보처리방침 여섯번쨰값을 저장 한다." +
            "2. 다섯번째 뎁스의 대한 데이터를 받는다." +
            "3. 해당 내용을 저장한다.")
    @PostMapping(value = "/privacyPolicySixthSave")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> privacyPolicySixthSave(@RequestBody PolicySaveSixthDto policySaveSixthDto) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return policyService.privacyPolicySixthSave(policySaveSixthDto, jwtFilterDto);
    }

//    @ApiOperation(value="개인정보보호 두번쨰 뎁스 등록", notes= "" +
//            "1. 개인정보처리방침 두번쨰값을 저장 한다." +
//            "2. 두번째 뎁스의 대한 데이터를 받는다." +
//            "3. 해당 내용을 저장한다.")
//    @PostMapping(value = "/privacyPolicySecondSave")
//    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
//    public ResponseEntity<Map<String,Object>> privacyPolicySecondSave(@RequestBody PolicySaveSecondDto policySaveSecondDto) {
//        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
//        return policyService.privacyPolicySecondSave(policySaveSecondDto, jwtFilterDto);
//    }






}
