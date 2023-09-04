package com.app.kokonut.navercloud;

import com.app.kokonut.common.Converter;
import com.app.kokonut.common.Utils;
import com.app.kokonut.email.email.dtos.EmailCheckDto;
import com.app.kokonut.navercloud.dto.NCloudPlatformMailRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

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

    // 이메일발송 메서드(API : createMailRequest)
    public String sendMail(NCloudPlatformMailRequest request) {
        log.info("sendMail 호출");

        String requestId = null;

        final String URL = "https://mail.apigw.ntruss.com/api/v1/mails";

        try {
            long currentTime = System.currentTimeMillis();
            String currentTimeStr = Long.toString(currentTime);
            HttpURLConnection conn = (HttpURLConnection) new URL(URL).openConnection();

            String signature = makeSignature(currentTimeStr, "/api/v1/mails", "POST");

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setRequestProperty("x-ncp-apigw-timestamp", currentTimeStr );
            conn.setRequestProperty("x-ncp-iam-access-key", accessKey);
            conn.setRequestProperty("x-ncp-apigw-signature-v2", signature);
            conn.setDoOutput(true);

            // 보내는 데이터
//            String reqStr = Converter.ObjectToJsonString(request);
//            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//            log.info("reqStr : "+reqStr);
//            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

            // 요청 데이터 넣기
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

            br.close();
            conn.disconnect();

            log.info("code: {}, data: {}", code, data);

            if(code.equals("201")) {
                JSONObject jsonObj = new JSONObject(data);
                requestId = jsonObj.getString("requestId");
                log.info("requestId : " + requestId);
            }

        }
        catch (Exception e) {
            log.error("예외처리 : "+e);
			log.error("예외처리 메세지 : "+e.getMessage());
        }

        return requestId;
    }

    // 이메일발송 첨부파일호출 메서드(API : createFile)
    public String fileMail(MultipartFile multipartFile) {
        log.info("fileMail 호출");

        String result = null;

        CloseableHttpClient httpClient = HttpClients.createDefault();

        // HttpPost 객체 생성
        HttpPost httpPost = new HttpPost("https://mail.apigw.ntruss.com/api/v1/files");

        try {
            // MultipartFile을 java.io.File로 변환
            File file = Utils.convertMultipartFileToFile(multipartFile);

            long currentTime = System.currentTimeMillis();
            String currentTimeStr = Long.toString(currentTime);
            String signature = makeSignature(currentTimeStr, "/api/v1/files", "POST");
//            log.info("signature : "+signature);

            // 파일을 이용해 FileBody 객체 생성
            FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);

            // MultipartEntityBuilder를 이용해 HttpEntity 객체 생성
            HttpEntity entity = MultipartEntityBuilder.create() // 자동으로 "Content-Type","multipart/form-data"이 주입됨
                    .addPart("fileList", fileBody)
                    .build();

            // HttpPost 객체에 HttpEntity 설정
            httpPost.setEntity(entity);

            // 필요한 헤더 추가
//            httpPost.setHeader("accept", "application/json");
//            httpPost.setHeader("Content-Type", "multipart/form-data");
            httpPost.setHeader("x-ncp-iam-access-key", accessKey);
            httpPost.setHeader("x-ncp-apigw-timestamp", currentTimeStr);
            httpPost.setHeader("x-ncp-apigw-signature-v2", signature);
//            log.info("httpPost : " + httpPost);

            // POST 요청 실행
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {

                // 응답 처리
                String responseString = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = new JSONObject(responseString);
                JSONArray filesArray = jsonObject.getJSONArray("files");
                result = filesArray.getJSONObject(0).getString("fileId");
//                log.info("File ID : " + result);
            }

        }

        catch (Exception e) {
            log.error("예외처리 : "+e);
            log.error("예외처리 메세지 : "+e.getMessage());
        }

        return result;
    }

    // 발송된 이메일 상태 체크호출(API : getMailRequestStatus)
    public EmailCheckDto sendEmailCheck(String requestId) throws Exception{

        EmailCheckDto emailCheckDto = null;

        String url = "https://mail.apigw.ntruss.com/api/v1/mails/requests/"+requestId+"/status";
        log.info("url : "+url);

        try {
            URL apiurl = new URL(url);

            long currentTime = System.currentTimeMillis();
            String currentTimeStr = Long.toString(currentTime);

            String signature = makeSignature(currentTimeStr, "/api/v1/mails/requests/"+requestId+"/status", "GET");

            HttpURLConnection conn = (HttpURLConnection) apiurl.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("content-type", "application/json");
            conn.setRequestProperty("x-ncp-apigw-timestamp", currentTimeStr);
            conn.setRequestProperty("x-ncp-iam-access-key", accessKey);
            conn.setRequestProperty("x-ncp-apigw-signature-v2", signature);
            conn.setUseCaches(false);
            conn.setDoOutput(false);
            conn.setDoInput(true);

            // 응답 데이터 얻기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            String code = String.valueOf(conn.getResponseCode());
            String data = sb.toString();

            br.close();
            conn.disconnect();

            log.info("code: {}, data: {}", code, data);

            if(code.equals("200")) {
                emailCheckDto = new EmailCheckDto();

                JSONObject jsonObj = new JSONObject(data);

                int requestCount = jsonObj.getInt("requestCount");
                int sentCount = jsonObj.getInt("sentCount");
//                log.info("requestCount : " + requestCount);
//                log.info("sentCount : " + sentCount);

                emailCheckDto.setRequestCount(requestCount);
                emailCheckDto.setSentCount(sentCount);
                emailCheckDto.setFailCount(requestCount-sentCount);
            }


        }
        catch (Exception e) {
            log.error("예외처리 : "+e);
            log.error("예외처리 메세지 : "+e.getMessage());
        }

        return emailCheckDto;
    }

    // 시그네처 생성 함수
    public String makeSignature(String timestamp, String url, String method) {
        String space = " ";  // 공백
        String newLine = "\n";  // 줄바꿈

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
        }

        return encodeBase64String;
    }

    // 코코넛 API호출 API 테스트용
    public boolean kokonutApiHocul() {
        boolean result = false;

        String url = "http://localhost:8050/v3/api/Auth/hoculPostTest";
        log.info("url : "+url);

        try {
            URL apiurl = new URL(url);

            HttpURLConnection conn = (HttpURLConnection) apiurl.openConnection();
//            conn.setRequestMethod("GET");
            conn.setRequestMethod("POST");
            conn.setRequestProperty("content-type", "application/json");
            conn.setRequestProperty("x-api-key", "23f553a39c61554401216602c089a8f9");
            conn.setUseCaches(false);
            conn.setDoOutput(false);
            conn.setDoInput(true);

            // 응답 데이터 얻기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            String code = String.valueOf(conn.getResponseCode());
            String data = sb.toString();

            br.close();
            conn.disconnect();

            log.info("code: {}, data: {}", code, data);

            if(code.equals("200")) {
                result = true;
            }

        }
        catch (Exception e) {
            log.error("예외처리 : "+e);
            log.error("예외처리 메세지 : "+e.getMessage());
        }

        return result;
    }

}
