package com.lsh.instawebv2.service;

import com.lsh.instawebv2.domain.Member;
import com.lsh.instawebv2.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void save(String loginId, String password) {
        memberRepository.save(makeNewMember(loginId, password));
    }

    private Member makeNewMember(String loginId, String password) {
        return new Member(loginId, password);
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
