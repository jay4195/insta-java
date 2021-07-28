package com.jay.instagram.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
public class Comment {
    @JsonProperty("user")
    private UserSchema userSchema;
    private Post post;
    private String text = "great";
    private Date date = new Date();
}
