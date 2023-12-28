package com.lsh.instawebv2.service;

import com.lsh.instawebv2.domain.Member;
import com.lsh.instawebv2.domain.Page;
import com.lsh.instawebv2.repository.MemberRepository;
import com.lsh.instawebv2.repository.PageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

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

    public void delete(Long id) {
        Page page = findOne(id);
        if(page != null) {
            pageRepository.delete(page);
        }
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
    public void createPageForMember(Long memberId, String content, String frontImg, String frontText, String title) {
        Page page = new Page(content, LocalDateTime.now(), frontImg, frontText, title);
        Member member = memberRepository.findById(memberId);
        // 연관관계
        page.setMember(member);
        member.addPage(page);
        // 레포지토리에 저장
        pageRepository.save(page);
    }

    /**
     *  사용자가 글 수정으로 글의 데이터 수정함
     * @param pageId
     * @param content
     * @param frontImg
     * @param frontText
     * @param title
     */
    public ResponseEntity<String> updatePage(Long pageId, String content, String frontImg, String frontText, String title) {
        Optional<Page> page = pageRepository.findById(pageId);
        if (page.isPresent()) {
            page.get().update(content, frontImg, frontText, title);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Files uploaded successfully!");
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Files uploaded failed!");
        }
    }

    public org.springframework.data.domain.Page<Page> findByMember(Member member, Pageable pageable) {
        return pageRepository.findByMember(member, pageable);
    }

    /**
     * Page 가 member 가 작성한 글이 맞는지 확인
     * @param id : pageId
     * @param member : 인증된 회원
     */
    public Page findByIdAndMember(Long id, Member member) {
        return pageRepository.findByIdAndMember(id, member);
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
