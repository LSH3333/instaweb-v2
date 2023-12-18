package com.lsh.instawebv2.controller;

import com.lsh.instawebv2.SessionConst;
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
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        }

        return "info";
    }


}
