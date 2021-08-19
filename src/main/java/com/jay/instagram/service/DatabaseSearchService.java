package com.jay.instagram.service;

import com.jay.instagram.bean.SearchSchema;

import java.util.List;

public interface DatabaseSearchService {
    List<SearchSchema> getAllPost();
}
