package com.jay.instagram.dao;

import com.jay.instagram.bean.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    void addPost(Post post);
    void addPostImages(Long postId, String imageUrl);
    void addPostTags(Long postId, String hashTag);

    List<Long> getRandomPosts();
    Post getPost(Long postId);
    List<String> getPostImages(Long postId);
    List<String> getPostTags(Long postId);
}
