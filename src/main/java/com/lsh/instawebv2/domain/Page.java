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

    // 제목
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    // Page 를 소유하는 Member
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    // 작성일
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdTime;

    // 미리보기 카드에 보여줄 이미지, base64 로 저장. 이미지 하나도 없을시 null
    @Lob
    private String frontImg;
    // 미리보기 카드에 보여줄 내용
    private String frontText;
    // Page가 생성된지 얼마나 지났는지. "9 min", "2 days" ..
    private String pastTime;

    public Page(){}

    public Page(String content, LocalDateTime createdTime, String frontImg, String frontText, String title) {
        this.content = content;
        this.createdTime = createdTime;
        this.frontImg = frontImg;
        this.frontText = frontText;
        this.title = title;
    }

    public Page(String content) {
        this.content = content;
    }

    // Page 를 소유하는 Member 연관관계
    public void setMember(Member member) {
        this.member = member;
    }

    public void setPastTime(String pastTime) {
        this.pastTime = pastTime;
    }
}
