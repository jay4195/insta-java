package com.jay.instagram.dao;

import com.jay.instagram.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    void addUser(User user);
    User getUserById(Long id);
    User getUserByEmail(String email);
    User getUserByUsername(String userName);
    boolean updateUser(User user);
    List<User> getRecentUsers();
}
