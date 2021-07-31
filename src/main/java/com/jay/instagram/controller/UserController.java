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
        if (tokenUserEmail.equals(user.getEmail())) {
            userSchema.setMe(true);
        } else {
            userSchema.setMe(false);
        }
        userSchema.setAvatar(fileService.getPictureUrl(user.getAvatar()));
        List<Post> posts = postService.getPostByUid(user.getId());
        List<Post> savedPosts = postService.getSavedPosts(user.getId());
        List<User> followers = userService.getFollowers(user.getId());
        List<User> followings = userService.getFollowingUsers(user.getId());
        List<UserSchema> followerSchema = new LinkedList<>();
        List<UserSchema> followingSchema = new LinkedList<>();
        for (User tempUser : followers) {
            UserSchema schema = new UserSchema(tempUser);
            schema.setFollowingStatus(userService.followStatus(user.getId(), schema.getId()));
            followerSchema.add(schema);
        }
        for (User tempUser : followings) {
            UserSchema schema = new UserSchema(tempUser);
            schema.setFollowingStatus(true);
            followingSchema.add(schema);
        }
        userSchema.setPosts(posts);
        userSchema.setSavedPosts(savedPosts);
        userSchema.setPostCount((long) posts.size());
        userSchema.setFollowers(followerSchema);
        userSchema.setFollowing(followingSchema);
        userSchema.setFollowingCount((long) followingSchema.size());
        userSchema.setFollowersCount((long) followerSchema.size());
        retJsonObj.put("data", userSchema);
        return retJsonObj;
    }

    /**
     * @return {"data", List<Post>}
     */
    @RequestMapping(value = "/feed",
            method = RequestMethod.GET)
    @ResponseBody
    public JSONObject feed(HttpServletRequest httpServletRequest) {
        JSONObject retJsonObj = new JSONObject();
        //log.info(postService.getRandomPosts().toString());
        List<Post> posts = postService.getRandomPosts();
        String tokenUserEmail = tokenService.getEmailFromToken(httpServletRequest);
        User tokenUser = userService.getUserByEmail(tokenUserEmail);
        for (Post post : posts) {
            if (post.getUser().getEmail().equals(tokenUserEmail)) {
                post.setMine(true);
            } else {
                post.setMine(false);
            }
            post.setLiked(postService.likeStatus(post.getId(), tokenUser.getId()));
            post.setSaved(postService.saveStatus(post.getId(), tokenUser.getId()));
        }
        retJsonObj.put("data", posts);
        return retJsonObj;
    }

    /**
     * @return {"data", List<UserSchema>}
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public JSONObject RecommendUsers(HttpServletRequest httpServletRequest) {
        JSONObject retJsonObj = new JSONObject();
        List<User> originFeedList = userService.feedUser();
        String tokenUserEmail = tokenService.getEmailFromToken(httpServletRequest);
        User tokenUser = userService.getUserByEmail(tokenUserEmail);
        List<UserSchema> cleanedFeedList = new LinkedList<>();
        for (User user : originFeedList) {
            if (!user.getEmail() .equals(tokenUserEmail)) {
                user.setAvatar(fileService.getPictureUrl(user.getAvatar()));
                UserSchema userSchema = new UserSchema(user);
                userSchema.setFollowingStatus(userService.followStatus(user.getId(), tokenUser.getId()));
                cleanedFeedList.add(userSchema);
                log.info("Feed User Id: {}", user.getId());
            }
        }
        retJsonObj.put("data", cleanedFeedList);
        return retJsonObj;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public JSONObject editProfile(@Valid @RequestBody User user, HttpServletResponse response) {
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

    /** follow controller */
    @RequestMapping(value = "/{userId}/follow",
            method = RequestMethod.GET)
    @ResponseBody
    public JSONObject follow(@PathVariable("userId") Long userId,
                             HttpServletRequest httpServletRequest,
                             HttpServletResponse response) {
        JSONObject reponseJson = new JSONObject();
        String tokenUserEmail = tokenService.getEmailFromToken(httpServletRequest);
        User currentUser = userService.getUserByEmail(tokenUserEmail);
        if (currentUser.getId() == userId) {
            reponseJson.put("message", "You can't follow youself!");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return reponseJson;
        } else {
            userService.followUser(userId, currentUser.getId());
        }
        reponseJson.put("message", "ok!");
        return reponseJson;
    }

    @RequestMapping(value = "/{userId}/unfollow",
            method = RequestMethod.GET)
    @ResponseBody
    public JSONObject unFollow(@PathVariable("userId") Long userId,
                             HttpServletRequest httpServletRequest,
                             HttpServletResponse response) {
        JSONObject reponseJson = new JSONObject();
        String tokenUserEmail = tokenService.getEmailFromToken(httpServletRequest);
        User currentUser = userService.getUserByEmail(tokenUserEmail);
        if (currentUser.getId() == userId) {
            reponseJson.put("message", "You can't follow youself!");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return reponseJson;
        } else {
            userService.unFollowUser(userId, currentUser.getId());
        }
        reponseJson.put("message", "ok!");
        return reponseJson;
    }

}
