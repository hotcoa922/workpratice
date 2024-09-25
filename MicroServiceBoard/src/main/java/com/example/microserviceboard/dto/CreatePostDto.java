package com.example.microserviceboard.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class CreatePostDto {
    private String title;
    private String content;
    private Long authorId;

    @Builder
    public CreatePostDto(String title, String content, Long authorId) {
        this.title = title;
        this.content = content;
        this.authorId = authorId;
    }
}