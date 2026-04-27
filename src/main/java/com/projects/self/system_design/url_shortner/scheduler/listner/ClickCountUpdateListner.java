package com.projects.self.system_design.url_shortner.scheduler.listner;

import com.projects.self.system_design.url_shortner.scheduler.service.ClickCountUpdateService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
public class ClickCountUpdateListner {

    private final ClickCountUpdateService service;

    public ClickCountUpdateListner(ClickCountUpdateService service) {
        this.service = service;
    }

    @Scheduled(fixedDelay = 60000)
    public void syncClickCount() {
        service.updateClickCount();
    }

}
