package com.jay.instagram.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jay.instagram.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
    @RequestMapping(value = "/login",
            method = RequestMethod.POST)
    /**
     * login auth
     */
    @ResponseBody
    public Map<String, String> login(@RequestBody Map<String, String> loginParam, HttpServletResponse response) {
        log.info("email: " + loginParam.get("email") + " password: " + loginParam.get("password"));
        Map<String, String> responseMap = new HashMap<>();
//        responseMap.put("message", "The email is not yet registered to an accout");
//        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String token = JWT.create().withAudience(loginParam.get("email"))
                .sign(Algorithm.HMAC256(loginParam.get("password")));
        responseMap.put("token", token);
        return responseMap;
    }

    @RequestMapping(value = "me",
            method = RequestMethod.GET)
    @ResponseBody
    public Map<String, User> me() {
        Map<String, User> retMe = new HashMap<>();
        User user = new User("https://img.iplaysoft.com/wp-content/uploads/2019/free-images/free_stock_photo.jpg!0x0.webp"
                ,"jay4195", "Zhiyuan Wang", "jay4195@email.com",
                1L, "www.baidu.com", "founder of this website.");
        retMe.put("data", user);
        return retMe;
    }

    @RequestMapping(value = "signup",
            method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> signUp(@RequestBody Map<String, String> signUpParam) {
        for (String key : signUpParam.keySet()) {
            log.info("key {} value {}", key, signUpParam.get(key));
        }
        Map<String, String> responseMap = new HashMap<>();
        String token = JWT.create().withAudience(signUpParam.get("email"))
                .sign(Algorithm.HMAC256(signUpParam.get("password")));
        responseMap.put("token", token);
        return responseMap;
    }
}
