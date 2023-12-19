package com.lsh.instawebv2.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
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

    private String email;
    private String provider; // google, kakao ..
    private String providerId;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdTime;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @OrderBy("createdTime desc") // Page 의 생성 시간 기준 오름차순으로 저장
    private List<Page> pages = new ArrayList<>();

    // Member 가 갖고 있는 Comment 들 리스트
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @OrderBy("createdTime desc") // Comment 의 생성 시간 기준 오름차순으로 저장
    private List<Comment> comments = new ArrayList<>();




    public Member() {}

    public Member(String username, String password, String role) {
        this.username = username;
        this .password = password;
        this.role = role;
    }

    // oauth2 register
    public Member(String username, String password, String email, String role, String provider, String providerId, LocalDateTime createdTime) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.createdTime = createdTime;
    }

    public void addPage(Page page) {
        this.pages.add(page);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
//                ", pages=" + pages +
//                ", comments=" + comments +
                '}';
    }
}
