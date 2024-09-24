package com.example.microserviceboard.domain;

public class Comments {
    private Long id;
    private Long postId;    //외래키
    private String content;
    private Long authorId;

}
