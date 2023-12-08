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

    public Page(){}

    public Page(String content) {
        this.content = content;
    }
}
