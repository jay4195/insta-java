package com.jay.instagram.controller;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.jay.instagram.bean.Post;
import com.jay.instagram.bean.User;
import com.jay.instagram.bean.UserSchema;
import com.jay.instagram.config.ServerConfig;
import com.jay.instagram.service.FileService;
import com.jay.instagram.service.PostService;
import com.jay.instagram.service.TokenService;
import com.jay.instagram.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    FileService fileService;
    @Autowired
    TokenService tokenService;
    @Autowired
    PostService postService;
    /**
     * @param username 用户id
     * @return {"data" : UserSchema}
     */
    @RequestMapping(value = "/{username}",
            method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getUserInfo(@PathVariable("username") String username, HttpServletRequest httpServletRequest, HttpServletResponse response) {
        JSONObject retJsonObj = new JSONObject();
        User user = userService.getUserByUsername(username);
        if (user == null) {
            retJsonObj.put("message", "no such user");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return retJsonObj;
        }
        UserSchema userSchema = new UserSchema(user);
        String tokenUserEmail = tokenService.getEmailFromToken(httpServletRequest);
//        log.info("Email: " + tokenUserEmail);
        if (tokenUserEmail.equals(user.getEmail())) {
            userSchema.setMe(true);
        } else {
            userSchema.setMe(false);
        }
        userSchema.setAvatar(fileService.getPictureUrl(user.getAvatar()));
        retJsonObj.put("data", userSchema);
        return retJsonObj;
    }

    /**
     * @return {"data", List<Post>}
     */
    @RequestMapping(value = "/feed",
            method = RequestMethod.GET)
    @ResponseBody
    public JSONObject feed() {
        JSONObject retJsonObj = new JSONObject();
        retJsonObj.put("data", postService.getRandomPosts());
        return retJsonObj;
    }

    /**
     * @return {"data", List<UserScheme>}
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public JSONObject noFeed() {
        JSONObject retJsonObj = new JSONObject();
        List<User> noFeedList = userService.feedUser();
        for (User user : noFeedList) {
            user.setAvatar(fileService.getPictureUrl(user.getAvatar()));
        }
        retJsonObj.put("data", noFeedList);
        return retJsonObj;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public JSONObject editProfile(@Valid @RequestBody User user, HttpServletResponse response) {
        System.out.println(user);
        JSONObject reponseJson = new JSONObject();
        user.setAvatar(fileService.getPictureFileName(user.getAvatar()));
        User checkUserByName = userService.getUserByUsername(user.getUsername());
        //通过email来判断该user是否和当前user一致
        if (checkUserByName != null && !checkUserByName.getEmail().equals(user.getEmail())) {
            reponseJson.put("message", String.format("account with username:%s already exists!",
                    user.getUsername()));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return reponseJson;
        }
        if(!userService.updateUser(user)) {
            reponseJson.put("message", String.format("update account:%s failed!",
                    user.getEmail()));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return reponseJson;
        }
        reponseJson.put("user", user);
        return reponseJson;
    }
}
