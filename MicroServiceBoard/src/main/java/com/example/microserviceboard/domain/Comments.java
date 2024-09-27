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
    private String authorEmail;

    @Builder
    public Comments(Long id, Long postId, String content, String authorEmail) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.authorEmail = authorEmail;

    }

}
