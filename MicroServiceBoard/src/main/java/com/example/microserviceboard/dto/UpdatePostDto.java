package com.example.microserviceboard.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdatePostDto {

    private String title;
    private String content;

    @Builder
    public UpdatePostDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}