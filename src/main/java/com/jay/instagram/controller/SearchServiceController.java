package com.jay.instagram.controller;

import com.jay.instagram.bean.Post;
import com.jay.instagram.service.PostService;
import com.jay.instagram.service.SearchEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchServiceController {
    @Autowired
    SearchEngineService searchEngineService;

    @Autowired
    PostService postService;

    @RequestMapping(value = "/{query}",
            method = RequestMethod.GET)
    List<Post> search(@PathVariable("query") String query) {
        List<Post> postResults = new LinkedList<>();
        List<Long> postIds = searchEngineService.search(query);
        for (Long id : postIds) {
            postResults.add(postService.getPost(id));
        }
        return postResults;
    }

    @RequestMapping(value = "/index/{query}",
            method = RequestMethod.GET)
    List<Long> searchIndexs(@PathVariable("query") String query) {
        return searchEngineService.search(query);
    }
}
