package com.lsh.instawebv2.interceptor;

import com.lsh.instawebv2.config.auth.PrincipalDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;



@Slf4j
public class MemberInfoInterceptor implements HandlerInterceptor {

    // 컨트롤러 이후, dispatcherServlet 이 view 랜더링 하기 전 호출
    // Authentication 객체에서 인증된 유저 username 가져와서 MemberService 로 로그인된 유저 객체 가져와서 modelAndView 에 정보 추가
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 뷰 렌더링이 필요한 요청에 대해서만
        if (modelAndView != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // 스프링은 인증되지 않은 경우 SecurityContextHolder 에 AnonymousAuthenticationToken 을 저장한다
            if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
                PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
                modelAndView.addObject("loggedInMemberEmail", principalDetails.getMember().getEmail());
            }
        }
    }
}
