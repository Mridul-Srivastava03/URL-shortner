package com.projects.self.system_design.url_shortner.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
public class URLRequest {

    @NotBlank(message = "Long URL is required")
    @URL(message = "Enter proper URL")
    private String longUrl;
}
