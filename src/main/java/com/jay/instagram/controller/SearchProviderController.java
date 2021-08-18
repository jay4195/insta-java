package com.jay.instagram.controller;

import com.jay.instagram.bean.SearchSchema;
import com.jay.instagram.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@EnableEurekaClient
@RequestMapping("/search-schema")
public class SearchProviderController {
    @Autowired
    SearchService searchService;

    @RequestMapping (value = "/get-all-post",
            method = RequestMethod.GET)
    public List<SearchSchema> getAllPost() {
        return searchService.getAllPost();
    }
}
