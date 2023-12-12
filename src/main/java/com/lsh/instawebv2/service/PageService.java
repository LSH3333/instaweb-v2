package com.lsh.instawebv2.service;

import com.lsh.instawebv2.domain.Member;
import com.lsh.instawebv2.domain.Page;
import com.lsh.instawebv2.repository.MemberRepository;
import com.lsh.instawebv2.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class PageService {

    private final PageRepository pageRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public PageService(PageRepository pageRepository, MemberRepository memberRepository) {
        this.pageRepository = pageRepository;
        this.memberRepository = memberRepository;
    }

    public void save(String content) {
        Page page = new Page(content);
        pageRepository.save(page);
    }

    public Page findOne(Long pageId) {
        return pageRepository.findOne(pageId);
    }

    public Page createPageForMember(Long memberId, String content) {
        Page page = new Page(content, LocalDateTime.now());
        Member member = memberRepository.findById(memberId);
        // 연관관계
        page.setMember(member);
        member.addPage(page);
        // 레포지토리에 저장
        pageRepository.save(page);
        return page;
    }
}
