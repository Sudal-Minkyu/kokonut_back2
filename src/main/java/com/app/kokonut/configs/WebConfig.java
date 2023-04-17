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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Woody
 * Date : 2022-10-25
 * Time :
 * Remark : WebConfig
 */
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${kokonut.aws.s3.access}")
    private String AWSS3ACCESSKEY;

    @Value("${kokonut.aws.s3.secret}")
    private String AWSS3SECRETKEY;

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
        registry
            .addMapping("/*/api/**")
            .allowedOriginPatterns("http://localhost:5173")
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