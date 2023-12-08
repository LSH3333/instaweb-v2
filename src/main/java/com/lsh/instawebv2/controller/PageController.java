package com.lsh.instawebv2.controller;

import com.lsh.instawebv2.domain.Page;
import com.lsh.instawebv2.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class PageController {

    private final PageService pageService;

    public PageController(PageService pageService) {
        this.pageService = pageService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/page/create")
    public String createPage() {
        return "createPage";
    }

    @GetMapping("/page/view")
    public String viewPage(Model model) {
        Page page = pageService.findOne(1L);
        System.out.println("page = " + page);
        model.addAttribute("page", page);
        return "viewPage";
    }

    @PostMapping("/page/upload")
    public ResponseEntity<String> uploadPage(@RequestParam("content") String content) {
        System.out.println("content = " + content);

        pageService.save(content);

        String message;
        message = "Files uploaded successfully!";
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

}
