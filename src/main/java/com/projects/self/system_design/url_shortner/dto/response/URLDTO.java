package com.projects.self.system_design.url_shortner.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class URLDTO {
    private Long id;

    private String shortCode;

    private String longURL;

    private Long clickCount;

    private LocalDateTime createdAt;

    private LocalDateTime expiryAt;
}
