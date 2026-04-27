package com.projects.self.system_design.url_shortner;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;

@SpringBootTest
public class RedisTest {

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Test
    void testRedis() {
//        redisTemplate.opsForValue().set("user:3", "Raman");
//        Object val1 = redisTemplate.opsForValue().get("user:3");
//        Object val2 = redisTemplate.opsForValue().get("test 2");
        String counterKey = "url:click:";
        Set<String> keys = redisTemplate.keys(counterKey+"*");
        int i = 1;
    }
}
