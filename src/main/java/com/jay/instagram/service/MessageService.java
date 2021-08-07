package com.jay.instagram.service;


import com.jay.instagram.bean.Message;
import com.jay.instagram.bean.User;

import java.util.List;

public interface MessageService {
    void addMessage(Message message);
    List<Message> getMessage(User sender, User receiver);
    List<Message> getMyMessage(User user);
}
