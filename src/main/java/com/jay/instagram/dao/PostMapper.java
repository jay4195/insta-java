package com.jay.instagram.dao;

import com.jay.instagram.bean.Comment;
import com.jay.instagram.bean.Post;
import com.jay.instagram.bean.SearchSchema;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    void addPost(Post post);
    void addPostImages(Long postId, String imageUrl);
    void addPostTags(Long postId, String hashTag);

    List<Long> getRandomPosts();
    List<Long> getPostByUid(Long uid);
    Post getPost(Long postId);

    List<SearchSchema> searchAllPost();
    List<String> getPostImages(Long postId);
    List<String> getPostTags(Long postId);
    Long getPostNumbers(Long uid);
    void deletePost(Long id);

    void addPostComment(Comment comment);
    List<Comment> getPostComments(Long postId);
    Long getCommentsCount(Long postId);


    Long findToggleLike(Long postId, Long uid);
    void toggleLike(Long postId, Long uid);
    void toggleUnLike(Long postId, Long uid);
    Long getPostLikeNumbers(Long postId);

    Long findToggleSave(Long postId, Long uid);
    void toggleSave(Long postId, Long uid);
    void toggleUnSave(Long postId, Long uid);
    List<Long> getAllSavePosts(Long uid);
}
