package com.jay.instagram.service.impl;

import com.jay.instagram.bean.Message;
import com.jay.instagram.bean.User;
import com.jay.instagram.dao.MessageMapper;
import com.jay.instagram.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@EnableCaching
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageMapper messageMapper;

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "cache_message", key = "'send_' + #message.sender.id + '_recv_' + #message.receiver.id"),
            @CacheEvict(cacheNames = "cache_message", key = "'send_' + #message.receiver.id + '_recv_' + #message.sender.id")
    })
    public void addMessage(Message message) {
        messageMapper.addMessage(message);
    }

    @Override
    @Cacheable(cacheNames = "cache_message", key="'send_' + #sender.id + '_recv_' + #receiver.id")
    public List<Message> getMessage(User sender, User receiver) {
        log.info("[Get Message From DB] sender: {} receiver: {}", sender.getUsername(), receiver.getUsername());
        List<Message> messageList = messageMapper.getMessage(sender, receiver);
        return messageList;
    }

    @Override
    public List<Message> getMyMessage(User user) {
        List<Message> messageList = messageMapper.getMyMessage(user);
        return messageList;
    }
}
