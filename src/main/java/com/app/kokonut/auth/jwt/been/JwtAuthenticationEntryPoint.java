package com.app.kokonut.auth.jwt.been;

import com.app.kokonut.common.realcomponent.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Woody
 * Date : 2022-12-01
 * Remark :
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        // 쿠키리셋처리
        Utils.cookieLogout(request, response);

        log.info("인증되지 않은 사용자가 접근시 막아주는 핸들러 작동 (401 에러)");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
