package com.app.kokonut.configs;

import com.app.kokonut.apikey.ApiKeyService;
import com.app.kokonut.apikey.dtos.ApiKeyInfoDto;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.CommonUtil;
import com.app.kokonut.configs.exception.KokonutAPIException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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

	private static final String APIKEY_TYPE = "x-api-key";

	@Autowired
	private ApiKeyService apiKeyService;

	@Override
	public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        log.info("APIKey 인터셉터 읽음");

		String token = request.getHeader(BEARER_TYPE);
		String apikey = request.getHeader(APIKEY_TYPE);

		log.info("apikey : "+apikey);

		// 헤더 값에 API Key가 존재하지 않을 경우 -> 400에러를 내보낸다.
		// 코코넛 DB안에 존재하지 않은 API Key 일 경우 -> 404 에러를 내보낸다.
		// 관리자에 의해 사용에 제한된 API Key 일 경우 -> 402 에러를 내보낸다.
		// API Key에 사용될 수 없는 IP 접근 일 경우 -> 403 에러를 내보낸다.
		if(token == null) {
			if(apikey != null) {
				Long check = apiKeyService.validateApiKey(apikey);
				if(check == 0) {
					log.error("호출하신 APIKey는 존재하지 않은 APIKey 입니다. APIKey관리 페이지에서 APIKey를 확인해주세요. (404에러)");
					throw new KokonutAPIException(ResponseErrorCode.ERROR_CODE_97);
				} else {

					String ip = CommonUtil.publicIp();
					log.info("호출한 공인 IP : " + ip);

					ApiKeyInfoDto apiKeyInfoDto = apiKeyService.findByApiKeyInfo(apikey, ip);
					if(apiKeyInfoDto == null) {
						log.error("허용되지 않은 IP 입니다. APIKey관리 페이지에서 허용IP를 추가해주세요. (403에러)");
						throw new KokonutAPIException(ResponseErrorCode.ERROR_CODE_99);
					} else {
						if(apiKeyInfoDto.getAkUseYn().equals("N")) {
							log.error("관리자에 의해 사용에 제한된 APIKey 입니다. 관리자에게 문의해주세요. (402에러)");
							throw new KokonutAPIException(ResponseErrorCode.ERROR_CODE_98);
						} else {
							log.info("apiKeyInfoDto : " + apiKeyInfoDto);
							request.setAttribute("apiKeyInfoDto", apiKeyInfoDto);
						}
					}
				}
			} else {
				log.error("헤더에 APIKey가 존재하지 않습니다. APIKey를 담아 보내주세요. (400에러)");
				throw new KokonutAPIException(ResponseErrorCode.ERROR_CODE_96);
			}
		}
		return AsyncHandlerInterceptor.super.preHandle(request, response, handler);
	}

}
