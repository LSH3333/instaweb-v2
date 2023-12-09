package com.lsh.instawebv2.controller;

import com.lsh.instawebv2.SessionConst;
import com.lsh.instawebv2.domain.Member;
import com.lsh.instawebv2.dto.LoginForm;
import com.lsh.instawebv2.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * (@ModelAttribute 에 의해 LoginForm 객체 자동 생성)
     * @param loginForm : 로그인 정보 받기 위한 로그인폼
     * @return : 로그인 뷰
     */
    @GetMapping("/members/login")
    public String loginForm(@ModelAttribute("loginForm")LoginForm loginForm) {
        return "members/login";
    }

    // todo: 인터셉터 만들어서 redirectURL 처리 해야함
    @PostMapping("/members/login")
    public String login(@Valid @ModelAttribute("loginForm") LoginForm form,
                        BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String redirectURL,
                        HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "members/login";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        if(loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다");
            return "members/login";
        }

        // request 에 세션 있으면 있는 세션 반환, 없으면 신규 세션 생성
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        // 로그인 성공 -> request 가 온 url 로 되돌아가도록 리다이렉트 처리
        return "redirect:" + redirectURL;
    }

}
