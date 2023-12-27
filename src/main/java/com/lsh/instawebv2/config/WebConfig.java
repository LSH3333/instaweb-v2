package com.lsh.instawebv2.config;

import com.lsh.instawebv2.interceptor.MemberInfoInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // postHandle 로 뷰로 가기전에 model 에 유저 정보 추가
        registry.addInterceptor(new MemberInfoInterceptor());
    }
}
