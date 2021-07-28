package com.jay.instagram.controller;

import com.alibaba.fastjson.JSONObject;
import com.jay.instagram.bean.Post;
import com.jay.instagram.bean.UserSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/users")
@CrossOrigin
public class UsersController {
    /**
     * @param id 用户id
     * @return {"data" : UserSchema}
     */
    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET)
    @ResponseBody
    public Map<String, UserSchema> getUserInfo(@PathVariable("id") String id) {
        Map<String, UserSchema> userMap = new HashMap<>();
        userMap.put("data", new UserSchema());

        return userMap;
    }

    /**
     *
     * @return {"data", List<Post>}
     */
    @RequestMapping(value = "/feed",
            method = RequestMethod.GET)
    @ResponseBody
    public JSONObject feed() {
        JSONObject retJsonObj = new JSONObject();
        List<Post> noFeedList = new LinkedList<>();
        noFeedList.add(new Post());
        retJsonObj.put("data", noFeedList);
        return retJsonObj;
    }

    /**
     * @return {"data", List<UserScheme>}
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public JSONObject noFeed() {
        JSONObject retJsonObj = new JSONObject();
        List<UserSchema> noFeedList = new LinkedList<>();
        noFeedList.add(new UserSchema());
        retJsonObj.put("data", noFeedList);
        return retJsonObj;
    }
}
