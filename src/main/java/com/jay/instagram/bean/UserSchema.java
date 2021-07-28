package com.jay.instagram.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Data
public class UserSchema {
    @JsonProperty("isMe")
    private boolean isMe = true;
    private String fullname = "Zhiyuan Wang";
    private String username = "jay4195";
    private String email = "jay4195@qq.com";
    private String password = "123456";
    private String avatar = "https://img.iplaysoft.com/wp-content/uploads/2019/free-images/free_stock_photo.jpg!0x0.webp";
    private String bio = "life is like a box of chocolate";
    private String website = "https://github.com/jay4195";
    private List<UserSchema> followers = new LinkedList<>();
    private List<Post> posts = new LinkedList<>();
    private int followersCount = 10;
    private int followingCount = 9;
    private int postCount = 8;
    private Date createdAt = new Date();

    public UserSchema() {
        this.posts.add(new Post());
    }
}
