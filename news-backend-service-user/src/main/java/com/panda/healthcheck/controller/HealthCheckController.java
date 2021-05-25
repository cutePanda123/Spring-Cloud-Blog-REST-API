package com.panda.healthcheck.controller;

import com.panda.api.controller.healthcheck.HealthCheckControllerApi;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController implements HealthCheckControllerApi {
    public Object healthcheck() {
        return "Health check passed.";
    }
}
