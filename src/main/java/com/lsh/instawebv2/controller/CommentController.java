package com.lsh.instawebv2.controller;

import com.lsh.instawebv2.domain.Comment;
import com.lsh.instawebv2.domain.Member;
import com.lsh.instawebv2.dto.CommentDto;
import com.lsh.instawebv2.service.CommentService;
import com.lsh.instawebv2.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 댓글 작성
     * @param pageId
     * @param commentContent
     * @param principal
     * @return
     */
    @PostMapping("/comments")
    public ResponseEntity<String> comments(@RequestParam("pageId") String pageId,
                                           @RequestParam("comment") String commentContent,
                                           Principal principal) {

        log.info("pageId = {}", pageId);
        log.info("comment = {}", commentContent);
        Comment comment = new Comment(commentContent);
        return commentService.save(comment, Long.parseLong(pageId), principal);
    }

    /**
     * ajax 요청 받으면 Page 에 속한 Comment 들 정보 wrap 해서 클라이언트로 보냄
     */
    @GetMapping("/pages/{pageId}/comments")
    public  ResponseEntity<List<CommentDto>> getComments(@PathVariable("pageId") Long pageId) {
        List<CommentDto> comments = commentService.findByPageId(pageId);
        return ResponseEntity.ok(comments);
    }

}
