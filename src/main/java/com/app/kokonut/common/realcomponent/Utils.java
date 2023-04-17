package com.app.kokonut.common.realcomponent;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
			e.printStackTrace();
		}
		return map;
	}

	public static File convertMultipartFileToFile(MultipartFile mfile) {
		File file = new File(mfile.getOriginalFilename());
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(mfile.getBytes());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return file;
	}

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

	// 난수값 생성 함수(임시비밀번호 활용)
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


}
