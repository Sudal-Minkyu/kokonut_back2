package com.app.kokonut.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Woody
 * Date : 2022-10-24
 * Remark :
 */
@Configuration
@EnableWebMvc
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api1() {
        final ApiInfo apiInfo = new ApiInfoBuilder().title("코코넛 API")
                .description("<h3>코코넛 - JWT Token, ApiKey 없이 호출 가능한 RestAPI 문서</h3>")
                .contact(new Contact("Kokonut", "https://kokonut.me", "contact@kokonut.me"))
                .version("1.0").build();

        return new Docket(DocumentationType.SWAGGER_2) // Swagger 2.0 기반의 문서 작성
                .groupName("1. JWT Token, ApiKey 불필요")
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.app"))
                .paths(PathSelectors.ant("/v1/api/**"))
                .build().useDefaultResponseMessages(false);
    }

    @Bean
    public Docket api2() {
        final ApiInfo apiInfo = new ApiInfoBuilder().title("코코넛 API")
                .description("<h3>코코넛 - JWT Token을 보유해야 호출 가능한 RestAPI</h3>")
                .contact(new Contact("Kokonut", "https://kokonut.me", "contact@kokonut.me"))
                .version("1.0").build();

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("2. JWT Token 필요")
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/v2/api/**"))
                .build().useDefaultResponseMessages(false);
    }

    @Bean
    public Docket api3() {
        final ApiInfo apiInfo = new ApiInfoBuilder().title("코코넛 API")
                .description("<h3>코코넛 - ApiKey 또는 JWT Token 둘중 하나는 보유시 호출 가능한 RestAPI</h3>")
                .contact(new Contact("Kokonut", "https://kokonut.me", "contact@kokonut.me"))
                .version("1.0").build();

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("3. ApiKey 또는 JWT Token 둘중 하나는 필요")
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.app"))
                .paths(PathSelectors.ant("/v3/api/**"))
                .build().useDefaultResponseMessages(false);
    }

}
