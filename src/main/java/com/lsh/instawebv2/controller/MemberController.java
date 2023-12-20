package com.lsh.instawebv2.controller;

import com.lsh.instawebv2.domain.Member;
import com.lsh.instawebv2.service.MemberService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * 회원가입 뷰
     * @param member : member 객체 만들어서 뷰로 전달
     * @return : 회원가입 뷰
     */
    @GetMapping("/members")
    public String registerForm(@ModelAttribute("member") Member member) {
        return "members/register";
    }


    @PostMapping("/members")
    public String register(@Valid @ModelAttribute("member") Member member, BindingResult bindingResult, Principal principal) {
        log.info("member = {}, member.Username() = {}, member.getPassword() = {}", member, member.getUsername(), member.getPassword());

        // username 는 영어와 숫자로만 이루어져 있어야 한다
        if (!containOnlyEngAndNum(member.getUsername())) {
            bindingResult.addError(new FieldError("member", "username", member.getUsername(), false, null, null, "영어와 숫자만 가능"));
        }
        // username 는 4글자 이상 10 글자 이하
        if(member.getUsername().length() < 4 || member.getUsername().length() > 10) {
            bindingResult.addError(new FieldError("member", "username", member.getUsername(), false, null, null,"4글자 이상 10글자 이하"));
        }
        // email (OAuth2 아닌 일반 회원의 경우 nickname) 는 영어와 숫자로만 이루어져 있어야 한다
        if (!containOnlyEngAndNum(member.getEmail())) {
            bindingResult.addError(new FieldError("member", "email", member.getEmail(), false, null, null, "영어와 숫자만 가능"));
        }
        // email 은 4글자 이상 10 글자 이하
        if(member.getEmail().length() < 4 || member.getEmail().length() > 10) {
            bindingResult.addError(new FieldError("member", "email", member.getEmail(), false, null, null,"4글자 이상 10글자 이하"));
        }
        // password 는 4글자 이상 10 글자 이하
        if(member.getPassword().length() < 4 || member.getUsername().length() > 10) {
            bindingResult.addError(new FieldError("member", "password", member.getPassword(), false, null, null,"4글자 이상 10글자 이하"));
        }
        // username 는 중복될수 없다
        if (memberService.checkDuplicationByUsername(member.getUsername())) {
            bindingResult.addError(new FieldError("member", "username", member.getUsername(), false, null, null, "이미 존재합니다"));
        }

        // 에러 있을시 되돌아감
        if (bindingResult.hasErrors()) {
            return "members/register";
        }

        // 회원 등록
        memberService.save(member.getUsername(), member.getPassword(), member.getEmail());

        return "redirect:/";
    }


    /**
     * loginId 는 영어 or 숫자여야 한다.
     * 영어도 아니고 숫자도 아닌 아닌 레터가 하나라도 포함되어 있으면 false 리턴
     */
    boolean containOnlyEngAndNum(String loginId) {
        for(int i = 0; i < loginId.length(); i++) {
            char ch = loginId.charAt(i);

            // 영어도 아니고, 숫자도 아니면 return false
            if (Character.UnicodeBlock.of(ch) != Character.UnicodeBlock.BASIC_LATIN && !Character.isDigit(ch)) {
                return false;
            }
        }
        return true;
    }
}
