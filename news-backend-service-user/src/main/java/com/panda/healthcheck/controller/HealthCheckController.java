package com.panda.healthcheck.controller;

import com.panda.api.controller.healthcheck.HealthCheckControllerApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController implements HealthCheckControllerApi {
    final static Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

    public Object healthcheck() {

        logger.info("health check request received.");
        return "Health check passed.";
    }
}
