package com.lsh.instawebv2.domain;

import jakarta.persistence.*;
import lombok.Getter;

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
//    @ManyToOne
//    @JoinColumn(name = "member_id")
//    private Member member;



    public Page(){}

    public Page(String content) {
        this.content = content;
    }
}
