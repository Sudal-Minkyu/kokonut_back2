package com.app.kokonut.configs;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.*;

/**
 * @author Woody
 * Date : 2022-10-25
 * Time :
 * Remark : WebConfig
 */
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${kokonut.front.server.domain}")
    public String frontServerDomainIp;

    @Value("${kokonut.aws.s3.access}")
    private String AWSS3ACCESSKEY;

    @Value("${kokonut.aws.s3.secret}")
    private String AWSS3SECRETKEY;

    private static final String[] CLASSPATH_PATH_PATTERNS = {"swagger-ui/index.html", "/webjars/**"};
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {"classpath:/META-INF/resources/", "classpath:/META-INF/resources/webjars/"};

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(CLASSPATH_PATH_PATTERNS).addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }

    // 스웨거 엔드포인트 경로 -> "/" 셋팅
    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addViewController("/swagger").setViewName("redirect:/swagger-ui/index.html");
    }

    @Bean
    public BasicAWSCredentials AwsCredentianls() {
        return new BasicAWSCredentials(AWSS3ACCESSKEY,AWSS3SECRETKEY);
    }

    @Bean
    public AmazonS3 AwsS3Client(){
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.AP_NORTHEAST_2)
                .withCredentials(new AWSStaticCredentialsProvider(this.AwsCredentianls()))
                .build();
    }

    // 인터셉터 설정
    @Bean
    public KokonutApiInterceptor customInterceptor() {
        return new KokonutApiInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(customInterceptor()).addPathPatterns("/v3/api/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
//        log.info("허용 프론트IP : "+frontServerDomainIp);
        registry
                .addMapping("/v*/api/**")
                .allowedOriginPatterns("beta.kokonut.me:8888","https://beta.kokonut.me:8888")
                .allowedHeaders("Authorization", "Content-type", "ApiKey")
                .exposedHeaders("Authorization")
                .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name())
                .allowCredentials(true)
                .maxAge(900); // 타임아웃 15분으로 설정
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper;
    }

    @Bean
    public JpaResultMapper jpaResultMapper(){
        return new JpaResultMapper();
    }

}