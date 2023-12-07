package com.lsh.instawebv2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/page/create")
    public String createPage() {
        return "createPage";
    }

    @GetMapping("/page/view")
    public String viewPage() {
        return "viewPage";
    }

    @PostMapping("/page/upload")
    public ResponseEntity<String> uploadPage(@RequestParam("content") String content) {
        System.out.println("content = " + content);

        String message;
        message = "Files uploaded successfully!";
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}
