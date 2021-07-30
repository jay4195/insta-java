package com.jay.instagram.service;

import com.jay.instagram.bean.Post;

import java.util.List;

public interface PostService {
    void createPost(Post post);
    Post getPost(Long postId);
    List<Post> getRandomPosts();
}
