package com.panda.api.controller.user;

import com.panda.json.result.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Api(value = "User management", tags = {"User management controller"})
@RequestMapping("/api/service-user/userMng")
public interface UserManagementControllerApi {
    @ApiOperation(value = "list all users", notes = "list all users", httpMethod = "POST")
    @PostMapping("/queryAll")
    public ResponseResult queryAll(
            @RequestParam String nickname,
            @RequestParam Integer status,
            @RequestParam Date startDate,
            @RequestParam Date endDate,
            @RequestParam Integer page,
            @RequestParam Integer pageSize);

    @ApiOperation(value = "get user detail", notes = "get user detail", httpMethod = "POST")
    @PostMapping("/userDetail")
    public ResponseResult userDetail(String userId);

    @ApiOperation(value = "set user account status", notes = "set user account status", httpMethod = "POST")
    @PostMapping("/setAccountStatus")
    public ResponseResult setAccountStatus(
            @RequestParam String userId,
            @RequestParam Integer status);
}
