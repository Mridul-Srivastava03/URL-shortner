package com.projects.self.system_design.url_shortner.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "url", indexes = {
        @Index(name = "idx_short_code", columnList = "short_code", unique = true),
        @Index(name = "idx_long_url", columnList = "long_url")
})
@NoArgsConstructor
@AllArgsConstructor
public class URLEntity {

    @Id()
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "short_code", unique = true)
    private String shortCode;

    @Column(name = "long_URL")
    private String longURL;

    @Column(name = "click_count")
    private Long clickCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "expiry_at")
    private LocalDateTime expiryAt;
}
