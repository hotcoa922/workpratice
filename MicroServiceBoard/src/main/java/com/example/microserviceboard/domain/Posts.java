package com.example.microserviceboard.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Posts extends BaseTime {

    private Long id;
    private String title;
    private String content;
    private String authorEmail;  // 작성자 ID

    @Builder
    public Posts(String title, String content, String authorEmail) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorEmail = authorEmail;
    }

}
