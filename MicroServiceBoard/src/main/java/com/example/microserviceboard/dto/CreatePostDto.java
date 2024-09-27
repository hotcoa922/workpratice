package com.example.microserviceboard.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class CreatePostDto {
    private String title;
    private String content;

    @Builder
    public CreatePostDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}