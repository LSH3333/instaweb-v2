package com.lsh.instawebv2.controller;

import com.lsh.instawebv2.domain.Comment;
import com.lsh.instawebv2.domain.Member;
import com.lsh.instawebv2.dto.CommentDto;
import com.lsh.instawebv2.service.CommentService;
import com.lsh.instawebv2.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final MemberService memberService;

    @Autowired
    public CommentController(CommentService commentService, MemberService memberService) {
        this.commentService = commentService;
        this.memberService = memberService;
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

    /**
     * comment 삭제
     * @param commentId : 삭제할 comment 의 id
     * @return : 성공 시 httpStatus.OK, 실패시 httpStatus.BAD_REQUEST
     */
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> delete(@PathVariable("commentId") Long commentId, Principal principal) {
        Member member = memberService.findByUsername(principal.getName()).orElse(null);
        // 내가 작성한 댓글이 아니라면 BAD REQUEST 리턴
        if (member == null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("delete", "fail");
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }
        return commentService.delete(commentId, principal.getName());
    }


}
