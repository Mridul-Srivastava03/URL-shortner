package com.projects.self.system_design.url_shortner.scheduler.service;

import com.projects.self.system_design.url_shortner.entity.URLEntity;
import com.projects.self.system_design.url_shortner.repository.URLRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ClickCountUpdateService {

    private final URLRepository urlRepository;
    private final StringRedisTemplate stringRedisTemplate;

    public ClickCountUpdateService(URLRepository urlRepository, StringRedisTemplate stringRedisTemplate) {
        this.urlRepository = urlRepository;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void updateClickCount() {
        String counterKey = "url:click:";
        Set<String> keys = stringRedisTemplate.keys(counterKey+"*");
        if (keys == null || keys.isEmpty()) return;
        for(String key : keys) {
            String shortCode = key.replace(counterKey, "");
            URLEntity entity = urlRepository.findByShortCode(shortCode).orElse(null);
            if(entity == null)
                continue;
            String countStr = stringRedisTemplate.opsForValue().getAndDelete(key);
            if (countStr == null)
                continue;
            long count = Long.parseLong(countStr);
            entity.setClickCount(entity.getClickCount()+count);
            urlRepository.save(entity);
        }
    }
}
