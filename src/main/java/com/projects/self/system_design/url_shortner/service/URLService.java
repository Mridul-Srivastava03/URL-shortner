package com.projects.self.system_design.url_shortner.service;

import com.projects.self.system_design.url_shortner.customException.ResourceNotFoundException;
import com.projects.self.system_design.url_shortner.customException.ShortCodeExpiredException;
import com.projects.self.system_design.url_shortner.dto.response.URLDTO;
import com.projects.self.system_design.url_shortner.entity.URLEntity;
import com.projects.self.system_design.url_shortner.helperService.RedisService;
import com.projects.self.system_design.url_shortner.repository.URLRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class URLService {

    private static final String BASE62_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final URLRepository repository;
    private final ModelMapper mapper;
    private final RedisService redisService;

    public URLService(URLRepository repository, ModelMapper mapper, RedisService redisService) {
        this.repository = repository;
        this.mapper = mapper;
        this.redisService = redisService;
    }


    public URLDTO createShortURL(String longURL) {
        Optional<URLEntity> entity = repository.findByLongURL(longURL);
        if (entity.isPresent()) {
            return mapper.map(entity.get(), URLDTO.class);
        }
        URLEntity newEntity = new URLEntity();
        newEntity.setLongURL(longURL);
        LocalDateTime dateTimeNow = LocalDateTime.now();
        newEntity.setCreatedAt(dateTimeNow);
        newEntity.setExpiryAt(dateTimeNow.plusDays(7));
        URLEntity savedEntity = repository.save(newEntity);
        Long dbId = savedEntity.getId();
        savedEntity.setShortCode(generateShortCode(dbId));
        repository.save(savedEntity);
        return mapper.map(savedEntity, URLDTO.class);
    }

    private String generateShortCode(Long dbId) {
        if (dbId == 0) {
            return String.valueOf(BASE62_CHARACTERS.charAt(0));
        }
        StringBuilder sb = new StringBuilder();
        long num = dbId;
        while (num > 0) {
            int remainder = (int) (num % 62);
            sb.append(BASE62_CHARACTERS.charAt(remainder));
            num /= 62;
        }
        return sb.reverse().toString();
    }

    public String getLongUrl(String shortCode) {

        String key = "url:longUrl:" + shortCode;
        String counterKey = "url:click:" + shortCode;

        String longUrl = redisService.getValue(key);
        if (longUrl != null && !longUrl.isBlank()) {
            redisService.increment(counterKey);
            return longUrl;
        }

        URLEntity entity = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("Short Code not found"));

        LocalDateTime now = LocalDateTime.now();

        if (entity.getExpiryAt() != null && now.isAfter(entity.getExpiryAt())) {
            throw new ShortCodeExpiredException("Short Code is expired");
        }

        longUrl = entity.getLongURL();

        if (!redisService.hasKey(counterKey)) {
            redisService.setValue(counterKey, String.valueOf(entity.getClickCount()));
        }

        redisService.increment(counterKey);

        if (entity.getExpiryAt() != null) {
            long ttl = Duration.between(now, entity.getExpiryAt()).toSeconds();

            if (ttl > 0) {
                redisService.setValue(key, longUrl, ttl, TimeUnit.SECONDS);
                redisService.expire(counterKey, ttl, TimeUnit.SECONDS);
            }
        } else {
            redisService.setValue(key, longUrl, 1, TimeUnit.DAYS);
            redisService.expire(counterKey, 1, TimeUnit.DAYS);
        }

        return longUrl;
    }
}
