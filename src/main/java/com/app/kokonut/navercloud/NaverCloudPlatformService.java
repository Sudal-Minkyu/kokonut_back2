package com.app.kokonut.navercloud;

import com.app.kokonut.common.realcomponent.Converter;
import com.app.kokonut.keydata.KeyDataService;
import com.app.kokonut.navercloud.dto.NCloudPlatformMailRequest;
import com.app.kokonut.navercloud.dto.NaverCloudPlatformResultDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
            log.error("예외처리 : "+e);
			log.error("예외처리 메세지 : "+e.getMessage());
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
            log.error("예외처리 : "+e);
			log.error("예외처리 메세지 : "+e.getMessage());
            log.error("예외 발생 : "+e.getMessage());
        }

        return encodeBase64String;
    }

}
