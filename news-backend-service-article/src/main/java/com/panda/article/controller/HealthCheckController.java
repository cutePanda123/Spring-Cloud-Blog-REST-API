package com.panda.article.controller;

import com.panda.article.stream.StreamService;
import com.panda.json.result.ResponseResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/service-article/healthcheck")
public class HealthCheckController {
    @Autowired
    private StreamService streamService;

    @ApiOperation(value = "server health check api", notes = "server health check api", httpMethod = "GET")
    @GetMapping("/checkwebserver")
    public Object checkWebServer() {
        log.info("health check request received.");
        return ResponseResult.ok("Health check passed.");
    }

    @ApiOperation(value = "test cloud stream message api", notes = "test cloud stream message api", httpMethod = "GET")
    @GetMapping("/testmessageapi")
    public Object testCloudStream() {
        log.info("test cloud stream message api request received.");
        streamService.sendMsg();
        return ResponseResult.ok("cloud stream message api passed.");
    }
}
