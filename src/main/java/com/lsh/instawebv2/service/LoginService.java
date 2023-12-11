package com.lsh.instawebv2.service;

import com.lsh.instawebv2.domain.Member;
import com.lsh.instawebv2.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginService {

    private final MemberRepository memberRepository;

    @Autowired
    public LoginService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    /**
     * loginId 를 갖는 Member 를 찾아 password 와 비교해 일치하는지 확인
     * @param loginId : Member.loginId
     * @param password : Member.password
     * @return : 로그인 성공시 로그인 성공한 Member return, else null
     */
    public Member login(String loginId, String password) {
        return memberRepository.findByUsername(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }

}
