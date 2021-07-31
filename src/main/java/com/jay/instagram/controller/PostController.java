package com.jay.instagram.controller;

import com.alibaba.fastjson.JSONObject;
import com.jay.instagram.bean.Comment;
import com.jay.instagram.bean.Post;
import com.jay.instagram.bean.User;
import com.jay.instagram.service.FileService;
import com.jay.instagram.service.PostService;
import com.jay.instagram.service.TokenService;
import com.jay.instagram.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


@Slf4j
@Controller
@RequestMapping("/posts")
@CrossOrigin
public class PostController {
    @Autowired
    UserService userService;
    @Autowired
    FileService fileService;
    @Autowired
    TokenService tokenService;
    @Autowired
    PostService postService;
    /**
     * @param id post id
     * @return {"data" : UserSchema}
     */
    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getPostInfo(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        JSONObject responseJson = new JSONObject();
        String tokenUserEmail = tokenService.getEmailFromToken(httpServletRequest);
        Post post = postService.getPost(id);
        if (post.getUser().getEmail().equals(tokenUserEmail)) {
            post.setMine(true);
        } else {
            post.setMine(false);
        }
        responseJson.put("data", post);
        return responseJson;
    }

    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE)
    public void deletePost(@PathVariable("id") Long id, HttpServletRequest httpServletRequest, HttpServletResponse response) {
        JSONObject responseJson = new JSONObject();
        String tokenUserEmail = tokenService.getEmailFromToken(httpServletRequest);
        Post post = postService.getPost(id);
        if (post.getUser().getEmail().equals(tokenUserEmail)) {
            postService.deletePost(id);
        } else {
            responseJson.put("message", "You Don't have the right to delete!");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        log.info("delete post id: {}", id);
    }

    @RequestMapping(value = "/avatar/{fileName}",
            method = RequestMethod.DELETE)
    public void deleteAvatar(@PathVariable String fileName) {
        log.info("Delete avatar {}", fileName);
        fileService.deletePicture(fileName);
    }

    @RequestMapping(
            method = RequestMethod.GET)
    public String explore() {
        return "redirect:/users/feed";
    }


    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public JSONObject createPost(@RequestBody Post post, HttpServletRequest httpServletRequest) {
        log.info("New Post Created! {}" , post.toString());
        if (post.getCaption().length() > 150) {
            post.setCaption(post.getCaption().substring(0, 150));
        }
        String tokenUserEmail = tokenService.getEmailFromToken(httpServletRequest);
        User user = userService.getUserByEmail(tokenUserEmail);
        user.setAvatar(fileService.getPictureUrl(user.getAvatar()));
        post.setUser(user);
        post.setMine(true);
        post.setCreatedAt(new Date());
        JSONObject responseJson = new JSONObject();
        postService.createPost(post);
        responseJson.put("data", post);
        return responseJson;
    }

    @RequestMapping(value = "/{id}/comments", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject postComments(@PathVariable(value = "id") Long postId,
                               @RequestBody JSONObject commentJson, HttpServletRequest httpServletRequest) {
        JSONObject responseJson = new JSONObject();
        String text = (String) commentJson.get("text");
        if (text.length() > 150) {
            text = text.substring(0, 150);
        }
        String tokenUserEmail = tokenService.getEmailFromToken(httpServletRequest);
        User user = userService.getUserByEmail(tokenUserEmail);
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUid(user.getId());
        comment.setComment(text);
        comment.setCreatedAt(new Date());
        postService.addPostComment(comment);
        user.setAvatar(fileService.getPictureUrl(user.getAvatar()));
        comment.setUser(user);
        responseJson.put("data", comment);
        log.info("[Comment] User: {} Text: {}", user.getUsername(), text);
        return responseJson;
    }
}
