package com.projects.self.system_design.url_shortner.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class URLDTO {

    private String shortCode;

    private String longURL;
}
