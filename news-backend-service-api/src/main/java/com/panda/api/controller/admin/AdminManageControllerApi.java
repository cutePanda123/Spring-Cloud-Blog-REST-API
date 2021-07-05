package com.panda.api.controller.admin;

import com.panda.json.result.ResponseResult;
import com.panda.pojo.bo.AdminLoginBO;
import com.panda.pojo.bo.NewAdminBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api(value = "admin management controller", tags = {"admin"})
@RequestMapping("/api/service-admin/adminMng")
public interface AdminManageControllerApi {
    @ApiOperation(value = "admin login api", notes = "admin login api", httpMethod = "POST")
    @PostMapping("/adminLogin")
    public ResponseResult adminLogin(@RequestBody @Valid AdminLoginBO adminLoginBO,
                                     BindingResult result,
                                     HttpServletRequest request,
                                     HttpServletResponse response);

    @ApiOperation(value = "check if admin name exists api", notes = "check if admin name exists api", httpMethod = "GET")
    @GetMapping("/isExistingUsername")
    public ResponseResult isExistingUsername(@RequestParam String username);

    @ApiOperation(value = "create admin api", notes = "create admin api", httpMethod = "POST")
    @PostMapping("/addNewAdmin")
    public ResponseResult addNewAdmin(@RequestBody NewAdminBO newAdminBO,
                                      HttpServletRequest request,
                                      HttpServletResponse response);

    @ApiOperation(value = "list admin users api", notes = "list admin users api", httpMethod = "POST")
    @PostMapping("/getAdminList")
    public ResponseResult getAdminList(
            @ApiParam(name = "page", value = "page number", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "size per page", required = false)
            @RequestParam Integer pageSize);

    @ApiOperation(value = "logout api", notes = "logout api", httpMethod = "POST")
    @PostMapping("/adminLogout")
    public ResponseResult adminLogout(@RequestParam String adminId,
                                      HttpServletRequest request,
                                      HttpServletResponse response);

    @ApiOperation(value = "face login api", notes = "face login api", httpMethod = "POST")
    @PostMapping("/adminFaceLogin")
    public ResponseResult adminFaceLogin(@RequestBody AdminLoginBO bo,
                                      HttpServletRequest request,
                                      HttpServletResponse response);
}
