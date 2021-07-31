package com.jay.instagram.service;

import com.jay.instagram.bean.Comment;
import com.jay.instagram.bean.Post;

import java.util.List;

public interface PostService {
    void createPost(Post post);
    Post getPost(Long postId);
    List<Post> getRandomPosts();
    List<Post> getPostByUid(Long uid);

    void deletePost(Long postId);
    void addPostComment(Comment comment);
}
