package com.lsh.instawebv2.service;

import com.lsh.instawebv2.domain.Page;
import com.lsh.instawebv2.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PageService {

    private final PageRepository pageRepository;

    @Autowired
    public PageService(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    public void save(String content) {
        Page page = new Page(content);
        pageRepository.save(page);
    }

    public Page findOne(Long pageId) {
        return pageRepository.findOne(pageId);
    }
}
