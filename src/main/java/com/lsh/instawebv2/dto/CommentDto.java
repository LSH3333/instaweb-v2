package com.lsh.instawebv2.dto;

import com.lsh.instawebv2.domain.Comment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private String commentContent;
    private LocalDateTime createdTime;
    private String memberName;

    public CommentDto(Comment comment) {
        this.commentContent = comment.getCommentContent();
        this.createdTime = comment.getCreatedTime();
        this.memberName = comment.getMember().getUsername();
    }
}
