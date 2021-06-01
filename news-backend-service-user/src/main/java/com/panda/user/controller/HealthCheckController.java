package com.panda.user.controller;

import com.panda.api.controller.healthcheck.HealthCheckControllerApi;
import com.panda.json.result.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "User health check controller", tags = {"health check"})
public class HealthCheckController implements HealthCheckControllerApi {
    final static Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

    @ApiOperation(value = "health check api", notes = "health chec api", httpMethod = "GET")
    public Object healthcheck() {
        logger.info("health check request received.");
        return ResponseResult.ok("Health check passed.");
    }
}
