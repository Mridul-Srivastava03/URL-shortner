package com.projects.self.system_design.url_shortner.controller;


import com.projects.self.system_design.url_shortner.customValidations.validations.Base62Encoded;
import com.projects.self.system_design.url_shortner.dto.response.StatsDTO;
import com.projects.self.system_design.url_shortner.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping(path="/api/stats")
public class StatsController {

    private final StatsService service;

    public StatsController(StatsService service) {
        this.service = service;
    }

    @GetMapping(path = "/{shortCode}")
    public ResponseEntity<?> getStats(@PathVariable @Base62Encoded String shortCode){
        StatsDTO dto = service.getStats(shortCode);
        return ResponseEntity.ok(dto);
    }
}
