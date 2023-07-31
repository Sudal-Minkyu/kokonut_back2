package com.app.kokonut.common.realcomponent;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Utils {
//	public static String getToday()
//	{
//		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
//		return sd.format(new Date());
//	}
//
//	public static String getYesterday()
//	{
//		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
//		Calendar cDateCal = Calendar.getInstance();
//		cDateCal.add(Calendar.DATE, -1);
//		Date date = cDateCal.getTime();
//		sd.format( date );
//		return sd.format( date );
//	}
//
//	@SuppressWarnings("unchecked")
//	public static JSONArray convertListToJson(List<HashMap<String, Object>> listMap) {
//		JSONArray jsonArray = new JSONArray();
//		for(HashMap<String, Object> map : listMap) {
//			jsonArray.add(convertMapToJson(map));
//		}
//
//		return jsonArray;
//	}
//
//	@SuppressWarnings("unchecked")
//	public static JSONObject convertMapToJson(HashMap<String, Object> map) {
//		JSONObject json = new JSONObject();
//		for (Entry<String, Object> entry : map.entrySet()) {
//			String key = entry.getKey();
//			Object value = entry.getValue();
//			json.put(key, value);
//		}
//
//		return json;
//	}
//
	public static String getClientIp(HttpServletRequest request) {
		String userIp = request.getHeader("X-Forwarded-For");

		if (userIp == null || userIp.length() == 0 || "unknown".equalsIgnoreCase(userIp)) {
			userIp = request.getHeader("Proxy-Client-IP");
		}

		if (userIp == null || userIp.length() == 0 || "unknown".equalsIgnoreCase(userIp)) {
			userIp = request.getHeader("WL-Proxy-Client-IP");
		}

		if (userIp == null || userIp.length() == 0 || "unknown".equalsIgnoreCase(userIp)) {
			userIp = request.getHeader("HTTP_CLIENT_IP");
		}

		if (userIp == null || userIp.length() == 0 || "unknown".equalsIgnoreCase(userIp)) {
			userIp = request.getHeader("HTTP_X_FORWARDED_FOR");
		}

		if (userIp == null || userIp.length() == 0 || "unknown".equalsIgnoreCase(userIp)) {
			userIp = request.getRemoteAddr();
		}

		return userIp;
	}
//
//	public static boolean isNumeric(String s) {
//		java.util.regex.Pattern pattern = Pattern.compile("[+-]?\\d+");
//		return pattern.matcher(s).matches();
//	}
//
//
//	public static boolean isSqlInjection(String str) {
//		Pattern specialChars = Pattern.compile("['\"\\-#()@;=*/+]");
//		str = specialChars.matcher(str).replaceAll("");
//		String regex = "(union|select|from|where)";
//		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
//		Matcher matcher = pattern.matcher(str);
//		if(matcher.find()) {
//			return true;
//		}
//
//		return false;
//	}

	public static HashMap<String,Object> convertJSONstringToMap(String json){
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Object> map = new HashMap<>();
		try {
			map = (HashMap<String, Object>) mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
		} catch (Exception e) {
			// TODO: handle exception
			log.error("예외처리 : "+e);
			log.error("예외처리 메세지 : "+e.getMessage());
		}
		return map;
	}

//	public static File convertMultipartFileToFile(MultipartFile mfile) {
//		File file = new File(mfile.getOriginalFilename());
//		try {
//			file.createNewFile();
//			FileOutputStream fos = new FileOutputStream(file);
//			fos.write(mfile.getBytes());
//			fos.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return file;
//	}

	/**
	 * 엑셀 Formula injection 검사
	 * @param contents
	 * @return Formula injection 기능 제거한 contents
	 */
	public static String weekPointForExcel(String contents) {
		Pattern pattern = Pattern.compile("[('='|'@'|'+|'\\-')]cmd");
		Matcher matcher = pattern.matcher(contents);
		if(matcher.find()) {
			return " ".concat(contents);
		} else {
			return contents;
		}
	}

	// 쿠키 저장함수 -> 옵션 고정 : HttpOnly = true, Secure = true, Path = "/"
	public static void cookieSave(String cookieName, String cookieValue, Integer maxAge, HttpServletResponse response) {
		Cookie cookieRefreshToken = new Cookie(cookieName, cookieValue);
		cookieRefreshToken.setMaxAge(maxAge); // 쿠키 값을 30일로 셋팅
		cookieRefreshToken.setPath("/");
		cookieRefreshToken.setHttpOnly(true);
		cookieRefreshToken.setSecure(true);
		response.addCookie(cookieRefreshToken);
	}

	// 쿠키 리셋함수
	public static void cookieLogout(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals("refreshToken")) {
					Cookie deleteCookie = new Cookie(cookie.getName(),"");
					deleteCookie.setMaxAge(-1);
					deleteCookie.setPath("/");
					deleteCookie.setHttpOnly(true);
					deleteCookie.setSecure(true);
					response.addCookie(deleteCookie);
				}
			}
		}
	}

	// List<LocalDataTime> stimeList를 반환하는 함수
	public static List<LocalDateTime> getStimeList(String stime) {
		List<LocalDateTime> stimeList = new ArrayList<>();

		String[] array = stime.split(" - ");
		String stimeStartStr = array[0]+" 00:00:00.000";
		String stimeEndStr = array[1]+" 00:00:00.000";

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		LocalDateTime stimeStart = LocalDateTime.parse(stimeStartStr, formatter);
		LocalDateTime stimeEnd = LocalDateTime.parse(stimeEndStr, formatter);

		stimeList.add(stimeStart);
		stimeList.add(stimeEnd);

		return stimeList;
	}

	// 알파값 생성 함수
	public static String getAlphabetStr(int size) {
		char[] charSet = new char[] {
				'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
				'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
		};
		StringBuilder sb = new StringBuilder();
		SecureRandom sr = new SecureRandom();
		sr.setSeed(new Date().getTime());

		int len = charSet.length;
		for (int i=0; i<size; i++) {
			int idx = sr.nextInt(len);
			sb.append(charSet[idx]);
		}

		return sb.toString();
	}

	// 난수값 생성 함수
	public static String getRamdomStr(int size) {
		char[] charSet = new char[] {
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
				'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
				'!', '@', '#', '$', '%', '^' };
		StringBuilder sb = new StringBuilder();
		SecureRandom sr = new SecureRandom();
		sr.setSeed(new Date().getTime());

		int len = charSet.length;
		for (int i=0; i<size; i++) {
			int idx = sr.nextInt(len); // 강력한 난수를 발생시키기 위해 SecureRandom을 사용한다.
			sb.append(charSet[idx]);
		}

		return sb.toString();
	}

	// LocalDateTime -> yyyy-mm-dd 형태로 변환
	public static String convertLocalDateTimeToString(LocalDateTime localDateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return localDateTime.format(formatter);
	}

	// 문자열 길이만큼 -> *로 반환
	public static String starsForString(String input) {
		StringBuilder stars = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			stars.append("*");
		}
		return stars.toString();
	}

	// 코코넛 서비스 월결제 금액 반환
	public static int kokonutMonthPrice(Integer countMonthAverage) {
		int price = 0;

		if(countMonthAverage < 10000) {
			price = 99000;
		} else if(countMonthAverage < 100000) {
			price = 390000;
		} else if(countMonthAverage < 300000) {
			price = 790000;
		} else if(countMonthAverage < 500000) {
			price = 1490000;
		} else if(countMonthAverage < 800000) {
			price = 2290000;
		} else if(countMonthAverage < 1000000) {
			price = 2990000;
		}

		return price;
	}

	// 이메일 체크 정규식 함수 - true일 경우 준수함, false일 경우 준수하지 않음
	public static boolean isValidEmail(String email) {
		String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

		Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);

		return matcher.find();
	}

	// 파일 인코딩 MultipartFile -> File 변환
	public static File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
		log.info("첨부파일 인코딩 convertMultipartFileToFile 호출");

		// 임시 파일 생성
		Path tempDir = Files.createTempDirectory("");
		String encodedFilename = URLEncoder.encode(Objects.requireNonNull(multipartFile.getOriginalFilename()), StandardCharsets.UTF_8);
		File tempFile = tempDir.resolve(encodedFilename).toFile();

		// MultipartFile 내용을 임시 파일에 쓰기
		multipartFile.transferTo(tempFile);

		return tempFile;
	}

	// 개인정보 암호화 데이터 복호화처리 함수
	public static String decrypResult(String securityName, String securityValue, SecretKey secretKey, String ivKey) throws Exception {
		String[] value = String.valueOf(securityValue).split("\\|\\|__\\|\\|"); // "||__||" 단위로 끊음
		String decryptValue = ""; // 복호화된 데이터

		if (value.length == 1) {

			log.info("구분자가 없는 암호화");
			decryptValue = AESGCMcrypto.decrypt(value[0], secretKey, ivKey);
		} else {

			log.info("||__|| 구분자로 들어간 암호화");

			if (securityName.equals("이름")) {
				if (value.length == 2) {
					// 이름이 2글자일경우
					decryptValue = value[0] + AESGCMcrypto.decrypt(value[1], secretKey, ivKey);
				} else {
					// 그 외 모든이름 공통
					decryptValue = value[0] + AESGCMcrypto.decrypt(value[1], secretKey, ivKey) + value[2];
				}
			} else if (securityName.equals("이메일주소") || securityName.equals("운전면허번호") || securityName.equals("여권번호")) {
				decryptValue = AESGCMcrypto.decrypt(value[0], secretKey, ivKey) + value[1];
			} else if (securityName.equals("휴대전화번호") || securityName.equals("연락처")) {
				decryptValue = value[0] + AESGCMcrypto.decrypt(value[1], secretKey, ivKey) + value[2];
				;
			} else if (securityName.equals("주민등록번호") || securityName.equals("거소신고번호") || securityName.equals("외국인등록번호")) {
				decryptValue = value[0] + AESGCMcrypto.decrypt(value[1], secretKey, ivKey);
			}
		}

		return decryptValue;
	}

	// 개인정보 복호화후 마스킹처리
	public static Map<String, Object> decrypMasking(String securityName, String[] securityValue, SecretKey secretKey, String ivKey) throws Exception {

		Map<String, Object> result = new HashMap<>();
		result.put("dch", "0"); // 기본값 복호화 "0"

		int trigger = 0;

		String securityResultValue = null; // 복호화된 데이터의 마스킹처리
		String decryptValue;

		// "이름" 항목을 조회시 3자리이상은 무조건 맨앞+'*'+맨뒤로 표시
		if(securityName.equals("이름")) {
			if (securityValue.length == 1) {
				// 이름이 1글자일 경우
				securityResultValue = "*";
			}
			else if (securityValue.length == 2) {
				// 이름이 2글자일경우
				securityResultValue = securityValue[0] + "*";
			} else {
				// 그 외 모든이름 공통
				securityResultValue = securityValue[0]+"*"+securityValue[2];
			}
			trigger = 1;
		}

		// "이메일주소" 항목 조회 아이디(3분의2형태만 보여줌)@도메인
		if(securityName.equals("이메일주소")) {
			decryptValue = AESGCMcrypto.decrypt(securityValue[0], secretKey, ivKey);

			int firstEmailLen = decryptValue.length();
			int firstEmailLenVal = (firstEmailLen*2)/3;

			securityResultValue = decryptValue.substring(0, firstEmailLenVal) + "*".repeat(Math.max(0, firstEmailLen - firstEmailLenVal + 1))+securityValue[1];

			result.put("dch", "1");

			trigger = 1;
		}

		// "휴대전화번호" 또는 "연락처" 항목 조회
		if(securityName.equals("휴대전화번호") || securityName.equals("연락처")) {
			securityResultValue = securityValue[0]+"****"+securityValue[2];
			trigger = 1;
		}

		// "주민등록번호" 또는 "거소신고번호" 또는 "외국인등록번호" 항목 조회
		if(securityName.equals("주민등록번호") || securityName.equals("거소신고번호") || securityName.equals("외국인등록번호")) {
			securityResultValue = securityValue[0]+"-*******";
			trigger = 1;
		}

		// "여권번호" 또는 "운전면허번호" 항목 조회
		if(securityName.equals("여권번호") || securityName.equals("운전면허번호")) {
			securityResultValue = securityValue[0]+"******";
			trigger = 1;
		}

		if(trigger == 0) {
			// 전체암호화 일 경우
			log.info("구분자가 없는 암호화");
			decryptValue = AESGCMcrypto.decrypt(securityValue[0], secretKey, ivKey);
			securityResultValue = decryptValue.charAt(0) + Utils.starsForString(decryptValue) + decryptValue.substring(decryptValue.length() - 1);

			result.put("dch", "1");
		}

		log.info("value : "+ Arrays.toString(securityValue));
		log.info("securityResultValue : "+ securityResultValue);

		result.put("result", securityResultValue);

		return result;
	}

	// 리스트에 값을 담을때 유니크한 값을 반환해주는 함수
	public static String generateUniqueName(String name, List<String> list) {
		int count = 1;
		String originalName = name;
		while (list.contains(name)) {
			count++;
			name = originalName + count;
		}
		return name;
	}

}
