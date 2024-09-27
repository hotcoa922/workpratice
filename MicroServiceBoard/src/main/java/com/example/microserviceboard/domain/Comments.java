package com.example.microserviceboard.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Comments extends BaseTime {
    private Long id;
    private Long postId;    //외래키
    private String content;
    private Long authorEmail;

    @Builder
    public Comments(Long postId, String content, Long authorEmail) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.authorEmail = authorEmail;

    }

}
