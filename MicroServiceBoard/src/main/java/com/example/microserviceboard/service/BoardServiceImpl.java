package com.example.microserviceboard.service;


import com.example.microserviceboard.domain.Posts;
import com.example.microserviceboard.mapper.PostMapper;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    private final PostMapper postMapper;

    @Autowired
    public BoardServiceImpl(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    @Override
    public List<Posts> findAllPosts() {
        return postMapper.findAll();
    }

    @Override
    public Posts findPostById(Long id) {
        return postMapper.findById(id);
    }

    @Override
    public void createPost(Posts post) {
        postMapper.insert(post);
    }

    @Override
    public void updatePost(Posts post) {
        postMapper.update(post);
    }

    @Override
    public void deletePost(Long id) {
        postMapper.delete(id);
    }
}

