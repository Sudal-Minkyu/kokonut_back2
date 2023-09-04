package com.app.kokonut.auth.niceId;

import com.app.kokonut.admin.Admin;
import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.auth.jwt.dto.RedisDao;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.Utils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.CookieGenerator;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

;

@Slf4j
@Service
@SuppressWarnings("crypto")
public class NiceIdService {

	@Value("${kokonut.front.server.domain}")
	public String frontServerDomainIp;

	@Value("${kokonut.nice.id}")
	public String clientId;

	@Value("${kokonut.nice.secret}")
	public String clientSecret;

	@Value("${kokonut.nice.product}")
	public String productId;

	@Value("${kokonut.nice.access}")
	public String accessToken;

	private final AdminRepository adminRepository;
	private final RedisDao redisDao;

	@Autowired
	public NiceIdService(AdminRepository adminRepository, RedisDao redisDao) {
		this.adminRepository = adminRepository;
		this.redisDao = redisDao;
	}

	// 본인인증 창 열기
	public ResponseEntity<Map<String, Object>> open(String state, HttpServletResponse response) {
		log.info("본인인증 open 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		getCryptoToken();
		CryptoToken cryptoToken = CryptoToken.getInstance();
//		log.info("cryptoToken : "+cryptoToken);

		String value = cryptoToken.getReqDtim().trim() + cryptoToken.getReqNo().trim() + cryptoToken.getTokenValue().trim();
		MessageDigest md = null;

		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			log.debug("e : "+e.getMessage());
		}
		assert md != null;
		md.update(value.getBytes());
		byte[] arrHashValue = md.digest();
		String resultVal = new String(Base64.getEncoder().encode(arrHashValue)).trim();

		String reqData = "{\"returnurl\":\""+frontServerDomainIp+
				"/#/niceId/redirect"+
				"?state="+state+"\", " +
				"\"sitecode\":\""+cryptoToken.getSiteCode()+"\", \"popupyn\" : \"Y\", \"receivedata\" : \"xxxxdddeee\", \"authtype\":\"M\", \"mobilceco\":\"S\"}";

		String key = resultVal.substring(0, 16);
		String iv = resultVal.substring(resultVal.length() - 16);
		String hmac_key = resultVal.substring(0, 32);
		try {
			SecretKey secureKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes()));
			byte[] encrypted =  c.doFinal(reqData.trim().getBytes());
			String reqDataEnc = new String(Base64.getEncoder().encode(encrypted));
			byte[] hmacSha256 = hmac256(hmac_key.getBytes(), reqDataEnc.getBytes());
			String integrity_value = new String(Base64.getEncoder().encode(hmacSha256));

			data.put("token_version_id", cryptoToken.getTokenVersionId());
			data.put("integrity_value", integrity_value);
			data.put("enc_data", reqDataEnc);

		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
				 InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			log.error("e : "+e.getMessage());
		}

		CookieGenerator cookieGener = new CookieGenerator();
		cookieGener.setCookieName("key");
		cookieGener.addCookie(response, key);
		cookieGener.setCookieName("iv");
		cookieGener.addCookie(response, iv);

		return ResponseEntity.ok(res.success(data));
	}

	public ResponseEntity<Map<String, Object>> redirect(String state, String enc_data, HttpServletRequest request, HttpServletResponse response) {
		log.info("본인인증 redirect 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		try {

			String key = "";
			String iv = "";
			Cookie[] cookies = request.getCookies();
			if(cookies != null) {
				for(Cookie c : cookies) {
					if(c.getName().equals("key") ) {
						key = c.getValue();
					} else if(c.getName().equals("iv") ) {
						iv = c.getValue();
					}
				}
			}
//			log.info("enc_data : " + enc_data);

			SecretKey secureKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes()));
			byte[] cipherEnc = Base64.getDecoder().decode(enc_data);

			String resData = new String(c.doFinal(cipherEnc), "euc-kr");
			JSONObject jsonObject = new JSONObject(resData);

			String knName = jsonObject.getString("name");
			String knPhoneNumber = jsonObject.getString("mobileno");

			log.info("휴대폰 본인인증 state : "+state);
			if(state.equals("1") || state.equals("5") || state.equals("6")) {

				if(state.equals("1")) {
					Utils.cookieSave("joinName", URLEncoder.encode(knName, StandardCharsets.UTF_8), 180, response); // 쿠키 제한시간 3분
					Utils.cookieSave("joinPhone", URLEncoder.encode(knPhoneNumber, StandardCharsets.UTF_8), 180, response); // 쿠키 제한시간 3분

					if(adminRepository.existsByKnPhoneNumber(knPhoneNumber)) {
						log.error("이미 회원가입된 핸드폰번호 입니다.");
						return ResponseEntity.ok(res.fail(ResponseErrorCode.KO034.getCode(),ResponseErrorCode.KO034.getDesc()));
					} else {
						log.info("회원가입 본인인증");
						// 회원가입떄 사용 -> 핸드폰번호 일치한지 확인하기위해서 사용됨

						data.put("joinName", knName);
						data.put("joinPhone", knPhoneNumber);
					}
				} else {
					if(state.equals("5")) {
						log.info("휴대전화번호 변경 본인인증");
					}
					else {
						log.info("해외로그인 본인인증");
					}
					data.put("joinName", knName);
					data.put("joinPhone", knPhoneNumber);
				}

			}
			else if(state.equals("2") || state.equals("3") || state.equals("4") || state.equals("7")) {
				// 이름과 번호를 통해 찾기?
				Optional<Admin> optionalAdmin = adminRepository.findAdminByKnNameAndKnPhoneNumber(knName, knPhoneNumber);

				if(optionalAdmin.isEmpty()) {
					log.error("회원가입 정보가 존재하지 않습니다.");
					return ResponseEntity.ok(res.fail(ResponseErrorCode.KO032.getCode(), ResponseErrorCode.KO032.getDesc()));
				} else {
					String knEmail = optionalAdmin.get().getKnEmail();

					if(state.equals("2")) {
						log.info("이메일찾기 본인인증");
						String keyEmail = Utils.getRamdomStr(10);

						// 인증번호 레디스에 담기
						redisDao.setValues("KE: " + keyEmail, knEmail, Duration.ofMillis(5000)); // 제한시간 5초
						log.info("레디스에 인증번호 저장성공");
						data.put("keyEmail", keyEmail);
					} else {
						if(state.equals("3")) {
							log.info("비밀번호찾기 본인인증");
							data.put("keyEmail", knEmail);
						} else if(state.equals("4")) {
							log.info("OTP변경 본인인증");
						} else { // state.equals("7")
							log.info("비밀번호변경 본인인증");
						}

						Utils.cookieSave("joinName", URLEncoder.encode(knName, StandardCharsets.UTF_8), 180, response); // 쿠키 제한시간 3분
						Utils.cookieSave("joinPhone", URLEncoder.encode(knPhoneNumber, StandardCharsets.UTF_8), 180, response); // 쿠키 제한시간 3분
					}
				}
			}
			else {
				log.info("그 외 본인인증");
			}

		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
				 InvalidAlgorithmParameterException | UnsupportedEncodingException | BadPaddingException |
				 IllegalBlockSizeException e) {
			log.error("e : "+e.getMessage());
		}

		CookieGenerator cookieGener = new CookieGenerator();
		cookieGener.setCookieName("key");
		cookieGener.addCookie(response, "");
		cookieGener.setCookieName("iv");
		cookieGener.addCookie(response, "");

		return ResponseEntity.ok(res.success(data));
	}

	// 핸드폰 인증 암호화 토큰 발급 요청
	public void getCryptoToken() {

		boolean isValid = true;
		try {

			if (CryptoToken.getInstance().getTokenVersionId().isEmpty()) {
				isValid = false;
			}
			else {
				Date today = new Date();

				/* 토큰 유효기간이 지났는지 확인 */
				boolean validToken = CryptoToken.getInstance().getIssDate().after(new Date(today.getTime() -( CryptoToken.getInstance().getPeriod() * 1000))); // 초를 밀리세컨드로 표시

				if(!validToken) {
					isValid = false;
				}
			}

			if(!isValid) {
				Date date = new Date();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

				long currentTimestamp = date.getTime() / 1000;

				URL url = new URL("https://svc.niceapi.co.kr:22001/digital/niceid/api/v1.0/common/crypto/token");

				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				String endcodeStr = accessToken+":"+currentTimestamp+":"+clientId;
				String authorizationEncoded = new String(Base64.getEncoder().encode(endcodeStr.getBytes()));
				String authorization = "bearer " + authorizationEncoded;
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json; utf-8");
				conn.setRequestProperty("Authorization", authorization);
				conn.setRequestProperty("client_id", clientId);
				conn.setRequestProperty("productID", productId);
				conn.setDoOutput(true);

				// 요청 데이터 넣기
				String reqDtim = simpleDateFormat.format(date);
				String reqNo = "REQ"+Calendar.getInstance().getTimeInMillis();

				JSONObject headerObject = new JSONObject();
				headerObject.put("CNTY_CD", "ko");

				JSONObject bodyObject = new JSONObject();
				bodyObject.put("req_dtim", reqDtim);
				bodyObject.put("req_no", reqNo);
				bodyObject.put("enc_mode", "1");

				JSONObject requestData = new JSONObject();
				requestData.put("dataBody", bodyObject);
				requestData.put("dataHeader", headerObject);

				OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				wr.write(requestData.toString());
				wr.flush();
				wr.close();

				// 응답 데이터 얻기
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
				StringBuilder sb = new StringBuilder();

				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}

				br.close();
				String code = String.valueOf(conn.getResponseCode());
				String data = sb.toString();

				if(code.equals("200")) {
//					log.info("data : "+data);
					JSONObject jsonObject = new JSONObject(data);
					JSONObject dataBody = jsonObject.getJSONObject("dataBody");

					String tokenVersionId = dataBody.getString("token_version_id");
					String tokenVal = dataBody.getString("token_val");
					String siteCode = dataBody.getString("site_code");
					long period = (long)dataBody.getDouble("period");

					CryptoToken cryptoToken = CryptoToken.getInstance();
					cryptoToken.setTokenVersionId(tokenVersionId);
					cryptoToken.setTokenValue(tokenVal);
					cryptoToken.setPeriod(period);

					Date issDate = new Date();
					cryptoToken.setIssDate(issDate);
					cryptoToken.setReqDtim(reqDtim);
					cryptoToken.setReqNo(reqNo);
					cryptoToken.setSiteCode(siteCode);
				}

				conn.disconnect();
			}
		} catch (Exception e) {
			log.error("예외처리 : "+e);
			log.error("예외처리 메세지 : "+e.getMessage());
		}

	}

	// hmac256 반환
	public byte[] hmac256(byte[] secretKey, byte[] message){
		byte[] hmac256 = null;

		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec sks = new SecretKeySpec(secretKey, "HmacSHA256");
			mac.init(sks);
			hmac256 = mac.doFinal(message);
		} catch(Exception e){
			log.info("e : "+e.getMessage());
		}

		return hmac256;
	}


}