package com.app.kokonut.privacy;

import com.app.kokonut.admin.AdminService;
import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Woody
 * Date : 2023-05-03
 * Time :
 * Remark :
 */
@RestController
@RequestMapping("/v2/api/Privacy")
public class PrivacyRestController {

    private final AdminService adminService;
    private final PrivacyService privacyService;

    @Autowired
    public PrivacyRestController(AdminService adminService, PrivacyService privacyService) {
        this.adminService = adminService;
        this.privacyService = privacyService;
    }

    @ApiOperation(value="내부제공, 외부제공 관리자목록 리스트 호출")
    @GetMapping(value = "/offerAdminList")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> offerAdminList(@RequestParam(value="type", defaultValue = "1") String type) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return adminService.offerAdminList(type, jwtFilterDto);
    }

//    @ApiOperation(value="개인정보처리방침 리스트 조회", notes="" +
//            "1. 개인정보처리방침을 제작한 리스트를 보여준다.")
//    @GetMapping(value = "/policyList")
//    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
//    public ResponseEntity<Map<String,Object>> policyList(@RequestParam(value="searchText", defaultValue = "") String searchText,
//                                                         @RequestParam(value="stime", defaultValue = "") String stime,
//                                                         @RequestParam(value="filterDate", defaultValue = "") String filterDate,
//                                                         @PageableDefault Pageable pageable) {
//        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
//        return policyService.policyList(searchText, stime, filterDate, jwtFilterDto, pageable);
//    }

//    @ApiOperation(value="개인정보보호 첫번째 뎁스 CUD", notes= "" +
//            "1. 개인정보처리방침 제작을 한다." +
//            "2. 첫번째 뎁스의 대한 데이터를 받는다." +
//            "3. 해당 내용을 저장한다.")
//    @PostMapping(value = "/privacyPolicyFirstSave")
//    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
//    public ResponseEntity<Map<String,Object>> privacyPolicyFirstSave(@RequestBody PolicySaveFirstDto policySaveFirstDto) {
//        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
//        return policyService.privacyPolicyFirstSave(policySaveFirstDto, jwtFilterDto);
//    }

//    @ApiOperation(value="개인정보보호 마지막 뎁스 등록", notes= "" +
//            "1. 개인정보처리방침 최종저장 한다.")
//    @PostMapping(value = "/privacyPolicyFinalSave")
//    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
//    public ResponseEntity<Map<String,Object>> privacyPolicyFinalSave(@RequestParam(name="piId", defaultValue = "") Long piId) {
//        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
//        return policyService.privacyPolicyFinalSave(piId, jwtFilterDto);
//    }


}
