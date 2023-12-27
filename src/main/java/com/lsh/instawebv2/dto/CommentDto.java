package com.lsh.instawebv2.dto;

import com.lsh.instawebv2.domain.Comment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private String commentContent;
    private LocalDateTime createdTime;
    private String memberName;
    private String email;

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.commentContent = comment.getCommentContent();
        this.createdTime = comment.getCreatedTime();
        this.memberName = comment.getMember().getUsername();
        this.email = comment.getMember().getEmail();
    }
}
