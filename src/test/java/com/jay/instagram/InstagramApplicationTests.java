package com.jay.instagram;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class InstagramApplicationTests {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void test1() {
        stringRedisTemplate.opsForValue().append("StringKey", "字符串数值");
        System.out.println(stringRedisTemplate.opsForValue().get("StringKey"));
    }

    @Test
    void contextLoads() {
    }

}
