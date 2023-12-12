package com.lsh.instawebv2.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty(message = "비어있을수 없습니다")
    private String username;
    @NotEmpty(message = "비어있을수 없습니다")
    private String password;

    private String role; // ROLE_USER, ROLE_ADMIN

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @OrderBy("createdTime desc") // Page 의 생성 시간 기준 오름차순으로 저장
    private List<Page> pages = new ArrayList<>();

    public Member() {}

    public Member(String username, String password, String role) {
        this.username = username;
        this .password = password;
        this.role = role;
    }

    public void addPage(Page page) {
        this.pages.add(page);
    }


}
