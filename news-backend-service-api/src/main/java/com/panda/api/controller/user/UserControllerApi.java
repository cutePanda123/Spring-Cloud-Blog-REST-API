package com.panda.api.controller.user;

import com.panda.api.config.ServiceList;
import com.panda.json.result.ResponseResult;
import com.panda.pojo.bo.UpdateUserInfoBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "User info", tags = {"User info controller"})
@RequestMapping("/api/service-user/user")
@FeignClient(value = ServiceList.USER_SERVICE)
public interface UserControllerApi {
    @ApiOperation(value = "Get user account info", notes = "Get user account info", httpMethod = "POST")
    @PostMapping("/getAccountInfo")
    public ResponseResult getAccountInfo(@RequestParam String userId);

    @ApiOperation(value = "Update user info", notes = "Update user info", httpMethod = "POST")
    @PostMapping("/updateUserInfo")
    public ResponseResult updateUserInfo(
            @RequestBody @Valid UpdateUserInfoBO updateUserInfoBO,
            @RequestParam BindingResult result);

    @ApiOperation(value = "Get user info", notes = "Get user info", httpMethod = "POST")
    @PostMapping("/getUserInfo")
    public ResponseResult getUserInfo(@RequestParam String userId);

    @ApiOperation(value = "list users", notes = "list users", httpMethod = "GET")
    @GetMapping("/list")
    public ResponseResult listUsers(@RequestHeader("userIds") String userIds);
}
