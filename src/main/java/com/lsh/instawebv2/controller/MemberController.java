package com.lsh.instawebv2.controller;

import com.lsh.instawebv2.domain.Member;
import com.lsh.instawebv2.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    @GetMapping("/member/register")
    public String registerForm(@ModelAttribute("member") Member member) {
        return "register";
    }

    @PostMapping("/member/register")
    public String register(@Valid @ModelAttribute("member") Member member, BindingResult bindingResult) {
        System.out.println("member = " + member);
        System.out.println("member.getLoginId() = " + member.getLoginId());
        System.out.println("member.getPassword() = " + member.getPassword());

        if (bindingResult.hasErrors()) {
            return "register";
        }


        memberService.save(member.getLoginId(), member.getPassword());

        return "redirect:/";
    }

}
