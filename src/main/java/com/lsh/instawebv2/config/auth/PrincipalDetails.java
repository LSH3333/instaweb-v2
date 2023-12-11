package com.lsh.instawebv2.config.auth;

import com.lsh.instawebv2.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


/**
 * security 가 SecurityConfig 에 설정한 loginProcessingUrl 에 요청이 오면 필터로 낚아채서 로그인 진행시켜줌
 * 로그인 진행 완료되면 security session 을 만들어 Security ContextHolder 에 보관한다
 * 그런데 보관되는 오브젝트의 타입이 Authentication 타입이다
 * 그리고 Authentication 내부에 User 의 정보가 보관된다
 * User 의 타입은 UserDetails 타입으로 저장된다
 */
public class PrincipalDetails implements UserDetails {

    private Member member;

    public PrincipalDetails(Member member) {
        this.member = member;
    }

    // 해당 User 의 권한을 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> collect = new ArrayList<>();

        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getRole();
            }
        });

        return collect;
    }


    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    // 계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠금 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정 비밀번호 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 활성화 여부
    @Override
    public boolean isEnabled() {
        return true;
    }
}
