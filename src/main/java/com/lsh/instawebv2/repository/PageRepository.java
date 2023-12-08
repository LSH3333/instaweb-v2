package com.lsh.instawebv2.repository;

import com.lsh.instawebv2.domain.Page;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;

@Repository
public class PageRepository {

    private final EntityManager em;

    @Autowired
    public PageRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Page page) {
        em.persist(page);
    }

    /**
     * pageId 와 일치하는 Page 객체 1개 리턴
     * @param pageId : pageId
     * @return : pageId 와 일치하는 Page 객체 1개
     */
    public Page findOne(Long pageId) {
        return em.find(Page.class, pageId);
    }

}
