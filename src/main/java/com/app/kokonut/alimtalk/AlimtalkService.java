package com.app.kokonut.alimtalk;

import com.app.kokonut.alimtalk.dtos.AlimtalkTemplateInfoDto;
import com.app.kokonut.common.realcomponent.Utils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class AlimtalkService {

    @Value("${kokonut.happytalk.userId}")
    public String userId;

    @Value("${kokonut.happytalk.alimHost}")
    public String alimHost;

    @Value("${kokonut.happytalk.bizHost}")
    public String bizHost;

    @Value("${kokonut.happytalk.testAccessKey}")
    public String testAccessKey;

    // 테스트 탬플릿 검수승인 (참고 : https://alimtalk-api.bizmsg.kr/startTest.html)
    public String alimtalkTemplateInspection(String templateCode) {
        log.info("alimtalkTemplateInspection 호출");

        try {
            // URL 설정
            URL url = new URL(bizHost+"/v2/partner/test/template/approve");

            // HttpURLConnection 생성 및 설정
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("userId", "tracker_partner");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            // POST 파라미터 설정
            String urlParameters = "senderKey=" + URLEncoder.encode(testAccessKey, StandardCharsets.UTF_8) +
                    "&templateCode=" + URLEncoder.encode(templateCode, StandardCharsets.UTF_8) +
                    "&senderKeyType=" + URLEncoder.encode("S", StandardCharsets.UTF_8) +
                    "&comment=" + URLEncoder.encode("코코넛 템플릿승인", StandardCharsets.UTF_8);

            // 요청 데이터 쓰기
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            // 응답 코드 및 메시지 얻기
            int responseCode = conn.getResponseCode();
            String data = conn.getResponseMessage();

            log.info("responseCode: {}, data: {}", responseCode, data);

            if (responseCode == 200) {
                log.info("템플릿승인 성공");
                return "Success";
            }
        } catch (Exception e) {
            log.error("예외처리 : "+e);
            log.error("예외처리 메세지 : "+e.getMessage());
        }

        return "Failure";
    }

    // 테스트 일회성 인증코드 받기 (참고 : https://alimtalk-api.bizmsg.kr/startTest.html)
    public String alimtalkTemplateAuth(String phoneNumber) {
        log.info("alimtalkTemplateAuth 호출");

        try {
            // URL 설정
            URL url = new URL(bizHost+"/v2/partner/test/user/token?");

            // HttpURLConnection 생성 및 설정
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // POST 파라미터 설정
            String urlParameters = "phoneNumber=" + URLEncoder.encode(phoneNumber, StandardCharsets.UTF_8);

            // 요청 데이터 쓰기
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            // 응답 코드 및 메시지 얻기
            int responseCode = conn.getResponseCode();
            String data = conn.getResponseMessage();

            log.info("responseCode: {}, data: {}", responseCode, data);

            if (responseCode == 200) {
                log.info("일회성토큰 성공");
                return "Success";
            }
        } catch (Exception e) {
            log.error("예외처리 : "+e);
            log.error("예외처리 메세지 : "+e.getMessage());
        }

        return "Failure";
    }

    // 테스트 일회성 인증승인 받기 (참고 : https://alimtalk-api.bizmsg.kr/startTest.html)
    public String alimtalkTemplateAuthCheck(String phoneNumber, String checkNumber) {
        log.info("alimtalkTemplateAuthCheck 호출");

        try {
            // URL 설정
            URL url = new URL(bizHost+"/v2/partner/test/user/certify?");

            // HttpURLConnection 생성 및 설정
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // POST 파라미터 설정
            String urlParameters = "phoneNumber=" + URLEncoder.encode(phoneNumber, StandardCharsets.UTF_8) +
                    "&token=" + URLEncoder.encode(checkNumber, StandardCharsets.UTF_8);

            // 요청 데이터 쓰기
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            // 응답 코드 및 메시지 얻기
            int responseCode = conn.getResponseCode();
            String data = conn.getResponseMessage();

            log.info("responseCode: {}, data: {}", responseCode, data);

            if (responseCode == 200) {
                log.info("일회성토큰 성공");
                return "Success";
            }
        } catch (Exception e) {
            log.error("예외처리 : "+e);
            log.error("예외처리 메세지 : "+e.getMessage());
        }

        return "Failure";
    }

    // 템플릿 정보 가져오기
    public AlimtalkTemplateInfoDto alimtalkTemplateInfo(String profileKey, String templateCode) {
        log.info("alimtalkTemplateInfo 호출");

        AlimtalkTemplateInfoDto alimtalkTemplateInfoDto = new AlimtalkTemplateInfoDto();

        try {
            // URL 설정
            URL url = new URL(bizHost+"/v2/template?");

            // HttpURLConnection 생성 및 설정
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("userId", userId);
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // POST 파라미터 설정
            String urlParameters = "senderKey=" + URLEncoder.encode(profileKey, StandardCharsets.UTF_8) +
                    "&templateCode=" + URLEncoder.encode(templateCode, StandardCharsets.UTF_8);

            // 요청 데이터 쓰기
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            // 응답 데이터 얻기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            int responseCode = conn.getResponseCode();
            JSONObject data = new JSONObject(sb.toString());

            br.close();
            conn.disconnect();

//            log.info("responseCode: {}, data: {}", responseCode, data);

            if (responseCode == 200) {
                String resultCode = data.getString("code");

                alimtalkTemplateInfoDto.setResult(resultCode);

                if(resultCode.equals("success")) {
                    JSONObject resultData = data.getJSONObject("data");

                    log.info("템플릿 정보 가져오기 성공");
//                    log.info("resultData : "+resultData);

                    String templateContent = resultData.getString("templateContent");
//                    log.info("템플릿 내용 templateContent : "+templateContent);

                    List<String> variableList = new ArrayList<>();

                    // 정규 표현식 패턴 생성
                    Pattern pattern = Pattern.compile("#\\{([^}]*)}");

                    // 매치 객체 생성
                    Matcher matcher = pattern.matcher(templateContent);

                    // 매치하는 부분 찾기
                    while (matcher.find()) {
//                        log.info("괄호내용 : "+matcher.group(1));
                        variableList.add(matcher.group(1));
                    }

                    alimtalkTemplateInfoDto.setVariableList(variableList);
                    alimtalkTemplateInfoDto.setResultMessage(templateContent);

                }else {
                    log.info("템플릿 정보 가져오기 실패");
                    alimtalkTemplateInfoDto.setResultMessage(data.getString("message"));
                }

            }else {
                log.info("템플릿 정보 가져오기 실패");
                alimtalkTemplateInfoDto.setResult("fail");
                alimtalkTemplateInfoDto.setResultMessage("템플릿 정보를 가져올 수 없습니다.");
            }
        } catch (Exception e) {
            log.error("예외처리 : "+e);
            log.error("예외처리 메세지 : "+e.getMessage());
        }

        return alimtalkTemplateInfoDto;
    }


    // 알림톡 전송
    public String alimtalkSend(String profileKey, String templateCode, String message) {
        log.info("alimtalkSend 호출");

        String url = alimHost + "/v2/" + profileKey + "/sendMessage";
        log.info("url : " + url);

        try {
            URL apiurl = new URL(url);

            HttpURLConnection conn = (HttpURLConnection) apiurl.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setRequestProperty("userId", userId);
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // JSON 을 활용한 body data 생성
            JSONObject bodyJson = new JSONObject();
            bodyJson.put("profile_key", profileKey);
            bodyJson.put("msgid", "알림톡전송_" + Utils.getRamdomStr(5));
            bodyJson.put("message_type", "AT");
            bodyJson.put("template_code", templateCode);
            bodyJson.put("message", message);
            bodyJson.put("receiver_num", "01064396533");
            bodyJson.put("reserved_time", "00000000000000");

            // JSON 객체를 배열에 추가
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(bodyJson);

            String body = jsonArray.toString();

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(body.getBytes());
            wr.flush();
            wr.close();

            // 응답 데이터 얻기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            int responseCode = conn.getResponseCode();
            String data = sb.toString();

            br.close();
            conn.disconnect();

            log.info("responseCode: {}, data: {}", responseCode, data);

            if (responseCode == 200) {
                JSONArray data_jsonArray = new JSONArray(data);

                // "result" 값 추출
                String result = data_jsonArray.getJSONObject(0).getString("result");
                log.info("result : "+result);

                if(!result.equals("N")) {
                    log.info("알림톡전송 성공");
                    return "Success";
                }else {
                    log.info("알림톡전송 실패");
                }

            }else {
                log.info("알림톡전송 실패");
            }
        } catch (Exception e) {
            log.error("예외처리 : "+e);
            log.error("예외처리 메세지 : "+e.getMessage());
        }

        return "Failure";
    }

}
