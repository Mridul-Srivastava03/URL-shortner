package com.projects.self.system_design.url_shortner.scheduler.service;

import com.projects.self.system_design.url_shortner.entity.URLEntity;
import com.projects.self.system_design.url_shortner.helperService.RedisService;
import com.projects.self.system_design.url_shortner.repository.URLRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ClickCountUpdateService {

    private final URLRepository urlRepository;
    private final RedisService redisService;

    public ClickCountUpdateService(URLRepository urlRepository, RedisService redisService) {
        this.urlRepository = urlRepository;
        this.redisService = redisService;
    }

    public void updateClickCount() {
        String counterKey = "url:click:";
        Set<String> keys = redisService.allKeys(counterKey+"*");
        if (keys == null || keys.isEmpty()) return;
        for(String key : keys) {
            String shortCode = key.replace(counterKey, "");
            URLEntity entity = urlRepository.findByShortCode(shortCode).orElse(null);
            if(entity == null)
                continue;
            String countStr = redisService.getAndDelete(key);
            if (countStr == null)
                continue;
            long count = Long.parseLong(countStr);
            entity.setClickCount(entity.getClickCount()+count);
            urlRepository.save(entity);
        }
    }
}
