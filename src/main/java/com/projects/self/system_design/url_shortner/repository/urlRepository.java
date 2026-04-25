package com.projects.self.system_design.url_shortner.repository;

import com.projects.self.system_design.url_shortner.entity.URLEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface urlRepository extends JpaRepository<URLEntity, Long> {
}
