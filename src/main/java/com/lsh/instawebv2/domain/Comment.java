package com.lsh.instawebv2.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
public class Comment {
    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    // 댓글 내용
    @Column(columnDefinition = "TEXT")
    private String commentContent;

    // 댓글 작성일
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdTime;

    // 이 Comment 를 소유하는 Page
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "page_id")
    private Page page;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    public Comment() {}

    public Comment(String commentContent) {
        this.commentContent = commentContent;
        this.createdTime = LocalDateTime.now();
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public void setMember(Member member) {
        this.member = member;
    }

}
