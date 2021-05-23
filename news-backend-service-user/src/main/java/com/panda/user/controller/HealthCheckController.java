package com.panda.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping("/healthcheck")
    public Object healthcheck() {
        return "Health check passed.";
    }
}
