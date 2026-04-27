package com.projects.self.system_design.url_shortner.service;

import com.projects.self.system_design.url_shortner.dto.response.URLDTO;
import com.projects.self.system_design.url_shortner.entity.URLEntity;
import com.projects.self.system_design.url_shortner.repository.URLRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    private final StringRedisTemplate redisTemplate;

    public URLService(URLRepository repository, ModelMapper mapper, StringRedisTemplate redisTemplate) {
        this.repository = repository;
        this.mapper = mapper;
        this.redisTemplate = redisTemplate;
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

        String longUrl = redisTemplate.opsForValue().get(key);
        if (longUrl != null && !longUrl.isBlank()) {
            redisTemplate.opsForValue().increment(counterKey);
            return longUrl;
        }

        URLEntity entity = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("ShortCode is invalid"));

        LocalDateTime now = LocalDateTime.now();

        if (entity.getExpiryAt() != null && now.isAfter(entity.getExpiryAt())) {
            throw new RuntimeException("Short Code is expired");
        }

        longUrl = entity.getLongURL();

        if (Boolean.FALSE.equals(redisTemplate.hasKey(counterKey))) {
            redisTemplate.opsForValue().set(counterKey, String.valueOf(entity.getClickCount()));
        }

        redisTemplate.opsForValue().increment(counterKey);

        if (entity.getExpiryAt() != null) {
            long ttl = Duration.between(now, entity.getExpiryAt()).toSeconds();

            if (ttl > 0) {
                redisTemplate.opsForValue().set(key, longUrl, ttl, TimeUnit.SECONDS);
                redisTemplate.expire(counterKey, ttl, TimeUnit.SECONDS);
            }
        } else {
            redisTemplate.opsForValue().set(key, longUrl, 1, TimeUnit.DAYS);
            redisTemplate.expire(counterKey, 1, TimeUnit.DAYS);
        }

        return longUrl;
    }
}
