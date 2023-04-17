package com.app.kokonut.configs;

import com.app.kokonut.admin.enums.AuthorityRole;
import com.app.kokonut.auth.jwt.been.JwtAccessDeniedHandler;
import com.app.kokonut.auth.jwt.been.JwtAuthenticationEntryPoint;
import com.app.kokonut.auth.jwt.been.JwtAuthenticationFilter;
import com.app.kokonut.auth.jwt.been.JwtTokenProvider;
import com.app.kokonut.auth.jwt.dto.RedisDao;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Woody
 * Date : 2022-11-01
 * Time :
 * Remark : 기본 시큐리티셋팅
 */
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisDao redisDao;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // JWT 필터 및 권한 제외 url
        return web -> web.ignoring()
            .antMatchers(
                // 필터 제외항목 API : JWT, ApiKey 모두 불필요한 API
                "/favicon.ico", "/swagger*/**", "/v2/api-docs", "/webjars/**",
                "/v1/api/**"
                // 임시 제외항목 API : JWT, ApiKey 모두 필요한 API
//                "/v2/api/**"
            );
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()

            // Exception handling 할 때 만든 클래스를 추가
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler)

            .and()
            .authorizeRequests()
            // 권한 : 없음
            .antMatchers("/favicon.ico","/swagger-ui/index.html/**",
                    "/v3/api/PersonalInfoProvision/list", "/v3/api/History/activityList").permitAll()
            // 권한 : 권한(코코넛직원:ROLE_SYSTEM, 대표관리자:ROLE_MASTER, 최고관리자:ROLE_ADMIN, 일반관리자:ROLE_USER, 게스트:ROLE_GUEST)
            // 권한에 따라 요청허용
            .antMatchers("/v2/api/Admin/systemTest")
                .hasAuthority(AuthorityRole.ROLE_SYSTEM.getDesc())

            .antMatchers("/v2/api/Admin/masterTest")
                .hasAnyAuthority(AuthorityRole.ROLE_MASTER.getDesc(), AuthorityRole.ROLE_SYSTEM.getDesc())

            .antMatchers("/v2/api/Admin/adminTest", "/v2/api/ApiKey/**", "/v2/api/Setting/**", "/v2/api/DynamicUser/**")
                .hasAnyAuthority(AuthorityRole.ROLE_ADMIN.getDesc(), AuthorityRole.ROLE_MASTER.getDesc(), AuthorityRole.ROLE_SYSTEM.getDesc())

            .antMatchers("/v2/api/Admin/userTest", "/v2/api/Admin/create", "/v2/api/Admin/**", "/v2/api/History/**", "/v2/api/Email/**", "/v2/api/Company/addCategoryList", "/v2/api/DynamicUser/tableColumnCall")
                .hasAnyAuthority(AuthorityRole.ROLE_USER.getDesc(), AuthorityRole.ROLE_ADMIN.getDesc(), AuthorityRole.ROLE_MASTER.getDesc(), AuthorityRole.ROLE_SYSTEM.getDesc())

            .antMatchers("/v2/api/Admin/guestTest")
                .hasAnyAuthority(AuthorityRole.ROLE_GUEST.getDesc(), AuthorityRole.ROLE_USER.getDesc(), AuthorityRole.ROLE_ADMIN.getDesc(), AuthorityRole.ROLE_MASTER.getDesc(), AuthorityRole.ROLE_SYSTEM.getDesc())
            .anyRequest().authenticated()   // 나머지 API 는 전부 인증 필요

            .and()
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, redisDao), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}


