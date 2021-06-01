package com.panda.api.controller.user;

import com.panda.json.result.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;

@Api(value = "User login/register", tags = {"Login/Register controller"})
public interface PassportControllerApi {
    @ApiOperation(value = "Login/Register controller", notes = "Login/Register controller", httpMethod = "POST")
    @PostMapping("/passport")
    public ResponseResult getSMSCode();
}
