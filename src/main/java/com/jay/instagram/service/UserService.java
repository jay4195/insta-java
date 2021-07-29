package com.jay.instagram.service;

import com.jay.instagram.bean.User;

import java.util.List;

public interface UserService {
    void addUser(User user);
    User getUserByEmail(String email);
    User getUserByUsername(String userName);
    boolean updateUser(User user);
    List<User> feedUser();
}
