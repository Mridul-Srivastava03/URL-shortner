package com.projects.self.system_design.url_shortner.helperService;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void setValue(String key, String value, long ttl, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, ttl, unit);
    }

    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void increment(String key) {
        redisTemplate.opsForValue().increment(key);
    }

    public Long incrementAndGet(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    public void incrementBy(String key, long value) {
        redisTemplate.opsForValue().increment(key, value);
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void expire(String key, long ttl, TimeUnit unit) {
        redisTemplate.expire(key, ttl, unit);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public String getAndDelete(String key) {
        return redisTemplate.opsForValue().getAndDelete(key);
    }

    public Set<String> allKeys(String s) {
        return redisTemplate.keys(s);
    }
}
