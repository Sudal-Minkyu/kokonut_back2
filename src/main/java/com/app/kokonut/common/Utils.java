package com.app.kokonut.common;

import com.app.kokonut.auth.dtos.AuthPhoneCheckDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Utils {

	// 쿠키 저장함수 -> 옵션 고정 : HttpOnly = true, Secure = true, Path = "/"
	public static void cookieSave(String cookieName, String cookieValue, Integer maxAge, HttpServletResponse response) {
		Cookie cookieRefreshToken = new Cookie(cookieName, cookieValue);
		cookieRefreshToken.setMaxAge(maxAge);
		cookieRefreshToken.setPath("/");
		cookieRefreshToken.setHttpOnly(true);
		cookieRefreshToken.setSecure(true);
		response.addCookie(cookieRefreshToken);
	}

	// 쿠키제거 함수
	public static void cookieDelete(String cookieName, HttpServletResponse response) {
		Cookie cookieRefreshToken = new Cookie(cookieName, "");
		cookieRefreshToken.setMaxAge(0);  // 유효 시간을 0으로 설정
		cookieRefreshToken.setPath("/"); // 필요한 경우 경로를 설정하세요
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
				'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
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

	/**
	 * 특수문자 포함 난수값 생성 함수 특정 길이의 암호를 생성하여 반환
	 * @param minMaxLength (optional) 첫번째 자리 - 생성할 암호의 최소길이, 두번째 자리 - 생성할 암호의 최대길이
	 * @return 길이에 맞춰 생성된 문자열
	 */
	public static String getSpecialRandomStr(int... minMaxLength) {

		// 암호에 사용될 후보 문자열군
		String PASSWORD_CHAR_POOL = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()-_+=<>?";

		int minLength;
		int maxLength;
		SecureRandom random = new SecureRandom();

		switch (minMaxLength.length) {
			case 0:
				// 인자가 없는 경우
				minLength = 8;
				maxLength = 8;
				break;
			case 1:
				// 최소 문자열 길이만 지정한 경우
				minLength = minMaxLength[0];
				maxLength = minMaxLength[0];
				break;
			case 2:
				// 최소, 최대 문자열 길이를 모두 지정한 경우
				minLength = minMaxLength[0];
				maxLength = minMaxLength[1];
				break;
			default:
				log.error("이 암호생성 메서드는 인자를 3개이상 받지 않습니다.");
				return "ERROR ERROR";
		}

		if (minLength > maxLength || minLength < 1) {
			log.error("잘못된 암호생성 길이입니다. 최소길이는 최대길이보다 작을 수 없으며, 길이가 0이 될 수도 없습니다.");
			return "ERROR ERROR";
		}

		int length = minLength + random.nextInt(maxLength - minLength + 1);
		StringBuilder password = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			password.append(PASSWORD_CHAR_POOL.charAt(random.nextInt(PASSWORD_CHAR_POOL.length())));
		}

		return password.toString();
	}

	// LocalDateTime -> String yyyy-mm-dd 형태로 변환
	public static String convertLocalDateTimeToString(LocalDateTime localDateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return localDateTime.format(formatter);
	}

	// 문자열 길이만큼 -> *로 반환
	public static String starsForString(String input) {
		return "*".repeat(input.length());
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

	// 휴대폰인증된 쿠키의 이름과 번호 반환함수
	public static AuthPhoneCheckDto authPhoneCheck(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();

		String joinPhone = "";
		String joinName = "";
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equals("joinPhone")) {
					joinPhone = URLDecoder.decode(c.getValue(), StandardCharsets.UTF_8);
					log.info("본인인증 된 핸드폰번호 : " + joinPhone);
				} else if(c.getName().equals("joinName")) {
					joinName = URLDecoder.decode(c.getValue(), StandardCharsets.UTF_8);
					log.info("본인인증 된 이름 : " + joinName);
				}
			}
		}

		return AuthPhoneCheckDto.builder()
				.joinName(joinName)
				.joinPhone(joinPhone)
				.build();
	}

	// 프론트암호화값 -> 복호화 체크 함수
	public static String decryptData(String encryptedData, String keyBuffer, String iv) {
		try {
			byte[] decodedKey = Base64.getDecoder().decode(keyBuffer);
			SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, Base64.getDecoder().decode(iv));
			cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);

			byte[] decryptedTextBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
			return new String(decryptedTextBytes, StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// 시각을 입력받아 x일전, x시간전, x분전 등의 결과문자를 반환
	public static String calculateTimeAgo(LocalDateTime refTime, LocalDateTime nowTime) {
		if (refTime != null) {
			long diffInSeconds = ChronoUnit.SECONDS.between(refTime, nowTime);
			long diffInMinutes = diffInSeconds / 60;
			long diffInHours = diffInMinutes / 60;
			long diffInDays = diffInHours / 24;
			long diffInMonths = diffInDays / 30;
			long diffInYears = diffInDays / 365;

			if (diffInYears > 0) {
				return diffInYears + "년 전";
			} else if (diffInMonths > 0) {
				return diffInMonths + "달 전";
			} else if (diffInDays > 0) {
				return diffInDays + "일 전";
			} else if (diffInHours > 0) {
				return diffInHours + "시간 전";
			} else if (diffInMinutes > 6) {
				// 6분이상일 경우 미접속으로 판단
				return diffInMinutes + "분 전";
			} else {
				return "방금전";
			}
		}
		return "";
	}
}
