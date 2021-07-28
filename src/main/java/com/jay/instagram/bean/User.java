package com.jay.instagram.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    /** picture url */
    private String avatar;
    private String username;
    private String fullname;
    private String email;
    private Long _id;
    private String website;
    private String bio;
}
