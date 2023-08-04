package com.app.kokonut.test;

import com.app.kokonut.alimtalk.AlimtalkSendService;
import com.app.kokonut.alimtalk.dtos.AlimtalkTemplateInfoDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.realcomponent.CommonUtil;
import com.app.kokonut.navercloud.NaverCloudPlatformService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Woody
 * Date : 2023-07-20
 * Time :
 * Remark : 테스트용 컨트롤러
 */
@Slf4j
@RequestMapping("/v1/api/Test")
@RestController
public class TestRestController {

    private final NaverCloudPlatformService naverCloudPlatformService;
    private final AlimtalkSendService alimtalkSendService;

    @Autowired
    public TestRestController(NaverCloudPlatformService naverCloudPlatformService, AlimtalkSendService alimtalkSendService){
        this.naverCloudPlatformService = naverCloudPlatformService;
        this.alimtalkSendService = alimtalkSendService;
    }

    @ApiOperation(value = "공인IP 호출 테스트용")
    @GetMapping(value = "/publicIpGet")
    public ResponseEntity<Map<String,Object>> publicIpGet(HttpServletRequest request) throws IOException {
        log.info("publicIpGet 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("request.getRemoteAddr() : "+request.getRemoteAddr());
        log.info("CommonUtil.publicIp() : "+CommonUtil.publicIp());
        log.info("CommonUtil.clientIp() : "+CommonUtil.clientIp());
        log.info("CommonUtil.testIp(request) : "+CommonUtil.testIp(request));
        log.info("CommonUtil.getServerIp() : "+ CommonUtil.getServerIp());

        return ResponseEntity.ok(res.success(data));
    }

    @ApiOperation(value = "코코넛 API호출 API 테스트용")
    @PostMapping(value = "/kokonutApiHocul")
    public ResponseEntity<Map<String,Object>> kokonutApiHocul() {
        log.info("kokonutApiHocul 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        boolean result = naverCloudPlatformService.kokonutApiHocul();
        log.info("result : "+result);

        return ResponseEntity.ok(res.success(data));
    }

    @ApiOperation(value = "테스트 탬플릿 검수승인")
    @PostMapping(value = "/alimtalkTemplateInspection")
    public ResponseEntity<Map<String,Object>> alimtalkTemplateInspection(@RequestParam(value="templateCode", defaultValue = "") String templateCode,
                                                                         @RequestParam(value="profileKey", defaultValue = "") String profileKey) {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String result = alimtalkSendService.alimtalkTemplateInspection(profileKey, templateCode);
        log.info("result : "+result);

        return ResponseEntity.ok(res.success(data));
    }

    @ApiOperation(value = "테스트 일회성 인증코드 받기")
    @PostMapping(value = "/alimtalkTemplateAuth")
    public ResponseEntity<Map<String,Object>> alimtalkTemplateAuth(@RequestParam(value="phoneNumber", defaultValue = "") String phoneNumber) {

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String result = alimtalkSendService.alimtalkTemplateAuth(phoneNumber);
        log.info("result : "+result);

        return ResponseEntity.ok(res.success(data));
    }

    @ApiOperation(value = "테스트 일회성 인증승인 받기")
    @PostMapping(value = "/alimtalkTemplateAuthCheck")
    public ResponseEntity<Map<String,Object>> alimtalkTemplateAuthCheck(@RequestParam(value="phoneNumber", defaultValue = "") String phoneNumber,
                                                                        @RequestParam(value="checkNumber", defaultValue = "") String checkNumber) {

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String result = alimtalkSendService.alimtalkTemplateAuthCheck(phoneNumber, checkNumber);
        log.info("result : "+result);

        return ResponseEntity.ok(res.success(data));
    }

    @ApiOperation(value = "비즈엠 템플릿정보 가져오기 API 테스트용")
    @PostMapping(value = "/alimtalkTemplateInfo")
    public ResponseEntity<Map<String,Object>> alimtalkTemplateInfo(@RequestParam(value="profileKey", defaultValue = "") String profileKey,
                                                               @RequestParam(value="templateCode", defaultValue = "") String templateCode) {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        AlimtalkTemplateInfoDto result = alimtalkSendService.alimtalkTemplateInfo(profileKey, templateCode);
        log.info("result : "+result);

        return ResponseEntity.ok(res.success(data));
    }

    @ApiOperation(value = "비즈엠 알림톡전송 API 테스트용")
    @PostMapping(value = "/alimtalkApiHocul")
    public ResponseEntity<Map<String,Object>> alimtalkApiHocul(@RequestParam(value="profileKey", defaultValue = "") String profileKey,
                                                               @RequestParam(value="templateCode", defaultValue = "") String templateCode,
                                                               @RequestParam(value="message", defaultValue = "") String message,
                                                               @RequestParam(value="receiver_num", defaultValue = "") String receiver_num) {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String result = alimtalkSendService.alimtalkSend(profileKey, templateCode, message, receiver_num, null);
        log.info("result : "+result);

        return ResponseEntity.ok(res.success(data));
    }

    @ApiOperation(value = "부트페이 웹훅 호출용 테스트 API")
    @PostMapping(value = "/bootPayWebhookCallBack")
    public ResponseEntity<Map<String,Object>> bootPayWebhookCallBack(@RequestBody HashMap<String,Object> paramMap,
                                                                     HttpServletRequest request, HttpServletResponse response) {
        log.info("bootPayWebhookCallBack 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("paramMap : "+paramMap);
        log.info("request : "+request);
        log.info("response : "+response);

        return ResponseEntity.ok(res.success(data));
    }


}
