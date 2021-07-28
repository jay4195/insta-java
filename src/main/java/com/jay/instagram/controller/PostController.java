package com.jay.instagram.controller;

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

//    /**
//     * @param id post id
//     * @return {"data" : UserSchema}
//     */
//    @RequestMapping(value = "/{id}/comments",
//            method = RequestMethod.GET)
//    @ResponseBody
//    public String getCommentInfo(@PathVariable("id") String id) {
//        return "1";
//    }
}
