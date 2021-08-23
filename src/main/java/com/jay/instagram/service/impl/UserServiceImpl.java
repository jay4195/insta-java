package com.jay.instagram.service.impl;

import com.jay.instagram.bean.User;
import com.jay.instagram.dao.UserMapper;
import com.jay.instagram.service.FileService;
import com.jay.instagram.service.UserService;
import com.jay.instagram.util.CryptoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@EnableCaching
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    FileService fileService;

    @Override
    public void addUser(User user) {
        // encode password
        user.setPassword(CryptoUtil.encode(user.getPassword()));
        userMapper.addUser(user);
    }

    @Override
    public String getPasswordById(Long id) {
        // decode password
        return CryptoUtil.decode(userMapper.getPasswordById(id));
    }

    @Override
    public String getPasswordByEmail(String email) {
        // decode password
        return CryptoUtil.decode(userMapper.getPasswordByEmail(email));
    }

    @Override
    @Cacheable(cacheNames = "cache_user", key="'user_' + #id")
    public User getUserById(Long id) {
        return userMapper.getUserById(id);
    }

    @Override
    @Cacheable(cacheNames = "cache_user", key="'user_' + #email")
    public User getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }

    @Override
    @Cacheable(cacheNames = "cache_user", key="'user_' + #userName")
    public User getUserByUsername(String userName) {
        return userMapper.getUserByUsername(userName);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "cache_user", key="'user_' + #user.id"),
            @CacheEvict(cacheNames = "cache_user", key="'user_' + #user.email"),
            @CacheEvict(cacheNames = "cache_user", key="'user_' + #user.username")
    })

    /**
     * UserMapper不更改用户密码
     */
    public boolean updateUser(User user) {
        return userMapper.updateUser(user);
    }


    @Override
    public boolean changePassword(User user) {
        user.setPassword(CryptoUtil.encode(user.getPassword()));
        return userMapper.changePassword(user);
    }

    @Override
    public List<User> feedUser() {
        return userMapper.getRecentUsers();
    }

    @Override
    public boolean followStatus(Long uid, Long followerId) {
        return userMapper.findFollow(uid, followerId) > 0;
    }

    @Override
    public void followUser(Long uid, Long followerId) {
        if(!followStatus(uid, followerId)) {
            userMapper.follow(uid, followerId);
            log.info("[Follow] {} follow {}", followerId, uid);
        }
    }

    @Override
    public void unFollowUser(Long uid, Long followerId) {
        if(followStatus(uid, followerId)) {
            userMapper.unFollow(uid, followerId);
            log.info("[unFollow] {} follow {}", followerId, uid);
        }
    }

    @Override
    public List<User> getFollowers(Long uid) {
        List<Long> followerList = userMapper.findFollowers(uid);
        return getUsers(followerList);
    }

    @Override
    public List<User> getFollowingUsers(Long uid) {
        List<Long> followingList = userMapper.findFollowingUsers(uid);
        return getUsers(followingList);
    }

    private List<User> getUsers(List<Long> userList) {
        List<User> followers = new LinkedList<>();
        for (Long id : userList) {
            User tempUser = userMapper.getUserById(id);
            tempUser.setAvatar(fileService.getPictureUrl(tempUser.getAvatar()));
            followers.add(tempUser);
        }
        return followers;
    }
}
