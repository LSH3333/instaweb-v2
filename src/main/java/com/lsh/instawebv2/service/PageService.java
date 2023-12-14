package com.lsh.instawebv2.service;

import com.lsh.instawebv2.domain.Member;
import com.lsh.instawebv2.domain.Page;
import com.lsh.instawebv2.repository.MemberRepository;
import com.lsh.instawebv2.repository.PageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
    public Page createPageForMember(Long memberId, String content, String frontImg, String frontText) {
        Page page = new Page(content, LocalDateTime.now(), frontImg, frontText);
        Member member = memberRepository.findById(memberId);
        // 연관관계
        page.setMember(member);
        member.addPage(page);
        // 레포지토리에 저장
        pageRepository.save(page);
        return page;
    }

    public org.springframework.data.domain.Page<Page> findByMember(Member member, Pageable pageable) {
        return pageRepository.findByMember(member, pageable);
    }


    /**
     * Page가 생성된지 얼마나 지났는지 계산
     * @param createdTime : Page 생성 시간
     * @return : "2 days", "10 mins" 등 Page가 생성된지 얼마나 지났는지 계산후 String 형태로 리턴
     */
    public static String getTime(LocalDateTime createdTime) {
        // 현재 시간
        LocalDateTime currentTime = LocalDateTime.now();

        // 차이 계산
        long minutesDifference = ChronoUnit.MINUTES.between(createdTime, currentTime);
        long hoursDifference = ChronoUnit.HOURS.between(createdTime, currentTime);
        long daysDifference = ChronoUnit.DAYS.between(createdTime, currentTime);

        String type = "mins";
        long diff = minutesDifference;
        if(minutesDifference > 60) {
            type = "hours";
            diff = hoursDifference;
            if(hoursDifference > 24) {
                type = "days";
                diff = daysDifference;
            }
        }

        return Long.toString(diff) + ' ' + type;
    }




}
