package com.lsh.instawebv2.repository;

import com.lsh.instawebv2.domain.Member;
import com.lsh.instawebv2.domain.Page;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

//@Repository
public interface PageRepository extends JpaRepository<Page, Long> {

    org.springframework.data.domain.Page<Page> findByCreatedTime(LocalDateTime createdTime, Pageable pageable);

    org.springframework.data.domain.Page<Page> findByMember(Member member, Pageable pageable);

    Page findByIdAndMember(Long id, Member member);

//    private final EntityManager em;
//
//    @Autowired
//    public PageRepository(EntityManager em) {
//        this.em = em;
//    }
//
//    public void save(Page page) {
//        em.persist(page);
//    }
//
//    /**
//     * pageId 와 일치하는 Page 객체 1개 리턴
//     * @param pageId : pageId
//     * @return : pageId 와 일치하는 Page 객체 1개
//     */
//    public Page findOne(Long pageId) {
//        return em.find(Page.class, pageId);
//    }

}
