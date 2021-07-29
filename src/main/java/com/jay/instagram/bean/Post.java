package com.jay.instagram.bean;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
public class Post {
    @JsonProperty("_id")
    private Long id = 1L;
    private String caption = "111";
    @JsonProperty("isLiked")
    private boolean isLiked = false;
    @JsonProperty("isSaved")
    private boolean isSaved = false;
    @JsonProperty("isMine")
    private boolean isMine = true;
    private List<String> tags = new LinkedList<>();
    private List<String> files = new LinkedList<>();
    // likes
    private Long likesCount = 10010000L;
    // comments
    private List<Comment> comments = new LinkedList<>();
    private Long commentsCount = 123L;
    private Date createdAt = new Date();

}
