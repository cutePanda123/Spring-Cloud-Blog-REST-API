package com.panda.api.controller.user;

import com.panda.json.result.ResponseResult;
import com.panda.pojo.bo.RegisterLoginBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api(value = "User login/register", tags = {"Login/Register controller"})
@RequestMapping("/api/service-user/passport")
public interface PassportControllerApi {
    @ApiOperation(value = "Get sms message endpoint", notes = "Get sms message endpoint", httpMethod = "GET")
    @GetMapping("/getSMSCode")
    public ResponseResult getSMSCode(String mobile, HttpServletRequest request);

    @ApiOperation(value = "Login/Register endpoint", notes = "Login/Register endpoint", httpMethod = "POST")
    @PostMapping ("/doLogin")
    public ResponseResult doLogin(@RequestBody @Valid RegisterLoginBO registerLoginBO,
                                  BindingResult result);
}
