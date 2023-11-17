package com.app.kokonut.test;

import com.app.kokonut.alimtalk.AlimtalkSendService;
import com.app.kokonut.alimtalk.dtos.AlimtalkTemplateInfoDto;
import com.app.kokonut.auth.jwt.dto.RedisDao;
import com.app.kokonut.common.*;
import com.app.kokonut.company.companytablecolumninfo.CompanyTableColumnInfoRepository;
import com.app.kokonut.configs.ExcelService;
import com.app.kokonut.configs.MailSender;
import com.app.kokonut.email.email.EmailService;
import com.app.kokonut.navercloud.NaverCloudPlatformService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

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

    private final RedisDao redisDao;
    private final MailSender mailSender;
    private final EmailService emailService;
    private final ExcelService excelService;
    private final NaverCloudPlatformService naverCloudPlatformService;
    private final AlimtalkSendService alimtalkSendService;
    private final CompanyTableColumnInfoRepository companyTableColumnInfoRepository;

    @Autowired
    public TestRestController(RedisDao redisDao, MailSender mailSender, EmailService emailService,
                              ExcelService excelService, NaverCloudPlatformService naverCloudPlatformService,
                              AlimtalkSendService alimtalkSendService, CompanyTableColumnInfoRepository companyTableColumnInfoRepository){
        this.redisDao = redisDao;
        this.mailSender = mailSender;
        this.emailService = emailService;
        this.excelService = excelService;
        this.naverCloudPlatformService = naverCloudPlatformService;
        this.alimtalkSendService = alimtalkSendService;
        this.companyTableColumnInfoRepository = companyTableColumnInfoRepository;
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

    @ApiOperation(value = "엑셀다운로드 API 테스트용")
    @GetMapping(value = "/excelDownload")
    public ResponseEntity<Map<String,Object>> excelDownload() throws IOException {
        log.info("excelDownload 호출");

        List<Map<String, Object>> dataList = new ArrayList<>();

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data;

        Map<String, Object> row1 = new HashMap<>();
        row1.put("id", 1);
        row1.put("name2", "John");
        row1.put("age", 35);

        Map<String, Object> row2 = new HashMap<>();
        row2.put("id", 2);
        row2.put("name", "Sally");
        row2.put("age", 28);

        dataList.add(row1);
        dataList.add(row2);

        data = excelService.createExcelFile("테스트압축파일", "테스트시트명", dataList, "1234");

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

    @ApiOperation(value = "JWT토큰 삭제 테스트")
    @GetMapping(value = "/jwtDeleteTest")
    public ResponseEntity<Map<String,Object>> jwtDeleteTest(@RequestParam(value="knEmail", defaultValue = "") String knEmail) {

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        String publicIpReplace = CommonUtil.publicIp().replaceAll("\\.",""); // 111.222.333 = 111222333
        String refreshToken = redisDao.getValues("RT: "+knEmail+"-"+publicIpReplace);
        log.info("refreshToken : "+refreshToken);

        if(refreshToken != null) {
            redisDao.deleteValues("RT: "+knEmail+"-"+publicIpReplace);
        }

        refreshToken = redisDao.getValues("RT: "+knEmail+"-"+publicIpReplace);
        log.info("refreshToken : "+refreshToken);

        return ResponseEntity.ok(res.success(data));
    }

    @ApiOperation(value="사진등록 테스트")
    @PostMapping(value = "/phototest")
    public ResponseEntity<Map<String,Object>> phototest(@ModelAttribute PhototestDto phototestDto) {
        log.info("phototest 호출");

        log.info("codeList : "+phototestDto.getCodeList());
        log.info("dataList : "+phototestDto.getDataList());
        log.info("multipartFile : "+phototestDto.getMultipartFile());

        return apifilesendtest(phototestDto);
    }

    public ResponseEntity<Map<String,Object>> apifilesendtest(PhototestDto phototestDto) {
        log.info("apifilesendtest 호출");

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        try {
            String url = "http://127.0.0.1:8050/v3/api/Auth/register";
//            log.info("url : " + url);

            try {
                URL apiurl = new URL(url);

                HttpURLConnection conn = (HttpURLConnection) apiurl.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("content-type", "application/json");
                conn.setRequestProperty("x-api-key", "ebb36afbd976c0aa4ff157616e30e674");
                conn.setUseCaches(false);
                conn.setDoOutput(true);
                conn.setDoInput(true);

                MultipartFile multipartFile = phototestDto.getMultipartFile().get(0);
                log.info("multipartFile.getOriginalFilename() : "+multipartFile.getOriginalFilename());
                log.info("multipartFile.getSize() : "+multipartFile.getSize());
                log.info("multipartFile.getOriginalFilename() : "+multipartFile.getOriginalFilename());

                byte[] binary = multipartFile.getBytes();

                // Request Body 설정
                String jsonInputString = "{"
                        + "\"1_id\": \"test111\","
                        + "\"1_pw\": \"test222\","
                        + "\"1_50\": \"woody@kokonut.me\","
                        + "\"1_51\": \"김우디\","
                        + "\"1_7\": \""+ Arrays.toString(binary) +"\"}";

                try(OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                // 응답 데이터 얻기
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                String code = String.valueOf(conn.getResponseCode());
                String data2 = sb.toString();

                br.close();
                conn.disconnect();

                log.info("code: {}, data: {}", code, data2);

            } catch (Exception e) {
                log.error("예외처리 : " + e);
                log.error("예외처리 메세지 : " + e.getMessage());
            }
        } catch (Exception e) {
            log.error("예외처리 1 : "+e);
            log.error("예외처리 메세지 1 : "+e.getMessage());
        }

        return ResponseEntity.ok(res.apisuccess(data));
    }


}
