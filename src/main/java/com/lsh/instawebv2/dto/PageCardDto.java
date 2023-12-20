package com.lsh.instawebv2.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * home 에 미리보기 보여주기 위해 보내는 Page 정보 dto
 */
@Data
public class PageCardDto {
    private Long id;
    private String title;
    private String content;
    private String memberName; // 작성자 이름
    private LocalDateTime createdTime;
    private String frontImg;
    private String frontText;
    private String pastTime;
}
