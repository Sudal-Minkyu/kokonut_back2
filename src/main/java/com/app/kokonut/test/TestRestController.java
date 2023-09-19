package com.app.kokonut.test;

import com.app.kokonut.alimtalk.AlimtalkSendService;
import com.app.kokonut.alimtalk.dtos.AlimtalkTemplateInfoDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.CommonUtil;
import com.app.kokonut.common.ReqUtils;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.configs.MailSender;
import com.app.kokonut.email.email.EmailService;
import com.app.kokonut.navercloud.NaverCloudPlatformService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Value("${kokonut.happytalk.profilekey}")
    public String profilekey;

    private final MailSender mailSender;
    private final EmailService emailService;
    private final NaverCloudPlatformService naverCloudPlatformService;
    private final AlimtalkSendService alimtalkSendService;

    @Autowired
    public TestRestController(MailSender mailSender, EmailService emailService,
                              NaverCloudPlatformService naverCloudPlatformService, AlimtalkSendService alimtalkSendService){
        this.mailSender = mailSender;
        this.emailService = emailService;
        this.naverCloudPlatformService = naverCloudPlatformService;
        this.alimtalkSendService = alimtalkSendService;
    }

    @ApiOperation(value = "메일전송 테스트용")
    @GetMapping(value = "/mailSendTest")
    public ResponseEntity<Map<String,Object>> mailSendTest() {
        log.info("mailSendTest 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        // 인증번호 메일전송
        String title = ReqUtils.filter("메일전송을 완료했습니다.");
        String contents = ReqUtils.unFilter("메일내용");

        // 템플릿 호출을 위한 데이터 세팅
		HashMap<String, String> callTemplate = new HashMap<>();
//		callTemplate.put("template", "MailTemplate");
		callTemplate.put("title", title);
		callTemplate.put("content", contents);

//		// 템플릿 TODO 템플릿 디자인 추가되면 수정
		contents = mailSender.getHTML6(callTemplate);
        String reciverName = "kokonut";

        String mailSenderResult = mailSender.sendKokonutMail("woody@kokonut.me", reciverName, title, contents);
        if(mailSenderResult != null) {
            // mailSender 성공
            log.info("### 메일전송 성공했습니다. reciver Email : woody@kokonut.me");
        }else{
            // mailSender 실패
            log.error("### 해당 메일 전송에 실패했습니다. 관리자에게 문의하세요. reciverEmail : woody@kokonut.me");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.KO041.getCode(), ResponseErrorCode.KO041.getDesc()));
        }

        return ResponseEntity.ok(res.success(data));
    }

    @ApiOperation(value = "현재 원화가치 호출하기")
    @GetMapping(value = "/wonPriceGet")
    public ResponseEntity<Map<String,Object>> wonPriceGet() {
        log.info("wonPriceGet 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        int wonPrice = CommonUtil.wonPriceGet();
        log.info("원화가치(USD기준) : "+wonPrice);

        return ResponseEntity.ok(res.success(data));
    }

    @ApiOperation(value = "공인IP 호출 테스트용")
    @GetMapping(value = "/publicIpGet")
    public ResponseEntity<Map<String,Object>> publicIpGet(HttpServletRequest request) {
        log.info("publicIpGet 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        log.info("request.getRemoteAddr() : "+request.getRemoteAddr());
        log.info("CommonUtil.publicIp() : "+CommonUtil.publicIp());
        log.info("CommonUtil.getServerIp() : "+ CommonUtil.getServerIp());

        // 테스트하는곳
//        String referrer = request.getHeader("Referer");
//        log.info("referrer : "+ referrer);

        data.put("request.getRemoteAddr()", request.getRemoteAddr());
        data.put("CommonUtil.publicIp()",CommonUtil.publicIp());
        data.put("CommonUtil.getServerIp()",CommonUtil.getServerIp());

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
