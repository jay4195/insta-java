package com.jay.instagram.service.impl;

import com.jay.instagram.bean.Comment;
import com.jay.instagram.bean.Post;
import com.jay.instagram.bean.User;
import com.jay.instagram.dao.PostMapper;
import com.jay.instagram.service.FileService;
import com.jay.instagram.service.PostService;
import com.jay.instagram.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class PostServiceImpl implements PostService {
    private static final int hashtagLengthLimit = 50;
    private static final int captionLengthLimit = 150;
    @Autowired
    PostMapper postMapper;
    @Autowired
    FileService fileService;
    @Autowired
    UserService userService;

    @Override
    public void createPost(Post post) {
        //过滤不出来hashtag再过滤一次
        String caption = post.getCaption();
        List<String> hashTags = post.getTags();
        List<String> captions = Arrays.asList(caption.split("\n"));
        StringBuffer captionBuilder = new StringBuffer();
        for (int i = 0; i < captions.size(); i++) {
            if (captions.get(i).startsWith("#")) {
                hashTags.add(captions.get(i));
            } else {
                if (i != captions.size() - 1) {
                    captionBuilder.append(captions.get(i)).append(" ");
                } else {
                    captionBuilder.append(captions.get(i));
                }
            }
        }
        caption = captionBuilder.toString();
        if (caption.length() > captionLengthLimit) {
            caption = caption.substring(0, captionLengthLimit);
        }
        post.setCaption(caption);
        postMapper.addPost(post);
        Long postId = post.getId();
        List<String> imageUrls = post.getFiles();
        hashTags = post.getTags();
        for (String url : imageUrls) {
            postMapper.addPostImages(postId, fileService.getPictureFileName(url));
        }
        for (String tag : hashTags) {
            if (tag.length() > hashtagLengthLimit) {
                tag = tag.substring(0, hashtagLengthLimit);
            }
            postMapper.addPostTags(postId, tag);
        }
    }

    @Override
    public Post getPost(Long postId) {
        Post post = postMapper.getPost(postId);
        if (post == null) {
            return null;
        }
        Long userid = post.getUser().getId();
        User user = userService.getUserById(userid);
        user.setAvatar(fileService.getPictureUrl(user.getAvatar()));
        post.setUser(user);
        List<String> imageNames = postMapper.getPostImages(postId);
        List<String> imageUrls = new LinkedList<>();
        for (String name : imageNames) {
            imageUrls.add(fileService.getPictureUrl(name));
        }
        List<Comment> comments = getPostComments(postId);
        Map<Long, User> commentUserMap = new HashMap<>();
        for (Comment cmt : comments) {
            User whoMakeComment;
            if (!commentUserMap.containsKey(cmt.getUid())) {
                whoMakeComment = userService.getUserById(cmt.getUid());
                whoMakeComment.setAvatar(fileService.getPictureUrl(whoMakeComment.getAvatar()));
                commentUserMap.put(cmt.getUid(), whoMakeComment);
            } else {
                whoMakeComment = commentUserMap.get(cmt.getUid());
            }
            cmt.setUser(whoMakeComment);
        }
        post.setLikesCount(postMapper.getPostLikeNumbers(postId));
        post.setCommentsCount((long) comments.size());
        post.setComments(comments);
        post.setFiles(imageUrls);
        post.setTags(postMapper.getPostTags(postId));
        return post;
    }

    @Override
    public List<Post> getRandomPosts() {
        List<Long> postIds = postMapper.getRandomPosts();
        List<Post> posts = new LinkedList<>();
        for (Long postId : postIds) {
            posts.add(getPost(postId));
        }
        return posts;
    }

    @Override
    public List<Post> getPostByUid(Long uid) {
        List<Long> postIds = postMapper.getPostByUid(uid);
        List<Post> posts = new LinkedList<>();
        for (Long postId : postIds) {
            posts.add(getPost(postId));
        }
        return posts;
    }

    @Override
    public void deletePost(Long postId) {
        List<String> fileNames = postMapper.getPostImages(postId);
        int totalNum = fileNames.size();
        int deletedNum = 0;
        for (String file : fileNames) {
            if (fileService.deletePicture(file)) {
                deletedNum++;
            }
        }
        log.info("deleted: {} total: {}", deletedNum, totalNum);
        postMapper.deletePost(postId);
    }

    @Override
    public void addPostComment(Comment comment) {
        postMapper.addPostComment(comment);
    }

    @Override
    public void toggleLike(Long postId, Long uid) {
        if (postMapper.findToggleLike(postId, uid) > 0) {
            postMapper.toggleUnLike(postId, uid);
            log.info("[Unlike] postId:{}", postId);
        } else {
            postMapper.toggleLike(postId, uid);
            log.info("[Like] postId:{}", postId);
        }
    }

    @Override
    public boolean likeStatus(Long postId, Long uid) {
        return postMapper.findToggleLike(postId, uid) > 0;
    }

    @Override
    public void toggleSave(Long postId, Long uid) {
        if (postMapper.findToggleSave(postId, uid) > 0) {
            postMapper.toggleUnSave(postId, uid);
            log.info("[UnSave] postId:{}", postId);
        } else {
            postMapper.toggleSave(postId, uid);
            log.info("[Save] postId:{}", postId);
        }
    }

    @Override
    public boolean saveStatus(Long postId, Long uid) {
        return postMapper.findToggleSave(postId, uid) > 0;
    }

    @Override
    public List<Post> getSavedPosts(Long uid) {
        List<Long> savedPostIds = postMapper.getAllSavePosts(uid);
        List<Post> savedPosts = new LinkedList<>();
        for (Long postId : savedPostIds) {
            savedPosts.add(getPost(postId));
        }
        return savedPosts;
    }

    public List<Comment> getPostComments(Long postId) {
        return postMapper.getPostComments(postId);
    }

    public Long getPostNumbers(Long uid) {
        return postMapper.getPostNumbers(uid);
    }
}
