package com.jay.instagram.controller;

import com.alibaba.fastjson.JSONObject;
import com.jay.instagram.bean.Post;
import com.jay.instagram.bean.User;
import com.jay.instagram.bean.UserSchema;
import com.jay.instagram.service.FileService;
import com.jay.instagram.service.PostService;
import com.jay.instagram.service.TokenService;
import com.jay.instagram.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    public JSONObject getPostInfo(@PathVariable("id") Long id) {
        JSONObject responseJson = new JSONObject();
        responseJson.put("data", postService.getPost(id));
        return responseJson;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public JSONObject createPost(@RequestBody Post post, HttpServletRequest httpServletRequest) {
        System.out.println(post);
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
}
