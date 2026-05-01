package com.projects.self.system_design.url_shortner.helperService;

import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RateLimiterService {
    private final RedisService redisService;

    public RateLimiterService(RedisService redisService) {
        this.redisService = redisService;
    }

    public boolean rate(String key, Integer request, Integer second) {
        Long current = redisService.incrementAndGet(key);
        if (current != null && current == 1L) {
            redisService.expire(key, second, TimeUnit.SECONDS);
        }

        return current != null && current <= request;
     }
}
