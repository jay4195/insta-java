package com.jay.instagram.bean;


import lombok.Data;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Data
public class Post {
    private Long _id = 1L;
    private String caption = "111";
    private List<String> tags = new LinkedList<>();
    private List<String> files = new LinkedList<>();
    // likes
    private Long likesCount = 10010000L;
    // comments
    private List<Comment> comments = new LinkedList<>();
    private Long commentsCount = 123L;
    private Date createdAt = new Date();

    public Post() {
        tags.add("hello");
        files.add("https://img2.baidu.com/it/u=3538454884,1709757134&fm=26&fmt=auto&gp=0.jpg");
    }

}
