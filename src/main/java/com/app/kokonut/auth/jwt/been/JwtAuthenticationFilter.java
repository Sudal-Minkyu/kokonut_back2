package com.app.kokonut.auth.jwt.been;

import com.app.kokonut.auth.jwt.dto.RedisDao;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Woody
 * Date : 2022-12-01
 * Time :
 * Remark : JWT Filter
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_TYPE = "Authorization";

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisDao redisDao;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        log.info("JwtAuthenticationFilter 호출");

        String token = resolveToken(request);

        if(token != null) {
            log.info("필터 거쳐감 Jwt Access Token");

            if(token.equals("kokonut")) {
                log.info("로그인 이후 이용해주세요.");
                throw new RuntimeException("로그인 이후 이용해주세요.");
            } else {
                // validateToken으로 토큰 유효성 검사 + ApiKey 유효성 검사
                int result = jwtTokenProvider.validateToken(token);
                if (result == 200) {
                    // Redis 에 해당 accessToken logout 여부 확인
                    String isLogout = redisDao.getValues(token);
                    if (ObjectUtils.isEmpty(isLogout)) {
                        this.setAuthentication(token);
                    }
                } else if(result == 901) {
                    log.info("여기왔슴둥 : "+result);
                    throw new io.jsonwebtoken.security.SecurityException("잘못된 JWT 토큰입니다.");
                } else if(result == 902) {
                    log.info("여기왔슴둥 : "+result);
                    // JWT 엑세스토큰 재발급하기
                    Cookie[] cookies = request.getCookies();
                    String refreshToken = null;
                    if(cookies != null) {
                        for(Cookie cookie : cookies) {
                            if(cookie.getName().equals("refreshToken")) {
                                refreshToken = cookie.getValue();
                            }
                        }
                    }
                    log.info("리플레쉬 토큰 : "+refreshToken);

                    if(refreshToken == null) {
                        log.info("리플레쉬 토큰이 만료되었습니다. 새로 로그인해주시길 바랍니다.");
                        throw new RuntimeException("만료된 JWT 토큰입니다.");
                    } else {
                        // 토큰 검증
                        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);

                        // 토큰 발급
                        String newAccessToken = jwtTokenProvider.reissueAccessToken(authentication);
                        log.info("엑세스토큰 만료 -> 새로발급된 토큰 : "+newAccessToken);

                        // 헤더에 어세스 토큰 추가
                        jwtTokenProvider.setHeaderAccessToken(response, newAccessToken);

                        // 컨텍스트에 넣기
                        this.setAuthentication(newAccessToken);
                    }
                } else if(result == 903) {
                    log.info("여기왔슴둥 : "+result);
                    throw new UnsupportedJwtException("지원되지 않는 JWT 토큰입니다.");
                } else if(result == 904) {
                    log.info("여기왔슴둥 : "+result);
                    throw new IllegalArgumentException("JWT 토큰이 맞지 않습니다.");
                } else {
                    log.info("여기왔슴둥 : "+result);
                    throw new RuntimeException("인증된 정보가 없습니다.");
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    // Request Header 에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        return request.getHeader(BEARER_TYPE);
    }

    // SecurityContext 에 Authentication 객체를 저장합니다.
    public void setAuthentication(String token) {
        // 토큰으로부터 유저 정보를 받아옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}

