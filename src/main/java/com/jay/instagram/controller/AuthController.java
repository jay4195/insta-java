package com.jay.instagram.controller;

import com.jay.instagram.bean.User;
import com.jay.instagram.service.FileService;
import com.jay.instagram.service.TokenService;
import com.jay.instagram.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
    @Autowired
    UserService userService;
    @Autowired
    FileService fileService;
    @Autowired
    TokenService tokenService;

    @RequestMapping(value = "/login",
            method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> login(@RequestBody Map<String, String> loginParam, HttpServletResponse response) {
        Map<String, String> responseMap = new HashMap<>();
        String email = loginParam.get("email");
        String password = loginParam.get("password");
        User checkUserExist = userService.getUserByEmail(email);
        if (checkUserExist == null) {
            responseMap.put("message", "The email is not yet registered to an account!");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return responseMap;
        }
        if (!userService.getPasswordByEmail(checkUserExist.getEmail()).equals(password)) {
            responseMap.put("message", "wrong password");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return responseMap;
        }
        String token = tokenService.createToken(email, password);
        responseMap.put("token", token);
        return responseMap;
    }

    @RequestMapping(value = "me",
            method = RequestMethod.GET)
    @ResponseBody
    public Map<String, User> authMe(HttpServletRequest httpServletRequest) {
        Map<String, User> retMe = new HashMap<>();
        String tokenUserEmail = tokenService.getEmailFromToken(httpServletRequest);
        User user = userService.getUserByEmail(tokenUserEmail);
        user.setAvatar(fileService.getPictureUrl(user.getAvatar()));
        retMe.put("data", user);
        log.info("[User Login] {}", user.getEmail());
        return retMe;
    }

    @RequestMapping(value = "signup",
            method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> signUp(@Valid @RequestBody User newUser, HttpServletResponse response) {
        Map<String, String> responseMap = new HashMap<>();
        User checkUserByEmail = userService.getUserByEmail(newUser.getEmail());
        if (checkUserByEmail != null) {
            responseMap.put("message", String.format("account with email:%s already exists!",
                    newUser.getEmail()));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return responseMap;
        }
        User checkUserByName = userService.getUserByUsername(newUser.getUsername());
        if (checkUserByName != null) {
            responseMap.put("message", String.format("account with username:%s already exists!",
                    newUser.getUsername()));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return responseMap;
        }
        newUser.setCreatedAt(new Date());
        log.info("[signUp] " + newUser);
        userService.addUser(newUser);
        String token = tokenService.createToken(newUser.getEmail(), newUser.getPassword());
        responseMap.put("token", token);
        return responseMap;
    }
}
