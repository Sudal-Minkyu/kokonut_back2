package com.app.kokonut.auth.jwt.been;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
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
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        // 필요한 권한이 없이 접근하려 할때 403
        log.info("권한없는 사용자가 접근시 막아주는 핸들러 작동");
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}
