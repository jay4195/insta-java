package com.jay.instagram.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class TokenService {
    public String getEmailFromToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        String tokenUserEmail = JWT.decode(token).getAudience().get(0);
        return tokenUserEmail;
    }

    public String createToken(String username, String password) {
        String token = JWT.create().withAudience(username)
                .sign(Algorithm.HMAC256(password));
        return token;
    }
}
