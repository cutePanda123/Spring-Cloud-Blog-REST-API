package com.panda.user.controller;

import com.panda.api.controller.user.HealthCheckControllerApi;
import com.panda.json.result.ResponseResult;
import com.panda.utils.RedisAdaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController implements HealthCheckControllerApi {
    final static Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

    @Autowired
    private RedisAdaptor redisAdaptor;

    public Object checkWebServer() {
        logger.info("health check request received.");
        return ResponseResult.ok("Health check passed.");
    }

    public Object checkRedis() {
        redisAdaptor.set("abc", "redis is running well");
        return ResponseResult.ok(redisAdaptor.get("abc"));
    }
}
