package com.jay.instagram.service;

import com.jay.instagram.bean.User;

import java.util.List;

public interface UserService {
    void addUser(User user);
    String getPasswordById(Long id);
    String getPasswordByEmail(String email);

    User getUserById(Long id);
    User getUserByEmail(String email);
    User getUserByUsername(String userName);
    boolean updateUser(User user);
    List<User> feedUser();

    void followUser(Long uid, Long followerId);
    void unFollowUser(Long uid, Long followerId);
    boolean followStatus(Long uid, Long followerId);

    List<User> getFollowers(Long uid);
    List<User> getFollowingUsers(Long uid);
}
