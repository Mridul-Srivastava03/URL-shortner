package com.projects.self.system_design.url_shortner.repository;

import com.projects.self.system_design.url_shortner.entity.URLEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface URLRepository extends JpaRepository<URLEntity, Long> {
    Optional<URLEntity> findByLongURL(String longURL);

    Optional<URLEntity> findByShortCode(String shortCode);
}
