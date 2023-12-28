package com.lsh.instawebv2.controller;

import com.lsh.instawebv2.domain.Member;
import com.lsh.instawebv2.domain.Page;
import com.lsh.instawebv2.dto.PasswordChangeDto;
import com.lsh.instawebv2.service.MemberService;
import com.lsh.instawebv2.service.PageService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final PageService pageService;

    @Autowired
    public MemberController(MemberService memberService, PageService pageService) {
        this.memberService = memberService;
        this.pageService = pageService;
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

    /**
     * 로그인한 Member 가 소유한 모든 Page 들 찾아서 뷰로 넘겨준다
     *
     * @return : username 을 갖는 Member 가 작성한 Page 들 랜더링되는 뷰
     */
    @GetMapping("/members/mypage")
    public String userPage(@RequestParam(name = "page", defaultValue = "0") int page, Model model, Principal principal) {
        String username = principal.getName();
        int size = 6;
        Member member = memberService.findByUsername(username).orElse(null);
        // member 가 소유한 page 들
        org.springframework.data.domain.Page<Page> pages = pageService.findByMember(member, PageRequest.of(page, size, Sort.by("createdTime").descending()));

        // page objects
        model.addAttribute("pages", pages);
        // current page
        model.addAttribute("page", page + 1);
        return "members/mypage";
    }

    /**
     * 회원 정보 페이지
     */
    @GetMapping("/members/info")
    public String membersInfo() {
        return "members/info";
    }

    /**
     * 로그인 되어 있는 회원 정보 수정
     */
    @GetMapping("/members/password")
    public String changePasswordForm(@ModelAttribute("PasswordChangeDto")PasswordChangeDto passwordChangeDto) {
        return "members/changePassword";
    }

    @PostMapping("/members/password")
    public String changePassword(@ModelAttribute("PasswordChangeDto")PasswordChangeDto passwordChangeDto, BindingResult bindingResult, Principal principal) {

        // pw 는 4 글자 이상 10 글자 이하
        if(passwordChangeDto.getPassword1().length() < 4 || passwordChangeDto.getPassword1().length() > 10) {
            bindingResult.addError(new FieldError("PasswordChangeDto", "password1", passwordChangeDto.getPassword1(), false, null, null,"4글자 이상 10글자 이하"));
        }
        if(passwordChangeDto.getPassword2().length() < 4 || passwordChangeDto.getPassword2().length() > 10) {
            bindingResult.addError(new FieldError("PasswordChangeDto", "password2", passwordChangeDto.getPassword2(), false, null, null,"4글자 이상 10글자 이하"));
        }
        // pw1 과 pw2 가 다른 경우 에러
        if (!passwordChangeDto.getPassword1().equals(passwordChangeDto.getPassword2())) {
            bindingResult.addError(new ObjectError("pwDiffErr", "입력한 비밀번호가 서로 다릅니다"));
        }

        // 에러 있을시 되돌아감
        if (bindingResult.hasErrors()) {
            return "members/changePassword";
        }

        // 비밀번호 변경
        Member member = memberService.findByUsername(principal.getName()).orElse(null);
        if(member != null) {
            memberService.changePassword(member.getId(), passwordChangeDto.getPassword1());
            logout();
        }

        return "redirect:/";
    }

    /**
     * 로그인 되어 있는 회원 탈퇴
     */
    @GetMapping("/members/resign")
    public ResponseEntity<String> resign(Principal principal) {
        Member member = memberService.findByUsername(principal.getName()).orElse(null);
        if(member != null) {
            memberService.delete(member.getId());

            // authentication 토큰 null 로 만들어서 로그 아웃 처리
            logout();
        }
        return ResponseEntity.ok("member resign success");
    }

    private void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
    }
}
