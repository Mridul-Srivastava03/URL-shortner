package com.projects.self.system_design.url_shortner.controller;

import com.projects.self.system_design.url_shortner.customValidations.validations.Base62Encoded;
import com.projects.self.system_design.url_shortner.dto.request.URLRequest;
import com.projects.self.system_design.url_shortner.dto.response.URLDTO;
import com.projects.self.system_design.url_shortner.helperService.RateLimiterService;
import com.projects.self.system_design.url_shortner.service.URLService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Validated
@RestController
@RequestMapping(("/api/url"))
public class URLController {

    private final URLService service;

    private final RateLimiterService rateLimiterService;

    public URLController(URLService service, RateLimiterService rateLimiterService) {
        this.service = service;
        this.rateLimiterService = rateLimiterService;
    }

    @PostMapping(path = "/shorten")
    public ResponseEntity<?> createURL(@RequestBody @Valid URLRequest request) {
        if (request.getLongUrl() == null || request.getLongUrl().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        URLDTO dto = service.createShortURL(request.getLongUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping(path = "/{shortCode}")
    public ResponseEntity<?> redirectToLongUrl(@PathVariable @Base62Encoded String shortCode, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String key = "url:rate:"+ip;
        if (!rateLimiterService.rate(key, 100, 60)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        String longUrl = service.getLongUrl(shortCode);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(longUrl))
                .build();
    }
}
