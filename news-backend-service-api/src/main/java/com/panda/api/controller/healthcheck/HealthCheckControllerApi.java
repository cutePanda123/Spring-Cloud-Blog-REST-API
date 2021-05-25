package com.panda.api.controller.healthcheck;

import org.springframework.web.bind.annotation.GetMapping;

public interface HealthCheckControllerApi {
    @GetMapping("/healthcheck")
    Object healthcheck();
}
