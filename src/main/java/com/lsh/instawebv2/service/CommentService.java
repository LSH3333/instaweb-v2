package com.lsh.instawebv2.service;

import com.lsh.instawebv2.domain.Comment;
import com.lsh.instawebv2.domain.Member;
import com.lsh.instawebv2.domain.Page;
import com.lsh.instawebv2.dto.CommentDto;
import com.lsh.instawebv2.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class CommentService {

    private final PageService pageService;
    private final MemberService memberService;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(PageService pageService, MemberService memberService, CommentRepository commentRepository) {
        this.pageService = pageService;
        this.memberService = memberService;
        this.commentRepository = commentRepository;
    }

    /**
     * Comment 는 Page 와 다대일 관계, Member 와 다대일 관계
     * 트랜잭션 내부인 서비스 영역에서 Page, Member 조회해서 Comment 와 관계 맺은 후 레포지토리를 통해 저장
     * @param comment : Comment 객체
     * @param pageId : 댓글이 달린 Page 의 pageId
     *
     */
    public ResponseEntity<String> save(Comment comment, Long pageId, Principal principal) {
        // member
        String username = principal.getName();
        Member member = memberService.findByUsername(username).orElse(null);
        if(member == null) {
            return ResponseEntity.status(HttpStatus.OK).body("Comments uploaded failed!");
        }

        // page
        Page page = pageService.findOne(pageId);

        // comment - page 연관관계
        comment.setPage(page);
        page.addComment(comment);

        // comment - member 연관관계
        comment.setMember(member);
        member.addComment(comment);

        commentRepository.save(comment);
        return ResponseEntity.status(HttpStatus.OK).body("Comments uploaded successfully!");
    }

    public List<CommentDto> findByPageId(Long pageId) {
        List<Comment> comments = commentRepository.findByPageId(pageId);
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentDtoList.add(new CommentDto(comment));
        }
        return commentDtoList;
    }
}
