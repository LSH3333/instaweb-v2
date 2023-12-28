package com.lsh.instawebv2.config;

import com.lsh.instawebv2.config.oauth.PrincipalOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final PrincipalOAuth2UserService principalOAuth2UserService;

    @Autowired
    public SecurityConfig(PrincipalOAuth2UserService principalOAuth2UserService) {
        this.principalOAuth2UserService = principalOAuth2UserService;
    }



    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(request -> request
                        //.anyRequest().authenticated() // 인증만 되면 접근 가능한 경로

                        .requestMatchers("/", "/members", "/members/login", "/errors/**", "/login").permitAll() // 인증없이 접근 가능 경로
                        .requestMatchers(RegexRequestMatcher.regexMatcher("/pages/[0-9]+/comments")).permitAll() // Page 에 속한 Comment 요청
                        .requestMatchers(RegexRequestMatcher.regexMatcher("/pages/[0-9]+")).permitAll() // 작성글들은 인증 없이 볼 수 있음
                        .requestMatchers("/js/**", "/css/**", "/bootstrap/**", "/ckeditor5/**", "/img/**", "/*.ico", "/error").permitAll()
                        .requestMatchers("/page/create", "/page/upload",
                                "/members/mypage", // 로그인한 Member 의 글작성 목록
                                "/members/resign", // 로그인한 Member 회원 탈퇴
                                "/members/password", // 로그인한 Member 비밀번호 변경
                                "/members/info", // 로그인한 Member 의 정보 페이지
                                "/pages/[0-9]+/edit", // 로그인한 Member 가 작성한 {pageId} 글 수정
                                "/comments", // Comment 작성
                                "/comments/[0-9]+") // Comment 삭제
                        .hasRole("USER") // "ROLE_USER" role 이 있어야 접근 가능한 경로 (자동 prefix: ROLE_)
                        .anyRequest().authenticated() // 이외에는 모두 인증만 있으면 접근 가능 
                )
                .formLogin(formLogin -> formLogin
                                .loginPage("/login")
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .permitAll()
                                .failureHandler(authenticationFailureHandler()) // 로그인 실패시 핸들러

                )
                // OAuth2 로그인 처리
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                // PrincipalOAuth2UserService extends DefaultOAuth2UserService 가 회원가입 처리함
                                .userService(principalOAuth2UserService))
                )
                .csrf(csrf -> csrf.disable());


        return http.build();
    }


    // 로그인 실패 핸들러
    @Bean
    AuthenticationFailureHandler authenticationFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler("/login?error=true");
    }


}
