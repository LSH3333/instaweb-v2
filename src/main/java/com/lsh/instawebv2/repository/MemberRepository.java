package com.lsh.instawebv2.repository;

import com.lsh.instawebv2.domain.Member;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberRepository {

    private final EntityManager em;

    @Autowired
    public MemberRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Member member) {
        em.persist(member);
    }

    // loginId 로 Member 찾는다
    // 해당 없다면 null 리턴

    /**
     * loginId 를 갖는 Member 를 DB 에서 찾는다
     * @param username : Member.username
     * @return : loginId 를 갖는 Member 가 있다면 Optional 에 담아서 리턴
     */
    public Optional<Member> findByUsername(String username) {

        return em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultList()
                .stream()
                .filter(m -> m.getUsername().equals(username)).findFirst();
    }

}
