package com.example.microserviceboard.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class UpdatePostDto {
    private long id;
    private String title;
    private String content;
    private String authorEmail;

    @Builder
    public UpdatePostDto(long id, String title, String content, String authorEmail) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorEmail = authorEmail;
    }
}