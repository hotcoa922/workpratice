package com.example.microserviceboard.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class UpdateCommentDto {
    private Long id;
    private String content;
    private Long authorId;
    private Long postId;

    @Builder
    public UpdateCommentDto(Long id, Long postId, String content, Long authorId) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.authorId = authorId;
    }
}
