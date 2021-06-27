package com.panda.fs.controller;

import com.panda.api.controller.user.HealthCheckControllerApi;
import com.panda.json.result.ResponseResult;
import com.panda.utils.RedisAdaptor;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service-user/healtcheck")
public class HealthCheckController {
    final static Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

    @ApiOperation(value = "server health check api", notes = "server health check api", httpMethod = "GET")
    @GetMapping("/checkwebserver")
    public Object checkWebServer() {
        logger.info("health check request received.");
        return ResponseResult.ok("Health check passed.");
    }
}
