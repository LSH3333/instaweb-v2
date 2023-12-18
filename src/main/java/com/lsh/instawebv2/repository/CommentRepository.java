package com.lsh.instawebv2.repository;

import com.lsh.instawebv2.domain.Comment;
import com.lsh.instawebv2.domain.Page;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepository {

    private final EntityManager em;

    @Autowired
    public CommentRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Comment comment) {
        em.persist(comment);
    }

    /**
     * 주어진 Page 와 연관관계에 있는 Comment 찾는다
     * Comment 는 Page 에 @OrderBy("createdTime desc") 로 저장됨
     * @param page : 댓글을 검색해야 하는 Page
     * @return : Page 와 연관관계 있는 Comment 의 리스트 (createdTime 기준 내림차순)
     */
    public List<Comment> findByPageId(Page page) {
        return page.getComments();
    }
}
