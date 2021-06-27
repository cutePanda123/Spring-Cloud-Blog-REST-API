package com.panda.api.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(value = "User health check controller", tags = {"health check"})
@RequestMapping("/api/service-user/healtcheck")
public interface HealthCheckControllerApi {
    @ApiOperation(value = "server health check api", notes = "server health check api", httpMethod = "GET")
    @GetMapping("/checkwebserver")
    Object checkWebServer();

    @ApiOperation(value = "redis health check api", notes = "redis health check api", httpMethod = "GET")
    @GetMapping("/checkredis")
    public Object checkRedis();
}
