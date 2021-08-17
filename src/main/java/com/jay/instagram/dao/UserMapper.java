package com.jay.instagram.dao;

import com.jay.instagram.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    void addUser(User user);
    String getPasswordById(Long id);
    String getPasswordByEmail(String email);

    User getUserById(Long id);
    User getUserByEmail(String email);
    User getUserByUsername(String userName);
    boolean updateUser(User user);
    List<User> getRecentUsers();

    void follow(Long userId, Long followerId);
    void unFollow(Long userId,Long followerId);
    Long findFollow(Long userId, Long followerId);
    List<Long> findFollowers(Long userId);
    List<Long> findFollowingUsers(Long userId);
}
