package com.lsh.instawebv2.repository;

import com.lsh.instawebv2.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPageId(Long pageId);
}
