package com.lsh.instawebv2.service;

import com.lsh.instawebv2.domain.Member;
import com.lsh.instawebv2.domain.Page;
import com.lsh.instawebv2.repository.MemberRepository;
import com.lsh.instawebv2.repository.PageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Service
@Transactional
@Slf4j
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
        return pageRepository.findById(pageId).orElse(null);
    }

    /**
     * Member-Page 연관관계 맺은 Page 생성
     * @param memberId : Page 소유하는 Member.id
     * @param content : 작성 내용
     * @return : 연관관계 맺기 완료된 Page
     */
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





//    public void pageTest() {
//        org.springframework.data.domain.Page<Page> pages = pageRepository.findAll(PageRequest.of(0, 5));
//
//        for (Page page : pages) {
//            log.info("page = {}", page.getId());
//        }
//    }

}
