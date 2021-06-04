package com.panda.api.controller.user;

import com.panda.json.result.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Api(value = "User login/register", tags = {"Login/Register controller"})
@RequestMapping("passport")
public interface PassportControllerApi {
    @ApiOperation(value = "Login/Register controller", notes = "Login/Register controller", httpMethod = "POST")
    @PostMapping("/getSMSCode")
    public ResponseResult getSMSCode(String mobile, HttpServletRequest request);
}
