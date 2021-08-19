package com.jay.instagram.controller;

import com.jay.instagram.service.SearchEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchServiceController {
    @Autowired
    SearchEngineService searchEngineService;

    @RequestMapping(value = "/{query}",
            method = RequestMethod.GET)
    List<Long> search(@PathVariable("query") String query) {
        return searchEngineService.search(query);
    }
}
