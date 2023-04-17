package com.app.kokonut.navercloud;

import com.app.kokonut.bizMessage.alimtalkMessage.dto.AlimtalkMessageSendDto;
import com.app.kokonut.bizMessage.alimtalkMessage.dto.AlimtalkMessageSendSubDto;
import com.app.kokonut.bizMessage.alimtalkTemplate.dto.AlimtalkTemplateSaveAndUpdateDto;
import com.app.kokonut.bizMessage.friendtalkMessage.dto.FriendtalkMessageSendDto;
import com.app.kokonut.bizMessage.friendtalkMessage.dto.FriendtalkMessageSendSubDto;
import com.app.kokonut.common.realcomponent.Converter;
import com.app.kokonut.keydata.KeyDataService;
import com.app.kokonut.navercloud.dto.NCloudPlatformMailRequest;
import com.app.kokonut.navercloud.dto.NaverCloudPlatformResultDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Slf4j
@Service
public class NaverCloudPlatformService {

    @Value("${kokonut.ncloud.serviceId}")
    public String serviceId;

    @Value("${kokonut.ncloud.accessKey}")
    public String accessKey;

    @Value("${kokonut.ncloud.secretKey}")
    public String secretKey;

    @Value("${kokonut.ncloud.primaryKey}")
    public String primaryKey;

    @Value("${kokonut.ncloud.categoryCode}")
    public String categoryCode;

    public static final String typeAlimTalk = "alimtalk";
    public static final String typeFriendTalk = "friendtalk";

    @Autowired
    public NaverCloudPlatformService(KeyDataService keyDataService) {
//        KeyDataNCLOUDDto keyDataNCLOUDDto = keyDataService.ncloud_key();
//        this.serviceId = keyDataNCLOUDDto.getNCLOUDSERVICEID();
//        this.accessKey = keyDataNCLOUDDto.getNCLOUDSERVICEACCESS();
//        this.secretKey = keyDataNCLOUDDto.getNCLOUDSERVICESECRET();
//        this.primaryKey = keyDataNCLOUDDto.getNCLOUDSERVICEPRIMARY();
//        this.categoryCode = keyDataNCLOUDDto.getNCLOUDSERVICECATEGORY();
    }

    /**
     * 시그니쳐 생성
     *
     * @param url
     * @param timestamp
     * @param method
     */
    public String makeSignature(String url, String timestamp, String method) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {

        String space = " ";
        String newLine = "\n";

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey;
        String encodeBase64String;

        signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);

        return encodeBase64String;
    }

    // 비즈메시지 채널 조회
    public List<HashMap<String, Object>> getChannels() throws Exception {
		List<HashMap<String, Object>> channelList;

		String hostNameUrl = "https://sens.apigw.ntruss.com";
        String requestUrl= "/alimtalk/v2/services/";
        String requestUrlType = "/channels";
        String method = "GET";
        String timestamp = Long.toString(System.currentTimeMillis());

        requestUrl += serviceId + requestUrlType;
        String apiUrl = hostNameUrl + requestUrl;
        URL url = new URL(apiUrl);

        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setUseCaches(false);
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestProperty("content-type", "application/json");
        con.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
        con.setRequestProperty("x-ncp-iam-access-key", accessKey);
        con.setRequestProperty("x-ncp-apigw-signature-v2", makeSignature(requestUrl, timestamp, method));
        con.setRequestMethod(method);
        con.setDoOutput(true);

        int responseCode = con.getResponseCode();
        BufferedReader br;

        if(responseCode == 200) {
            // 정상 호출
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            // 에러 발생
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }

        br.close();
        channelList = new ObjectMapper().readValue(response.toString(), new TypeReference<>() {
        });
        con.disconnect();

		return channelList;
    }

    /**
     * 비즈메시지 채널 등록
     *
     * @param channelId     카카오 채널 아이디  (required)
     * @param adminTelNo    관리자 전화번호    (required)
     */
    public NaverCloudPlatformResultDto postChannels(String channelId, String adminTelNo) throws Exception {

        NaverCloudPlatformResultDto naverCloudPlatformResultDto = new NaverCloudPlatformResultDto();

    	String hostNameUrl = "https://sens-biz.apigw.ntruss.com";
        String requestUrl= "/kkobizmsg/v2/services/";
        String requestUrlType = "/channels";
        String method = "POST";
        String timestamp = Long.toString(System.currentTimeMillis());

        requestUrl += serviceId + requestUrlType;
        String apiUrl = hostNameUrl + requestUrl;

        // JSON 을 활용한 body data 생성
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("categoryCode", categoryCode);
        bodyJson.put("adminTelNo", adminTelNo);
        bodyJson.put("channelId", channelId);
        String body = bodyJson.toString();

        URL url = new URL(apiUrl);

        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setUseCaches(false);
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestProperty("content-type", "application/json");
        con.setRequestProperty("x-ncp-apigw-api-key", primaryKey);
        con.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
        con.setRequestProperty("x-ncp-iam-access-key", accessKey);
        con.setRequestProperty("x-ncp-apigw-signature-v2", makeSignature(requestUrl, timestamp, method));
        con.setRequestMethod(method);
        con.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.write(body.getBytes());
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();
        BufferedReader br;

        if(responseCode == 200) {
            // 정상 호출
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            // 에러 발생
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }

        br.close();

        con.disconnect();

        naverCloudPlatformResultDto.setResultCode(responseCode);
        naverCloudPlatformResultDto.setResultText(String.valueOf(response));

	    return naverCloudPlatformResultDto;
    }

    /**
     * 비즈메시지 채널 인증
     *
     * @param channelId  카카오 채널 아이디 (required)
     * @param token 인증번호 (required)
     */
	public NaverCloudPlatformResultDto postChannelToken(String channelId, String token) throws Exception {

        NaverCloudPlatformResultDto naverCloudPlatformResultDto = new NaverCloudPlatformResultDto();

		String hostNameUrl = "https://sens-biz.apigw.ntruss.com";
        String requestUrl= "/kkobizmsg/v2/services/";
        String requestUrlType = "/channel-token";
        String method = "POST";
        String timestamp = Long.toString(System.currentTimeMillis());

        requestUrl += serviceId + requestUrlType;
        String apiUrl = hostNameUrl + requestUrl;

        // JSON 을 활용한 body data 생성
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("channelId", channelId);
        bodyJson.put("token", token);
        String body = bodyJson.toString();

        URL url = new URL(apiUrl);

        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setUseCaches(false);
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestProperty("content-type", "application/json");
        con.setRequestProperty("x-ncp-apigw-api-key", primaryKey);
        con.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
        con.setRequestProperty("x-ncp-iam-access-key", accessKey);
        con.setRequestProperty("x-ncp-apigw-signature-v2", makeSignature(requestUrl, timestamp, method));
        con.setRequestMethod(method);
        con.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.write(body.getBytes());
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();
        BufferedReader br;

        if(responseCode == 200) {
            // 정상 호출
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            // 에러 발생
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }

        br.close();

        con.disconnect();
        naverCloudPlatformResultDto.setResultCode(responseCode);
        naverCloudPlatformResultDto.setResultText(String.valueOf(response));

        return naverCloudPlatformResultDto;
    }

    /**
     * 비즈메시지 채널 상태 변경
     *
     * @param channelId  카카오 채널 아이디 (required)
     * @param status     변경할 상태      (required)
     */
	public NaverCloudPlatformResultDto patchChannelStatus(String channelId, String status) throws Exception {

        NaverCloudPlatformResultDto naverCloudPlatformResultDto = new NaverCloudPlatformResultDto();

		allowMethods("PATCH");

		String hostNameUrl = "https://sens-biz.apigw.ntruss.com";
        String requestUrl= "/kkobizmsg/v2/services/";
        String requestUrlType = "/channel-status";
        String method = "PATCH";
        String timestamp = Long.toString(System.currentTimeMillis());

        requestUrl += serviceId + requestUrlType;
        String apiUrl = hostNameUrl + requestUrl;

        // JSON 을 활용한 body data 생성
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("channelId", channelId);
        bodyJson.put("status", status);
        String body = bodyJson.toString();

        URL url = new URL(apiUrl);

        HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
        con.setUseCaches(false);
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestProperty("content-type", "application/json");
        con.setRequestProperty("x-ncp-apigw-api-key", primaryKey);
        con.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
        con.setRequestProperty("x-ncp-iam-access-key", accessKey);
        con.setRequestProperty("x-ncp-apigw-signature-v2", makeSignature(requestUrl, timestamp, method));
        con.setRequestProperty("charset", "utf-8");
        con.setRequestMethod(method);

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.write(body.getBytes());
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();
        BufferedReader br;

        log.info("responseCode" +" " + responseCode);

        if(responseCode == 200) {
            // 정상 호출
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            // 에러 발생
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }

        log.info("response" +" " + response);

        br.close();

        con.disconnect();

        naverCloudPlatformResultDto.setResultCode(responseCode);
        naverCloudPlatformResultDto.setResultText(String.valueOf(response));

        return naverCloudPlatformResultDto;
	}

    /**
     * 비즈메시지 채널 삭제
     *
     * @param channelId 카카오 채널 아이디 (required)
     */
    public NaverCloudPlatformResultDto deleteChannels(String channelId) throws Exception {

        NaverCloudPlatformResultDto naverCloudPlatformResultDto = new NaverCloudPlatformResultDto();

    	String hostNameUrl = "https://sens-biz.apigw.ntruss.com";
        String requestUrl= "/kkobizmsg/v2/services/";
        String requestUrlType = "/channels?channelId=";
        String method = "DELETE";
        String timestamp = Long.toString(System.currentTimeMillis());

        requestUrl += serviceId + requestUrlType + channelId;
        String apiUrl = hostNameUrl + requestUrl;

        URL url = new URL(apiUrl);

        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setUseCaches(false);
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestProperty("content-type", "application/json");
        con.setRequestProperty("x-ncp-apigw-api-key", primaryKey);
        con.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
        con.setRequestProperty("x-ncp-iam-access-key", accessKey);
        con.setRequestProperty("x-ncp-apigw-signature-v2", makeSignature(requestUrl, timestamp, method));
        con.setRequestMethod(method);
        con.setDoOutput(true);

        int responseCode = con.getResponseCode();
        BufferedReader br;

        if(responseCode == 200) {
            // 정상 호출
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            // 에러 발생
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }

        br.close();
        con.disconnect();

        naverCloudPlatformResultDto.setResultCode(responseCode);
        naverCloudPlatformResultDto.setResultText(String.valueOf(response));

        return naverCloudPlatformResultDto;
    }

    /**
     * 알림톡 템플릿 조회
     */
	public NaverCloudPlatformResultDto getTemplates(String channelId, String templateCode, String templateName) throws Exception {

        NaverCloudPlatformResultDto naverCloudPlatformResultDto = new NaverCloudPlatformResultDto();

		String hostNameUrl = "https://sens.apigw.ntruss.com";
        String requestUrl= "/alimtalk/v2/services/";
        String requestUrlType = "/templates?channelId=";
        String method = "GET";
        String timestamp = Long.toString(System.currentTimeMillis());

        requestUrl += serviceId + requestUrlType + channelId;
        if(!templateCode.equals("")) {
        	requestUrl += "&templateCode=" + templateCode;
        }
        if(!templateCode.equals("")) {
        	requestUrl += "&templateName=" + templateName;
        }
        String apiUrl = hostNameUrl + requestUrl;

        URL url = new URL(apiUrl);

        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setUseCaches(false);
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestProperty("content-type", "application/json");
        con.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
        con.setRequestProperty("x-ncp-iam-access-key", accessKey);
        con.setRequestProperty("x-ncp-apigw-signature-v2", makeSignature(requestUrl, timestamp, method));
        con.setRequestMethod(method);
        con.setDoOutput(true);

        int responseCode = con.getResponseCode();
        BufferedReader br;

        if(responseCode == 200) {
            // 정상 호출
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            // 에러 발생
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }

        br.close();

        con.disconnect();
        naverCloudPlatformResultDto.setResultCode(responseCode);
        naverCloudPlatformResultDto.setResultText(String.valueOf(response));

        return naverCloudPlatformResultDto;
    }

    /**
     * 알림톡 템플릿 등록 + 수정
     *
     * @param alimtalkTemplateSaveAndUpdateDto
     *
     * 필수값
     * templateCode
     * templateName
     * content
     * messageType
     * emphasizeType
     * securityFlag
     *
     * 서브값
     * extraContent
     * adContent
     * emphasizeTitle
     * emphasizeSubTitle
     * buttons > type*
     * buttons > name*
     * buttons > linkMobile
     * buttons > linkPc
     * buttons > schemeAndroid
     * buttons > schemeIos
     * buttons > order
     */
	public NaverCloudPlatformResultDto postTemplates(AlimtalkTemplateSaveAndUpdateDto alimtalkTemplateSaveAndUpdateDto, String method) {
        NaverCloudPlatformResultDto naverCloudPlatformResultDto = new NaverCloudPlatformResultDto();

		String hostNameUrl = "https://sens-biz.apigw.ntruss.com";
        String requestUrl= "/alimtalk/v2/services/";
        String requestUrlType = "/templates";

        log.info("method POST - 신규등록, PUT - 수정 : "+method);

        String timestamp = Long.toString(System.currentTimeMillis());

        requestUrl += serviceId + requestUrlType;
        String apiUrl = hostNameUrl + requestUrl;

        // JSON 을 활용한 body data 생성
        JSONObject bodyJson = new JSONObject();
        JSONArray toArr = new JSONArray();

        // 필수값
        bodyJson.put("channelId", alimtalkTemplateSaveAndUpdateDto.getKcChannelId());
        bodyJson.put("templateCode", alimtalkTemplateSaveAndUpdateDto.getAtTemplateCode());
        bodyJson.put("templateName", alimtalkTemplateSaveAndUpdateDto.getAtTemplateName());
        bodyJson.put("content", alimtalkTemplateSaveAndUpdateDto.getAtAdContent());
        bodyJson.put("messageType", alimtalkTemplateSaveAndUpdateDto.getAtMessageType());
        bodyJson.put("emphasizeType", alimtalkTemplateSaveAndUpdateDto.getAtEmphasizeType());
        bodyJson.put("securityFlag", alimtalkTemplateSaveAndUpdateDto.getSecurityFlag());

		if(alimtalkTemplateSaveAndUpdateDto.getAtExtraContent() != null) {
			bodyJson.put("extraContent", alimtalkTemplateSaveAndUpdateDto.getAtExtraContent());
		}
		if(alimtalkTemplateSaveAndUpdateDto.getAtAdContent() != null) {
			bodyJson.put("adContent", alimtalkTemplateSaveAndUpdateDto.getAtAdContent());
		}
		if(alimtalkTemplateSaveAndUpdateDto.getAtEmphasizeTitle() != null) {
			bodyJson.put("emphasizeTitle", alimtalkTemplateSaveAndUpdateDto.getAtEmphasizeTitle());
		}
		if(alimtalkTemplateSaveAndUpdateDto.getAtEmphasizeSubTitle() != null) {
			bodyJson.put("emphasizeSubTitle", alimtalkTemplateSaveAndUpdateDto.getAtEmphasizeSubTitle());
		}

        if(alimtalkTemplateSaveAndUpdateDto.getBtnSize() != null) {
	        int btnSize = alimtalkTemplateSaveAndUpdateDto.getBtnSize();

            List<String> btnTypeList = alimtalkTemplateSaveAndUpdateDto.getBtnTypeList();
            List<String> btnNameList = alimtalkTemplateSaveAndUpdateDto.getBtnNameList();

            List<String> btnLink1List = alimtalkTemplateSaveAndUpdateDto.getBtnLink1List();
            List<String> btnLink2List = alimtalkTemplateSaveAndUpdateDto.getBtnLink2List();

	        for(int i = 0; i < btnSize; i++) {
	        	String btnType = btnTypeList.get(i);
	        	String btnName = btnNameList.get(i);

	            JSONObject toJson = new JSONObject();

	            toJson.put("type", btnType);
	            toJson.put("name", btnName);

	            // 웹 링크 버튼타입
	            if("WL".equals(btnType)) {
//		        	String linkMobile = paramMap.get("btnLink1_" + i).toString();
//		        	String linkPc = paramMap.get("btnLink2_" + i).toString();

                    String linkMobile = btnLink1List.get(i);
                    String linkPc = btnLink2List.get(i);

	            	toJson.put("linkMobile", linkMobile);
		            toJson.put("linkPc", linkPc);

		        // 앱 링크 버튼 타입
	            } else if("AL".equals(btnType)) {
//		        	String schemeAndroid = paramMap.get("btnLink1_" + i).toString();
//		        	String schemeIos = paramMap.get("btnLink2_" + i).toString();

                    String schemeAndroid = btnLink1List.get(i);
		        	String schemeIos = btnLink2List.get(i);

		            toJson.put("schemeAndroid", schemeAndroid);
		            toJson.put("schemeIos", schemeIos);
	            }

	            toJson.put("order", i+1);
	            toArr.put(toJson);
	        }
		}

        bodyJson.put("buttons", toArr);

        String body = bodyJson.toString();

        try {
            URL url = new URL(apiUrl);

            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("content-type", "application/json");
            con.setRequestProperty("x-ncp-apigw-api-key", primaryKey);
            con.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
            con.setRequestProperty("x-ncp-iam-access-key", accessKey);
            con.setRequestProperty("x-ncp-apigw-signature-v2", makeSignature(requestUrl, timestamp, method));
            con.setRequestMethod(method);
            con.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(body.getBytes());
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;

            if(responseCode == 200) {
                // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close();

            con.disconnect();

            naverCloudPlatformResultDto.setResultCode(responseCode);
            naverCloudPlatformResultDto.setResultText(String.valueOf(response));

            if(method.equals("POST")){
                log.info("알림톡 템플릿 생성 성공");
            }else {
                log.info("알림톡 템플릿 수정 성공");
            }
	    } catch (Exception e) {
            if(method.equals("POST")){
                log.error("알림톡 템플릿 생성 실패"+e.getMessage());
            }else {
                log.error("알림톡 템플릿 수정 실패"+e.getMessage());
            }

	    }

        return naverCloudPlatformResultDto;
    }

    /**
     * 알림톡 템플릿 삭제
     *
     * @param channelId     카카오 채널 아이디 (required)
     * @param templateCode  탬플릿 코드      (required)
     */
    public NaverCloudPlatformResultDto deleteTemplates(String channelId, String templateCode) {
        NaverCloudPlatformResultDto naverCloudPlatformResultDto = new NaverCloudPlatformResultDto();

		String hostNameUrl = "https://sens-biz.apigw.ntruss.com";
        String requestUrl= "/alimtalk/v2/services/";
        String requestUrlType = "/templates?channelId=";
        String method = "DELETE";
        String timestamp = Long.toString(System.currentTimeMillis());

        requestUrl += serviceId + requestUrlType + channelId + "&templateCode=" + templateCode;
        String apiUrl = hostNameUrl + requestUrl;

        try {
            URL url = new URL(apiUrl);

            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("content-type", "application/json");
            con.setRequestProperty("x-ncp-apigw-api-key", primaryKey);
            con.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
            con.setRequestProperty("x-ncp-iam-access-key", accessKey);
            con.setRequestProperty("x-ncp-apigw-signature-v2", makeSignature(requestUrl, timestamp, method));
            con.setRequestMethod(method);
            con.setDoOutput(true);

            int responseCode = con.getResponseCode();
            BufferedReader br;

            if(responseCode == 200) {
                // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close();
            con.disconnect();

            naverCloudPlatformResultDto.setResultCode(responseCode);
            naverCloudPlatformResultDto.setResultText(String.valueOf(response));

            log.error("알림톡 템플릿 삭제 성공");
	    } catch (Exception e) {
            log.error("알림톡 템플릿 삭제 실패 : "+e.getMessage());
	    }

        return naverCloudPlatformResultDto;
    }

    /**
     * 알림톡/친구톡 메시지 발송 요청 조회
     *
     * @param requestId 요청 아이디 (required)
     * @param serviceType  취소 타입 지정. 알림톡/친구톡 - 호출 시 클래스 상단에 지정된 타입을 사용하자
     */
	public NaverCloudPlatformResultDto getMessages(String requestId, String serviceType) {
        NaverCloudPlatformResultDto naverCloudPlatformResultDto = new NaverCloudPlatformResultDto();

		String hostNameUrl = "https://sens.apigw.ntruss.com";
        String requestUrl= "/" + serviceType + "/v2/services/";
        String requestUrlType = "/messages?requestId=";
        String method = "GET";
        String timestamp = Long.toString(System.currentTimeMillis());

        requestUrl += serviceId + requestUrlType + requestId;
        String apiUrl = hostNameUrl + requestUrl;

        try {
            URL url = new URL(apiUrl);

            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("content-type", "application/json");
            con.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
            con.setRequestProperty("x-ncp-iam-access-key", accessKey);
            con.setRequestProperty("x-ncp-apigw-signature-v2", makeSignature(requestUrl, timestamp, method));
            con.setRequestMethod(method);
            con.setDoOutput(true);

            int responseCode = con.getResponseCode();
            BufferedReader br;

            if(responseCode == 200) {
                // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close();

            con.disconnect();

            naverCloudPlatformResultDto.setResultCode(responseCode);
            naverCloudPlatformResultDto.setResultText(String.valueOf(response));

            log.info("알림톡/친구톡 메시지 발송 요청 조회 성공");
	    } catch (Exception e) {
            log.error("알림톡/친구톡 메시지 발송 요청 조회 실패"+e.getMessage());
	    }

	    return naverCloudPlatformResultDto;
    }


    /**
     * 예약 메시지 상태 조회
     *
     * @param reserveId    예약 발송 요청 조회 시 반환되는 메시지 식별자(requestId)
     * @param serviceType  취소 타입 지정. 알림톡/친구톡 - 호출 시 클래스 상단에 지정된 타입을 사용하자
     */
    public NaverCloudPlatformResultDto getReserveState(String reserveId, String serviceType) {
        NaverCloudPlatformResultDto naverCloudPlatformResultDto = new NaverCloudPlatformResultDto();

        String hostNameUrl = "https://sens.apigw.ntruss.com";
        String requestUrl= "/" + serviceType + "/v2/services/";
        String requestUrlSub = "/reservations/" + reserveId + "/reserve-status";
        String method = "GET";
        String timestamp = Long.toString(System.currentTimeMillis());

        requestUrl += serviceId + requestUrlSub;

        String apiUrl = hostNameUrl + requestUrl;

        try {
            URL url = new URL(apiUrl);

            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("content-type", "application/json");
            con.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
            con.setRequestProperty("x-ncp-iam-access-key", accessKey);
            con.setRequestProperty("x-ncp-apigw-signature-v2", makeSignature(requestUrl, timestamp, method));
            con.setRequestMethod(method);
            con.setDoOutput(true);

            int responseCode = con.getResponseCode();
            BufferedReader br;

            if(responseCode == 200) {
                // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close();

            con.disconnect();

            naverCloudPlatformResultDto.setResultCode(responseCode);
            naverCloudPlatformResultDto.setResultText(String.valueOf(response));

            log.info("예약메세지 상태 조회 성공");
        } catch (Exception e) {
            log.error("예약메세지 상태 조회 실패"+e.getMessage());
        }

        return naverCloudPlatformResultDto;
    }


    /**
     * 알림톡 메시지 발송 요청
     *
     * @param alimtalkMessageSendDto
     *
     * 필수값
     * templateCode
     * plusFriendId
     * to
     * content
     *
     * 서브값
     * itemHighlight > title
     * itemHighlight > description
     * item > list > title
     * item > list > description
     * item > summary > title
     * item > summary > description
     * buttons > type
     * buttons > name
     */
	public NaverCloudPlatformResultDto postMessages(AlimtalkMessageSendDto alimtalkMessageSendDto) {

        NaverCloudPlatformResultDto naverCloudPlatformResultDto = new NaverCloudPlatformResultDto();

		String hostNameUrl = "https://sens.apigw.ntruss.com";
        String requestUrl= "/alimtalk/v2/services/";
        String requestUrlType = "/messages";
        String method = "POST";
        String timestamp = Long.toString(System.currentTimeMillis());

        requestUrl += serviceId + requestUrlType;
        String apiUrl = hostNameUrl + requestUrl;

        // JSON 을 활용한 body data 생성
        JSONObject bodyJson = new JSONObject();
        JSONArray toMessagesArr = new JSONArray();
        JSONArray toButtonArr = new JSONArray();

        // 필수값
        String templateCode = alimtalkMessageSendDto.getAtTemplateCode();
        String plusFriendId = alimtalkMessageSendDto.getKcChannelId();
        String content = alimtalkMessageSendDto.getTemplateContent();

        String emphasizeType = alimtalkMessageSendDto.getAtEmphasizeType();
        String emphasizeTitle = alimtalkMessageSendDto.getAtEmphasizeTitle();

        bodyJson.put("plusFriendId", plusFriendId);
        bodyJson.put("templateCode", templateCode);

//        List<Map<String, Object>> recipientList = (List<Map<String, Object>>) paramMap.get("recipients");
        List<AlimtalkMessageSendSubDto> alimtalkMessageSendSubDtoList = alimtalkMessageSendDto.getAlimtalkMessageSendSubDtoList();

        JSONObject toMessagesJson;
        JSONObject toButtonJson;
		for(AlimtalkMessageSendSubDto alimtalkMessageSendSubDto : alimtalkMessageSendSubDtoList) {

			toMessagesJson = new JSONObject();
			toMessagesJson.put("countryCode", "");
	        toMessagesJson.put("to", alimtalkMessageSendSubDto.getPhoneNumber());
	        toMessagesJson.put("content", content);

	        if("TEXT".equals(emphasizeType)) {
	        	toMessagesJson.put("title", emphasizeTitle);
	        }

	        if(alimtalkMessageSendDto.getBtnSize() != null) {
		        int btnSize = alimtalkMessageSendDto.getBtnSize();
		        for(int i = 0; i < btnSize; i++) {
		        	String btnType = alimtalkMessageSendDto.getBtnTypeList().get(i);
		        	String btnName = alimtalkMessageSendDto.getBtnNameList().get(i);

		        	toButtonJson = new JSONObject();
		            toButtonJson.put("type", btnType);
		            toButtonJson.put("name", btnName);

		            // 웹 링크 버튼타입
		            if("WL".equals(btnType)) {
			        	String linkMobile = alimtalkMessageSendDto.getBtnLink1List().get(i);
			        	String linkPc = alimtalkMessageSendDto.getBtnLink2List().get(i);
			        	toButtonJson.put("linkMobile", linkMobile);
		            	toButtonJson.put("linkPc", linkPc);
		            }

			        // 앱 링크 버튼 타입
		            if("AL".equals(btnType)) {
			        	String schemeAndroid = alimtalkMessageSendDto.getBtnLink1List().get(i);
			        	String schemeIos = alimtalkMessageSendDto.getBtnLink2List().get(i);
			        	toButtonJson.put("schemeAndroid", schemeAndroid);
			        	toButtonJson.put("schemeIos", schemeIos);
		            }

		            toButtonArr.put(toButtonJson);
		        }
			}

	        toMessagesJson.put("buttons", toButtonArr);
	        toMessagesArr.put(toMessagesJson);

		}

        //발송타입(즉시, 예약)
        String transmitDateType = alimtalkMessageSendDto.getAmTransmitType();

        // 예약발송 일 경우
        if(transmitDateType.equals("reservation")) {
            String reservationDate = alimtalkMessageSendDto.getAmReservationDate();
            if (reservationDate != null) {
                bodyJson.put("reserveTime", reservationDate);
            }
        }

        bodyJson.put("messages", toMessagesArr);

        String body = bodyJson.toString();

        try {
            URL url = new URL(apiUrl);

            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("content-type", "application/json");
            con.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
            con.setRequestProperty("x-ncp-iam-access-key", accessKey);
            con.setRequestProperty("x-ncp-apigw-signature-v2", makeSignature(requestUrl, timestamp, method));
            con.setRequestMethod(method);
            con.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(body.getBytes());
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;

            if(responseCode == 202) {
                // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close();
            con.disconnect();

            naverCloudPlatformResultDto.setResultCode(responseCode);
            naverCloudPlatformResultDto.setResultText(String.valueOf(response));

            log.info("알림톡 메시지전송 성공");
        } catch (Exception e) {
            log.error("알림톡 메시지전송 실패"+e.getMessage());
        }

        return naverCloudPlatformResultDto;
    }

	/**
     * 예약 메시지 취소
     *
     * @param reserveId    예약 발송 요청 조회 시 반환되는 메시지 식별자(requestId)
     * @param serviceType  취소 타입 지정. 알림톡/친구톡 - 호출 시 클래스 상단에 지정된 타입을 사용하자
     */
	public NaverCloudPlatformResultDto reserveMessage(String reserveId, String serviceType) {
        NaverCloudPlatformResultDto naverCloudPlatformResultDto = new NaverCloudPlatformResultDto();

		String hostNameUrl = "https://sens.apigw.ntruss.com";
        String requestUrl= "/" + serviceType + "/v2/services/";
        String requestUrlSub = "/reservations/" + reserveId;
        String method = "DELETE";
        String timestamp = Long.toString(System.currentTimeMillis());

        requestUrl += serviceId + requestUrlSub;

        String apiUrl = hostNameUrl + requestUrl;

        try {
            URL url = new URL(apiUrl);

            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("content-type", "application/json");
            con.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
            con.setRequestProperty("x-ncp-iam-access-key", accessKey);
            con.setRequestProperty("x-ncp-apigw-signature-v2", makeSignature(requestUrl, timestamp, method));
            con.setRequestMethod(method);
            con.setDoOutput(true);

            int responseCode = con.getResponseCode();
            BufferedReader br;

            if(responseCode == 204) {
                // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close();

            con.disconnect();
            naverCloudPlatformResultDto.setResultCode(responseCode);
            naverCloudPlatformResultDto.setResultText(String.valueOf(response));

            log.info("알림톡 메시지 예약전송 취소 성공");
        } catch (Exception e) {
            log.error("알림톡 메시지 예약전송 취소 실패"+e.getMessage());
        }

        return naverCloudPlatformResultDto;
    }


    /**
     * 친구톡 이미지 업로드
     *
     * @param plusFriendId    예약 발송 요청 조회 시 반환되는 메시지 식별자(requestId)
     * @param contentType  취소 타입 지정. 알림톡/친구톡 - 호출 시 클래스 상단에 지정된 타입을 사용하자
     */
    public NaverCloudPlatformResultDto setImageFile(String plusFriendId, File imageFile, String contentType) {
        NaverCloudPlatformResultDto naverCloudPlatformResultDto = new NaverCloudPlatformResultDto();

        String hostNameUrl = "https://sens.apigw.ntruss.com";
        String requestUrl= "/friendtalk/v2/services/";
        String requestUrlSub = "/images";
        String method = "POST";
        String timestamp = Long.toString(System.currentTimeMillis());

        requestUrl += serviceId + requestUrlSub;

        String apiUrl = hostNameUrl + requestUrl;

        try {
            String boundary = contentType.substring(contentType.lastIndexOf("=")+1);

            URL url = new URL(apiUrl);

            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("ENCTYPE", "multipart/form-data");
            con.setRequestProperty("content-type", contentType);
            con.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
            con.setRequestProperty("x-ncp-iam-access-key", accessKey);
            con.setRequestProperty("x-ncp-apigw-signature-v2", makeSignature(requestUrl, timestamp, method));
            con.setRequestMethod(method);

            byte[] buffer;
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            String lineEnd = "\r\n";
            String charset = "UTF-8";

            String postDataBuilder = "--" + boundary + lineEnd +
                    "Content-Disposition: form-data; name=\"plusFriendId\"" + lineEnd +
                    "Content-Type: text/plain; charset=" + charset + lineEnd +
                    lineEnd +
                    plusFriendId +
                    lineEnd +
                    "--" + boundary + lineEnd +
                    "Content-Disposition: form-data; name=\"" +
                    "imageFile" +
                    "\";filename=\"" +
                    imageFile.getName() +
                    "\"" + lineEnd;


            wr.write(postDataBuilder.getBytes());
            wr.writeBytes(lineEnd);
            FileInputStream fStream = new FileInputStream(imageFile);

            buffer = new byte[(int) imageFile.length()];
            int length = -1;
            while ((length = fStream.read(buffer)) != -1) {
                wr.write(buffer, 0, length);
                log.info("fStream length" + length);
            }
            wr.writeBytes(lineEnd);
            wr.writeBytes(lineEnd);
            wr.writeBytes("--" + boundary + "--" + lineEnd); // requestbody end
            fStream.close();

            log.info("wr.size() : " + wr.size());
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;

            if(responseCode == 200) {
                // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close();
            con.disconnect();

            naverCloudPlatformResultDto.setResultCode(responseCode);
            naverCloudPlatformResultDto.setResultText(String.valueOf(response));

            log.info("친구톡 이지지업로드 성공");
        } catch (Exception e) {
            log.error("친구톡 이지지업로드 실패"+e.getMessage());
        }

        return naverCloudPlatformResultDto;
    }


    /**
     * 친구톡 메시지 발송 요청
     *
     * @param friendtalkMessageSendDto, imageId, imageUrl
     */
//	@SuppressWarnings("unchecked")
	public NaverCloudPlatformResultDto postFriendMessages(FriendtalkMessageSendDto friendtalkMessageSendDto, String imageId, String imageUrl) {
        NaverCloudPlatformResultDto naverCloudPlatformResultDto = new NaverCloudPlatformResultDto();

		String hostNameUrl = "https://sens.apigw.ntruss.com";
        String requestUrl= "/friendtalk/v2/services/";
        String requestUrlType = "/messages";
        String method = "POST";
        String timestamp = Long.toString(System.currentTimeMillis());

        requestUrl += serviceId + requestUrlType;
        String apiUrl = hostNameUrl + requestUrl;

        // JSON 을 활용한 body data 생성
        JSONObject bodyJson = new JSONObject();
        JSONArray toMessagesArr = new JSONArray();
        JSONArray toButtonArr = new JSONArray();
        JSONObject image = new JSONObject();

        // 필수값
        String plusFriendId = friendtalkMessageSendDto.getKcChannelId();
        String content = friendtalkMessageSendDto.getMessageContent();

        bodyJson.put("plusFriendId", plusFriendId);
        List<FriendtalkMessageSendSubDto> friendtalkMessageSendSubDtoList = friendtalkMessageSendDto.getFriendtalkMessageSendSubDtoList();

        JSONObject toMessagesJson;
        JSONObject toButtonJson;

		for(FriendtalkMessageSendSubDto friendtalkMessageSendSubDto : friendtalkMessageSendSubDtoList) {

			toMessagesJson = new JSONObject();
			toMessagesJson.put("countryCode", "");
	        toMessagesJson.put("to", friendtalkMessageSendSubDto.getPhoneNumber());
	        toMessagesJson.put("content", content);

            if(friendtalkMessageSendDto.getBtnSize() != null) {
                int btnSize = friendtalkMessageSendDto.getBtnSize();
                for(int i = 0; i < btnSize; i++) {
                    String btnType = friendtalkMessageSendDto.getBtnTypeList().get(i);
                    String btnName = friendtalkMessageSendDto.getBtnNameList().get(i);

                    toButtonJson = new JSONObject();
                    toButtonJson.put("type", btnType);
                    toButtonJson.put("name", btnName);

                    // 웹 링크 버튼타입
                    if("WL".equals(btnType)) {
                        String linkMobile = friendtalkMessageSendDto.getBtnLink1List().get(i);
                        String linkPc = friendtalkMessageSendDto.getBtnLink2List().get(i);
                        toButtonJson.put("linkMobile", linkMobile);
                        toButtonJson.put("linkPc", linkPc);
                    }

                    // 앱 링크 버튼 타입
                    if("AL".equals(btnType)) {
                        String schemeAndroid = friendtalkMessageSendDto.getBtnLink1List().get(i);
                        String schemeIos = friendtalkMessageSendDto.getBtnLink2List().get(i);
                        toButtonJson.put("schemeAndroid", schemeAndroid);
                        toButtonJson.put("schemeIos", schemeIos);
                    }
                    toButtonArr.put(toButtonJson);
                }
            }

	        toMessagesJson.put("buttons", toButtonArr);

	        if(!imageId.equals("")) {
	        	image.put("imageId", imageId);
	        	image.put("imageLink", imageUrl);
	        	toMessagesJson.put("image", image);
	        }

	        toMessagesArr.put(toMessagesJson);

		}

        //발송타입(즉시, 예약)
        String transmitDateType = friendtalkMessageSendDto.getTransmitType();

        // 예약발송 일 경우
        if(transmitDateType.equals("reservation")) {
            String reservationDate = friendtalkMessageSendDto.getReservationDate();
            if (reservationDate != null) {
                bodyJson.put("reserveTime", reservationDate);
            }
        }

        bodyJson.put("messages", toMessagesArr);

        String body = bodyJson.toString();

        try {
            URL url = new URL(apiUrl);

            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("content-type", "application/json");
            con.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
            con.setRequestProperty("x-ncp-iam-access-key", accessKey);
            con.setRequestProperty("x-ncp-apigw-signature-v2", makeSignature(requestUrl, timestamp, method));
            con.setRequestMethod(method);
            con.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(body.getBytes());
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;

            if(responseCode == 202) {
                // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close();
            con.disconnect();

            naverCloudPlatformResultDto.setResultCode(responseCode);
            naverCloudPlatformResultDto.setResultText(String.valueOf(response));

            log.info("친구톡 발송 성공");
        } catch (Exception e) {
            log.error("친구톡 발송 실패"+e.getMessage());
        }

        return naverCloudPlatformResultDto;
    }

	/**
	 * 메서드 허용
	 */
	private void allowMethods(String... methods) {
		try {
			Field methodsField = HttpURLConnection.class.getDeclaredField("methods");

			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);

			methodsField.setAccessible(true);

			String[] oldMethods = (String[]) methodsField.get(null);
			Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
			methodsSet.addAll(Arrays.asList(methods));
			String[] newMethods = methodsSet.toArray(new String[0]);

			methodsField.set(null/*static field*/, newMethods);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}



    // 이메일용 메서드
    public boolean sendMail(NCloudPlatformMailRequest request) {

        //
        boolean isSuccess = false;

        final String URL = "https://mail.apigw.ntruss.com/api/v1/mails";

        try {
            long currentTime = System.currentTimeMillis();
            String currentTimeStr = Long.toString(currentTime);
            HttpURLConnection conn = (HttpURLConnection) new URL(URL).openConnection();

            String signature = makeSignature(currentTimeStr, "/api/v1/mails");

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setRequestProperty("x-ncp-apigw-timestamp", currentTimeStr );
//	        Authorization
            conn.setRequestProperty("x-ncp-iam-access-key", accessKey);
            conn.setRequestProperty("x-ncp-apigw-signature-v2", signature);
            conn.setDoOutput(true);

            // 요청 데이터 넣기
            String reqStr = Converter.ObjectToJsonString(request);
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            log.info("reqStr : "+reqStr);
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

            OutputStream os = conn.getOutputStream();
            byte[] requestByte = Converter.ObjectToJsonString(request).getBytes(StandardCharsets.UTF_8);

            os.write(requestByte);
            os.close();

            // 응답 데이터 얻기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            String code = String.valueOf(conn.getResponseCode());
            String data = sb.toString();

            log.info("code: {}, data: {}", code, data);

            br.close();
            conn.disconnect();

            isSuccess = true;
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("예외 발생 : "+e.getMessage());
        }

        return isSuccess;
    }

    public String makeSignature(String timestamp, String url) {
        String space = " ";  // 공백
        String newLine = "\n";  // 줄바꿈
        String method = "POST";  // HTTP 메소드
//	    String url = "/api/v1/mails";  // 도메인을 제외한 "/" 아래 전체 url (쿼리스트링 포함)

        String encodeBase64String = "";

        String message = method +
                space +
                url +
                newLine +
                timestamp +
                newLine +
                accessKey;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            Mac mac;
            mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            log.error("예외 발생 : "+e.getMessage());
        }

        return encodeBase64String;
    }

}
