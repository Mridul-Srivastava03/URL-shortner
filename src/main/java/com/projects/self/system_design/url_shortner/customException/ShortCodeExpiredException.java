package com.projects.self.system_design.url_shortner.customException;

public class ShortCodeExpiredException extends RuntimeException {
    public ShortCodeExpiredException(String message) {
        super(message);
    }
}
