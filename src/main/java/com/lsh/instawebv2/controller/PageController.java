package com.lsh.instawebv2.controller;

import com.lsh.instawebv2.domain.Member;
import com.lsh.instawebv2.domain.Page;
import com.lsh.instawebv2.service.MemberService;
import com.lsh.instawebv2.service.PageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class PageController {

    private final PageService pageService;
    private final MemberService memberService;

    public PageController(PageService pageService, MemberService memberService) {
        this.pageService = pageService;
        this.memberService = memberService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/pages/create")
    public String createPage() {
        return "pages/create";
    }

    // todo: 임시
    @GetMapping("/pages/view")
    public String viewPage(Model model) {
        Page page = pageService.findOne(1L);
        model.addAttribute("page", page);
        return "pages/view";
    }

    /**
     *
     * @param content : 'pages/create.html' 로 부터 ajax 요청 전달 받아서 Page 저장함
     * @return : HTTStatus OK 
     */
    @PostMapping("/pages/upload")
    public ResponseEntity<String> uploadPage(@RequestParam("content") String content) {
        log.info("content = {}", content);
        String message;

        Member loggedInMember = getLoggedInMember();
        pageService.createPageForMember(loggedInMember.getId(), content);

        message = "Files uploaded successfully!";
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    /**
     * SecurityContextHolder 에 저장되있는 인증 받은 사용자 찾는다
     * @return : 인증 받은 (로그인된) Member 있으면 리턴 없으면 null 리턴
     */
    private Member getLoggedInMember() {
        Member member = null;
        // SecurityContextHolder 에 저장되있는 인증 받은 사용자
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            member = memberService.findByUsername(username).orElse(null);
        }
        return member;
    }
}
