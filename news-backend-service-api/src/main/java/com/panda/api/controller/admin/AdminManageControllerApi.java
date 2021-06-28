package com.panda.api.controller.admin;

import com.panda.json.result.ResponseResult;
import com.panda.pojo.bo.AdminLoginBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api(value = "admin management controller", tags = {"admin"})
@RequestMapping("/api/service-admin/admin-manage")
public interface AdminManageControllerApi {
    @ApiOperation(value = "admin login api", notes = "admin login api", httpMethod = "POST")
    @PostMapping("/adminLogin")
    public ResponseResult adminLogin(@RequestBody @Valid AdminLoginBO adminLoginBO,
                                     BindingResult result,
                                     HttpServletRequest request,
                                     HttpServletResponse response);
}
