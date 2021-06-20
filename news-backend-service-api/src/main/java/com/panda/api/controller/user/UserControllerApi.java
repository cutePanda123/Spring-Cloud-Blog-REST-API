package com.panda.api.controller.user;

import com.panda.json.result.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@Api(value = "User info", tags = {"User info controller"})
@RequestMapping("/api/service-user/user")
public interface UserControllerApi {
    @ApiOperation(value = "Get user info endpoint", notes = "Get user info endpoint", httpMethod = "POST")
    @PostMapping("/getAccountInfo")
    public ResponseResult getAccountInfo(@RequestParam String userId);
}
