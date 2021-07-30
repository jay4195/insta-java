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
    private String fullname = "default name";
    private String username = "default username";
    private String email = "sample@email.com";
    private String password = "123456";
    private String avatar;
    private String bio = "No bios";
    private String website = "unknown";
    private List<UserSchema> followers = new LinkedList<>();
    private List<Post> posts = new LinkedList<>();
    private List<Post> savedPosts = new LinkedList<>();
    private Long followersCount = 0L;
    private Long followingCount = 0L;
    private Long postCount = 0L;
    private Date createdAt ;
    public UserSchema() {
        this.posts.add(new Post());
    }
    public UserSchema(User user) {
        this.fullname = user.getFullname();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.bio = user.getBio();
        this.website = user.getWebsite();
    }
}
