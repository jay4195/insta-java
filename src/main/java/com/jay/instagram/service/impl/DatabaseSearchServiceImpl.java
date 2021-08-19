package com.jay.instagram.service.impl;

import com.jay.instagram.bean.SearchSchema;
import com.jay.instagram.dao.PostMapper;
import com.jay.instagram.service.DatabaseSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseSearchServiceImpl implements DatabaseSearchService {
    @Autowired
    PostMapper postMapper;

    @Override
    public List<SearchSchema> getAllPost() {
        List<SearchSchema> searchResult = postMapper.searchAllPost();
        for (SearchSchema s : searchResult) {
            List<String> hashTags = postMapper.getPostTags(s.getPostId());
            if (hashTags != null) {
                s.setHashtags(hashTags);
            }
        }
        return searchResult;
    }
}
