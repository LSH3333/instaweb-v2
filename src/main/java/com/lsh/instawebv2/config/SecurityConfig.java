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
//                .requestMatchers("/cashcards/**").hasRole("ROLE_USER")
                     .requestMatchers("/", "/members/**").permitAll()
                     .requestMatchers("/js/**", "/css/**", "/bootstrap/**", "/ckeditor5/**", "/img/**", "/*.ico", "/error").permitAll()
                     .anyRequest().authenticated()
             )
                .formLogin(formLogin -> formLogin
                        .loginPage("/members/login"))
             .csrf(csrf -> csrf.disable());



        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
