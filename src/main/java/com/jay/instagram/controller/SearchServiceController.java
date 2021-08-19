package com.jay.instagram.controller;

import com.alibaba.fastjson.JSONObject;
import com.jay.instagram.bean.Post;
import com.jay.instagram.service.PostService;
import com.jay.instagram.service.SearchEngineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/search")
@CrossOrigin
@Slf4j
public class SearchServiceController {
    @Autowired
    SearchEngineService searchEngineService;

    @Autowired
    PostService postService;

    @RequestMapping(value = "/{query}",
            method = RequestMethod.GET)
    public JSONObject search(@PathVariable("query") String query, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        Date startTime = new Date();
        log.info("[Search] {}", query);
        List<Post> postResults = new LinkedList<>();
        List<Long> postIds = searchEngineService.search(query);
        for (Long id : postIds) {
            postResults.add(postService.getPost(id));
        }
        jsonObject.put("data", postResults);
        jsonObject.put("time", System.currentTimeMillis() - startTime.getTime());
        return jsonObject;
    }

    @RequestMapping(value = "/index/{query}",
            method = RequestMethod.GET)
    public List<Long> searchIndexs(@PathVariable("query") String query) {
        return searchEngineService.search(query);
    }
}
