package com.app.kokonut.common;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
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
import java.time.LocalDate;
import java.time.YearMonth;

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

	// 서버 IP
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

}
