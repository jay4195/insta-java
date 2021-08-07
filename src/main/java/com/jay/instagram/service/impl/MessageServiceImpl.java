package com.jay.instagram.service.impl;

import com.jay.instagram.bean.Message;
import com.jay.instagram.bean.User;
import com.jay.instagram.dao.MessageMapper;
import com.jay.instagram.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageMapper messageMapper;

    @Override
    public void addMessage(Message message) {
        messageMapper.addMessage(message);
    }

    @Override
    public List<Message> getMessage(User sender, User receiver) {
        List<Message> messageList = messageMapper.getMessage(sender, receiver);
        return messageList;
    }

    @Override
    public List<Message> getMyMessage(User user) {
        List<Message> messageList = messageMapper.getMyMessage(user);
        return messageList;
    }
}
