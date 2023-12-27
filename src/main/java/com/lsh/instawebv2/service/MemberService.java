package com.lsh.instawebv2.service;

import com.lsh.instawebv2.domain.Member;
import com.lsh.instawebv2.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public void save(String username, String password, String email) {
        memberRepository.save(makeNewMember(username, password, email));
    }

    public void save(Member member) {
        memberRepository.save(member);
    }

    public void delete(Long id) {
        memberRepository.delete(id);
    }

    // "ROLE_USER" 의 role 을 갖는 member 등록
    // password 는 암호화
    private Member makeNewMember(String username, String password, String email) {
        return new Member(username, passwordEncoder.encode(password), "ROLE_USER", email);
    }

    /**
     *
     * @param username : Member.username
     * @return : username 를 갖는 Member 가 이미 DB 에 존재한다면 return true
     */
    public boolean checkDuplicationByUsername(String username) {
        return memberRepository.findByUsername(username).isPresent();
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

}
