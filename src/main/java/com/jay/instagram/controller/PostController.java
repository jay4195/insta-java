package com.jay.instagram.controller;

import com.alibaba.fastjson.JSONObject;
import com.jay.instagram.bean.Post;
import com.jay.instagram.bean.UserSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/posts")
@CrossOrigin
public class PostController {
    /**
     * @param id post id
     * @return {"data" : UserSchema}
     */
    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Post> getPostInfo(@PathVariable("id") String id) {
        Map<String, Post> retPost = new HashMap<>();
        retPost.put("data", new Post());
        return retPost;
    }


    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String createPost(@RequestBody JSONObject jsonObject) {
        System.out.println(jsonObject);
        return "1";
    }
}
