package com.jay.instagram.dao;

import com.jay.instagram.bean.Message;
import com.jay.instagram.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {
    void addMessage(Message message);
    List<Message> getMessage(User sender, User receiver);
    List<Message> getMyMessage(User user);
}
