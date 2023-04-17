package com.app.kokonut.apiKey;

import com.app.kokonut.apiKey.dtos.ApiKeyIpDeleteDto;
import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v2/api/ApiKey")
public class ApiKeyRestController {

    private final ApiKeyService apiKeyService;

    @Autowired
    public ApiKeyRestController(ApiKeyService apiKeyService){
        this.apiKeyService = apiKeyService;
    }

    /**
     * APIKey 존재여부 체크
     */
    @GetMapping("/apiKeyCheck")
    @ApiOperation(value = "APIKey 발급여부", notes = "" +
            "1. 해당 유저가 APIKey를 발급했는지 체크한다." +
            "2. 참/거짓 여부를 보낸다.")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token", required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> apiKeyCheck(){
        String knEmail = SecurityUtil.getCurrentJwt().getEmail();
        return apiKeyService.apiKeyCheck(knEmail);
    }

    /**
     * APIKey 발급 -> JWT토큰 존재해야 발급가능
     */
    @PostMapping("/apiKeyIssue")
    @ApiOperation(value = "APIKey 발급", notes = "APIKey를 발급해준다.")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token", required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> apiKeyIssue() throws NoSuchAlgorithmException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return apiKeyService.apiKeyIssue(jwtFilterDto);
    }

    @PostMapping("/apiKeyIpSave")
    @ApiOperation(value = "APIKey 허용 IP 등록", notes = "" +
            "1. APIKey에 호출을 허용할 IP를 등록한다.")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token", required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> apiKeyIpSave(@RequestParam(value="accessIp", defaultValue = "") String accessIp,
                                                           @RequestParam(value="ipMemo", defaultValue = "") String ipMemo) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return apiKeyService.apiKeyIpSave(accessIp, ipMemo, jwtFilterDto);
    }

    @PostMapping("/apiKeyIpDelete")
    @ApiOperation(value = "APIKey 허용 IP 삭제", notes = "" +
            "1. 등록한 허용 IP를 삭제한다.")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token", required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> apiKeyIpDelete(@RequestBody ApiKeyIpDeleteDto apiKeyIpDeleteDto) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return apiKeyService.apiKeyIpDelete(apiKeyIpDeleteDto, jwtFilterDto);
    }

//    /**
//     *  시스템 관리자 > API 관리 > API key 리스트
//     */
//    @PostMapping("list")
//    @ApiOperation(value = "ApiKey 리스트 호출 API", notes = "" +
//            "시스템 관리자 > API 관리 > API key 리스트")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
//    })
//    public ResponseEntity<Map<String,Object>> ApiKeyList(@RequestBody ApiKeySetDto apiKeySetDto){
//
//        log.info("API key 리스트 호출");
//
//        log.info("@RequestBody apiKeySetDto : "+apiKeySetDto);
//
//        AjaxResponse res = new AjaxResponse();
//        HashMap<String, Object> data = new HashMap<>();
//
////        DataTables dataTables;
//
////        try{
////            HashMap<String, Object> searchMap;
////            searchMap = (HashMap<String, Object>) paramMap.get("searchData");
//
////            if(paramMap.containsKey("searchData")){
////                searchMap = (HashMap<String, Object>) paramMap.get("searchData");
////                paramMap.putAll(searchMap);
////            }
//
//        List<ApiKeyListAndDetailDto> apiKeyListAndDetailDtos = apiKeyService.findByApiKeyList(apiKeySetDto.getApiKeyMapperDto());
//        Long total = apiKeyService.findByApiKeyListCount(apiKeySetDto.getApiKeyMapperDto());
//        log.info("apiKeyListAndDetailDtos : "+apiKeyListAndDetailDtos);
//        log.info("total : "+total);
//
//        data.put("apiKeyListAndDetailDtos",apiKeyListAndDetailDtos);
//        data.put("total",total);
//
////            dataTables = new DataTables(apiKeyTestDto.getSearchData(), data, total);
//
////            return dataTables.getJsonString();
////        }
////        catch (Exception e) {
////            log.info("예외발생 : "+e);
////
////            return "아무일도없었다.";
////        }
//
//        return ResponseEntity.ok(res.success(data));
//    }
//
//    /**
//     * 서비스 > API 연동관리 > API key 연동관리
//     */
//    @PostMapping("/apiKeyManagement")
//    @ApiOperation(value = "ApiKey 발급 내역 조회", notes = "서비스 > API 연동관리 > API key 연동관리")
//    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
//    public ResponseEntity<Map<String,Object>> apiKeyManagement(){
//        log.info("ApiKey 발급 내역 조회 호출");
//
//        String email = SecurityUtil.getCurrentJwt().getEmail();
//        String userRole = SecurityUtil.getCurrentJwt().getRole();
//
//        return apiKeyService.apiKeyManagement(email, userRole);
//    }
//
//    /**
//     * 서비스 > API 관리 > API key 발급내역
//     */
//    @PostMapping("/testIssue")
//    @ApiOperation(value = "테스트 ApiKey 발급", notes = "서비스 > API 연동관리 > API key 연동관리")
//    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
//    public ResponseEntity<Map<String,Object>> testIssue(){
//        log.info("테스트 ApiKey 발급");
//        String email = SecurityUtil.getCurrentJwt().getEmail();
//        String userRole = SecurityUtil.getCurrentJwt().getRole();
//        return apiKeyService.testIssue(email, userRole);
//    }
//
//    @PostMapping("/issue")
//    @ApiOperation(value = "ApiKey 발급", notes = "서비스 > API 연동관리 > API key 연동관리, ApiKey 발급")
//    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
//    public ResponseEntity<Map<String,Object>> issue() throws NoSuchAlgorithmException {
//        log.info("ApiKey 발급 호출");
//        String email = SecurityUtil.getCurrentJwt().getEmail();
//        String userRole = SecurityUtil.getCurrentJwt().getRole();
//        return apiKeyService.issue(email, userRole);
//    }
//
//    @PostMapping("/reIssue")
//    @ApiOperation(value = "ApiKey 재발급", notes = "서비스 > API 연동관리 > API key 연동관리, ApiKey 재발급")
//    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
//    public ResponseEntity<Map<String,Object>> reIssue() throws NoSuchAlgorithmException {
//        log.info("ApiKey 재발급 호출");
//        String email = SecurityUtil.getCurrentJwt().getEmail();
//        String userRole = SecurityUtil.getCurrentJwt().getRole();
//        return apiKeyService.reIssue(email, userRole);
//    }
//
//    @PostMapping("/modify")
//    @ApiOperation(value = "ApiKey 수정", notes = "서비스 > API 연동관리 > API key 연동관리")
//    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
//    public ResponseEntity<Map<String,Object>> modify(@RequestParam(name="idx") Integer idx, @RequestParam(name="useYn") String useYn, @RequestParam(name="reason") String reason) {
//        log.info("ApiKey 수정 호출");
//        String email = SecurityUtil.getCurrentJwt().getEmail();
//        String userRole = SecurityUtil.getCurrentJwt().getRole();
//        return apiKeyService.modify(idx, useYn, reason, email, userRole);
//    }
}
