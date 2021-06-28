package com.panda.admin.controller;

import com.panda.admin.service.AdminUserService;
import com.panda.api.controller.BaseController;
import com.panda.api.controller.admin.AdminManageControllerApi;
import com.panda.json.result.ResponseResult;
import com.panda.json.result.ResponseStatusEnum;
import com.panda.pojo.AdminUser;
import com.panda.pojo.bo.AdminLoginBO;
import com.panda.utils.RedisAdaptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@RestController
public class AdminManageController extends BaseController implements AdminManageControllerApi {
    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private RedisAdaptor  redisAdaptor;

    @Override
    public ResponseResult adminLogin(@Valid AdminLoginBO adminLoginBO, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        AdminUser adminUser = adminUserService.queryAdminUserByUsername(adminLoginBO.getUsername());
        if (adminUser == null) {
            return ResponseResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIST_ERROR);
        }
        boolean isMatchedPassword = BCrypt.checkpw(adminLoginBO.getPassword(), adminUser.getPassword());
        if (!isMatchedPassword) {
            return ResponseResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIST_ERROR);
        }
        loginSetup(adminUser, request, response);
        return ResponseResult.ok();
    }

    private void loginSetup(AdminUser adminUser, HttpServletRequest request, HttpServletResponse response) {
        String redisToken = UUID.randomUUID().toString();
        redisAdaptor.set(REDIS_ADMIN_TOKEN_PREFIX + ":" + adminUser.getId(), redisToken);

        setCookie(request, response, "atoken", redisToken, COOKIE_DURATION);
        setCookie(request, response, "aid", adminUser.getId(), COOKIE_DURATION);
        setCookie(request, response, "aname", adminUser.getAdminName(), COOKIE_DURATION);
    }
}
