package com.app.kokonut.common.realcomponent;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Iterator;

@Slf4j
@Component
public class CommonUtil {

	private static String kokonutState;

	@Value("${kokonut.state}")
	public void setKokonutState(String kokonutState) {
		CommonUtil.kokonutState = kokonutState;
	}

    public static String publicIp() {
		String ip = "";

		log.info("kokonutState : "+kokonutState);

		if(kokonutState.equals("1")) {
			try {
				URL url = new URL("https://checkip.amazonaws.com");

				try(BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))){
					ip = br.readLine();
				}

			} catch (IOException e) {
				log.error("개발용 public Ip 조회 에러 : "+e);
			}

		} else {
			ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpServletRequest request = sra.getRequest();

			ip = request.getHeader("X-Forwarded-For");
//			ip = Inet4Address.getLocalHost().getHostAddress(); // IPV4 가져오기

			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}

			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}

			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}

			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}

			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("X-Real-IP");
			}

			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("X-RealIP");
			}

			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("REMOTE_ADDR");
			}

			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
		}

		if (ip != null) {
			String[] ips = ip.split(",");
			if (ips.length > 0) {
				ip = ips[0].trim(); // 첫 번째 IP만 반환
			}
		}

		return ip;
    }

	// 코코넛 서비스 월결제 금액 반환
	public static int kokonutMonthPrice(Integer countMonthAverage) {
		int price = 0;

		if(countMonthAverage < 10000) {
			price = 99000;
//			price = 500;
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

	// 구독해지 대상 사용한만큼 금액결제
	public static int calculateUsedAmount(int totalAmount, LocalDate currentDate, int daysUsed) {
		// 현재 월의 총 일수를 가져옵니다.
		YearMonth yearMonth = YearMonth.from(currentDate);
		int daysInCurrentMonth = yearMonth.lengthOfMonth();

		// 일일 서비스 비용 계산
		int dailyServiceAmount = totalAmount / daysInCurrentMonth;

		// 사용한 서비스에 대한 비용 계산
		return dailyServiceAmount * daysUsed;
	}

	// 현재 원화가치 가져오기 -> USD 기준
	public static int wonPriceGet() {
		int wonPrice = 0;

		try {
			URL url = new URL("https://quotation-api-cdn.dunamu.com/v1/forex/recent?codes=FRX.KRWUSD");

			// 응답 데이터 얻기
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuilder sb = new StringBuilder();

			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			br.close();

			// JSON 파싱
			JSONArray jsonArray = new JSONArray(sb.toString());
			JSONObject jsonObject = jsonArray.getJSONObject(0);
			wonPrice = jsonObject.getInt("basePrice");
		} catch (IOException e) {
			log.error("원화 환율 가져오기 실패");
		}

		return wonPrice;
	}

//
//    /**
//	 * 브라우저별 파일 이름 가져오기
//	 * @param HttpServletRequest - HttpServletRequest
//	 * @param fileName - 파일이름
//	 */
//    public static String getBrowser(HttpServletRequest request) {
//    	String browser = "";
//		String header = request.getHeader("User-Agent");
//
//		if ( header.indexOf("MSIE") > -1 || header.indexOf("Trident") > -1 )  {
//			browser = "MSIE";
//		} else if (header.indexOf("Opera") > -1 || header.indexOf("OPR") > -1 ) {
//			browser = "Opera";
//		} else if (header.indexOf("Safari") > -1) {
//			if(header.indexOf("Chrome") > -1){
//				browser = "Chrome";
//			}else{
//				browser = "Safari";
//			}
//		} else {
//			browser = "Firefox";
//		}
//
//		return browser;
//    }
//
//    /**
//	 * 브라우저별 파일 이름 가져오기
//	 * @param HttpServletRequest - HttpServletRequest
//	 * @param fileName - 파일이름
//	 */
//    public static String getFileNameByBrowser(HttpServletRequest request, String fileName) {
//    	String browser = "";
//		String header = request.getHeader("User-Agent");
//
//		if ( header.indexOf("MSIE") > -1 || header.indexOf("Trident") > -1 )  {
//			browser = "MSIE";
//		} else if (header.indexOf("Opera") > -1 || header.indexOf("OPR") > -1 ) {
//			browser = "Opera";
//		} else if (header.indexOf("Safari") > -1) {
//			if(header.indexOf("Chrome") > -1){
//				browser = "Chrome";
//			}else{
//				browser = "Safari";
//			}
//		} else {
//			browser = "Firefox";
//		}
//
//		try {
//			if (browser.equals("MSIE")) {
//				fileName = new String(fileName.getBytes("euc-kr"), "8859_1");
//			} else if (browser.equals("Firefox")) {
//	        	fileName =  '"' + new String(fileName.getBytes("UTF-8"), "8859_1") + '"';
//	         } else if (browser.equals("Safari")) {
//	        	fileName =  '"' + new String(fileName.getBytes("UTF-8"), "8859_1") + '"';
//	         } else if (browser.equals("Opera")) {
//	        	fileName =  '"' + new String(fileName.getBytes("UTF-8"), "8859_1") + '"';
//	         } else if (browser.equals("Chrome")) {
//	             StringBuffer sb = new StringBuffer();
//	             for (int i = 0; i < fileName.length(); i++) {
//	                 char c = fileName.charAt(i);
//	                 if (c > '~') {
//	                     sb.append(URLEncoder.encode("" + c, "UTF-8"));
//	                 } else {
//	                     sb.append(c);
//	                 }
//	             }
//	             fileName = sb.toString();
//	         } else {
//	             logger.error("Not supported browse");
//	         }
//		} catch (UnsupportedEncodingException e) {
//			logger.error(e.getMessage());
//		}
//
//		return fileName;
//    }
//
//	/**
//	 * Map에 대한 데이타 가져오기
//	 * @param map - 대상Map
//	 * @param key - 데이터를 가져올 Key
//	 * @param defaultValue - 데이터가 없는 경우 기본값
//	 * @return String
//	 */
//    public static String getMapValue(HashMap<String, Object> map, String key, String defaultValue){
//        if (map == null) return "";
//        Object value = map.get(key);
//        if (value == null) return defaultValue;
//        if (StringUtils.isEmpty("" + value)) return defaultValue;
//        return "" + value;
//    }
//
//    /**
//     * 파라미터를 확인하여 Null이면 공백문자 반환
//     * @param parameter - Object
//     * @return - String(default="")
//     */
//    public static String toString(Object parameter){
//        return (parameter == null) ? "" : parameter.toString();
//    }
//
//    /**
//     * 파라미터를 확인하여 Null이면 대체문자열(replacement)반환
//     * @param parameter - Object
//     * @param replacement - String : Null인경우 대체문자열
//     * @return - String
//     */
//    public static String toString(Object parameter, String replacement){
//    	return (parameter == null) ? replacement : parameter.toString();
//    }
//
//    /**
//     * 파라미터를 확인하여 Null이면 0 반환
//     * @param parameter - Object
//     * @return - int
//     */
//    public static int toInt(Object parameter){
//    	if(parameter == null || parameter.toString().trim().equals("")) return 0;
//    	else return Integer.parseInt(parameter.toString().trim()) ;
//    }
//
//    /**
//     * 파라미터를 확인하여 Null이면 0 반환
//     * @param parameter - Object
//     * @return - long
//     */
//    public static long toLong(Object parameter){
//    	if(parameter == null || parameter.toString().trim().equals("")) return 0;
//    	else return Long.parseLong(parameter.toString().trim()) ;
//    }
//
//    /**
//     * 파라미터를 확인하여 Null이면 0 반환
//     * @param parameter - Object
//     * @return - float
//     */
//    public static float toFloat(Object parameter){
//    	if(parameter == null || parameter.toString().trim().equals("")) return 0;
//    	else return Float.parseFloat(parameter.toString().trim()) ;
//    }
//
//    /**
//     * 파라미터를 확인하여 Null이면 0 반환
//     * @param parameter - Object
//     * @return - double
//     */
//    public static double toDouble(Object parameter){
//    	if(parameter == null || parameter.toString().trim().equals("")) return 0;
//    	else return Double.parseDouble(parameter.toString().trim()) ;
//    }
//
//    /**
//	 * 숫자형 데이터를 회계단위로 표현
//	 * @param num - String 텍스트형 숫자 데이타(10000)
//	 * @return String 변형된 데이타(10,000)
//	 */
//	public static String addComma(String num){
//		NumberFormat nf = NumberFormat.getInstance();
//		if(num == null || num.trim().equals("")) num = "0";
//		return nf.format(Long.parseLong(num));
//	}
//
//	/**
//	* 구분자가 있는 문자열을 배열로 반환(StringTokenizer)
//	* @param value - String : 대상문자열(ex.  "A01,A02,A03")
//	* @param separator - String : 구분자(ex. ",")
//	* @return  String[]
//	*/
//	public static String[] toArray(String parameter, String separator){
//		String[] array = null;
//		if(parameter != null && !parameter.trim().equals("")){
//			StringTokenizer st = new StringTokenizer(parameter, separator);
//			array = new String[st.countTokens()];
//			int i = 0;
//			while(st.hasMoreTokens()){
//				array[i] = st.nextToken();
//				i++;
//			}
//		}
//		return array;
//	}
//
//    /**
//     * 파라미터에서 regex 찾아 제거하고 반환
//     * @param parameter - Object regex가 포함된 변환대상
//     * @param regex - String 제거할 문자 ex) '-' , ','
//     * @return String : regex가 제거된 변환된 문자열
//     */
//    public static String stripExp(Object parameter, String regex) {
//        return toString(parameter).replaceAll(regex, "");
//    }
//
//    /**
//     * 파라미터를 구분(cls)에 따라 포맷하여 반환
//     * @param parameter - Object 포맷대상
//     * @param cls - String 포맷종류(JUMIN|POST|DATE)
//     * @param format - String 포맷문자(ex. "-", "/")
//     * @return String : 포맷된 문자열
//     */
//    public static String formatString(Object parameter, String cls, String format){
//    	String formattedStr = "";
//    	formattedStr = toString(parameter);
//    	formattedStr = stripExp(formattedStr, format);
//    	if(cls == null || cls.trim().equals("") ||
//    			format == null || format.trim().equals("")){
//    		//할수있는게 없음
//    	}else if(cls.equals("JUMIN")){
//    		if(formattedStr.trim().length() == 13){
//    			formattedStr = formattedStr.substring(0,6) + format
//    					       + formattedStr.substring(6, 13);
//    		}
//		}else if(cls.equals("POST")){
//			if(formattedStr.trim().length() == 6){
//				formattedStr = formattedStr.substring(0,3) + format
//						      + formattedStr.substring(3, 6);
//    		}
//		}else if(cls.equals("DATE")){
//			if(formattedStr.trim().length() == 8){
//				formattedStr = formattedStr.substring(0,4) + format
//						      + formattedStr.substring(4, 6) + format
//						      + formattedStr.substring(6, 8);
//			}else if (formattedStr.trim().length() == 6) {
//				formattedStr = formattedStr.substring(0,4) + format
//						       + formattedStr.substring(4, 6);
//			}
//		}
//    	return formattedStr;
//    }
//
//    /**
//     * StringTokenizer를 이용해서 문자열을 분리해서
//     * 쿼리용(IN ())으로 문자열 조정
//     * @param str String : 대상문자열
//     * @param delim String : 분리자
//     */
//    public static String makeQueryString(String str, String delim){
//    	StringTokenizer stz = new StringTokenizer(str, delim);
//    	String inStr = "";
//    	int i = 0;
//    	while(stz.hasMoreTokens()){
//    		inStr = inStr+ (i==0?"":",") + "'"+stz.nextToken().trim()+"'";
//    		i++;
//    	}
//    	return inStr;
//    }
//
//    /**
//	 * humanReadableByteCount 이용해서 파일의 사이즈를 인간 읽을 수 있게 만든다.
//	 * @param byteStr String : 사이즈 (byte)
//	 * @param si boolean : kB 단위
//	 */
//	public static String humanReadableByteCount(String byteStr, boolean si){
//		long bytes = 0;
//		try {
//			bytes= Long.parseLong(byteStr);
//		} catch (Exception e) {}
//		int unit = si ? 1024 : 1000;
//		if (bytes < unit) return bytes + " B";
//		int exp = (int) (Math.log(bytes) / Math.log(unit));
//		//String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
//		String pre = "KMGTPE".charAt(exp-1) + "";
//		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
//	}

	/**
	 * 접속한 서버 ip 확인
	 */
	public static String getServerIp() {
		InetAddress local = null;
		try {
			local = InetAddress.getLocalHost();
		} catch ( UnknownHostException e ) {
			log.error(" 접속한 서버 ip UnknownHostException : "+e.getMessage());
		}

		if( local == null ) {
			return "";
		} else {
			return local.getHostAddress();
		}
	}

	/***
	 * 랜덤 문자열 생성
	 */
	public static String makeRandomChar(int length) {
		SecureRandom rnd = new SecureRandom();
		StringBuilder randomChar = new StringBuilder();
		for (int i = 1; i <= length; i++) {
			char ch = (char) (rnd.nextInt(26) + 97);
			randomChar.append(ch);
		}
		return randomChar.toString();
	}

	/***
	 * JSON String -> HashMap 변환
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> jsonStringToHashMap(String str) {
		HashMap<String, Object> dataMap = new HashMap<>();
		JSONParser parser = new JSONParser();
		try{
			Object obj = parser.parse(str);
			JSONObject jsonObject = (JSONObject)obj;

			Iterator<String> keyStr = jsonObject.keySet().iterator();
			while(keyStr.hasNext()){
				String key = keyStr.next();
				Object value = jsonObject.get(key);

				dataMap.put(key, value);
			}
		} catch(Exception e){
			log.error("예외처리 : "+e);
			log.error("예외처리 메세지 : "+e.getMessage());
		}

		return dataMap;
	}

//
//	/***
//	 * 카드번호 16자리 또는 15자리 마스킹처리 '-'포함/미포함 상관없음
//	 */
//	public static String cardMasking(String cardNo) throws Exception {
//		String regex = "(\\d{4})-?(\\d{4})-?(\\d{4})-?(\\d{3,4})$";
//		Matcher matcher = Pattern.compile(regex).matcher(cardNo);
//		if(matcher.find()) {
//			String target = matcher.group(2) + matcher.group(3);
//			int length = target.length();
//			char[] c = new char[length];
//			Arrays.fill(c, '*');
//			return cardNo.replace(target, String.valueOf(c));
//		}
//
//		return cardNo;
//	}
//
//	/** * getCookie * *
//	 * @return cookie *
//	 * @throws Exception */
//	public static String getCookie(HttpServletRequest request, String key) throws Exception {
//		Cookie[] cookies = request.getCookies();
//		if(key == null) return null;
//		String value = "";
//		if(cookies != null){
//			for(int i = 0; i < cookies.length; i++){
//				if(key.equals(cookies[i].getName())){
//					value = java.net.URLDecoder.decode(cookies[i].getValue(), "UTF-8"); break;
//					}
//				}
//			}
//
//		return value;
//	}
//
//	/**
//	 * 레스트 API에서 제이슨 오브젝트 넘겨 받는 메서드
//	 * @param url                   타겟이 되는 서버 Uri
//	 * @param headerParam           http header
//	 * @param bodyParam             http request body
//	 * @param methodType            http method
//	 * @param jsonParam             request json data
//	 * @param connectionTimeout     http request connectionTimeout
//	 * @param readTimeout           http request readTimeout
//	 * @return
//	 */
//	public static JSONObject getJsonFromRestApi(String url, Map<String, String> headerParam, Map<String, Object> bodyParam,
//										 String methodType, JSONObject jsonParam, int connectionTimeout, int readTimeout) {
//		logger.debug("getJsonFromRestApi started.");
//
//		HttpHeaders headers = new HttpHeaders();
//		if (headerParam != null) {
//			for (String key : headerParam.keySet()) {
//				headers.set(key, headerParam.get(key));
//			}
//		}
//
//		HttpEntity<?> entity = new HttpEntity<>(jsonParam, headers);
//
//		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
//
//		if (bodyParam != null) {
//			for(String key : bodyParam.keySet()){
//				builder.queryParam(key, bodyParam.get(key));
//			}
//		}
//
//		RestTemplate rest = null;
//
//		if (methodType.equals("patch")) {
//			ClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//			rest = new RestTemplate(httpRequestFactory);
//		} else if (connectionTimeout > 0 || readTimeout > 0) {
//			HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//			httpRequestFactory.setConnectTimeout(connectionTimeout);
//			httpRequestFactory.setReadTimeout(readTimeout);
//			rest = new RestTemplate(httpRequestFactory);
//		} else {
//			rest = new RestTemplate();
//		}
//
//		HttpMethod method = null;
//		switch (methodType) {
//			case "get":
//				method = HttpMethod.GET;
//				break;
//			case "put":
//				method = HttpMethod.PUT;
//				break;
//			case "post":
//				method = HttpMethod.POST;
//				break;
//			case "delete":
//				method = HttpMethod.DELETE;
//				break;
//			case "patch":
//				method = HttpMethod.PATCH;
//				break;
//			default:
//				method = HttpMethod.GET;
//				break;
//		}
//		ResponseEntity<String> result = rest.exchange(builder.build().encode().toUri(), method, entity, String.class);
//
//		JSONParser jp = new JSONParser();
//
//		JSONObject resultBody = null;
//
//		try {
//			resultBody = (JSONObject) jp.parse(result.getBody());
//		} catch (org.json.simple.parser.ParseException e) {
//			e.printStackTrace();
//		}
//
//		logger.debug("getJsonFromRestApi ended.");
//		return resultBody;
//	}

}
