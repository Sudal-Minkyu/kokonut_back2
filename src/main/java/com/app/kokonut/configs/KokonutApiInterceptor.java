package com.app.kokonut.configs;

import com.app.kokonut.apiKey.ApiKeyService;
import com.app.kokonut.apiKey.dtos.ApiKeyInfoDto;
import com.app.kokonut.common.realcomponent.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Woody
 * Date : 2022-10-24
 * Remark :
 */
@Slf4j
@Component
public class KokonutApiInterceptor implements AsyncHandlerInterceptor {

	private static final String BEARER_TYPE = "Authorization";

	private static final String APIKEY_TYPE = "ApiKey";

	@Autowired
	private ApiKeyService apiKeyService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("APIKey 인터셉터 읽음");

		String token = request.getHeader(BEARER_TYPE);
		String apikey = request.getHeader(APIKEY_TYPE);

		log.info("apikey : "+apikey);

		// 헤더 값에 API Key가 존재하지 않을 경우 -> 400에러를 내보낸다.
		// 코코넛 DB안에 존재하지 않은 API Key 일 경우 -> 404 에러를 내보낸다.
		// 사용에 제한된 API Key 일 경우 -> 403 에러를 내보낸다.
		// API Key에 사용될 수 없는 IP 접근 일 경우 -> 403 에러를 내보낸다.
		if(token == null) {
			if(apikey != null) {
				Long check = apiKeyService.validateApiKey(apikey);
				if(check == 0) {
					log.error("존재하지 않은 APIKey 입니다. (404에러)");
					response.sendError(HttpStatus.SC_NOT_FOUND);
					return false;
				} else {

					String userIp = Utils.getClientIp(request);
					log.info("유저 IP : " + userIp);

					// 해당 APiKey의 사용할 수 있는 IP 인지 체크하는 거 추가해야됨. woody
//                	Long ipCheck = apiKeyService.findByApiKeyCheck(userIp);
//					log.error("APIKey 사용에 적절하지 않은 사용처 입니다. (403에러)");
//					response.setStatus(HttpStatus.SC_FORBIDDEN);

					ApiKeyInfoDto apiKeyInfoDto = apiKeyService.findByApiKeyInfo(apikey);
					if(apiKeyInfoDto.getAkUseYn().equals("N")) {
						log.error("사용에 제한된 APIKey 입니다. (402에러)");
						response.sendError(HttpStatus.SC_PAYMENT_REQUIRED);
						return false;
					} else {
						log.info("apiKeyInfoDto : " + apiKeyInfoDto);
						request.setAttribute("apiKeyInfoDto", apiKeyInfoDto);
					}
				}
			} else {
				log.error("헤더에 APIKey가 존재하지 않습니다. (400에러)");
				response.sendError(HttpStatus.SC_BAD_REQUEST);
				return false;
			}
		}
		return AsyncHandlerInterceptor.super.preHandle(request, response, handler);
	}

}
