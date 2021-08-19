package com.jay.instagram.service;

import com.jay.instagram.bean.SearchSchema;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "instagram-search-server")
public interface SearchEngineService {
    @RequestMapping(value = "/search/{query}",
            method = RequestMethod.GET)
    List<Long> search(@PathVariable("query") String query);
}
