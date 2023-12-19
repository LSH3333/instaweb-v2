package com.lsh.instawebv2.controller;

import com.lsh.instawebv2.SessionConst;
import com.lsh.instawebv2.config.auth.PrincipalDetails;
import com.lsh.instawebv2.domain.Member;
import com.lsh.instawebv2.dto.LoginForm;
import com.lsh.instawebv2.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * 로그인 뷰로. 실제 로그인 처리는 spring security 가 수행.
     * @return : 로그인 뷰
     */
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }



    ////////////////////////////////// TEST

    /**
     * 임시 디버그용 메소드.
     * 현재 로그인 되어있는 사용자 정보 로깅
     */
    @GetMapping("/members/info")
    public String info() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            // Get the username
            String username = authentication.getName();

            List<String> roles = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            log.info("username = {}, role = {}", username, roles);

            //
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            Member member = principalDetails.getMember();
            log.info("authentication = {}", member);
        }

        return "index";
    }

    // 세션 내부에 스프링이 관리하는 SecurityContextHolder 가 있고,
    // SecurityContextHolder 내부에 SecurityContext 가 있고 그 안에는 Authentication 타입만 들어갈수 있다
    // Authentication 은 2가지 타입만을 받을수 있다 : UserDetails, OAuth2User

    // 일반 로그인을 하면 Authentication 에 UserDetails 가 보관되고
    // OAuth2 로그인을 하면 OAuth2User 가 보관된다
    // 문제: 따라서 경우에 따라 타입이 다르게 전달된다

    // 해결방법: UserDetails 를 상속받는 커스텀 클래스 PrincipalDetails 가 OAuth2User 도 상속받도록 한다
    @GetMapping("/test/login")
    public @ResponseBody String testLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 일반 로그인했을때는 UserDetails 타입이 Authentication.principal 에 보관됨
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        log.info("authentication = {}", principal.getMember());
        return "세션 정보 확인";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(Authentication authentication) {
        // oauth 로그인 시에는 OAuth2User 타입이 Authentication.principal 에 보관됨
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.info("authentication = {}", oAuth2User.getAttributes());
        return "세션 정보 확인";
    }

    @GetMapping("/test/principal/login")
    public @ResponseBody String testPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        log.info("principalDetails.getMember() = {}", principalDetails.getMember());
        return "세션 정보 확인";
    }
}
