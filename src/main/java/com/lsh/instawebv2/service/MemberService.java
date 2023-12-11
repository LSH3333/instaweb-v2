package com.lsh.instawebv2.service;

import com.lsh.instawebv2.domain.Member;
import com.lsh.instawebv2.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(String loginId, String password) {
        memberRepository.save(makeNewMember(loginId, password));
    }

    // "ROLE_USER" 의 role 을 갖는 member 등록
    // password 는 암호화
    private Member makeNewMember(String loginId, String password) {
        return new Member(loginId, passwordEncoder.encode(password), "ROLE_USER");
    }

    /**
     *
     * @param loginId : Member.loginId
     * @return : loginId 를 갖는 Member 가 이미 DB 에 존재한다면 return true
     */
    public boolean checkDuplicationByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId).isPresent();
    }


}
