package com.lsh.instawebv2.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty(message = "Login name can't be empty")
    private String loginId;
    @NotEmpty(message = "Password can't be empty")
    private String password;

//    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
//    @OrderBy("createdTime desc") // Page 의 생성 시간 기준 오름차순으로 저장
//    private List<Page> pages = new ArrayList<>();

    public Member() {}

    public Member(String loginId, String password) {
        this.loginId = loginId;
        this .password = password;
    }



}
