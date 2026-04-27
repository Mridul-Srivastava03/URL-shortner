package com.projects.self.system_design.url_shortner.scheduler.listner;

import com.projects.self.system_design.url_shortner.scheduler.service.ClickCountUpdateService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ClickCountUpdateListener {

    private final ClickCountUpdateService service;

    public ClickCountUpdateListener(ClickCountUpdateService service) {
        this.service = service;
    }

    @Scheduled(fixedDelay = 60000)
    public void syncClickCount() {
        service.updateClickCount();
    }

}
