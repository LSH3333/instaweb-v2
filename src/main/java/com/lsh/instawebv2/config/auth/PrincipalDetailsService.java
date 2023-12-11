package com.lsh.instawebv2.config.auth;

import com.lsh.instawebv2.domain.Member;
import com.lsh.instawebv2.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * SecurityConfig 에 설정한 loginProcessingUrl 에 요청이 오면
 * 자동으로 UserDetailsService 빈의 loadUserByUsername() 실행 됨
 * loadUserByUsername 은 UserDetails 를 리턴하는데 이는 AuthenticationProvider 에게 전달되고 이를 기반으로 인증 진행함
 */
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Autowired
    public PrincipalDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    /**
     * security session <- Authentication <- UserDetails <- 사용자 정보
     * @param username
     * @return : UserDetails 타입을 반환하고 UserDetails 는 Authentication 내부에 보관된다.
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> byUsername = memberRepository.findByUsername(username);
        Member member = byUsername.orElse(null);

        if (member != null) {
            return new PrincipalDetails(member);
        } else {
            return null;
        }
    }
}
