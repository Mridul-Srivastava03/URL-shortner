package com.projects.self.system_design.url_shortner.service;

import com.projects.self.system_design.url_shortner.customException.ResourceNotFoundException;
import com.projects.self.system_design.url_shortner.dto.response.StatsDTO;
import com.projects.self.system_design.url_shortner.entity.URLEntity;
import com.projects.self.system_design.url_shortner.repository.URLRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class StatsService {

    private final URLRepository repository;
    private final ModelMapper mapper;
    private final StringRedisTemplate redisTemplate;

    public StatsService(URLRepository repository, ModelMapper mapper, StringRedisTemplate redisTemplate) {
        this.repository = repository;
        this.mapper = mapper;
        this.redisTemplate = redisTemplate;
    }

    public StatsDTO getStats (String shortCode) {
        URLEntity entity = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("Short Code is not valid"));
        String counterKey = "url:click:" + shortCode;
        String redisCountStr = redisTemplate.opsForValue().get(counterKey);
        Long redisCount = 0L;
        if (redisCountStr != null)
            try {
                redisCount = Long.parseLong(redisCountStr);
            } catch (NumberFormatException e) {
                redisTemplate.delete(counterKey);
                redisCount = 0L;
            }
        Long dbCount = entity.getClickCount();
        entity.setClickCount(dbCount+redisCount);
        StatsDTO dto = mapper.map(entity, StatsDTO.class);
        dto.setClickCount(dbCount + redisCount);
        return dto;
    }
}
