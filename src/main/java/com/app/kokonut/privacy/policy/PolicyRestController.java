package com.app.kokonut.privacy.policy;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.privacy.policy.dtos.PolicySaveFirstDto;
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
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> policyList(@PageableDefault Pageable pageable) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return policyService.policyList(jwtFilterDto, pageable);
    }

    @ApiOperation(value="개인정보처리방침 상세내용 조회", notes="" +
            "1. 리스트의 상세보기를 누르면 나오는 데이터를 보내준다.")
    @GetMapping(value = "/policyDetail/{idx}")
    @ApiImplicitParams({
            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    })
    public ResponseEntity<Map<String,Object>> policyDetail(@PathVariable("idx") Long qnaId) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return policyService.policyDetail(qnaId, jwtFilterDto);
    }

    @ApiOperation(value="개인정보보호 첫번째 뎁스 등록", notes= "" +
            "1. 개인정보처리방침 제작을 한다." +
            "2. 첫번째 뎁스의 대한 데이터를 받는다." +
            "3. 해당 내용을 저장한다.")
    @PostMapping(value = "/privacyPolicyFirstSave")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> privacyPolicyFirstSave(@RequestBody PolicySaveFirstDto policySaveFirstDto, HttpServletRequest request) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
        return policyService.privacyPolicyFirstSave(policySaveFirstDto, jwtFilterDto);
    }

//    // 테이블의 컬럼조회
//    @GetMapping(value = "/tableColumnCall")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey"),
//    })
//    public ResponseEntity<Map<String,Object>> tableColumnCall(@RequestParam(name="tableName", defaultValue = "") String tableName, HttpServletRequest request) {
//        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
//        return dynamicUserService.tableColumnCall(tableName, jwtFilterDto);
//    }



}
