package com.app.kokonut.auth.niceId;

import com.app.kokonut.admin.Admin;
import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.auth.jwt.dto.RedisDao;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.realcomponent.CommonUtil;
import com.app.kokonut.common.realcomponent.Utils;
import com.app.kokonut.keydata.KeyDataService;
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
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
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
	public NiceIdService(KeyDataService keyDataService, AdminRepository adminRepository, RedisDao redisDao) {
//		KeyDataNICEDto keyDataNICEDto = keyDataService.nice_key();
//		this.frontServerDomainIp = keyDataNICEDto.getFRONTSERVERDOMAINIP();
//		this.clientId = keyDataNICEDto.getNICEID();
//		this.clientSecret = keyDataNICEDto.getNICESECRET();
//		this.productId = keyDataNICEDto.getNICEPRODUCT();
//		this.accessToken = keyDataNICEDto.getNICEACCESS();

		this.adminRepository = adminRepository;
		this.redisDao = redisDao;
	}

	/*** 
	 * AccessToken 발급 요청(최초 1회 요청)
	 */
	@SuppressWarnings("unchecked")
	public ResponseEntity<Map<String,Object>> getToken() throws IOException {
		log.info("getToken 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		String token = "";

		HttpsURLConnection conn = (HttpsURLConnection) new URL("https://svc.niceapi.co.kr:22001/digital/niceid/oauth/oauth/token").openConnection();
		String endcodeStr = clientId+":"+clientSecret;
		String authorizationEncoded = new String(Base64.getEncoder().encode(endcodeStr.getBytes()));
		String authorization = "Basic " + authorizationEncoded;
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		conn.setRequestProperty("Authorization", authorization);
		conn.setDoOutput(true);

		// 요청 데이터 넣기
		String postDatas = "grant_type=client_credentials&scope=default";
		PrintWriter pw = new PrintWriter(conn.getOutputStream());
		pw.write(postDatas);
		pw.flush();
		pw.close();

		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
		StringBuilder sb = new StringBuilder();

		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}

		br.close();
		String code = String.valueOf(conn.getResponseCode());
		String getdata = sb.toString();

		HashMap<String, Object> responseJson;
		log.info("code: {}, getdata: {}", code, getdata);

		if(code.equals("200")) {
			responseJson = CommonUtil.jsonStringToHashMap(getdata);
			HashMap<String, Object> bodyMap = (HashMap<String, Object>) responseJson.get("dataBody");
			token = bodyMap.get("access_token").toString();
		}

		conn.disconnect();

		data.put("accessToken", token);

		return ResponseEntity.ok(res.success(data));
	}
	
//	/***
//	 * AccessToken 페기
//	 */
//	public String removeToken() throws IOException {
//
//		String token = "";
//
//		HttpsURLConnection conn = (HttpsURLConnection) new URL("https://svc.niceapi.co.kr:22001/digital/niceid/oauth/oauth/token/revokeById").openConnection();
//		String endcodeStr = clientId+":"+clientSecret;
//		String authorizationEncoded = new String(Base64.getEncoder().encode(endcodeStr.getBytes()));
//		String authorization = "Basic " + authorizationEncoded;
//		conn.setRequestMethod("POST");
//		conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//		conn.setRequestProperty("Authorization", authorization);
//		conn.setDoOutput(true);
//
//		// 요청 데이터 넣기
//		String postDatas = "";
//		PrintWriter pw = new PrintWriter(conn.getOutputStream());
//		pw.write(postDatas);
//		pw.flush();
//		pw.close();
//
//		// 응답 데이터 얻기
//		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
//		StringBuilder sb = new StringBuilder();
//
//		String line;
//		while ((line = br.readLine()) != null) {
//			sb.append(line);
//		}
//
//		br.close();
//		String code = String.valueOf(conn.getResponseCode());
//		String data = sb.toString();
//
//		log.info("code: {}, data: {}", code, data);
//
//		if(code.equals("200")) {
////			responseMap = CommonUtil.jsonStringToHashMap(data);
////			HashMap<String, Object> bodyMap = (HashMap<String, Object>) responseMap.get("dataBody");
////			token = bodyMap.get("access_token").toString();
////			logger.info("[getToken] Token Value - " + token);
//		}
//
//		conn.disconnect();
//
//		return token;
//	}

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

//		String reqData = "{\"returnurl\":\""+CommonUtil.getDomain(request)+"/v1/api/NiceId/redirect"+"\", \"sitecode\":\""+cryptoToken.getSiteCode()+"\", \"popupyn\" : \"Y\", \"receivedata\" : \"xxxxdddeee\", \"authtype\":\"M\"}";
		String reqData = "{\"returnurl\":\""+frontServerDomainIp+"/#/niceId/redirect"+"?state="+state+"\", \"sitecode\":\""+cryptoToken.getSiteCode()+"\", \"popupyn\" : \"Y\", \"receivedata\" : \"xxxxdddeee\", \"authtype\":\"M\"}";

		String key = resultVal.substring(0, 16);
		String iv = resultVal.substring(resultVal.length() - 16, resultVal.length());
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
			//TODO: 서버 안정화 후 주석 제거 예정
//			int getStatus = response.getStatus();
//			log.info("[NICEID] getStatus : " + getStatus);
//			Enumeration<String> paramsHeader = request.getHeaderNames();
//			while(paramsHeader.hasMoreElements()) {
//				String name = paramsHeader.nextElement();
//				log.info("[NICEID] paramsHeader name : " + name + ", value : " + request.getHeader(name));
//			}
//
//			String enc_data_req = request.getParameter("enc_data") == null ? "":request.getParameter("enc_data").toString();
//			Enumeration<String> params = request.getParameterNames();
//			while(params.hasMoreElements()) {
//				String name = params.nextElement();
//			  	log.info(name + " : " + request.getParameter(name) + "     ");
//			  	log.info("[NICEID] redirect params name : " + name + ", value : " + request.getParameter(name));
//			}
//
//			log.info("[NICEID] redirect Controller!!!!!!!");
//			log.info("[NICEID] enc_data_req - " + enc_data_req);
//			log.info("[NICEID] open ### enc_data = " + enc_data);

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

			log.info("enc_data : " + enc_data);

			SecretKey secureKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes()));
			byte[] cipherEnc = Base64.getDecoder().decode(enc_data);

			String resData =   new String(c.doFinal(cipherEnc), "euc-kr");
			HashMap<String, Object> resMap = CommonUtil.jsonStringToHashMap(resData);
//			String mobileno = "01020450716" ;
//			resMap.get("mobileno").toString();
//			String name = "김민규";
//			resMap.get("name").toString();

//			data.put("mobileno", resMap.get("mobileno").toString());
//			data.put("name", resMap.get("name").toString());
			String knName = resMap.get("name").toString();
			String knPhoneNumber = resMap.get("mobileno").toString();


			if(state.equals("1")) {
				if(adminRepository.existsByKnPhoneNumber(knPhoneNumber)) {
					log.error("이미 회원가입된 핸드폰번호 입니다.");
					return ResponseEntity.ok(res.fail(ResponseErrorCode.KO034.getCode(),ResponseErrorCode.KO034.getDesc()));
				} else {
					log.info("회원가입 본인인증");
					// 회원가입떄 사용 -> 핸드폰번호 일치한지 확인하기위해서 사용됨

					data.put("joinName", knName);
					data.put("joinPhone", knPhoneNumber);
					Utils.cookieSave("joinPhone", knPhoneNumber, 1000 * 60 * 30, response); // 쿠키 제한시간 30분
				}
			} else if(state.equals("2") || state.equals("3") || state.equals("4")) {
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
					}else if(state.equals("3")) {
						log.info("비밀번호찾기 본인인증");
						data.put("keyEmail", knEmail); // -> 입력한 이메일과 DB데이터 이메일과 일치할 경우 임시비밀번호 메일전송 + 업데이트
					}else {
						log.info("OTP변경 본인인증");
					}
				}
			} else if(state.equals("5")) {
				log.info("휴대전화번호 변경 본인인증");
				data.put("joinName", knName);
				data.put("joinPhone", knPhoneNumber);
			}else {
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
	@SuppressWarnings("unchecked")
	public void getCryptoToken() {

		HashMap<String, Object> responseMap;
		boolean isValid = true;
		try {

			if (CryptoToken.getInstance().getTokenVersionId().isEmpty()) {
				isValid = false;

			} else {
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
				HttpsURLConnection conn = (HttpsURLConnection) new URL("https://svc.niceapi.co.kr:22001/digital/niceid/api/v1.0/common/crypto/token").openConnection();
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

				JSONObject requestData = new JSONObject();
				JSONObject bodyObject = new JSONObject();
				JSONObject headerObject = new JSONObject();
				headerObject.put("CNTY_CD", "ko");
				bodyObject.put("req_dtim", reqDtim);
				bodyObject.put("req_no", reqNo);
				bodyObject.put("enc_mode", "1");
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
					responseMap = CommonUtil.jsonStringToHashMap(data);
					HashMap<String, Object> bodyMap = (HashMap<String, Object>) responseMap.get("dataBody");
					String tokenVersionId = bodyMap.get("token_version_id").toString();
					String tokenValue = bodyMap.get("token_val").toString();
					String siteCode = bodyMap.get("site_code").toString();
					long period = (long)Double.parseDouble((bodyMap.get("period").toString()));

					CryptoToken cryptoToken = CryptoToken.getInstance();
					cryptoToken.setTokenValue(tokenValue);
					cryptoToken.setTokenVersionId(tokenVersionId);
					cryptoToken.setPeriod(period);
					Date issDate = new Date();
					cryptoToken.setIssDate(issDate);
					cryptoToken.setReqDtim(reqDtim);
					cryptoToken.setReqNo(reqNo);
					cryptoToken.setSiteCode(siteCode);

				} else {
					responseMap = CommonUtil.jsonStringToHashMap(data);
					HashMap<String, Object> bodyMap = (HashMap<String, Object>) responseMap.get("dataBody");
				}

				conn.disconnect();
			}
		} catch (Exception e) {
			log.info("e : "+e.getMessage());
			e.printStackTrace();
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