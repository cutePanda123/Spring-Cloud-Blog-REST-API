package com.panda.api.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

@Api(value = "User health check controller", tags = {"health check"})
public interface HealthCheckControllerApi {
    @ApiOperation(value = "health check api", notes = "health chec api", httpMethod = "GET")
    @GetMapping("/healthcheck")
    Object healthcheck();
}
