package com.example.microserviceboard.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class UpdatePostDto {
    private long id;
    private String title;
    private String content;
    private Long authorId;

    @Builder
    public UpdatePostDto(long id, String title, String content, Long authorId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
    }
}