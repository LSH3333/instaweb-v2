package com.lsh.instawebv2.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
public class Page {
    @Id
    @GeneratedValue
    @Column(name = "page_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    // Page 를 소유하는 Member
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    // 작성일
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdTime;

    // 미리보기 카드에 보여줄 이미지, base64 로 저장. 이미지 하나도 없을시 "null" 로 저장
    @Lob
    private String frontImg;
    // 미리보기 카드에 보여줄 내용
    private String frontText;

    public Page(){}

    public Page(String content, LocalDateTime createdTime, String frontImg, String frontText) {
        this.content = content;
        this.createdTime = createdTime;
        this.frontImg = frontImg;
        this.frontText = frontText;
    }

    public Page(String content) {
        this.content = content;
    }

    // Page 를 소유하는 Member 연관관계
    public void setMember(Member member) {
        this.member = member;
    }
}
