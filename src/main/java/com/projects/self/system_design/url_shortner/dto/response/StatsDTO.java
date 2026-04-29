package com.projects.self.system_design.url_shortner.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StatsDTO {

    private String shortCode;

    private String longURL;

    private Long clickCount;

    @JsonFormat(pattern = "hh:mm:ss dd-MM-YYYY")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "hh:mm:ss dd-MM-YYYY")
    private LocalDateTime expiryAt;
}
