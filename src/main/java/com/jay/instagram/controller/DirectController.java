package com.jay.instagram.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jay.instagram.bean.Message;
import com.jay.instagram.bean.Post;
import com.jay.instagram.bean.User;
import com.jay.instagram.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/direct")
@CrossOrigin
public class DirectController {
    @Autowired
    UserService userService;
    @Autowired
    FileService fileService;
    @Autowired
    TokenService tokenService;
    @Autowired
    MessageService messageService;

    @RequestMapping(value = "/contact",
                    method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getContactList(HttpServletRequest httpServletRequest,
                                     HttpServletResponse response) {
        JSONObject responseJson = new JSONObject();
        String tokenUserEmail = tokenService.getEmailFromToken(httpServletRequest);
        User tokenUser = userService.getUserByEmail(tokenUserEmail);
        List<Message> messageList = messageService.getMyMessage(tokenUser);
        Set<Long> userIdList = new HashSet<>();
        for (Message msg : messageList) {
            userIdList.add(msg.getSender().getId());
            userIdList.add(msg.getReceiver().getId());
        }
        List<User> contactList = new ArrayList<>();
        List<String> contactListName = new ArrayList<>();
        for (Long id : userIdList) {
            if (!id.equals(tokenUser.getId())) {
                User temp = userService.getUserById(id);
                temp.setAvatar(fileService.getPictureUrl(temp.getAvatar()));
                contactList.add(temp);
                contactListName.add(temp.getUsername());
            }
        }
        log.info("TokenUser: {}, ContactList: {}",tokenUser.getUsername(), contactListName);
        responseJson.put("data", contactList);
        return responseJson;
    }

    @RequestMapping(value = "/chatter/{username}",
                    method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getChatter(@PathVariable("username") String username, HttpServletResponse response) {
        JSONObject responseJson = new JSONObject();
        User user = userService.getUserByUsername(username);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseJson.put("message", String.format("No such user: %s", username));
            return responseJson;
        }
        user.setAvatar(fileService.getPictureUrl(user.getAvatar()));
        responseJson.put("data", user);
        return responseJson;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getMyMessageList(HttpServletRequest httpServletRequest) {
        JSONObject responseJson = new JSONObject();
        String tokenUserEmail = tokenService.getEmailFromToken(httpServletRequest);
        User tokenUser = userService.getUserByEmail(tokenUserEmail);
        tokenUser.setAvatar(fileService.getPictureUrl(tokenUser.getAvatar()));
        List<Message> messageList = messageService.getMessage(tokenUser, tokenUser);
        for (Message msg : messageList) {
            msg.setSender(tokenUser);
            msg.setReceiver(tokenUser);
            msg.setSenderIsMe(true);
        }
        responseJson.put("data", messageList);
        return responseJson;
    }

    @RequestMapping(value = "/{username}",
            method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getMessageList(@PathVariable("username") String username,
                                     HttpServletRequest httpServletRequest,
                                     HttpServletResponse response) {
        JSONObject responseJson = new JSONObject();
        String tokenUserEmail = tokenService.getEmailFromToken(httpServletRequest);
        User tokenUser = userService.getUserByEmail(tokenUserEmail);
        User userChatWith = userService.getUserByUsername(username);
        if (userChatWith == null) {
            responseJson.put("message", String.format("No such user: %s", username));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return responseJson;
        }
        tokenUser.setAvatar(fileService.getPictureUrl(tokenUser.getAvatar()));
        userChatWith.setAvatar(fileService.getPictureUrl(userChatWith.getAvatar()));

//        log.warn("tokenUser: {} userChatWith: {}", tokenUser.getUsername(), userChatWith.getUsername());
        List<Message> messageList = messageService.getMessage(tokenUser, userChatWith);
        for (Message msg : messageList) {
            if (msg.getSender().getId().equals(tokenUser.getId())) {
                msg.setSender(tokenUser);
            } else {
                msg.setSender(userChatWith);
            }
            if (msg.getReceiver().getId().equals(tokenUser.getId())) {
                msg.setReceiver(tokenUser);
            } else {
                msg.setReceiver(userChatWith);
            }
            msg.setSenderIsMe(msg.getSender().getId().equals(tokenUser.getId()));
        }
//        log.info("[MESSAGE] user: {} msgNumber: {}",tokenUser.getUsername(), messageList.size());
        responseJson.put("data", messageList);
        return responseJson;
    }

    @RequestMapping(value = "/{username}",
            method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addMessage(@PathVariable("username") String username,
                                     @RequestBody JSONObject msg,
                                     HttpServletRequest httpServletRequest) {
        JSONObject responseJson = new JSONObject();
        String tokenUserEmail = tokenService.getEmailFromToken(httpServletRequest);
        User tokenUser = userService.getUserByEmail(tokenUserEmail);
        User userChatWith = null;
        log.info(username);
        if (username.equals("undefined")) {
            userChatWith = tokenUser;
        } else {
            userChatWith = userService.getUserByUsername(username);
        }
        tokenUser.setAvatar(fileService.getPictureUrl(tokenUser.getAvatar()));
        userChatWith.setAvatar(fileService.getPictureUrl(userChatWith.getAvatar()));
        Message newMessage = new Message();
        newMessage.setSender(tokenUser);
        newMessage.setReceiver(userChatWith);
        newMessage.setText((String) msg.get("text"));
        newMessage.setCreatedAt(new Date());
        messageService.addMessage(newMessage);
        log.info("[New Message]: sender: {}, receiver: {}, msg: {}, timestamp: {}", newMessage.getSender().getUsername(), newMessage.getReceiver().getUsername(),
                newMessage.getText(), newMessage.getCreatedAt());
        List<Message> messageList = messageService.getMessage(tokenUser, userChatWith);
        for (Message msgtemp : messageList) {
            if (msgtemp.getSender().getId().equals(tokenUser.getId())) {
                msgtemp.setSender(tokenUser);
            } else {
                msgtemp.setSender(userChatWith);
            }
            if (msgtemp.getReceiver().getId().equals(tokenUser.getId())) {
                msgtemp.setReceiver(tokenUser);
            } else {
                msgtemp.setReceiver(userChatWith);
            }
            msgtemp.setSenderIsMe(msgtemp.getSender().getId().equals(tokenUser.getId()));
        }
        responseJson.put("data", messageList);
        return responseJson;
    }
}
