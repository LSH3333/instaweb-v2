package com.lsh.instawebv2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(request -> request
                        //.anyRequest().authenticated() // 인증만 되면 접근 가능한 경로

                        .requestMatchers("/", "/members/**").permitAll() // 인증없이 접근 가능 경로
                        .requestMatchers("/js/**", "/css/**", "/bootstrap/**", "/ckeditor5/**", "/img/**", "/*.ico", "/error").permitAll()
                        .requestMatchers("/page/create", "/page/upload").hasRole("USER") // role 이 있어야 접근 가능한 경로 (prefix: ROLE_)
                        .anyRequest().authenticated() // 이외에는 모두 인증만 있으면 접근 가능 
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/members/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .loginProcessingUrl("/members/login-proc")
                        .defaultSuccessUrl("/")
                )
                .logout(logoutConfig -> logoutConfig.logoutUrl("/members/logout").logoutSuccessUrl("/"))
                .csrf(csrf -> csrf.disable());


        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
