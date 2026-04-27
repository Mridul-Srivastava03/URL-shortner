package com.projects.self.system_design.url_shortner.controller;

import com.projects.self.system_design.url_shortner.dto.request.URLRequest;
import com.projects.self.system_design.url_shortner.dto.response.URLDTO;
import com.projects.self.system_design.url_shortner.service.URLService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(("/api/url"))
public class URLController {

    private final URLService service;

    public URLController(URLService service) {
        this.service = service;
    }

    @PostMapping(path = "/shorten")
    public ResponseEntity<?> createURL(@RequestBody @Valid URLRequest request) {
        if (request.getLongUrl() == null || request.getLongUrl().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        URLDTO dto = service.createShortURL(request.getLongUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    //TODO: Write custom validation for ShortCode to make it base 62 encoded
    @GetMapping(path = "/{shortCode}")
    public ResponseEntity<?> redirectToLongUrl(@PathVariable String shortCode) {
        String longUrl = service.getLongUrl(shortCode);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(longUrl))
                .build();
    }
}
