package com.example.microserviceboard.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateCommentDto {
    private String content;


    @Builder
    public UpdateCommentDto(String content) {
        this.content = content;
    }
}
